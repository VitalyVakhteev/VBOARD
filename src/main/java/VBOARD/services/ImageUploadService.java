package VBOARD.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Service class for handling image uploads.
 */
@Service
public class ImageUploadService {
    private static final Logger logger = LoggerFactory.getLogger(ImageUploadService.class);

    @Value("${imgur.client.id}")
    private String clientId;

    /**
     * Method to upload an image file to Imgur.
     *
     * @param file The image file to be uploaded.
     * @return The URL of the uploaded image on Imgur.
     * @throws RuntimeException if the image upload fails.
     */
    public String uploadImage(MultipartFile file) {
        logger.info("Attempting to upload image @ timestamp: " + System.currentTimeMillis());
        RestTemplate restTemplate = new RestTemplate();
        final String url = "https://api.imgur.com/3/image";

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.set("Authorization", "Client-ID " + clientId);
        headers.set("Content-Type", "multipart/form-data");

        org.springframework.http.HttpEntity<MultipartFile> requestBody = new org.springframework.http.HttpEntity<>(file, headers);

        org.springframework.http.ResponseEntity<Map> response = restTemplate.postForEntity(url, requestBody, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
            String link = (String) data.get("link");
            logger.info("Image uploaded successfully. Link: " + link + " @ timestamp: " + System.currentTimeMillis());
            return (String) data.get("link");
        } else {
            logger.error("Failed to upload image to Imgur @ timestamp: " + System.currentTimeMillis());
            throw new RuntimeException("Failed to upload image to Imgur");
        }
    }
}
