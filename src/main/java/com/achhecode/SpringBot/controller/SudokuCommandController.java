package com.achhecode.SpringBot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.achhecode.SpringBot.automation.sudoku.SudokuCommandService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/sudoku")
@RequiredArgsConstructor 
public class SudokuCommandController {

    private final SudokuCommandService sudokuCommandService;


    // POST http://localhost:8080/api/sudoku/execute?instruction=123312231&delayMs=50
    @PostMapping("/execute")
    public ResponseEntity<Map<String, Object>> execute(
            @RequestParam String instruction,
            @RequestParam(defaultValue = "50") int delayMs
    ) {

        String executionId =
                UUID.randomUUID().toString();

        sudokuCommandService.executeCommand(
                instruction,
                delayMs,
                executionId
        );

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "executionId", executionId,
                        "message",
                        "Sudoku keyboard automation executed"
                )
        );
    }
}