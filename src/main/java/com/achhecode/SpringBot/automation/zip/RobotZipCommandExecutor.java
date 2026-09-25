package com.achhecode.SpringBot.automation.zip;

import com.achhecode.SpringBot.exception.ZipCommandExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.List;

@Slf4j
@Component
public class RobotZipCommandExecutor implements ZipCommandExecutor {

    private Robot robot;

    @Value("${automation.keyboard.preparation-delay-ms:0}")
    private int preparationDelayMs;

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
            List<ZipCommand> commands,
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

            long start = System.nanoTime();


            for (ZipCommand command : commands) {
                robot.keyPress(command.getKeyCode());
                robot.keyRelease(command.getKeyCode());
            }

            long elapsedMs = (System.nanoTime() - start) / 1_000_000;

            log.info(
                    "Keyboard commands executed. executionId={}, count={}, elapsedMs={}",
                    executionId,
                    commands.size(),
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
}