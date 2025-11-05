/*
 * Created by minmin_tranova on 05.11.2025
 */

package cz.cvut.fel.cinetrack.dto.user.response;

public class CombinedUserProfile {

    private UserProfileResponse profile;
    private UserProfileHeaderResponse header;

    public CombinedUserProfile(UserProfileResponse profile,
                               UserProfileHeaderResponse header) {
        this.profile = profile;
        this.header = header;
    }

    public UserProfileResponse getProfile() {
        return profile;
    }

    public void setProfile(UserProfileResponse profile) {
        this.profile = profile;
    }

    public UserProfileHeaderResponse getHeader() {
        return header;
    }

    public void setHeader(UserProfileHeaderResponse header) {
        this.header = header;
    }
}
