package com.achhecode.SpringBot.automation.sudoku;

import com.achhecode.SpringBot.exception.SudokuCommandExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.List;

@Slf4j
@Component
public class RobotSudokuCommandExecutor
        implements SudokuCommandExecutor {

    private Robot robot;

    @Value("${automation.keyboard.preparation-delay-ms:0}")
    private int preparationDelayMs;

    @Value("${automation.keyboard.switch-application:true}")
    private boolean switchApplication;

    @Value("${automation.keyboard.switch-delay-ms:0}")
    private int switchDelayMs;

    @Value("${automation.keyboard.command-delay-ms:0}")
    private int commandDelayMs;

    private Robot getRobot() {

        if (robot == null) {

            try {

                robot = new Robot();

                log.info(
                        "Java Robot initialized successfully"
                );

            } catch (AWTException e) {

                log.error(
                        "Unable to initialize Java Robot",
                        e
                );

                throw new IllegalStateException(
                        "Unable to initialize Java Robot",
                        e
                );
            }
        }

        return robot;
    }

    @Override
    public void execute(
            List<SudokuInputCommand> commands,
            int delayMs,
            String executionId
    ) {

        Robot robot = getRobot();

        log.info(
                "Sudoku keyboard automation started. " +
                "executionId={}, commandCount={}, " +
                "switchApplication={}",
                executionId,
                commands.size(),
                switchApplication
        );

        try {

            /*
             * Switch to previous application.
             */
            if (switchApplication) {

                log.debug(
                        "Switching to previous application. " +
                        "executionId={}",
                        executionId
                );

                robot.keyPress(KeyEvent.VK_META);

                try {

                    robot.keyPress(KeyEvent.VK_TAB);
                    robot.keyRelease(KeyEvent.VK_TAB);

                } finally {

                    robot.keyRelease(KeyEvent.VK_META);
                }

                if (switchDelayMs > 0) {
                    robot.delay(switchDelayMs);
                }
            }

            /*
             * Allow Sudoku application to receive focus.
             */
            if (preparationDelayMs > 0) {
                robot.delay(preparationDelayMs);
            }

            long start = System.nanoTime();

            for (SudokuInputCommand command : commands) {

                executeCommand(
                        robot,
                        command
                );

                int effectiveDelay =
                        delayMs > 0
                                ? delayMs
                                : commandDelayMs;

                if (effectiveDelay > 0) {
                    robot.delay(effectiveDelay);
                }
            }

            long elapsedMs =
                    (System.nanoTime() - start)
                            / 1_000_000;

            log.info(
                    "Sudoku commands executed. " +
                    "executionId={}, count={}, elapsedMs={}",
                    executionId,
                    commands.size(),
                    elapsedMs
            );

        } catch (Exception e) {

            log.error(
                    "Sudoku keyboard automation failed. " +
                    "executionId={}",
                    executionId,
                    e
            );

            throw new SudokuCommandExecutionException(
                    "Sudoku keyboard automation failed",
                    executionId,
                    e
            );
        }
    }

    private void executeCommand(
            Robot robot,
            SudokuInputCommand command
    ) {

        int keyCode = command.getKeyCode();

        robot.keyPress(keyCode);

        try {

            robot.keyRelease(keyCode);

        } catch (Exception e) {

            /*
             * Make sure the key isn't left pressed.
             */
            try {
                robot.keyRelease(keyCode);
            } catch (Exception ignored) {
                // Ignore secondary release failure.
            }

            throw e;
        }
    }
}