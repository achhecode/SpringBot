package com.achhecode.SpringBot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.achhecode.SpringBot.automation.typing.TextTypingRequest;
import com.achhecode.SpringBot.automation.typing.TextTypingResponse;
import com.achhecode.SpringBot.automation.typing.TextTypingService;

@RestController
@RequestMapping("/api/automation/typing")
public class TextTypingController {

    private final TextTypingService textTypingService;

    public TextTypingController(
            TextTypingService textTypingService
    ) {
        this.textTypingService = textTypingService;
    }

    @PostMapping
    public ResponseEntity<TextTypingResponse> type(
            @RequestBody TextTypingRequest request
    ) {

        TextTypingResponse response =
                textTypingService.type(request);

        return ResponseEntity.ok(response);
    }
}