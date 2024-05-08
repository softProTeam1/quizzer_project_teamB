package fi.haagahelia.quizzer.repository;

import jakarta.validation.constraints.*;

public class SignupForm {
    @NotEmpty
    @Size(min = 3, max = 30)
    private String username = "";

    @NotEmpty
    @Size(min = 8, max = 30)
    private String password = "";

    @NotEmpty
    @Size(min = 8, max = 30)
    private String passwordCheck = "";

    @NotEmpty
    @Size(max = 30)
    private String email = "";

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotEmpty
    private String role = "ADMIN";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordCheck() {
        return passwordCheck;
    }

    public void setPasswordCheck(String passwordCheck) {
        this.passwordCheck = passwordCheck;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
