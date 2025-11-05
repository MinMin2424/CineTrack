/*
 * Created by minmin_tranova on 31.10.2025
 */

package cz.cvut.fel.cinetrack.dto.auth;

import jakarta.validation.constraints.NotNull;

public class LoginRequest {

    @NotNull(message = "Email is required!")
    private String email;

    @NotNull(message = "Password is required!")
    private String password;

    public LoginRequest() {}

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public @NotNull(message = "Email is required") String getEmail() {
        return email;
    }

    public void setEmail(@NotNull(message = "Email is required") String email) {
        this.email = email;
    }

    public @NotNull(message = "Password is required") String getPassword() {
        return password;
    }

    public void setPassword(@NotNull(message = "Password is required") String password) {
        this.password = password;
    }
}
