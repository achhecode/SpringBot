package com.achhecode.SpringBot.automation.zip;

import com.achhecode.SpringBot.exception.ZipCommandExecutionException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.AWTException;
import java.awt.GraphicsEnvironment;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.List;

@Slf4j
@Component
public class RobotZipCommandExecutor implements ZipCommandExecutor {

    private Robot robot;

    @Value("${automation.keyboard.enabled:true}")
    private boolean enabled;

    @Value("${automation.keyboard.preparation-delay-ms:100}")
    private int preparationDelayMs;

    @Value("${automation.keyboard.switch-application:true}")
    private boolean switchApplication;

    @Value("${automation.keyboard.switch-delay-ms:100}")
    private int switchDelayMs;

    @PostConstruct
    public void initialize() {

        if (!enabled) {
            log.info("Keyboard automation is disabled");
            return;
        }

        if (GraphicsEnvironment.isHeadless()) {
            log.warn("Keyboard automation unavailable: JVM is running in headless mode");
            return;
        }

        try {
            robot = new Robot();
            robot.setAutoDelay(0);
            robot.setAutoWaitForIdle(false);

            log.info("Java Robot initialized successfully");

        } catch (AWTException e) {
            log.error("Unable to initialize Java Robot", e);
            robot = null;
        }
    }

    /**
     * Returns the initialized Robot instance.
     */
    private Robot getRobot() {

        if (!enabled) {
            throw new IllegalStateException(
                    "Keyboard automation is disabled"
            );
        }

        if (robot == null) {
            throw new IllegalStateException(
                    "Java Robot is unavailable. Run Spring Boot in a graphical desktop session."
            );
        }

        return robot;
    }

    /**
     * Executes keyboard commands sequentially.
     *
     * synchronized is intentional:
     * two HTTP requests must never control the keyboard
     * simultaneously.
     */
    @Override
    public synchronized void execute(
            List<ZipCommand> commands,
            int delayMs,
            String executionId
    ) {

        if (!enabled) {

            log.warn(
                    "Keyboard automation request rejected because automation is disabled. executionId={}",
                    executionId
            );

            throw new IllegalStateException(
                    "Keyboard automation is disabled"
            );
        }

        if (commands == null || commands.isEmpty()) {

            log.warn(
                    "No keyboard commands to execute. executionId={}",
                    executionId
            );

            return;
        }

        Robot robot = getRobot();

        log.info(
                "Keyboard automation started. executionId={}, commandCount={}, delayMs={}, switchApplication={}",
                executionId,
                commands.size(),
                delayMs,
                switchApplication
        );

        try {

            /*
             * Give the user/application time to prepare.
             */
            if (preparationDelayMs > 0) {

                log.debug(
                        "Preparation delay: {} ms. executionId={}",
                        preparationDelayMs,
                        executionId
                );

                robot.delay(preparationDelayMs);
            }

            /*
             * Switch to the previous application.
             *
             * macOS:
             * Command + Tab
             */
            if (switchApplication) {

                log.debug(
                        "Switching to previous application. executionId={}",
                        executionId
                );

                switchToPreviousApplication(robot);

                /*
                 * Give macOS time to switch the active application.
                 */
                if (switchDelayMs > 0) {
                    robot.delay(switchDelayMs);
                }
            }

            long startTime = System.nanoTime();

            int executedCount = 0;

            for (ZipCommand command : commands) {

                if (command == null) {
                    log.warn(
                            "Skipping null command. executionId={}",
                            executionId
                    );
                    continue;
                }

                int keyCode = command.getKeyCode();

                log.debug(
                        "Executing key. executionId={}, command={}, keyCode={}",
                        executionId,
                        command,
                        keyCode
                );

                robot.keyPress(keyCode);
                robot.keyRelease(keyCode);

                executedCount++;

                /*
                 * Delay between commands.
                 */
                if (delayMs > 0) {
                    robot.delay(delayMs);
                }
            }

            long elapsedMs =
                    (System.nanoTime() - startTime) / 1_000_000;

            log.info(
                    "Keyboard automation completed. executionId={}, requestedCount={}, executedCount={}, elapsedMs={}",
                    executionId,
                    commands.size(),
                    executedCount,
                    elapsedMs
            );

        } catch (Exception e) {

            log.error(
                    "Keyboard automation failed. executionId={}",
                    executionId,
                    e
            );

            throw new ZipCommandExecutionException(
                    "Keyboard automation failed",
                    executionId,
                    e
            );
        }
    }

    /**
     * macOS:
     *
     * Command + Tab
     *
     * Switches to the previous application.
     */
    private void switchToPreviousApplication(Robot robot) {

        log.debug("Executing macOS Command + Tab");

        robot.keyPress(KeyEvent.VK_META);

        try {

            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);

        } finally {

            /*
             * Always release META even if something goes wrong.
             */
            robot.keyRelease(KeyEvent.VK_META);
        }
    }
}