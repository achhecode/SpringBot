package com.achhecode.SpringBot.controller;

import com.achhecode.SpringBot.automation.keyboard.monitoring.KeyboardMonitorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/automation/keyboard/track")
public class KeyboardMonitorController {

    private final KeyboardMonitorService keyboardMonitorService;

    public KeyboardMonitorController(
            KeyboardMonitorService keyboardMonitorService
    ) {
        this.keyboardMonitorService = keyboardMonitorService;
    }

    @PostMapping("/start")
    public Map<String, Object> start() {

        keyboardMonitorService.startTracking();

        return Map.of(
                "status", "TRACKING",
                "message", "Keyboard tracking started"
        );
    }

    @PostMapping("/stop")
    public Map<String, Object> stop() {

        List<String> commands =
                keyboardMonitorService.stopTracking();

        return Map.of(
                "status", "STOPPED",
                "commandCount", commands.size(),
                "commands", commands,
                "instruction", String.join(",", commands)
        );
    }

    @GetMapping
    public Map<String, Object> status() {

        List<String> commands =
                keyboardMonitorService.getRecordedCommands();

        return Map.of(
                "tracking",
                keyboardMonitorService.isTracking(),
                "commandCount", commands.size(),
                "commands", commands,
                "instruction", String.join(",", commands)
        );
    }
}