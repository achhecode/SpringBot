package com.achhecode.SpringBot.controller;

import com.achhecode.SpringBot.service.AutomationService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

@Slf4j 
@RestController
@RequestMapping("/api/automation")
public class ArrowAutomationController {

    private final AutomationService automationService;

    public ArrowAutomationController(AutomationService automationService) {
        this.automationService = automationService;
    }


    // POST /api/automation/keyboard?instruction=UP,UP,UP,DOWN,LEFT,LEFT,RIGHT

    // POST /api/automation/keyboard?instruction=UP,UP,UP,DOWN,LEFT,LEFT,RIGHT&delayMS=10
    @PostMapping("/keyboard")
    public String keyboard(
            @RequestParam String instruction,
            @RequestParam(defaultValue = "0") int delayMS
    ) {

        automationService.executeKeyboard(instruction, delayMS);

        return "Keyboard automation executed";
    }
}
