/*
 * Created by minmin_tranova on 27.04.2026
 */

package cz.cvut.fel.cinetrack.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class VerifyUserRequestDTO {

    @NotBlank(message = "Email is required!")
    @Email(message = "Invalid email format!")
    private String email;

    @NotBlank(message = "Username is required!")
    private String username;

    public VerifyUserRequestDTO(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public @NotBlank(message = "Email is required!") @Email(message = "Invalid email format!") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email is required!") @Email(message = "Invalid email format!") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Username is required!") String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank(message = "Username is required!") String username) {
        this.username = username;
    }
}
