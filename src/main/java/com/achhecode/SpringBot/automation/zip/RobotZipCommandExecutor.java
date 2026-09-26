package com.achhecode.SpringBot.automation.zip;

import com.achhecode.SpringBot.exception.ZipCommandExecutionException;
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
public class RobotZipCommandExecutor
        implements ZipCommandExecutor {

    private Robot robot;

    @Value("${automation.keyboard.switch-application:true}")
    private boolean switchApplication;

    @PostConstruct
    public void initialize() {
        try {
            robot = new Robot();
            robot.setAutoDelay(0);
            robot.setAutoWaitForIdle(false);

            log.info("Java Robot initialized successfully");

        } catch (AWTException e) {
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
            List<ZipCommand> commands,
            String executionId
    ) {

        if (commands == null || commands.isEmpty()) {
            log.warn(
                    "No keyboard commands to execute. executionId={}",
                    executionId
            );
            return;
        }

        Robot robot = getRobot();

        try {

            /*
             * Switch application first.
             */

            long totalStart = System.nanoTime();
            if (switchApplication) {
                long switchStart = System.nanoTime();

                switchToPreviousApplication(robot);

                long switchMs =
                        (System.nanoTime() - switchStart) / 1_000_000;

                log.info("Application switch took {} ms", switchMs);
            }

            long commandStart = System.nanoTime();

            for (ZipCommand command : commands) {
                int keyCode = command.getKeyCode();

                robot.keyPress(keyCode);
                robot.keyRelease(keyCode);
            }

            long commandMs =
                    (System.nanoTime() - commandStart) / 1_000_000;

            long totalMs =
                    (System.nanoTime() - totalStart) / 1_000_000;

            log.info(
                    "Keyboard timing: commands={} ms, total={} ms",
                    commandMs,
                    totalMs
            );

            log.info(
                    "Keyboard automation completed. " +
                    "executionId={}, requestedCount={}",
                    executionId,
                    commands.size()
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

    private void switchToPreviousApplication(Robot robot) {

        log.debug("Executing macOS Command + Tab");

        robot.keyPress(KeyEvent.VK_META);

        try {
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
        } finally {
            robot.keyRelease(KeyEvent.VK_META);
        }
    }
}