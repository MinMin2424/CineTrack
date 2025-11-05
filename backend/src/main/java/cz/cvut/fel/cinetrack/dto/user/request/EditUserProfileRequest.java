/*
 * Created by minmin_tranova on 05.11.2025
 */

package cz.cvut.fel.cinetrack.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EditUserProfileRequest {

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

    public EditUserProfileRequest(String username,
                                  String firstname,
                                  String lastname,
                                  String email) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    public @NotNull(message = "Username is required!") @Size(min = 5, max = 15, message = "Username must be between 5 and 15 characters!") String getUsername() {
        return username;
    }

    public void setUsername(@NotNull(message = "Username is required!") @Size(min = 5, max = 15, message = "Username must be between 5 and 15 characters!") String username) {
        this.username = username;
    }

    public @NotNull(message = "First name is required!") @Size(min = 3, max = 15, message = "First name must be between 3 and 15 characters!") String getFirstname() {
        return firstname;
    }

    public void setFirstname(@NotNull(message = "First name is required!") @Size(min = 3, max = 15, message = "First name must be between 3 and 15 characters!") String firstname) {
        this.firstname = firstname;
    }

    public @NotNull(message = "Last name is required!") @Size(min = 3, max = 15, message = "Last name must be between 3 and 15 characters!") String getLastname() {
        return lastname;
    }

    public void setLastname(@NotNull(message = "Last name is required!") @Size(min = 3, max = 15, message = "Last name must be between 3 and 15 characters!") String lastname) {
        this.lastname = lastname;
    }

    public @NotNull(message = "Email is required!") @Email(message = "Email should be valid!") String getEmail() {
        return email;
    }

    public void setEmail(@NotNull(message = "Email is required!") @Email(message = "Email should be valid!") String email) {
        this.email = email;
    }
}
