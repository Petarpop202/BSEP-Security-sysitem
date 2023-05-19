package com.example.newsecurity.Controller;

import com.example.newsecurity.DTO.RequestResponse;
import com.example.newsecurity.DTO.UserRequest;
import com.example.newsecurity.Model.RegistrationRequest;
import com.example.newsecurity.Model.Test;
import com.example.newsecurity.Model.User;
import com.example.newsecurity.Service.IRegistrationRequestService;
import com.example.newsecurity.Service.IUserService;
import com.example.newsecurity.Util.TokenUtils;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

//Kontroler zaduzen za autentifikaciju korisnika
@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:3000")
public class AuthenticationController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUserService userService;

    @Autowired
    private IRegistrationRequestService registrationRequestService;

    @PostMapping("/signup")
    public ResponseEntity<User> addUser(@RequestBody UserRequest userRequest, UriComponentsBuilder ucBuilder) throws MessagingException, UnsupportedEncodingException {
        User existUser = this.userService.findByUsername(userRequest.getUsername());

        if (existUser != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        String verification = RandomString.make();
        userRequest.setVerification(verification);
        User user = this.userService.save(userRequest);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping ("/response")
    public ResponseEntity<RequestResponse> RequestResponse(@RequestBody RequestResponse response, UriComponentsBuilder ucBuilder) throws NoSuchAlgorithmException, InvalidKeyException {
        registrationRequestService.setResponse(response);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/activate")
    public ResponseEntity<User> activateUser(@RequestParam String code) throws NoSuchAlgorithmException, InvalidKeyException {
        registrationRequestService.Activate(code);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
