/*
 * Created by minmin_tranova on 08.03.2026
 */

package cz.cvut.fel.cinetrack.controller;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/media/upload")
@CrossOrigin(origins = "http://localhost:3000")
public class PosterUploadController {

    @Value("${app.upload.dir:uploads/posters}")
    private String uploadDir;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @PostMapping("/poster")
    public ResponseEntity<Map<String, String>> uploadPoster(
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        // validation
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Only image files are allowed"));
        }
        // unique name
        String extension = getExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID() + extension;
        // create path if not exist
        Path uploadPath = Paths.get(uploadDir);
        Files.createDirectories(uploadPath);
        // save file
        Path filePath = uploadPath.resolve(filename);
        Files.write(filePath, file.getBytes());
        // return url
        String posterUrl = baseUrl + "/uploads/posters/" + filename;
        return ResponseEntity.ok(Map.of("posterUrl", posterUrl));
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return ".jpg";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
}
