package com.achhecode.SpringBot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ZipCommandExecutionException.class)
    public ResponseEntity<Map<String, Object>> handleKeyboardExecution(
            ZipCommandExecutionException ex
    ) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "status", "FAILED",
                        "executionId", ex.getExecutionId(),
                        "message", "Keyboard automation failed",
                        "timestamp", Instant.now().toString()
                ));
    }
}