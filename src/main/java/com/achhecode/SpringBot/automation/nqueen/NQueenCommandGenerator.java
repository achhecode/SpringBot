package com.achhecode.SpringBot.automation.nqueen;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class NQueenCommandGenerator {

    public List<NQueenInputCommand> generate(
            int n,
            List<NQueenPosition> queenPositions
    ) {

        validate(n, queenPositions);

        Set<String> queenSet = new HashSet<>();

        for (NQueenPosition position : queenPositions) {
            queenSet.add(
                    key(position.row(), position.col())
            );
        }

        List<NQueenInputCommand> commands =
                new ArrayList<>();

        for (int row = 0; row < n; row++) {

            boolean leftToRight = row % 2 == 0;

            if (leftToRight) {

                for (int col = 0; col < n; col++) {

                    placeQueenIfRequired(
                            commands,
                            queenSet,
                            row,
                            col
                    );

                    if (col < n - 1) {

                        commands.add(
                                NQueenInputCommand.of(
                                        NQueenNavigationCommand.RIGHT
                                )
                        );
                    }
                }

            } else {

                for (int col = n - 1; col >= 0; col--) {

                    placeQueenIfRequired(
                            commands,
                            queenSet,
                            row,
                            col
                    );

                    if (col > 0) {

                        commands.add(
                                NQueenInputCommand.of(
                                        NQueenNavigationCommand.LEFT
                                )
                        );
                    }
                }
            }

            // Move down to the next row.
            if (row < n - 1) {

                commands.add(
                        NQueenInputCommand.of(
                                NQueenNavigationCommand.DOWN
                        )
                );
            }
        }

        return commands;
    }

    private void placeQueenIfRequired(
            List<NQueenInputCommand> commands,
            Set<String> queenSet,
            int row,
            int col
    ) {

        if (!queenSet.contains(key(row, col))) {
            return;
        }

        // Place queen = SPACE twice.
        commands.add(
                NQueenInputCommand.of(
                        NQueenNavigationCommand.SPACE
                )
        );

        commands.add(
                NQueenInputCommand.of(
                        NQueenNavigationCommand.SPACE
                )
        );
    }

    private String key(int row, int col) {
        return row + ":" + col;
    }

    private void validate(
            int n,
            List<NQueenPosition> queenPositions
    ) {

        if (n < 1) {
            throw new IllegalArgumentException(
                    "N must be greater than 0"
            );
        }

        if (queenPositions == null) {
            throw new IllegalArgumentException(
                    "Queen positions cannot be null"
            );
        }

        if (queenPositions.size() != n) {
            throw new IllegalArgumentException(
                    "Expected exactly " +
                            n +
                            " queen positions, but received " +
                            queenPositions.size()
            );
        }

        Set<String> uniquePositions = new HashSet<>();

        for (NQueenPosition position : queenPositions) {

            if (position.row() < 0 ||
                    position.row() >= n ||
                    position.col() < 0 ||
                    position.col() >= n) {

                throw new IllegalArgumentException(
                        "Invalid queen position: [" +
                                position.row() +
                                "," +
                                position.col() +
                                "] for " +
                                n +
                                "x" +
                                n +
                                " board"
                );
            }

            if (!uniquePositions.add(
                    key(position.row(), position.col())
            )) {

                throw new IllegalArgumentException(
                        "Duplicate queen position: [" +
                                position.row() +
                                "," +
                                position.col() +
                                "]"
                );
            }
        }
    }
}