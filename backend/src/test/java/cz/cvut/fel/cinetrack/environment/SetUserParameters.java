/*
 * Created by minmin_tranova on 29.11.2025
 */

package cz.cvut.fel.cinetrack.environment;

import cz.cvut.fel.cinetrack.model.User;
import cz.cvut.fel.cinetrack.service.AvatarService;

public class SetUserParameters {

    private static AvatarService avatarService = new AvatarService();

    public static void setUserParameters(User user, String email, String username) {
        user.setUsername(username);
        user.setFirstname("Firstname");
        user.setLastname("Lastname");
        user.setEmail(email);
        user.setPassword("Test01");
        user.setAvatar(avatarService.getRandomAvatar());
    }
}
