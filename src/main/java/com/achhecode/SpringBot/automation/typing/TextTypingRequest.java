package com.achhecode.SpringBot.automation.typing;

public record TextTypingRequest(
        String text,
        TextTypingMode mode
) {
}