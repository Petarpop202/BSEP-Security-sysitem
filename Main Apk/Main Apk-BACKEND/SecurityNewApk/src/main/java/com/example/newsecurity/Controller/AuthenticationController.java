package com.example.newsecurity.Controller;

import com.example.newsecurity.DTO.*;
import com.example.newsecurity.Model.Engineer;
import com.example.newsecurity.Model.RegistrationRequest;
import com.example.newsecurity.Model.Test;
import com.example.newsecurity.Model.User;
import com.example.newsecurity.Service.IAlarmService;
import com.example.newsecurity.Service.IRegistrationRequestService;
import com.example.newsecurity.Service.IUserService;
import com.example.newsecurity.Util.TokenUtils;
import net.bytebuddy.utility.RandomString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private IAlarmService alarmService;

    private static final Logger logger = LogManager.getLogger(AuthenticationController.class);

    @PostMapping("/login")
    public ResponseEntity<Jwt> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletRequest request) {
        // Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se
        // AuthenticationException
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = (User) authentication.getPrincipal();
            String jwt = tokenUtils.generateToken(user);
            String refreshJwt = tokenUtils.generateRefreshToken(user);

            int expiresIn = tokenUtils.getExpiredIn();
            //logger.info("HTTPS request successfully passed!");
            logger.info("User {} is logged.", user.getUsername());
            return ResponseEntity.ok(new Jwt(jwt, refreshJwt, expiresIn));
        }
        catch (AuthenticationException e) {
            // Greška pri autentifikaciji
            // Ovde možete dodati odgovarajuću logiku za obradu greške
            logger.error("Failed to authenticate user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @PostMapping("/passwordlessLogin")
    public ResponseEntity<Jwt> passwordlessLogin(@RequestBody MailDTO mail) {
        String token = tokenUtils.generatePasswordlessToken(mail.getMail());
        userService.passwordlessLogin(token,mail.getMail());
        logger.info("Passwordless login token generated for email: {}", mail.getMail());
        return null;
    }

    @GetMapping("/passwordlessLoginActivate")
    public RedirectView activatePasswordless(@RequestParam String code) {
        String token = code;
        if (tokenUtils.validatePasswordlessToken(token))
        {
            String mail = tokenUtils.getUsernameFromToken(token);
            String jwt = tokenUtils.generatePasswordlessToken(mail);
            String refreshJwt = tokenUtils.generatePasswordlessRefreshToken(mail);
            // Definišite URL adresu na koju želite da se korisnik preusmeri na frontendu
            String redirectUrl = "https://localhost:3000/guestlogin/?token=" + jwt + "&refreshToken=" + refreshJwt;

            logger.info("Passwordless login activated for email: {}", mail);
            return new RedirectView(redirectUrl);
        }
        return null;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> addUser(@RequestBody UserRequest userRequest, UriComponentsBuilder ucBuilder) throws Exception {
        User existUser = this.userService.findByUsername(userRequest.getUsername());

        if (existUser != null) {
            logger.error("Failed to add user. Username {} already exists.", userRequest.getUsername());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        User user = this.userService.save(userRequest);

        logger.info("User {} successfully registered.", user.getUsername());
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping ("/response")
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public ResponseEntity<RequestResponse> RequestResponse(@RequestHeader("Authorization") String authorizationHeader, @RequestBody RequestResponse response, UriComponentsBuilder ucBuilder) throws NoSuchAlgorithmException, InvalidKeyException {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String token_username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(token_username);
        if (!user.hasPermission("RESPONSE")){
            logger.warn("User {} does not have permission to respond to requests.", user.getUsername());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        RegistrationRequest request = registrationRequestService.setResponse(response);
        if(request.isAccepted()) {
            logger.info("Request {} successfully accepted.", request.getId());
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        else{
            logger.info("Request {} rejected.", request.getId());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/activate")
    public ResponseEntity<User> activateUser(@RequestParam String code) throws NoSuchAlgorithmException, InvalidKeyException {
        registrationRequestService.Activate(code);
        logger.info("User successfully activated.");
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/refresh")
    //@PreAuthorize("hasAnyRole('ROLE_HUMAN_RESOURCE_MANAGER', 'ROLE_PROJECT_MANAGER', 'ROLE_ADMINISTRATOR', 'ROLE_ENGINEER')")
    public ResponseEntity<?> refreshAccessToken(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Jwt refreshJwt) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String token_username = tokenUtils.getUsernameFromToken(jwtToken);
        User token_user = userService.findByUsername(token_username);
        if (!token_user.hasPermission("REFRESH_ACCESS_TOKEN")){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String refreshToken = refreshJwt.getRefreshJwt();
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

    @GetMapping("/getRequests")
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public ResponseEntity<?> getRequests(@RequestHeader("Authorization") String authorizationHeader){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String token_username = tokenUtils.getUsernameFromToken(jwtToken);
        User token_user = userService.findByUsername(token_username);
        if (!token_user.hasPermission("GET_REQUESTS")){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<RegistrationRequest> list = registrationRequestService.getAllUnresponded();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/forgotPassword/{username}")
    public ResponseEntity<?> forgotPassword(@PathVariable String username) {
        userService.resetPasswordMail(username);
        return ResponseEntity.ok("Mail send");
    }

    @PutMapping("/resetPassword/{username}")
    public User resetPassword(@PathVariable String username, @RequestBody String newPassword){
        return userService.resetPassword(username, newPassword);
    }
}
