package com.achhecode.SpringBot.automation.typing;

public record TextTypingResponse(

        String status,

        String executionId,

        int characterCount,

        long executionTimeMs,

        TextTypingMode mode

) {
}