/*
 * Created by minmin_tranova on 27.04.2026
 */

package cz.cvut.fel.cinetrack.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordRequestDTO {

    @NotBlank(message = "Email is required!")
    private String email;

    @NotBlank(message = "Username is required!")
    private String username;

    @NotBlank(message = "Password is required!")
    @Size(min = 6, message = "Password must be at least 6 characters!")
    private String password;

    @NotBlank(message = "Please confirm your password!")
    private String confirmPassword;

    public ResetPasswordRequestDTO(String email, String username, String password, String confirmPassword) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public @NotBlank(message = "Email is required!") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email is required!") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Username is required!") String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank(message = "Username is required!") String username) {
        this.username = username;
    }

    public @NotBlank(message = "Password is required!") @Size(min = 6, message = "Password must be at least 6 characters!") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password is required!") @Size(min = 6, message = "Password must be at least 6 characters!") String password) {
        this.password = password;
    }

    public @NotBlank(message = "Please confirm your password!") String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(@NotBlank(message = "Please confirm your password!") String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
