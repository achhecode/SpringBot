package com.achhecode.SpringBot.automation.typing;

public enum TextTypingMode {

    /**
     * Fastest method on macOS.
     * Copies the complete text to clipboard and pastes it.
     */
    FAST,

    /**
     * Generates individual keyboard events.
     * Useful when the target application requires key events.
     */
    KEY_EVENT
}