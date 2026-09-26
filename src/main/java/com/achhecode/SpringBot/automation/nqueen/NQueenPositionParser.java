package com.achhecode.SpringBot.automation.nqueen;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NQueenPositionParser {

    public List<NQueenPosition> parse(String instruction) {

        if (instruction == null || instruction.isBlank()) {
            throw new IllegalArgumentException(
                    "N-Queen positions cannot be null or empty"
            );
        }

        String normalized = instruction
                .trim()
                .replace("[", "")
                .replace("]", "")
                .replace(" ", "");

        String[] positions = normalized.split(";");

        List<NQueenPosition> result = new ArrayList<>();

        for (String position : positions) {

            String[] coordinates = position.split(",");

            if (coordinates.length != 2) {
                throw new IllegalArgumentException(
                        "Invalid N-Queen position: " + position
                );
            }

            try {

                int row = Integer.parseInt(coordinates[0]);
                int col = Integer.parseInt(coordinates[1]);

                result.add(
                        new NQueenPosition(row, col)
                );

            } catch (NumberFormatException e) {

                throw new IllegalArgumentException(
                        "Invalid N-Queen position: " + position,
                        e
                );
            }
        }

        return result;
    }
}