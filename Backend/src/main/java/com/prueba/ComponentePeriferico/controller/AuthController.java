package com.prueba.PruebaConcepto.controller;

import com.prueba.PruebaConcepto.Dto.LoginRequestDto;
import com.prueba.PruebaConcepto.Dto.LoginResponseDto;
import com.prueba.PruebaConcepto.entity.User;
import com.prueba.PruebaConcepto.repository.UserRepository;
import com.prueba.PruebaConcepto.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(request.getPassword())) {
            String token = jwtUtil.generateToken(request.getEmail());
            return ResponseEntity.ok(new LoginResponseDto(token));
        }
        return ResponseEntity.status(401).body("Credenciales inválidas");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // El logout se maneja en frontend borrando el token
        return ResponseEntity.ok("Logout exitoso");
    }
}
