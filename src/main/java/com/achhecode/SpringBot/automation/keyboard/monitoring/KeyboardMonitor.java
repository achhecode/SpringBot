package com.achhecode.SpringBot.automation.keyboard.monitoring;

import java.util.List;

public interface KeyboardMonitor {

    void start();

    List<String> stop();

    boolean isRunning();

    List<String> getRecordedCommands();
}