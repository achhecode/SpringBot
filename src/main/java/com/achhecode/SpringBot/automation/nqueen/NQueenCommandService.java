package com.achhecode.SpringBot.automation.nqueen;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class NQueenCommandService {

    private final NQueenPositionParser parser;
    private final NQueenCommandGenerator generator;
    private final NQueenCommandExecutor executor;

    public NQueenCommandService(
            NQueenPositionParser parser,
            NQueenCommandGenerator generator,
            NQueenCommandExecutor executor
    ) {

        this.parser = parser;
        this.generator = generator;
        this.executor = executor;
    }

    public void executeCommand(
            int n,
            String instruction,
            String executionId
    ) {

        long startTime =
                System.currentTimeMillis();

        try {

            List<NQueenPosition> queenPositions =
                    parser.parse(instruction);

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
                    System.currentTimeMillis()
                            - startTime;

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