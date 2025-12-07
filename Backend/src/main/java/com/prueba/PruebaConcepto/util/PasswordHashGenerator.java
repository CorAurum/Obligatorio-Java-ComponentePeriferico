package com.prueba.PruebaConcepto.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utility class to generate BCrypt password hashes for seed data
 * Run this main method to generate hashes for your seed file
 */
public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        
        System.out.println("=== BCrypt Password Hashes ===");
        
        String password = "password123";
        System.out.println("Hash: " + encoder.encode(password));
    }
}

