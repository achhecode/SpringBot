package com.achhecode.SpringBot.automation.keyboard;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.List;

@Component
public class RobotKeyboardExecutor implements KeyboardExecutor {

    private Robot robot;

    @Value("${automation.keyboard.initial-delay-ms:5000}")
    private int initialDelayMs;

    private Robot getRobot() {
        if (robot == null) {
            try {
                robot = new Robot();
            } catch (AWTException e) {
                throw new IllegalStateException(
                        "Unable to initialize Java Robot for keyboard automation",
                        e
                );
            }
        }

        return robot;
    }

    @Override
    public void execute(List<KeyboardCommand> commands, int delayMs) {

        Robot robot = getRobot();

        // Give user time to focus/select the target application
        if (initialDelayMs > 0) {
            robot.delay(initialDelayMs);
        }

        for (KeyboardCommand command : commands) {

            robot.keyPress(command.getKeyCode());
            robot.keyRelease(command.getKeyCode());

            if (delayMs > 0) {
                robot.delay(delayMs);
            }
        }
    }
}
