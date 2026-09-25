package com.achhecode.SpringBot.service;

import com.achhecode.SpringBot.automation.keyboard.KeyboardCommand;
import com.achhecode.SpringBot.automation.keyboard.KeyboardCommandParser;
import com.achhecode.SpringBot.automation.keyboard.KeyboardExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class AutomationService {

    private final KeyboardCommandParser parser;
    private final KeyboardExecutor keyboardExecutor;

    public AutomationService(
            KeyboardCommandParser parser,
            KeyboardExecutor keyboardExecutor
    ) {
        this.parser = parser;
        this.keyboardExecutor = keyboardExecutor;
    }

    public void executeKeyboard(String instruction, int delayMs) {

        String executionId = UUID.randomUUID().toString();

        long startTime = System.currentTimeMillis();

        log.info(
                "Keyboard automation started. executionId={}, commandLength={}",
                executionId,
                instruction != null ? instruction.length() : 0
        );

        try {

            List<KeyboardCommand> commands = parser.parse(instruction);

            log.info(
                    "Keyboard instructions parsed. executionId={}, commandCount={}",
                    executionId,
                    commands.size()
            );

            keyboardExecutor.execute(
                    commands,
                    delayMs,
                    executionId
            );

            long duration = System.currentTimeMillis() - startTime;

            log.info(
                    "Keyboard automation completed successfully. executionId={}, commandCount={}, durationMs={}",
                    executionId,
                    commands.size(),
                    duration
            );

        } catch (Exception e) {

            long duration = System.currentTimeMillis() - startTime;

            log.error(
                    "Keyboard automation failed. executionId={}, durationMs={}, error={}",
                    executionId,
                    duration,
                    e.getMessage(),
                    e
            );

            throw e;
        }
    }
}