package com.achhecode.SpringBot.controller;

import com.achhecode.SpringBot.automation.nqueen.NQueenCommandService;
import com.achhecode.SpringBot.automation.nqueen.NQueenRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/nqueen")
@RequiredArgsConstructor
public class NQueenCommandController {

    private final NQueenCommandService nQueenCommandService;

    /*
     * Example:
     *
     * POST
     * /api/nqueen/execute?n=9&positions=0,8;1,1;2,4;3,7;4,0;5,3;6,6;7,2;8,5
     */

    @PostMapping("/execute")
    public ResponseEntity<Map<String, Object>> execute(
                @RequestBody NQueenRequest request) {

        String executionId = UUID.randomUUID().toString();

        int n = nQueenCommandService.executeCommand(
                request.positions(),
                executionId
        );

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "executionId", executionId,
                        "n", n,
                        "message", "N-Queen keyboard automation executed"
                )
        );
        }
}