package com.achhecode.SpringBot.automation.zip;

import java.util.List;

public interface ZipCommandExecutor {
    void execute(
        List<ZipCommand> commands,
        String executionId
    );
}