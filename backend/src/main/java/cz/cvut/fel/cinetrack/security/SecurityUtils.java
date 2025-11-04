/*
 * Created by minmin_tranova on 04.11.2025
 */

package cz.cvut.fel.cinetrack.security;

import cz.cvut.fel.cinetrack.model.User;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static CustomUserDetails getCurrentUserDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return null;
        Object principal = auth.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal);
        }
        return null;
    }

    public static User getCurrentUser() {
        CustomUserDetails userDetails = getCurrentUserDetails();
        if (userDetails == null || userDetails.getUser() == null) {
            throw new AuthenticationCredentialsNotFoundException("User is not logged in");
        }
        return userDetails.getUser();
    }

    public static Long getCurrentUserId() {
        User user = getCurrentUser();
        return user == null ? null : user.getId();
    }

}
