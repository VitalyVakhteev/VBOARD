package VBOARD.vboard;

import lombok.Getter;

@Getter
public class RegisterRequest {
    private final String username;
    private final String password;
    private final String confirmPassword;

    public RegisterRequest(String username, String password, String confirmPassword) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
