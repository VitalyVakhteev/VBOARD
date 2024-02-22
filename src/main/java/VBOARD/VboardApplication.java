package VBOARD;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VboardApplication {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("imgur.client.id", dotenv.get("IMGUR_CLIENT_ID"));
        SpringApplication.run(VboardApplication.class, args);
    }

}
