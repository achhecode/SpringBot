package com.achhecode.SpringBot.automation.tango;

import java.util.List;

public interface TangoCommandExecutor {

    void execute(
            List<TangoInputCommand> commands,
            String executionId
    );
}