package com.achhecode.SpringBot.automation.sudoku;

import java.util.List;

public interface SudokuCommandExecutor {

    void execute(
            List<SudokuInputCommand> commands,
            int delayMs,
            String executionId
    );
}