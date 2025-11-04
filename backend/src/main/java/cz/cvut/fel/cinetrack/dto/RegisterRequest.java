/*
 * Created by minmin_tranova on 31.10.2025
 */

package cz.cvut.fel.cinetrack.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotNull(message = "Username is required!")
    @Size(min = 5, max = 15, message = "Username must be between 5 and 15 characters!")
    private String username;

    @NotNull(message = "First name is required!")
    @Size(min = 3, max = 15, message = "First name must be between 3 and 15 characters!")
    private String firstname;

    @NotNull(message = "Last name is required!")
    @Size(min = 3, max = 15, message = "Last name must be between 3 and 15 characters!")
    private String lastname;

    @NotNull(message = "Email is required!")
    @Email(message = "Email should be valid!")
    private String email;

    @NotNull(message = "Password is required!")
    @Size(min = 6, message = "Password must be at least 6 characters!")
    private String password;

    public RegisterRequest() {}

    public RegisterRequest(String username,
                           String firstname,
                           String lastname,
                           String email,
                           String password) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public @NotNull(message = "Username is required") @Size(min = 5, max = 15, message = "Username must be between 5 and 15 characters") String getUsername() {
        return username;
    }

    public void setUsername(@NotNull(message = "Username is required") @Size(min = 5, max = 15, message = "Username must be between 5 and 15 characters") String username) {
        this.username = username;
    }

    public @NotNull(message = "First name is required") @Size(min = 3, max = 15, message = "First name must be between 3 and 15 characters") String getFirstname() {
        return firstname;
    }

    public void setFirstname(@NotNull(message = "First name is required") @Size(min = 3, max = 15, message = "First name must be between 3 and 15 characters") String firstname) {
        this.firstname = firstname;
    }

    public @NotNull(message = "Last name is required") @Size(min = 3, max = 15, message = "Last name must be between 3 and 15 characters") String getLastname() {
        return lastname;
    }

    public void setLastname(@NotNull(message = "Last name is required") @Size(min = 3, max = 15, message = "Last name must be between 3 and 15 characters") String lastname) {
        this.lastname = lastname;
    }

    public @NotNull(message = "Email is required") @Email(message = "Email should be valid") String getEmail() {
        return email;
    }

    public void setEmail(@NotNull(message = "Email is required") @Email(message = "Email should be valid") String email) {
        this.email = email;
    }

    public @NotNull(message = "Password is required") @Size(min = 6, message = "Password must be at least 6 characters") String getPassword() {
        return password;
    }

    public void setPassword(@NotNull(message = "Password is required") @Size(min = 6, message = "Password must be at least 6 characters") String password) {
        this.password = password;
    }
}
