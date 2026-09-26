package com.achhecode.SpringBot.automation.tango;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TangoCommandGenerator {

    /**
     * Generates keyboard commands for an N x N Tango grid.
     *
     * Traversal:
     *
     * Row 1: LEFT -> RIGHT
     * Row 2: RIGHT -> LEFT
     * Row 3: LEFT -> RIGHT
     * Row 4: RIGHT -> LEFT
     *
     * For every cell:
     *
     * S -> press 1 + SPACE
     * M -> press 2 + SPACE
     */
    public List<TangoInputCommand> generate(
            int gridSize,
            TangoCommand[][] values
    ) {

        validateGrid(gridSize, values);

        List<TangoInputCommand> commands =
                new ArrayList<>();

        for (int row = 0; row < gridSize; row++) {

            boolean leftToRight =
                    row % 2 == 0;

            if (leftToRight) {

                /*
                 * Row:
                 *
                 * LEFT -> RIGHT
                 */
                for (int col = 0;
                     col < gridSize;
                     col++) {

                    addCell(
                            commands,
                            values[row][col]
                    );

                    if (col < gridSize - 1) {

                        commands.add(
                                TangoInputCommand.move(
                                        TangoNavigationCommand.RIGHT
                                )
                        );
                    }
                }

            } else {

                /*
                 * Row:
                 *
                 * RIGHT -> LEFT
                 */
                for (int col = gridSize - 1;
                     col >= 0;
                     col--) {

                    addCell(
                            commands,
                            values[row][col]
                    );

                    if (col > 0) {

                        commands.add(
                                TangoInputCommand.move(
                                        TangoNavigationCommand.LEFT
                                )
                        );
                    }
                }
            }

            /*
             * Move down to next row.
             */
            if (row < gridSize - 1) {

                commands.add(
                        TangoInputCommand.move(
                                TangoNavigationCommand.DOWN
                        )
                );
            }
        }

        return commands;
    }

    private void addCell(
        List<TangoInputCommand> commands,
        TangoCommand command
    ) {

        if (command == null) {
            throw new IllegalArgumentException(
                    "Tango cell cannot be null"
            );
        }

        // S = SPACE once
        if (command == TangoCommand.S) {

            commands.add(
                    TangoInputCommand.move(
                            TangoNavigationCommand.SPACE
                    )
            );
        }

        // M = SPACE twice
        else if (command == TangoCommand.M) {

            commands.add(
                    TangoInputCommand.move(
                            TangoNavigationCommand.SPACE
                    )
            );

            commands.add(
                    TangoInputCommand.move(
                            TangoNavigationCommand.SPACE
                    )
            );
        }
    }

    private void validateGrid(
            int gridSize,
            TangoCommand[][] values
    ) {

        if (gridSize < 2) {
            throw new IllegalArgumentException(
                    "Tango grid size must be at least 2"
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

        for (TangoCommand[] row : values) {

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