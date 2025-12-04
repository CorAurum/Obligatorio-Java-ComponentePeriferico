package com.prueba.PruebaConcepto.controller;

import com.prueba.PruebaConcepto.Dto.LoginRequest;
import com.prueba.PruebaConcepto.Dto.LoginResponse;
import com.prueba.PruebaConcepto.config.JwtUtils;
import com.prueba.PruebaConcepto.config.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        log.info("=== LOGIN REQUEST RECEIVED ===");
        log.info("LoginRequest object: {}", loginRequest);
        log.info("Cedula: {}", loginRequest != null ? loginRequest.getCedula() : "NULL");
        log.info("Password: {}", loginRequest != null ? (loginRequest.getPassword() != null ? "***" : "NULL") : "NULL");

        try {
            if (loginRequest == null) {
                log.error("LoginRequest is NULL");
                return ResponseEntity.badRequest().body("Request body is null");
            }

            if (loginRequest.getCedula() == null || loginRequest.getCedula().isEmpty()) {
                log.error("Cedula is NULL or empty");
                return ResponseEntity.badRequest().body("Cédula is required");
            }

            if (loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
                log.error("Password is NULL or empty");
                return ResponseEntity.badRequest().body("Password is required");
            }

            log.info("Attempting authentication for cedula: {}", loginRequest.getCedula());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getCedula(),
                            loginRequest.getPassword()));

            log.info("Authentication successful for cedula: {}", loginRequest.getCedula());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            String refreshToken = jwtUtils.generateRefreshToken(authentication);

            UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();

            LoginResponse response = new LoginResponse(
                    jwt,
                    refreshToken,
                    "Bearer",
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getAuthorities().iterator().next().getAuthority().replace("ROLE_", ""),
                    userDetails.getClinicaId());

            log.info("Login successful for user: {}", userDetails.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("=== LOGIN ERROR ===");
            log.error("Exception type: {}", e.getClass().getName());
            log.error("Exception message: {}", e.getMessage());
            log.error("Exception stack trace: ", e);
            return ResponseEntity.badRequest().body("Cédula o contraseña inválida: " + e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody String refreshToken) {
        // Implementation for token refresh - can be enhanced later
        return ResponseEntity.ok().build();
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        try {
            String jwt = token.substring(7); // Remove "Bearer " prefix
            boolean isValid = jwtUtils.validateJwtToken(jwt);

            if (isValid) {
                return ResponseEntity.ok().body("Token válido");
            } else {
                return ResponseEntity.badRequest().body("Token inválido");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Formato de token inválido");
        }
    }
}
