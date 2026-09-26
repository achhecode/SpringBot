package com.achhecode.SpringBot.automation.nqueen;

import java.util.List;

public interface NQueenCommandExecutor {

    void execute(
            List<NQueenInputCommand> commands,
            String executionId
    );
}