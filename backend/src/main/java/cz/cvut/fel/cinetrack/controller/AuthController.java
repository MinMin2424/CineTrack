/*
 * Created by minmin_tranova on 04.11.2025
 */

package cz.cvut.fel.cinetrack.controller;

import cz.cvut.fel.cinetrack.dto.auth.LoginRequest;
import cz.cvut.fel.cinetrack.dto.auth.RegisterRequest;
import cz.cvut.fel.cinetrack.dto.auth.ResetPasswordRequestDTO;
import cz.cvut.fel.cinetrack.dto.auth.VerifyUserRequestDTO;
import cz.cvut.fel.cinetrack.exception.user.UserNotFoundException;
import cz.cvut.fel.cinetrack.model.User;
import cz.cvut.fel.cinetrack.security.AuthenticationResponse;
import cz.cvut.fel.cinetrack.security.JwtService;
import cz.cvut.fel.cinetrack.service.AuthService;
import cz.cvut.fel.cinetrack.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthController(AuthService authService, JwtService jwtService, UserService userService) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/logout")
//    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logout() {
        authService.logout();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");

        if (refreshToken == null || !jwtService.isTokenValid(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String type = jwtService.extractClaims(refreshToken, c -> c.get("type", String.class));
        if (!"refresh".equals(type)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = jwtService.extractEmail(refreshToken);
        User user = userService.getUserByEmail(email);

        String newAccessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        return ResponseEntity.ok(new AuthenticationResponse(
                newAccessToken,
                newRefreshToken,
                jwtService.getExpirationTime(newAccessToken)
        ));
    }

    @PostMapping("/forgot-password/verify")
    public ResponseEntity<?> verifyUserForReset(
            @Valid @RequestBody VerifyUserRequestDTO request
    ) {
        try {
            userService.verifyUserForPasswordReset(request);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/forgot-password/reset")
    public ResponseEntity<?> resetPassword(
            @Valid @RequestBody ResetPasswordRequestDTO request
    ) {
        try {
            userService.resetPassword(request);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}
