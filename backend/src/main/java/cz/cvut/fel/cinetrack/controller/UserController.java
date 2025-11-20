/*
 * Created by minmin_tranova on 05.11.2025
 */

package cz.cvut.fel.cinetrack.controller;

import cz.cvut.fel.cinetrack.dto.user.request.ChangeUserAvatarRequestDTO;
import cz.cvut.fel.cinetrack.dto.user.request.ChangeUserPasswordRequestDTO;
import cz.cvut.fel.cinetrack.dto.user.request.EditUserProfileRequestDTO;
import cz.cvut.fel.cinetrack.dto.user.response.CombinedUserProfileResponseDTO;
import cz.cvut.fel.cinetrack.dto.user.response.UserProfileResponseDTO;
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
    public ResponseEntity<CombinedUserProfileResponseDTO> getCurrentUserProfile() {
        CombinedUserProfileResponseDTO profile = userService.getCombinedUserProfile();
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileResponseDTO> editUserProfile(
            @Valid @RequestBody EditUserProfileRequestDTO editUserProfileRequest
    ) {
        UserProfileResponseDTO response = userService.editUserProfile(editUserProfileRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changePassword(
            @Valid @RequestBody ChangeUserPasswordRequestDTO changeUserPasswordRequestDTO
    ) {
        userService.changeUserPassword(changeUserPasswordRequestDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/avatar")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changeAvatar(
            @Valid @RequestBody ChangeUserAvatarRequestDTO changeUserAvatarRequestDTO
    ) {
        userService.changeUserAvatar(changeUserAvatarRequestDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteUser() {
        userService.deleteCurrentUser();
        return ResponseEntity.ok().build();
    }
}
