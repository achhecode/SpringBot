package com.achhecode.SpringBot.automation.sudoku;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class SudokuCommandParser {

    public List<SudokuCommand> parse(String instruction) {

        if (instruction == null || instruction.isBlank()) {
            throw new IllegalArgumentException(
                    "Sudoku instruction cannot be null or empty"
            );
        }

        String normalized = instruction.trim();

        return Arrays.stream(normalized.split(""))
                .map(this::parseCommand)
                .toList();
    }

    private SudokuCommand parseCommand(String command) {

        return switch (command) {
            case "1" -> SudokuCommand.ONE;
            case "2" -> SudokuCommand.TWO;
            case "3" -> SudokuCommand.THREE;
            case "4" -> SudokuCommand.FOUR;
            case "5" -> SudokuCommand.FIVE;
            case "6" -> SudokuCommand.SIX;
            case "7" -> SudokuCommand.SEVEN;
            case "8" -> SudokuCommand.EIGHT;
            case "9" -> SudokuCommand.NINE;

            default -> throw new IllegalArgumentException(
                    "Unsupported Sudoku command: " + command
            );
        };
    }
}