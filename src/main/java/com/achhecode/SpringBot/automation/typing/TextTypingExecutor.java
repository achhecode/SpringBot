package com.achhecode.SpringBot.automation.typing;

public interface TextTypingExecutor {

    void type(
            String text,
            int delayMs,
            String executionId
    );
}