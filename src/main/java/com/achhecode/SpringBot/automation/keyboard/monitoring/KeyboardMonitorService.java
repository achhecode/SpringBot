package com.achhecode.SpringBot.automation.keyboard.monitoring;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeyboardMonitorService {

    private final KeyboardMonitor keyboardMonitor;

    public KeyboardMonitorService(
            KeyboardMonitor keyboardMonitor
    ) {
        this.keyboardMonitor = keyboardMonitor;
    }

    public void startTracking() {
        keyboardMonitor.start();
    }

    public List<String> stopTracking() {
        return keyboardMonitor.stop();
    }

    public boolean isTracking() {
        return keyboardMonitor.isRunning();
    }

    public List<String> getRecordedCommands() {
        return keyboardMonitor.getRecordedCommands();
    }
}