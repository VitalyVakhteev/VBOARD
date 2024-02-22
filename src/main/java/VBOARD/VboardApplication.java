package VBOARD;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class for the Vboard application.
 * This class contains the main method which starts the Spring Boot application.
 */
@SpringBootApplication
public class VboardApplication {
    /**
     * The main method for the Vboard application.
     * This method loads environment variables from a .env file using the dotenv library,
     * sets system properties for the Imgur client ID and the JDBC database connection,
     * and then starts the Spring Boot application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
//        Dotenv dotenv = Dotenv.load();
//        System.setProperty("imgur.client.id", dotenv.get("IMGUR_CLIENT_ID"));
//        System.setProperty("spring.datasource.url", dotenv.get("JDBC_DATABASE_URL"));
//        System.setProperty("spring.datasource.username", dotenv.get("JDBC_DATABASE_USERNAME"));
//        System.setProperty("spring.datasource.password", dotenv.get("JDBC_DATABASE_PASSWORD"));
        SpringApplication.run(VboardApplication.class, args);
    }

}
