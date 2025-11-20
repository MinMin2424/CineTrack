/*
 * Created by minmin_tranova on 05.11.2025
 */

package cz.cvut.fel.cinetrack.dto.user.response;

public class CombinedUserProfileResponseDTO {

    private UserProfileResponseDTO profile;
    private UserProfileHeaderResponseDTO header;

    public CombinedUserProfileResponseDTO(UserProfileResponseDTO profile,
                                          UserProfileHeaderResponseDTO header) {
        this.profile = profile;
        this.header = header;
    }

    public UserProfileResponseDTO getProfile() {
        return profile;
    }

    public void setProfile(UserProfileResponseDTO profile) {
        this.profile = profile;
    }

    public UserProfileHeaderResponseDTO getHeader() {
        return header;
    }

    public void setHeader(UserProfileHeaderResponseDTO header) {
        this.header = header;
    }
}
