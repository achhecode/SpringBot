package com.achhecode.SpringBot.controller;

import com.achhecode.SpringBot.service.ZipCommandService;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/zip")
public class ZipCommandController {

    private final ZipCommandService zipCommandService;

    public ZipCommandController(ZipCommandService zipCommandService) {
        this.zipCommandService = zipCommandService;
    }

    // POST /api/zip/command?instruction=UP,UP,UP,DOWN,LEFT,LEFT,RIGHT

    // POST /api/zip/command?instruction=UP,UP,UP,DOWN,LEFT,LEFT,RIGHT&delayMS=10
    @PostMapping("/command")
    public String keyboard(
            @RequestParam String instruction,
            @RequestParam(defaultValue = "0") int delayMS) {

        zipCommandService.executeCommand(instruction, delayMS);

        return "Keyboard automation executed";
    }

    @GetMapping("/command/reverse")
    public Map<String, String> reverse(
            @RequestParam String instruction) {
        return Map.of(
                "instruction", instruction,
                "reversed", zipCommandService.reverseInstruction(instruction));
    }
}
