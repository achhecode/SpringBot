package com.achhecode.SpringBot.automation.sudoku;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SudokuCommandService {

    private final SudokuCommandParser parser;
    private final SudokuCommandGenerator generator;
    private final SudokuCommandExecutor sudokuCommandExecutor;

    public SudokuCommandService(
            SudokuCommandParser parser,
            SudokuCommandGenerator generator,
            SudokuCommandExecutor sudokuCommandExecutor
    ) {
        this.parser = parser;
        this.generator = generator;
        this.sudokuCommandExecutor = sudokuCommandExecutor;
    }

    public void executeCommand(
            String instruction,
            int delayMs,
            String executionId
    ) {

        long startTime =
                System.currentTimeMillis();

        log.info(
                "Sudoku keyboard automation started. " +
                "executionId={}, commandLength={}",
                executionId,
                instruction != null
                        ? instruction.length()
                        : 0
        );

        try {

            /*
             * Parse the supplied Sudoku numbers.
             *
             * Example:
             *
             * 123
             * 312
             * 231
             *
             * becomes:
             *
             * List<SudokuCommand>
             */
            List<SudokuCommand> parsedCommands =
                    parser.parse(instruction);

            log.info(
                    "Sudoku instructions parsed. " +
                    "executionId={}, commandCount={}",
                    executionId,
                    parsedCommands.size()
            );

            /*
             * Determine grid size.
             *
             * For an N x N Sudoku:
             *
             * number of values = N * N
             *
             * 3x3 = 9
             * 4x4 = 16
             * 6x6 = 36
             * 9x9 = 81
             */
            int gridSize =
                    determineGridSize(
                            parsedCommands.size()
                    );

            /*
             * Convert the flat command list into
             * a 2D Sudoku grid.
             */
            int[][] values =
                    toGrid(
                            parsedCommands,
                            gridSize
                    );

            /*
             * Generate:
             *
             * NUMBER
             * RIGHT
             * NUMBER
             * RIGHT
             * NUMBER
             * DOWN
             * NUMBER
             * LEFT
             * ...
             */
            List<SudokuInputCommand> inputCommands =
                    generator.generate(
                            gridSize,
                            values
                    );

            log.info(
                    "Sudoku keyboard sequence generated. " +
                    "executionId={}, gridSize={}, inputCommandCount={}",
                    executionId,
                    gridSize,
                    inputCommands.size()
            );

            /*
             * Execute generated keyboard sequence.
             */
            sudokuCommandExecutor.execute(
                    inputCommands,
                    delayMs,
                    executionId
            );

            long duration =
                    System.currentTimeMillis()
                            - startTime;

            log.info(
                    "Sudoku keyboard automation completed successfully. " +
                    "executionId={}, gridSize={}, " +
                    "inputCommandCount={}, durationMs={}",
                    executionId,
                    gridSize,
                    inputCommands.size(),
                    duration
            );

        } catch (Exception e) {

            long duration =
                    System.currentTimeMillis()
                            - startTime;

            log.error(
                    "Sudoku keyboard automation failed. " +
                    "executionId={}, durationMs={}, error={}",
                    executionId,
                    duration,
                    e.getMessage(),
                    e
            );

            throw e;
        }
    }

    private int determineGridSize(
            int commandCount
    ) {

        double size =
                Math.sqrt(commandCount);

        int gridSize =
                (int) size;

        if (gridSize < 3 ||
                gridSize > 9 ||
                gridSize * gridSize != commandCount) {

            throw new IllegalArgumentException(
                    "Invalid Sudoku command count: "
                            + commandCount +
                    ". Expected N*N values where N is 3-9."
            );
        }

        return gridSize;
    }

    private int[][] toGrid(
            List<SudokuCommand> commands,
            int gridSize
    ) {

        int[][] grid =
                new int[gridSize][gridSize];

        int index = 0;

        for (int row = 0;
             row < gridSize;
             row++) {

            for (int col = 0;
                 col < gridSize;
                 col++) {

                grid[row][col] =
                        commands
                                .get(index++)
                                .getDigit();
            }
        }

        return grid;
    }
}