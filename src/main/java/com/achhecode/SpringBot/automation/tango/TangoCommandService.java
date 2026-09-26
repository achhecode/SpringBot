package com.achhecode.SpringBot.automation.tango;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TangoCommandService {

    private final TangoCommandParser parser;
    private final TangoCommandGenerator generator;
    private final TangoCommandExecutor executor;

    public TangoCommandService(
            TangoCommandParser parser,
            TangoCommandGenerator generator,
            TangoCommandExecutor executor
    ) {

        this.parser = parser;
        this.generator = generator;
        this.executor = executor;
    }

    public void executeCommand(
            String instruction,
            String executionId
    ) {

        long startTime =
                System.currentTimeMillis();

        try {

            List<TangoCommand> parsedCommands =
                    parser.parse(instruction);

            int gridSize =
                    determineGridSize(
                            parsedCommands.size()
                    );

            TangoCommand[][] values =
                    toGrid(
                            parsedCommands,
                            gridSize
                    );

            List<TangoInputCommand> inputCommands =
                    generator.generate(
                            gridSize,
                            values
                    );

            log.info(
                    "Tango keyboard sequence generated. " +
                    "executionId={}, gridSize={}, " +
                    "inputCommandCount={}",
                    executionId,
                    gridSize,
                    inputCommands.size()
            );

            executor.execute(
                    inputCommands,
                    executionId
            );

            long duration =
                    System.currentTimeMillis()
                            - startTime;

            log.info(
                    "Tango keyboard automation completed successfully. " +
                    "executionId={}, gridSize={}, " +
                    "inputCommandCount={}, durationMs={}",
                    executionId,
                    gridSize,
                    inputCommands.size(),
                    duration
            );

        } catch (Exception e) {

            log.error(
                    "Tango keyboard automation failed. " +
                    "executionId={}",
                    executionId,
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

        if (gridSize < 2 ||
                gridSize * gridSize != commandCount) {

            throw new IllegalArgumentException(
                    "Invalid Tango command count: "
                            + commandCount +
                    ". Expected N*N values."
            );
        }

        return gridSize;
    }

    private TangoCommand[][] toGrid(
            List<TangoCommand> commands,
            int gridSize
    ) {

        TangoCommand[][] grid =
                new TangoCommand[gridSize][gridSize];

        int index = 0;

        for (int row = 0;
             row < gridSize;
             row++) {

            for (int col = 0;
                 col < gridSize;
                 col++) {

                grid[row][col] =
                        commands.get(index++);
            }
        }

        return grid;
    }
}