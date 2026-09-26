package com.achhecode.SpringBot.automation.nqueen;

import com.achhecode.SpringBot.exception.NQueenCommandExecutionException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.List;

@Slf4j
@Component
public class RobotNQueenCommandExecutor
        implements NQueenCommandExecutor {

    private Robot robot;

    @Value("${automation.keyboard.switch-application:true}")
    private boolean switchApplication;

    @PostConstruct
    public void initialize() {

        try {

            robot = new Robot();

            robot.setAutoDelay(0);
            robot.setAutoWaitForIdle(false);

            log.info(
                    "Java Robot initialized successfully for N-Queen"
            );

        } catch (AWTException e) {

            log.error(
                    "Unable to initialize Java Robot for N-Queen",
                    e
            );

            throw new IllegalStateException(
                    "Unable to initialize Java Robot",
                    e
            );
        }
    }

    private Robot getRobot() {

        if (robot == null) {
            throw new IllegalStateException(
                    "Java Robot is unavailable"
            );
        }

        return robot;
    }

    @Override
    public synchronized void execute(
            List<NQueenInputCommand> commands,
            String executionId
    ) {

        if (commands == null ||
                commands.isEmpty()) {

            log.warn(
                    "No N-Queen commands to execute. executionId={}",
                    executionId
            );

            return;
        }

        Robot robot = getRobot();

        try {

            /*
             * Switch to previous application.
             */
            if (switchApplication) {

                robot.keyPress(KeyEvent.VK_META);

                try {

                    robot.keyPress(KeyEvent.VK_TAB);
                    robot.keyRelease(KeyEvent.VK_TAB);

                } finally {

                    robot.keyRelease(KeyEvent.VK_META);
                }
            }

            long start = System.nanoTime();

            /*
             * Execute generated keyboard commands.
             */
            for (NQueenInputCommand command : commands) {

                if (command == null) {
                    continue;
                }

                int keyCode = command.getKeyCode();

                robot.keyPress(keyCode);
                robot.keyRelease(keyCode);
            }

            long elapsedMs =
                    (System.nanoTime() - start)
                            / 1_000_000;

            log.info(
                    "N-Queen commands executed. " +
                            "executionId={}, count={}, elapsedMs={}",
                    executionId,
                    commands.size(),
                    elapsedMs
            );

        } catch (Exception e) {

            log.error(
                    "N-Queen keyboard automation failed. " +
                            "executionId={}",
                    executionId,
                    e
            );

            throw new NQueenCommandExecutionException(
                    "N-Queen keyboard automation failed",
                    executionId,
                    e
            );
        }
    }
}