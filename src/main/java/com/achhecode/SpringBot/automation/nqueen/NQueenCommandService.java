package com.achhecode.SpringBot.automation.nqueen;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class NQueenCommandService {

    private final NQueenCommandGenerator generator;
    private final NQueenCommandExecutor executor;

    public NQueenCommandService(
            NQueenCommandGenerator generator,
            NQueenCommandExecutor executor
    ) {
        this.generator = generator;
        this.executor = executor;
    }

    public int executeCommand(
            List<Integer> positions,
            String executionId
    ) {

        long startTime = System.currentTimeMillis();

        try {

            if (positions == null || positions.isEmpty()) {
                throw new IllegalArgumentException(
                        "N-Queen positions cannot be null or empty"
                );
            }

            int n = positions.size();

            List<NQueenPosition> queenPositions =
                    new ArrayList<>(n);

            for (int row = 0; row < n; row++) {

                Integer column = positions.get(row);

                if (column == null) {
                    throw new IllegalArgumentException(
                            "Queen column cannot be null. row=" + row
                    );
                }

                queenPositions.add(
                        new NQueenPosition(row, column)
                );
            }

            List<NQueenInputCommand> inputCommands =
                    generator.generate(
                            n,
                            queenPositions
                    );

            log.info(
                    "N-Queen keyboard sequence generated. " +
                            "executionId={}, n={}, " +
                            "queenCount={}, inputCommandCount={}",
                    executionId,
                    n,
                    queenPositions.size(),
                    inputCommands.size()
            );

            executor.execute(
                    inputCommands,
                    executionId
            );

            long duration =
                    System.currentTimeMillis() - startTime;

            log.info(
                    "N-Queen keyboard automation completed. " +
                            "executionId={}, n={}, " +
                            "queenCount={}, inputCommandCount={}, " +
                            "durationMs={}",
                    executionId,
                    n,
                    queenPositions.size(),
                    inputCommands.size(),
                    duration
            );

            return n;

        } catch (Exception e) {

            log.error(
                    "N-Queen keyboard automation failed. " +
                            "executionId={}",
                    executionId,
                    e
            );

            throw e;
        }
    }
}