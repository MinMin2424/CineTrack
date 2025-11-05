/*
 * Created by minmin_tranova on 05.11.2025
 */

package cz.cvut.fel.cinetrack.controller;

import cz.cvut.fel.cinetrack.dto.user.request.ChangeUserAvatarRequest;
import cz.cvut.fel.cinetrack.dto.user.request.ChangeUserPasswordRequest;
import cz.cvut.fel.cinetrack.dto.user.request.EditUserProfileRequest;
import cz.cvut.fel.cinetrack.dto.user.response.CombinedUserProfile;
import cz.cvut.fel.cinetrack.dto.user.response.UserProfileResponse;
import cz.cvut.fel.cinetrack.security.AuthenticationResponse;
import cz.cvut.fel.cinetrack.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/me")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<CombinedUserProfile> getCurrentUserProfile() {
        CombinedUserProfile profile = userService.getCombinedUserProfile();
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileResponse> editUserProfile(
            @Valid @RequestBody EditUserProfileRequest editUserProfileRequest
    ) {
        UserProfileResponse response = userService.editUserProfile(editUserProfileRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changePassword(
            @Valid @RequestBody ChangeUserPasswordRequest changeUserPasswordRequest
    ) {
        userService.changeUserPassword(changeUserPasswordRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/avatar")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changeAvatar(
            @Valid @RequestBody ChangeUserAvatarRequest changeUserAvatarRequest
    ) {
        userService.changeUserAvatar(changeUserAvatarRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteUser() {
        userService.deleteCurrentUser();
        return ResponseEntity.ok().build();
    }
}
