package com.prueba.PruebaConcepto.service;

import com.prueba.PruebaConcepto.config.UserPrincipal;
import com.prueba.PruebaConcepto.entity.Administrador;
import com.prueba.PruebaConcepto.entity.ProfesionalDeSalud;
import com.prueba.PruebaConcepto.repository.AdministradorRepository;
import com.prueba.PruebaConcepto.repository.ProfesionalDeSaludRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final AdministradorRepository administradorRepository;
    private final ProfesionalDeSaludRepository profesionalDeSaludRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @Override
    public UserDetails loadUserByUsername(String cedula) throws UsernameNotFoundException {
        log.info("=== LOADING USER BY USERNAME ===");
        log.info("Searching for user with cedula: {}", cedula);
        
        // First try to find as administrator by cedula
        log.info("Checking AdministradorRepository for cedula: {}", cedula);
        Administrador admin = administradorRepository.findByCedula(cedula)
            .orElse(null);

        if (admin != null) {
            log.info("Found Administrador with cedula: {}, id: {}", cedula, admin.getId());
            log.info("Admin password hash present: {}", admin.getPassword() != null);
            log.info("Admin password hash (first 20 chars): {}", admin.getPassword() != null ? admin.getPassword().substring(0, Math.min(20, admin.getPassword().length())) : "NULL");
            String clinicaId = admin.getClinica() != null ? admin.getClinica().getId() : null;
            log.info("Admin clinicaId: {}", clinicaId);
            return UserPrincipal.create(
                admin.getId(),
                admin.getCedula(),
                admin.getPassword(),
                admin.getRole().name(),
                clinicaId
            );
        }

        log.info("Administrador not found, checking ProfesionalDeSaludRepository");
        // Then try to find as professional by cedula
        ProfesionalDeSalud profesional = profesionalDeSaludRepository.findByCedulaIdentidad(cedula)
            .orElse(null);

        if (profesional != null) {
            log.info("Found ProfesionalDeSalud with cedula: {}, id: {}", cedula, profesional.getIdProfesional());
            log.info("Profesional password hash present: {}", profesional.getPassword() != null);
            String clinicaId = profesional.getClinica() != null ? profesional.getClinica().getId() : null;
            log.info("Profesional clinicaId: {}", clinicaId);
            return UserPrincipal.create(
                profesional.getIdProfesional(),
                profesional.getCedulaIdentidad(),
                profesional.getPassword(),
                profesional.getRole().name(),
                clinicaId
            );
        }

        log.error("User not found with cedula: {}", cedula);
        throw new UsernameNotFoundException("Usuario no encontrado con cédula: " + cedula);
    }

    @Transactional
    public Administrador createAdminWithHashedPassword(Administrador admin, String rawPassword) {
        validatePasswordStrength(rawPassword);

        // Hash the password with salt using BCrypt
        String hashedPassword = passwordEncoder.encode(rawPassword);
        admin.setPassword(hashedPassword);

        return administradorRepository.save(admin);
    }

    @Transactional
    public Administrador updateAdminPassword(Administrador admin, String newRawPassword) {
        validatePasswordStrength(newRawPassword);

        String hashedPassword = passwordEncoder.encode(newRawPassword);
        admin.setPassword(hashedPassword);

        return administradorRepository.save(admin);
    }

    @Transactional
    public ProfesionalDeSalud createProfesionalWithHashedPassword(ProfesionalDeSalud profesional, String rawPassword) {
        validatePasswordStrength(rawPassword);

        // Hash the password with salt using BCrypt
        String hashedPassword = passwordEncoder.encode(rawPassword);
        profesional.setPassword(hashedPassword);

        return profesionalDeSaludRepository.save(profesional);
    }

    @Transactional
    public ProfesionalDeSalud updateProfesionalPassword(ProfesionalDeSalud profesional, String newRawPassword) {
        validatePasswordStrength(newRawPassword);

        String hashedPassword = passwordEncoder.encode(newRawPassword);
        profesional.setPassword(hashedPassword);

        return profesionalDeSaludRepository.save(profesional);
    }

    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private void validatePasswordStrength(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos una letra mayúscula");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos una letra minúscula");
        }
        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos un dígito");
        }
    }
}
