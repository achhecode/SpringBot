package com.achhecode.SpringBot.automation.zip.monitoring;

import java.util.List;

public interface ZipCommandMonitor {

    void start();

    List<String> stop();

    boolean isRunning();

    List<String> getRecordedCommands();
}