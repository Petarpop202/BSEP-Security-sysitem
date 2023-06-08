package com.example.newsecurity.Controller;

import org.apache.tika.Tika;
import com.example.newsecurity.DTO.EngineerUpdateDTO;
import com.example.newsecurity.DTO.EngineerUpdateSkillsDTO;
import com.example.newsecurity.Model.Engineer;
import com.example.newsecurity.Model.User;
import com.example.newsecurity.Service.IEngineerService;
import com.example.newsecurity.Service.IUserService;
import com.example.newsecurity.Util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping(value = "/engineers",produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "https://localhost:3000")
public class EngineerController {
    private static final String path = "src/main/resources/cv/";
    @Autowired
    private IEngineerService engineerService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private IUserService userService;

    @PostMapping
    public Engineer newEngineer(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Engineer engineer) {
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
    public Engineer getEngineerById(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("GET_ENGINEER_ONE")){
            return null;
        }
        return engineerService.getEngineerById(id);
    }

    @GetMapping("/username={username}")
    public Engineer getEngineerByUsername(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String username){
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
    public void updateEngineer(@RequestHeader("Authorization") String authorizationHeader, @RequestBody EngineerUpdateDTO engineerUpdateDTO){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("UPDATE_ENGINEER")){
            return;
        }
        engineerService.updateEngineer(engineerUpdateDTO);
    }
    @PutMapping("/skills-update")
    public void updateEnginnerSkills(@RequestHeader("Authorization") String authorizationHeader, @RequestBody EngineerUpdateSkillsDTO engineerUpdateSkillsDTO){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("UPDATE_ENGINEER_SKILLS")){
            return;
        }
        engineerService.updateEngineerSkills(engineerUpdateSkillsDTO);
    }
    @PutMapping("/{id}")
    public Engineer updatePassword(@PathVariable Long id, @RequestBody String newPassword){
        return engineerService.updatePassword(id, newPassword);
    }

    @PostMapping("/upload-cv")
    public ResponseEntity<String> uploadCV(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("file") MultipartFile file) throws Exception{
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        //if (!user.hasPermission("UPLOAD_CV")){
        //    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        //}

        byte[] fileBytes = file.getBytes();
        Tika tika = new Tika();
        String mimeType = tika.detect(fileBytes);
        if(!mimeType.equals("application/pdf")){
            return ResponseEntity.badRequest().body("File is not a PDF file!");
        }
        return new ResponseEntity<>(engineerService.uploadCV(user.getId(), file), HttpStatus.OK);
    }

    @GetMapping("/get-cv/{username}")
    public ResponseEntity<Resource> getCV(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("username") String username) throws IOException {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String usernameFromToken = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(usernameFromToken);
        //if (!user.hasPermission("GET_CV")){
        //    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        //}
        return GetOrDownloadCV(username + "_CV.pdf", false);
    }
    @GetMapping("/download-cv/{username}")
    public ResponseEntity<Resource> downloadCV(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("username") String username) throws IOException {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String usernameFromToken = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(usernameFromToken);
        //if (!user.hasPermission("DOWNLOAD_CV")){
        //    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        //}
        return GetOrDownloadCV(username + "_CV.pdf", true);
    }

    private ResponseEntity<Resource> GetOrDownloadCV(String filename, boolean isDownload) throws IOException{
        File file = new File(path + filename);
        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(this.headers(filename))
                .contentLength(file.length())
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
