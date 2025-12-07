package com.prueba.PruebaConcepto.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("=== JSON PARSING ERROR ===");
        log.error("Error parsing request body: {}", e.getMessage());
        Throwable rootCause = e.getRootCause();
        log.error("Root cause: {}", rootCause != null ? rootCause.getMessage() : "N/A");
        return ResponseEntity.badRequest()
                .body("Formato del cuerpo de la solicitud inválido. Verifique la estructura JSON.");
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException e) {
        log.warn("Handled ResponseStatusException: {} {}", e.getStatusCode(), e.getReason());
        String body = e.getReason() != null ? e.getReason() : e.getMessage();
        return ResponseEntity.status(e.getStatusCode()).body(body);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleGenericException(Exception e) {
        log.error("=== UNHANDLED EXCEPTION ===");
        log.error("Exception type: {}", e.getClass().getName());
        log.error("Exception message: {}", e.getMessage());
        log.error("Stack trace: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ocurrió un error interno. Intente nuevamente o contacte al administrador.");
    }
}
