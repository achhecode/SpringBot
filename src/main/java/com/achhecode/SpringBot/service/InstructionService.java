package com.achhecode.SpringBot.service;

import org.springframework.stereotype.Service;


@Service
public class InstructionService {

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