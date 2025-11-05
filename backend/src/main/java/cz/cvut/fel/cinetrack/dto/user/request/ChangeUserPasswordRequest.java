/*
 * Created by minmin_tranova on 05.11.2025
 */

package cz.cvut.fel.cinetrack.dto.user.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ChangeUserPasswordRequest {

    @NotNull(message = "Password is required!")
    @Size(min = 6, message = "Password must be at least 6 characters!")
    private String password;

    public ChangeUserPasswordRequest(String password) {
        this.password = password;
    }

    public @NotNull(message = "Password is required!") @Size(min = 6, message = "Password must be at least 6 characters!") String getPassword() {
        return password;
    }

    public void setPassword(@NotNull(message = "Password is required!") @Size(min = 6, message = "Password must be at least 6 characters!") String password) {
        this.password = password;
    }
}
