package com.achhecode.SpringBot.automation.keyboard;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class KeyboardCommandParser {

    public List<KeyboardCommand> parse(String input) {

        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Keyboard instruction cannot be empty");
        }

        return Arrays.stream(input.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .map(this::parseCommand)
                .toList();
    }

    private KeyboardCommand parseCommand(String command) {
        try {
            return KeyboardCommand.valueOf(command);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Unsupported keyboard command: " + command
            );
        }
    }
}