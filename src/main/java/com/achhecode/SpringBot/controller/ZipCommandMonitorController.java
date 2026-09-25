package com.achhecode.SpringBot.controller;

import org.springframework.web.bind.annotation.*;

import com.achhecode.SpringBot.automation.zip.monitoring.ZipCommandMonitorService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/zip/command/track")
public class ZipCommandMonitorController {

    private final ZipCommandMonitorService zipCommandMonitorService;

    public ZipCommandMonitorController(
            ZipCommandMonitorService zipCommandMonitorService
    ) {
        this.zipCommandMonitorService = zipCommandMonitorService;
    }



    // POST /api/zip/command/track/start

    @PostMapping("/start")
    public Map<String, Object> start() {

        zipCommandMonitorService.startTracking();

        return Map.of(
                "status", "TRACKING",
                "message", "Keyboard tracking started"
        );
    }

    // POST /api/zip/command/track/stop

    @PostMapping("/stop")
    public Map<String, Object> stop() {

        List<String> commands =
                zipCommandMonitorService.stopTracking();

        return Map.of(
                "status", "STOPPED",
                "commandCount", commands.size(),
                "commands", commands,
                "instruction", String.join(",", commands)
        );
    }

    // GET /api/zip/command/track


    @GetMapping
    public Map<String, Object> status() {

        List<String> commands =
                zipCommandMonitorService.getRecordedCommands();

        return Map.of(
                "tracking",
                zipCommandMonitorService.isTracking(),
                "commandCount", commands.size(),
                "commands", commands,
                "instruction", String.join(",", commands)
        );
    }
}