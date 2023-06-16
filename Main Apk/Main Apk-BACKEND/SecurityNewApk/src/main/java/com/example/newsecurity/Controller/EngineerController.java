package com.example.newsecurity.Controller;

import com.example.newsecurity.Service.IFileService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.Tika;
import com.example.newsecurity.DTO.EngineerUpdateDTO;
import com.example.newsecurity.DTO.EngineerUpdateSkillsDTO;
import com.example.newsecurity.Model.Engineer;
import com.example.newsecurity.Model.User;
import com.example.newsecurity.Service.IEngineerService;
import com.example.newsecurity.Service.IUserService;
import com.example.newsecurity.Util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@ControllerAdvice
@RequestMapping(value = "/engineers",produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "https://localhost:3000")
public class EngineerController {
    private static final String path = "src/main/resources/cv/";
    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;
    @Autowired
    private IEngineerService engineerService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private IUserService userService;
    @Autowired
    private IFileService fileService;

    private static final Logger logger = LogManager.getLogger(EngineerController.class);
    @PostMapping
    public Engineer newEngineer(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Engineer engineer) throws Exception {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("CREATE_ENGINEER")){
            return null;
        }
        return engineerService.newEngineer(engineer);
    }
    @GetMapping
    public List<Engineer> getAllEngineers(@RequestHeader("Authorization") String authorizationHeader){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("GET_ENGINEER_ALL")){
            return null;
        }
        return engineerService.getAllEngineers();
    }

    @GetMapping("/{id}")
    public Engineer getEngineerById(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id) throws Exception {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("GET_ENGINEER_ONE")){
            return null;
        }
        return engineerService.getEngineerById(id);
    }

    @GetMapping("/username={username}")
    public Engineer getEngineerByUsername(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String username) throws Exception {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String token_username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(token_username);
        if (!user.hasPermission("GET_ENGINEER_BY_USERNAME")){
            return null;
        }
        return engineerService.getEngineerByUsername(username);
    }

    @DeleteMapping("/{id}")
    public String deleteEngineerById(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("DELETE_ENGINEER")){
            return null;
        }
        engineerService.deleteEngineerById(id);
        return "Engineer deleted successfully!";
    }
    @PutMapping
    public void updateEngineer(@RequestHeader("Authorization") String authorizationHeader, @RequestBody EngineerUpdateDTO engineerUpdateDTO) throws Exception {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("UPDATE_ENGINEER")){
            return;
        }
        engineerService.updateEngineer(engineerUpdateDTO);
    }
    @PutMapping("/skills-update")
    public void updateEnginnerSkills(@RequestHeader("Authorization") String authorizationHeader, @RequestBody EngineerUpdateSkillsDTO engineerUpdateSkillsDTO) throws Exception {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("UPDATE_ENGINEER_SKILLS")){
            return;
        }
        engineerService.updateEngineerSkills(engineerUpdateSkillsDTO);
    }
    @PutMapping("/{id}")
    public Engineer updatePassword(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id, @RequestBody String newPassword){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("UPDATE_ENGINEER_PASSWORD")){
            return null;
        }
        return engineerService.updatePassword(id, newPassword);
    }

    @PostMapping("/upload-cv")
    public ResponseEntity<String> uploadCV(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("file") MultipartFile file) throws Exception{
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("UPLOAD_CV")){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        byte[] fileBytes = file.getBytes();
        Tika tika = new Tika();
        String mimeType = tika.detect(fileBytes);
        if(!mimeType.equals("application/pdf")){
            return ResponseEntity.badRequest().body("File is not a PDF file!");
        }
        return new ResponseEntity<>(engineerService.uploadCV(user.getId(), file), HttpStatus.OK);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxSizeException(){
        return ResponseEntity.badRequest().body("File is too large! Max size is: " + maxFileSize);
    }

    @GetMapping("/get-cv/{username}")
    public ResponseEntity<Resource> getCV(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("username") String username) throws IOException {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String usernameFromToken = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(usernameFromToken);
        if (!user.hasPermission("GET_CV")){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try{
            return GetOrDownloadCV(username, false);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<Resource>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @GetMapping("/download-cv/{username}")
    public ResponseEntity<Resource> downloadCV(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("username") String username) throws Exception {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String usernameFromToken = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(usernameFromToken);
        if (!user.hasPermission("DOWNLOAD_CV")){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return GetOrDownloadCV(username, true);
    }

    private ResponseEntity<Resource> GetOrDownloadCV(String username, boolean isDownload) throws Exception{
        File file = new File(path + username + "_CV.pdf");
        byte[] decryptedData = fileService.decryptFile(file, username);
        if (decryptedData == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ByteArrayResource resource = new ByteArrayResource(decryptedData);

        return ResponseEntity.ok()
                .headers(this.headers(username + "_CV.pdf"))
                .contentLength(resource.contentLength())
                .contentType(isDownload ? MediaType.parseMediaType("application/octet-stream") : MediaType.APPLICATION_PDF)
                .body(resource);
    }

    private HttpHeaders headers(String name) {
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        return header;
    }
}
