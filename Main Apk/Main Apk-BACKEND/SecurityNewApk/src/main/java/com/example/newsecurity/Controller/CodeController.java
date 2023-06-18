package com.example.newsecurity.Controller;

import com.example.newsecurity.DTO.ValidateCodeDTO;
import com.example.newsecurity.Service.IUserService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.util.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.newsecurity.Model.User;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/code")
@CrossOrigin(origins = "https://localhost:3000")
public class CodeController {

    private final GoogleAuthenticator gAuth;

    @Autowired
    private IUserService userService;

    @SneakyThrows
    @GetMapping("/generate/{username}")
    public ResponseEntity<String> generate(@PathVariable String username) throws IOException {
        final GoogleAuthenticatorKey key = gAuth.createCredentials(username);

        // Generisanje QR koda na backend-u
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        String otpAuthURL = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("my-demo", username, key);

        BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthURL, BarcodeFormat.QR_CODE, 200, 200);

        // Konverzija BitMatrix u BufferedImage
        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);

        // Konverzija BufferedImage u Base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] imageBytes = baos.toByteArray();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        User user = userService.findByUsername(username);
        user.setMfa(true);
        userService.update(user);

        // Vraćanje slike kao odgovor u Base64 formatu
        return ResponseEntity.ok().body(base64Image);
    }


    @PostMapping("/validate/key")
    public boolean validateKey(@RequestBody ValidateCodeDTO body) {
        return gAuth.authorizeUser(body.getUsername(), body.getCode());
    }

}
