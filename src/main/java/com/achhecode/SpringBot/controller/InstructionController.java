package com.achhecode.SpringBot.controller;

import com.achhecode.SpringBot.service.InstructionService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/helper")
public class InstructionController {

    private final InstructionService instructionService;

    public InstructionController(InstructionService instructionService) {
        this.instructionService = instructionService;
    }

    // GET /api/helper/reverse?instruction=UP,UP,UP,DOWN,LEFT,LEFT,RIGHT

    @GetMapping("/reverse")
    public Map<String, String> reverse(
            @RequestParam String instruction
    ) {
        return Map.of(
                "instruction", instruction,
                "reversed", instructionService.reverseInstruction(instruction)
        );
    }
}