package com.achhecode.SpringBot.automation.keyboard;

import java.util.List;

public interface KeyboardExecutor {
    void execute(
        List<KeyboardCommand> commands,
        int delayMs,
        String executionId
    );
}