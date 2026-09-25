package com.achhecode.SpringBot.service;

import com.achhecode.SpringBot.automation.keyboard.KeyboardCommand;
import com.achhecode.SpringBot.automation.keyboard.KeyboardCommandParser;
import com.achhecode.SpringBot.automation.keyboard.KeyboardExecutor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutomationService {

    private final KeyboardCommandParser parser;
    private final KeyboardExecutor keyboardExecutor;

    public AutomationService(
            KeyboardCommandParser parser,
            KeyboardExecutor keyboardExecutor
    ) {
        this.parser = parser;
        this.keyboardExecutor = keyboardExecutor;
    }

    public void executeKeyboard(String instruction, int delayMS) {

        List<KeyboardCommand> commands = parser.parse(instruction);

        keyboardExecutor.execute(commands, delayMS);
    }
}
