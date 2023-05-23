package com.example.newsecurity.Controller;

import com.example.newsecurity.DTO.*;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
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
@CrossOrigin(origins = "https://localhost:3000")
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

    @PostMapping("/login")
    public ResponseEntity<Jwt> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {
        // Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se
        // AuthenticationException
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user);
        String refreshJwt = tokenUtils.generateRefreshToken(user);

        int expiresIn = tokenUtils.getExpiredIn();
        return ResponseEntity.ok(new Jwt(jwt,refreshJwt, expiresIn));
    }

    @GetMapping("/passwordlessLogin")
    public ResponseEntity<Jwt> passwordlessLogin(@RequestParam String mail) {
        String token = tokenUtils.generatePasswordlessToken(mail);
        userService.passwordlessLogin(token,mail);
        return null;
    }

    @GetMapping("/passwordlessLoginActivate")
    public ResponseEntity<Jwt> activatePasswordless(@RequestParam String code) {
        String token = code;
        if (tokenUtils.validatePasswordlessToken(token))
        {
            String mail = tokenUtils.getUsernameFromToken(token);
            String jwt = tokenUtils.generatePasswordlessToken(mail);
            String refreshJwt = tokenUtils.generatePasswordlessRefreshToken(mail);
            return ResponseEntity.ok(new Jwt(jwt,refreshJwt, tokenUtils.getExpiredIn()));
        }
        return null;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> addUser(@RequestBody UserRequest userRequest, UriComponentsBuilder ucBuilder) throws MessagingException, UnsupportedEncodingException {
        User existUser = this.userService.findByUsername(userRequest.getUsername());

        if (existUser != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        User user = this.userService.save(userRequest);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping ("/response")
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public ResponseEntity<RequestResponse> RequestResponse(@RequestBody RequestResponse response, UriComponentsBuilder ucBuilder) throws NoSuchAlgorithmException, InvalidKeyException {
        RegistrationRequest request = registrationRequestService.setResponse(response);
        if(request.isAccepted())
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        else return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/activate")
    public ResponseEntity<User> activateUser(@RequestParam String code) throws NoSuchAlgorithmException, InvalidKeyException {
        registrationRequestService.Activate(code);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/refresh")
    @PreAuthorize("hasAnyRole('ROLE_HUMAN_RESOURCE_MANAGER', 'ROLE_PROJECT_MANAGER', 'ROLE_ADMINISTRATOR', 'ROLE_ENGINEER')")
    public ResponseEntity<?> refreshAccessToken(@RequestBody Jwt refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshJwt();
        if (tokenUtils.validateRefreshToken(refreshToken)) {
            String username = tokenUtils.getUsernameFromToken(refreshToken);
            User user = userService.findByUsername(username);

            String accessToken = tokenUtils.generateToken(user);
            Jwt jwt = new Jwt(accessToken,refreshToken, tokenUtils.getExpiredIn());

            return ResponseEntity.ok(jwt);
        } else {
            return ResponseEntity.badRequest().body("Invalid refresh token.");
        }
    }

    @GetMapping("/getString")
    @PreAuthorize("hasAnyRole('ROLE_HUMAN_RESOURCE_MANAGER', 'ROLE_PROJECT_MANAGER', 'ROLE_ADMINISTRATOR', 'ROLE_ENGINEER', 'ROLE_GUEST')")
    public ResponseEntity<?> getString(){
        String Jej = "JEEEEJ";
        return ResponseEntity.ok(Jej);
    }
}
