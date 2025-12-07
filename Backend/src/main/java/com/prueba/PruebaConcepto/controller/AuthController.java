package com.prueba.PruebaConcepto.controller;

import com.prueba.PruebaConcepto.Dto.AuthProfileResponse;
import com.prueba.PruebaConcepto.Dto.LoginRequest;
import com.prueba.PruebaConcepto.Dto.LoginResponse;
import com.prueba.PruebaConcepto.config.JwtUtils;
import com.prueba.PruebaConcepto.config.UserPrincipal;
import com.prueba.PruebaConcepto.entity.Administrador;
import com.prueba.PruebaConcepto.entity.ProfesionalDeSalud;
import com.prueba.PruebaConcepto.repository.AdministradorRepository;
import com.prueba.PruebaConcepto.repository.ProfesionalDeSaludRepository;
import java.util.List;
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
    private final AdministradorRepository administradorRepository;
    private final ProfesionalDeSaludRepository profesionalDeSaludRepository;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        log.info("=== LOGIN REQUEST RECEIVED ===");
        log.info("LoginRequest object: {}", loginRequest);
        log.info("Cedula: {}", loginRequest != null ? loginRequest.getCedula() : "NULL");
        log.info("Password: {}", loginRequest != null ? (loginRequest.getPassword() != null ? "***" : "NULL") : "NULL");

        try {
            if (loginRequest == null) {
                log.error("LoginRequest is NULL");
                return ResponseEntity.badRequest().body("El cuerpo de la solicitud está vacío");
            }

            if (loginRequest.getCedula() == null || loginRequest.getCedula().isEmpty()) {
                log.error("Cedula is NULL or empty");
                return ResponseEntity.badRequest().body("La cédula es obligatoria");
            }

            if (loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
                log.error("Password is NULL or empty");
                return ResponseEntity.badRequest().body("La contraseña es obligatoria");
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

            // Resolve dominioSubdominio from the associated clinic to expose it explicitly
            // to the frontend
            String dominioSubdominio = null;
            Administrador admin = administradorRepository.findByCedula(userDetails.getUsername()).orElse(null);
            if (admin != null && admin.getClinica() != null) {
                dominioSubdominio = admin.getClinica().getDominioSubdominio();
            } else {
                ProfesionalDeSalud profesional = profesionalDeSaludRepository
                        .findByCedulaIdentidad(userDetails.getUsername())
                        .orElse(null);
                if (profesional != null && profesional.getClinica() != null) {
                    dominioSubdominio = profesional.getClinica().getDominioSubdominio();
                }
            }
            if (dominioSubdominio == null) {
                dominioSubdominio = userDetails.getClinicaId(); // fallback to whatever is stored in the principal
            }

            LoginResponse response = new LoginResponse(
                    jwt,
                    refreshToken,
                    "Bearer",
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getAuthorities().iterator().next().getAuthority().replace("ROLE_", ""),
                    userDetails.getClinicaId(),
                    dominioSubdominio);

            log.info("Login successful for user: {}", userDetails.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("=== LOGIN ERROR ===");
            log.error("Exception type: {}", e.getClass().getName());
            log.error("Exception message: {}", e.getMessage());
            log.error("Exception stack trace: ", e);
            return ResponseEntity.badRequest().body("Cédula o contraseña inválida");
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

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentProfile(
            @RequestHeader(value = "X-Tenant-Id", required = false) String tenantHeader) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
            return ResponseEntity.status(401).body("No autenticado");
        }

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String role = principal.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
        String clinicaId = principal.getClinicaId();

        // Tenant check if header provided
        if (tenantHeader != null && clinicaId != null && !clinicaId.equalsIgnoreCase(tenantHeader)) {
            return ResponseEntity.status(403).body("El tenant no coincide con la clínica del usuario");
        }

        if ("ADMINISTRADOR".equalsIgnoreCase(role)) {
            Administrador admin = administradorRepository.findByCedula(principal.getUsername())
                    .orElse(null);
            if (admin == null || (clinicaId != null && admin.getClinica() != null
                    && !(clinicaId.equalsIgnoreCase(admin.getClinica().getDominioSubdominio())
                            || clinicaId.equals(admin.getClinica().getId())))) {
                return ResponseEntity.status(404).body("Administrador no encontrado en el tenant");
            }

            AuthProfileResponse resp = AuthProfileResponse.builder()
                    .id(String.valueOf(admin.getId()))
                    .username(admin.getUsuario())
                    .role(role)
                    .clinicaId(clinicaId)
                    .dominioSubdominio(
                            admin.getClinica() != null ? admin.getClinica().getDominioSubdominio() : clinicaId)
                    .cedula(admin.getCedula())
                    .displayName(admin.getNombre() + " " + admin.getApellido())
                    .email(admin.getEmail())
                    .telefono(null)
                    .activo(admin.isActivo())
                    .especialidades(List.of())
                    .build();
            return ResponseEntity.ok(resp);
        } else if ("PROFESIONAL".equalsIgnoreCase(role)) {
            ProfesionalDeSalud profesional = profesionalDeSaludRepository.findByCedulaIdentidad(principal.getUsername())
                    .orElse(null);
            if (profesional == null || (clinicaId != null && profesional.getClinica() != null
                    && !(clinicaId.equalsIgnoreCase(profesional.getClinica().getDominioSubdominio())
                            || clinicaId.equals(profesional.getClinica().getId())))) {
                return ResponseEntity.status(404).body("Profesional no encontrado en el tenant");
            }

            List<AuthProfileResponse.EspecialidadSummary> especialidades = profesional.getEspecialidades() != null
                    ? profesional.getEspecialidades().stream()
                            .map(e -> new AuthProfileResponse.EspecialidadSummary(e.getId(), e.getNombre()))
                            .toList()
                    : List.of();

            String profesionalId = profesional.getIdProfesional();

            AuthProfileResponse resp = AuthProfileResponse.builder()
                    // Ensure ID is always present; fallback to principal ID if entity field is
                    // missing
                    .id(profesionalId != null ? profesionalId : principal.getId())
                    .username(profesional.getCedulaIdentidad())
                    .role(role)
                    .clinicaId(clinicaId)
                    .dominioSubdominio(profesional.getClinica() != null
                            ? profesional.getClinica().getDominioSubdominio()
                            : clinicaId)
                    .cedula(profesional.getCedulaIdentidad())
                    .displayName(profesional.getNombre() + " " + profesional.getApellido())
                    .email(profesional.getEmail())
                    .telefono(profesional.getTelefono())
                    .activo(profesional.isActivo())
                    .especialidades(especialidades)
                    .build();
            return ResponseEntity.ok(resp);
        } else {
            return ResponseEntity.status(403).body("Rol no soportado: " + role);
        }
    }
}
