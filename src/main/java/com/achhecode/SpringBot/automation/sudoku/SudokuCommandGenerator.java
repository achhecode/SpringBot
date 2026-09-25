package com.achhecode.SpringBot.automation.sudoku;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SudokuCommandGenerator {

    /**
     * Generates keyboard commands for an N x N Sudoku grid.
     *
     * Traversal:
     *
     * Row 1: LEFT -> RIGHT
     * Row 2: RIGHT -> LEFT
     * Row 3: LEFT -> RIGHT
     * Row 4: RIGHT -> LEFT
     *
     * @param gridSize Sudoku grid size
     * @param values   solved Sudoku values
     * @return keyboard input commands
     */
    public List<SudokuInputCommand> generate(
            int gridSize,
            int[][] values
    ) {

        validateGrid(gridSize, values);

        List<SudokuInputCommand> commands =
                new ArrayList<>();

        for (int row = 0; row < gridSize; row++) {

            boolean leftToRight = row % 2 == 0;

            if (leftToRight) {

                for (int col = 0; col < gridSize; col++) {

                    int value = values[row][col];

                    addNumber(
                            commands,
                            value,
                            gridSize
                    );

                    if (col < gridSize - 1) {

                        commands.add(
                                SudokuInputCommand.move(
                                        SudokuNavigationCommand.RIGHT
                                )
                        );
                    }
                }

            } else {

                for (int col = gridSize - 1;
                     col >= 0;
                     col--) {

                    int value = values[row][col];

                    addNumber(
                            commands,
                            value,
                            gridSize
                    );

                    if (col > 0) {

                        commands.add(
                                SudokuInputCommand.move(
                                        SudokuNavigationCommand.LEFT
                                )
                        );
                    }
                }
            }

            // Move down to the next row
            if (row < gridSize - 1) {

                commands.add(
                        SudokuInputCommand.move(
                                SudokuNavigationCommand.DOWN
                        )
                );
            }
        }

        return commands;
    }

    private void addNumber(
            List<SudokuInputCommand> commands,
            int value,
            int gridSize
    ) {

        if (value < 1 || value > gridSize) {

            throw new IllegalArgumentException(
                    "Invalid Sudoku value " + value +
                    ". Allowed range is 1-" + gridSize
            );
        }

        SudokuCommand command =
                switch (value) {

                    case 1 -> SudokuCommand.ONE;
                    case 2 -> SudokuCommand.TWO;
                    case 3 -> SudokuCommand.THREE;
                    case 4 -> SudokuCommand.FOUR;
                    case 5 -> SudokuCommand.FIVE;
                    case 6 -> SudokuCommand.SIX;
                    case 7 -> SudokuCommand.SEVEN;
                    case 8 -> SudokuCommand.EIGHT;
                    case 9 -> SudokuCommand.NINE;

                    default -> throw new IllegalArgumentException(
                            "Unsupported Sudoku value: " + value
                    );
                };

        commands.add(
                SudokuInputCommand.number(command)
        );
    }

    private void validateGrid(
            int gridSize,
            int[][] values
    ) {

        if (gridSize < 3 || gridSize > 9) {

            throw new IllegalArgumentException(
                    "Grid size must be between 3 and 9"
            );
        }

        if (values == null ||
                values.length != gridSize) {

            throw new IllegalArgumentException(
                    "Grid must contain " +
                    gridSize +
                    " rows"
            );
        }

        for (int[] row : values) {

            if (row == null ||
                    row.length != gridSize) {

                throw new IllegalArgumentException(
                        "Every row must contain " +
                        gridSize +
                        " values"
                );
            }
        }
    }
}