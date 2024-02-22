package VBOARD.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class ImageUploadService {

    @Value("${imgur.client.id}")
    private String clientId;

    public String uploadImage(MultipartFile file) {
        RestTemplate restTemplate = new RestTemplate();
        final String url = "https://api.imgur.com/3/image";

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.set("Authorization", "Client-ID " + clientId);
        headers.set("Content-Type", "multipart/form-data");

        org.springframework.http.HttpEntity<MultipartFile> requestBody = new org.springframework.http.HttpEntity<>(file, headers);

        org.springframework.http.ResponseEntity<Map> response = restTemplate.postForEntity(url, requestBody, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
            return (String) data.get("link");
        } else {
            throw new RuntimeException("Failed to upload image to Imgur");
        }
    }
}
