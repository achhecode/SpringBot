package com.achhecode.SpringBot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.achhecode.SpringBot.automation.zip.ZipCommand;
import com.achhecode.SpringBot.automation.zip.ZipCommandExecutor;
import com.achhecode.SpringBot.automation.zip.ZipCommandParser;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ZipCommandService {

    private final ZipCommandParser parser;
    private final ZipCommandExecutor zipCommandExecutor;

    public ZipCommandService(
            ZipCommandParser parser,
            ZipCommandExecutor zipCommandExecutor
    ) {
        this.parser = parser;
        this.zipCommandExecutor = zipCommandExecutor;
    }

    public void executeCommand(String instruction, int delayMs) {

        String executionId = UUID.randomUUID().toString();

        long startTime = System.currentTimeMillis();

        log.info(
                "Keyboard automation started. executionId={}, commandLength={}",
                executionId,
                instruction != null ? instruction.length() : 0
        );

        try {

            List<ZipCommand> commands = parser.parse(instruction);

            log.info(
                    "Keyboard instructions parsed. executionId={}, commandCount={}",
                    executionId,
                    commands.size()
            );

            zipCommandExecutor.execute(
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


    public String reverseInstruction(String instruction) {

        if (instruction == null || instruction.isBlank()) {
            return "";
        }

        String[] commands = instruction.split(",");

        StringBuilder result = new StringBuilder();

        for (int i = commands.length - 1; i >= 0; i--) {

            String command = commands[i].trim().toUpperCase();

            String reversedCommand = switch (command) {
                case "UP" -> "DOWN";
                case "DOWN" -> "UP";
                case "LEFT" -> "RIGHT";
                case "RIGHT" -> "LEFT";
                default -> throw new IllegalArgumentException(
                        "Unsupported keyboard command: " + command
                );
            };

            if (result.length() > 0) {
                result.append(",");
            }

            result.append(reversedCommand);
        }

        return result.toString();
    }
}