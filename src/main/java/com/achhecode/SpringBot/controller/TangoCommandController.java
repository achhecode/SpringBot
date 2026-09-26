package com.achhecode.SpringBot.controller;

import com.achhecode.SpringBot.automation.tango.TangoCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/tango")
@RequiredArgsConstructor
public class TangoCommandController {

    private final TangoCommandService tangoCommandService;

    // POST http://localhost:8080/api/tango/execute?instruction=SMSMMSMS
    @PostMapping("/execute")
    public ResponseEntity<Map<String, Object>> execute(
            @RequestParam String instruction
    ) {

        String executionId = UUID.randomUUID().toString();

        tangoCommandService.executeCommand(
                instruction,
                executionId
        );

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "executionId", executionId,
                        "message", "Tango keyboard automation executed"
                )
        );
    }
}