package com.prueba.PruebaConcepto.controller;

import com.prueba.PruebaConcepto.Dto.AuthProfileResponse;
import com.prueba.PruebaConcepto.Dto.OidcForwardRequest;
import com.prueba.PruebaConcepto.Dto.OidcForwardResponse;
import com.prueba.PruebaConcepto.entity.Administrador;
import com.prueba.PruebaConcepto.entity.ProfesionalDeSalud;
import com.prueba.PruebaConcepto.repository.AdministradorRepository;
import com.prueba.PruebaConcepto.repository.ProfesionalDeSaludRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController - Handles OIDC authentication forwarded from Central Component
 * 
 * This controller receives OIDC authentication data from the Central Component
 * (HCEN)
 * after a user authenticates via gub.uy. It looks up the user in the local
 * database
 * and returns the necessary information for redirect to the appropriate portal.
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

        private final AdministradorRepository administradorRepository;
        private final ProfesionalDeSaludRepository profesionalDeSaludRepository;

        // @Value("${central.api.secret:}")
        private String centralApiSecret = "myOtherSecretKeyThatShouldBeAtLeast256BitsLongForHS256AlgorithmAndChangedInProduction";

        /**
         * OIDC Forward endpoint - receives authentication data from Central Component
         * 
         * This endpoint is called server-to-server from the Central Backend after
         * a user authenticates via gub.uy OIDC. It looks up the user by cedula
         * in both Administrador and ProfesionalDeSalud tables to determine:
         * 1. If the user exists in this peripheral system
         * 2. What role they have (ADMINISTRADOR or PROFESIONAL)
         * 3. Which clinic (dominioSubdominio) they belong to
         * 
         * @param secret  The shared secret for authentication (X-Central-Secret header)
         * @param request The OIDC data forwarded from Central
         * @return OidcForwardResponse with user data or error
         */
        @PostMapping("/oidc-forward")
        public ResponseEntity<?> handleOidcForward(
                        @RequestHeader(value = "X-Central-Secret", required = false) String secret,
                        @RequestBody OidcForwardRequest request) {

                log.info("=== OIDC FORWARD REQUEST RECEIVED ===");
                log.info("Cedula: {}", request != null ? request.getCedula() : "NULL");
                log.info("Has idToken: {}", request != null && request.getIdToken() != null);
                log.info("Has accessToken: {}", request != null && request.getAccessToken() != null);
                log.info("======================================");

                // Validate shared secret if configured
                if (centralApiSecret != null && !centralApiSecret.isEmpty()) {
                        if (secret == null || !centralApiSecret.equals(secret)) {
                                log.warn("Invalid or missing X-Central-Secret header");
                                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                                .body(OidcForwardResponse.error("Invalid authentication"));
                        }
                }

                // Validate request
                if (request == null) {
                        log.error("Request body is null");
                        return ResponseEntity.badRequest()
                                        .body(OidcForwardResponse.error("Request body is required"));
                }

                String cedula = request.getCedula();
                if (cedula == null || cedula.trim().isEmpty()) {
                        log.error("Cedula is null or empty");
                        return ResponseEntity.badRequest()
                                        .body(OidcForwardResponse.error("Cedula is required"));
                }

                cedula = cedula.trim();
                log.info("Looking up user with cedula: {}", cedula);

                // First, try to find in Administrador table
                Administrador admin = administradorRepository.findByCedula(cedula).orElse(null);
                if (admin != null) {
                        log.info("Found user as ADMINISTRADOR: {} {}", admin.getNombre(), admin.getApellido());

                        String dominioSubdominio = admin.getClinica() != null
                                        ? admin.getClinica().getDominioSubdominio()
                                        : null;

                        if (dominioSubdominio == null && admin.getClinica() != null) {
                                dominioSubdominio = String.valueOf(admin.getClinica().getId());
                        }

                        log.info("Admin clinic dominioSubdominio: {}", dominioSubdominio);

                        OidcForwardResponse response = OidcForwardResponse.builder()
                                        .success(true)
                                        .role("ADMINISTRADOR")
                                        .userId(String.valueOf(admin.getId()))
                                        .cedula(cedula)
                                        .displayName(admin.getNombre() + " " + admin.getApellido())
                                        .dominioSubdominio(dominioSubdominio)
                                        .idToken(request.getIdToken())
                                        .accessToken(request.getAccessToken())
                                        .build();

                        return ResponseEntity.ok(response);
                }

                // Then, try to find in ProfesionalDeSalud table
                ProfesionalDeSalud profesional = profesionalDeSaludRepository
                                .findByCedulaIdentidad(cedula).orElse(null);
                if (profesional != null) {
                        log.info("Found user as PROFESIONAL: {} {}", profesional.getNombre(),
                                        profesional.getApellido());

                        String dominioSubdominio = profesional.getClinica() != null
                                        ? profesional.getClinica().getDominioSubdominio()
                                        : null;

                        if (dominioSubdominio == null && profesional.getClinica() != null) {
                                dominioSubdominio = String.valueOf(profesional.getClinica().getId());
                        }

                        log.info("Professional clinic dominioSubdominio: {}", dominioSubdominio);

                        String profesionalId = profesional.getIdProfesional();
                        if (profesionalId == null) {
                                profesionalId = cedula; // Fallback to cedula if no professional ID
                        }

                        OidcForwardResponse response = OidcForwardResponse.builder()
                                        .success(true)
                                        .role("PROFESIONAL")
                                        .userId(profesionalId)
                                        .cedula(cedula)
                                        .displayName(profesional.getNombre() + " " + profesional.getApellido())
                                        .dominioSubdominio(dominioSubdominio)
                                        .idToken(request.getIdToken())
                                        .accessToken(request.getAccessToken())
                                        .build();

                        return ResponseEntity.ok(response);
                }

                // User not found in either table
                log.warn("User with cedula {} not found in Administrador or ProfesionalDeSalud tables", cedula);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(OidcForwardResponse.error("Usuario no registrado en el sistema periférico"));
        }

        /**
         * Get current user profile by cedula
         * 
         * This endpoint is used by the frontend to get detailed user profile
         * information
         * after authentication. It can be called with just the cedula to look up the
         * user.
         * 
         * @param cedula       The user's cedula
         * @param tenantHeader Optional tenant header for multi-tenant validation
         * @return User profile information
         */
        @GetMapping("/me")
        public ResponseEntity<?> getCurrentProfile(
                        @RequestParam("cedula") String cedula,
                        @RequestHeader(value = "X-Tenant-Id", required = false) String tenantHeader) {

                if (cedula == null || cedula.trim().isEmpty()) {
                        return ResponseEntity.badRequest().body("La cédula es obligatoria");
                }
                String cedulaNormalized = cedula.trim();

                // Try Administrador first
                Administrador admin = administradorRepository.findByCedula(cedulaNormalized).orElse(null);
                if (admin != null) {
                        String clinicaDominio = admin.getClinica() != null ? admin.getClinica().getDominioSubdominio()
                                        : null;
                        String clinicaId = admin.getClinica() != null ? String.valueOf(admin.getClinica().getId())
                                        : null;
                        String resolvedClinicaId = clinicaDominio != null ? clinicaDominio : clinicaId;

                        // Validate tenant if header is provided
                        if (tenantHeader != null && resolvedClinicaId != null
                                        && !(resolvedClinicaId.equalsIgnoreCase(tenantHeader)
                                                        || (clinicaId != null && tenantHeader.equals(clinicaId)))) {
                                return ResponseEntity.status(404).body("Administrador no encontrado en el tenant");
                        }

                        AuthProfileResponse resp = AuthProfileResponse.builder()
                                        .id(String.valueOf(admin.getId()))
                                        .username(admin.getUsuario() != null ? admin.getUsuario() : admin.getCedula())
                                        .role("ADMINISTRADOR")
                                        .clinicaId(resolvedClinicaId)
                                        .dominioSubdominio(
                                                        admin.getClinica() != null
                                                                        ? admin.getClinica().getDominioSubdominio()
                                                                        : resolvedClinicaId)
                                        .cedula(admin.getCedula())
                                        .displayName(admin.getNombre() + " " + admin.getApellido())
                                        .email(admin.getEmail())
                                        .telefono(null)
                                        .activo(admin.isActivo())
                                        .especialidades(List.of())
                                        .build();
                        return ResponseEntity.ok(resp);
                }

                // Then try ProfesionalDeSalud
                ProfesionalDeSalud profesional = profesionalDeSaludRepository.findByCedulaIdentidad(cedulaNormalized)
                                .orElse(null);
                if (profesional != null) {
                        String clinicaDominio = profesional.getClinica() != null
                                        ? profesional.getClinica().getDominioSubdominio()
                                        : null;
                        String clinicaId = profesional.getClinica() != null
                                        ? String.valueOf(profesional.getClinica().getId())
                                        : null;
                        String resolvedClinicaId = clinicaDominio != null ? clinicaDominio : clinicaId;

                        // Validate tenant if header is provided
                        if (tenantHeader != null && resolvedClinicaId != null
                                        && !(resolvedClinicaId.equalsIgnoreCase(tenantHeader)
                                                        || (clinicaId != null && tenantHeader.equals(clinicaId)))) {
                                return ResponseEntity.status(404).body("Profesional no encontrado en el tenant");
                        }

                        List<AuthProfileResponse.EspecialidadSummary> especialidades = profesional
                                        .getEspecialidades() != null
                                                        ? profesional.getEspecialidades().stream()
                                                                        .map(e -> new AuthProfileResponse.EspecialidadSummary(
                                                                                        e.getId(), e.getNombre()))
                                                                        .toList()
                                                        : List.of();

                        String profesionalId = profesional.getIdProfesional();

                        AuthProfileResponse resp = AuthProfileResponse.builder()
                                        .id(profesionalId != null ? profesionalId : cedulaNormalized)
                                        .username(profesional.getCedulaIdentidad())
                                        .role("PROFESIONAL")
                                        .clinicaId(resolvedClinicaId)
                                        .dominioSubdominio(
                                                        profesional.getClinica() != null
                                                                        ? profesional.getClinica()
                                                                                        .getDominioSubdominio()
                                                                        : resolvedClinicaId)
                                        .cedula(profesional.getCedulaIdentidad())
                                        .displayName(profesional.getNombre() + " " + profesional.getApellido())
                                        .email(profesional.getEmail())
                                        .telefono(profesional.getTelefono())
                                        .activo(profesional.isActivo())
                                        .especialidades(especialidades)
                                        .build();
                        return ResponseEntity.ok(resp);
                }

                return ResponseEntity.status(404).body("Usuario no encontrado");
        }
}
