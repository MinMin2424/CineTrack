/*
 * Created by minmin_tranova on 05.11.2025
 */

package cz.cvut.fel.cinetrack.dto.user.request;

import jakarta.validation.constraints.NotNull;

public class ChangeUserAvatarRequest {

    @NotNull(message = "Avatar is required!")
    private String avatar;

    public ChangeUserAvatarRequest(String avatar) {
        this.avatar = avatar;
    }

    public @NotNull(message = "Avatar is required!") String getAvatar() {
        return avatar;
    }

    public void setAvatar(@NotNull(message = "Avatar is required!") String avatar) {
        this.avatar = avatar;
    }
}
