package com.achhecode.SpringBot.automation.keyboard;

import com.achhecode.SpringBot.exception.KeyboardExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.List;

@Slf4j
@Component
public class RobotKeyboardExecutor implements KeyboardExecutor {

    private Robot robot;

    @Value("${automation.keyboard.initial-delay-ms:1000}")
    private int initialDelayMs;

    @Value("${automation.keyboard.switch-application:true}")
    private boolean switchApplication;

    @Value("${automation.keyboard.switch-delay-ms:0}")
    private int switchDelayMs;

    private Robot getRobot() {
        if (robot == null) {
            try {
                robot = new Robot();

                log.info("Java Robot initialized successfully");

            } catch (AWTException e) {
                log.error("Unable to initialize Java Robot", e);

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
            List<KeyboardCommand> commands,
            int delayMs,
            String executionId
    ) {

        Robot robot = getRobot();

        log.info(
                "Keyboard automation started. executionId={}, commandCount={}, switchApplication={}",
                executionId,
                commands.size(),
                switchApplication
        );

        try {

            // Give the user time to prepare before automation starts
            if (initialDelayMs > 0) {
                log.debug(
                        "Initial automation delay. executionId={}, delayMs={}",
                        executionId,
                        initialDelayMs
                );

                robot.delay(initialDelayMs);
            }

            /*
             * Switch application FIRST.
             *
             * macOS:
             * Command + Tab
             */
            if (switchApplication) {

                log.debug(
                        "Switching to previous macOS application. executionId={}",
                        executionId
                );

                robot.keyPress(KeyEvent.VK_META);

                try {
                    robot.keyPress(KeyEvent.VK_TAB);
                    robot.keyRelease(KeyEvent.VK_TAB);
                } finally {
                    robot.keyRelease(KeyEvent.VK_META);
                }

                /*
                 * Normally 0ms is sufficient.
                 * Add a delay only if macOS/application needs it.
                 */
                if (switchDelayMs > 0) {
                    robot.delay(switchDelayMs);
                }
            }

            // Execute actual keyboard commands
            int index = 0;

            for (KeyboardCommand command : commands) {

                log.debug(
                        "Executing command. executionId={}, index={}, command={}",
                        executionId,
                        index,
                        command
                );

                robot.keyPress(command.getKeyCode());
                robot.keyRelease(command.getKeyCode());

                index++;

                if (delayMs > 0) {
                    robot.delay(delayMs);
                }
            }

            log.info(
                    "Keyboard automation completed. executionId={}, executedCommands={}",
                    executionId,
                    commands.size()
            );

        } catch (Exception e) {

            log.error(
                    "Keyboard automation failed. executionId={}",
                    executionId,
                    e
            );

            throw new KeyboardExecutionException(
                    "Keyboard automation failed",
                    executionId,
                    e
            );
        }
    }
}