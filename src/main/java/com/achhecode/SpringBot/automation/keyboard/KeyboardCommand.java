package com.achhecode.SpringBot.automation.keyboard;

import java.awt.event.KeyEvent;

public enum KeyboardCommand {
    UP(KeyEvent.VK_UP),
    DOWN(KeyEvent.VK_DOWN),
    LEFT(KeyEvent.VK_LEFT),
    RIGHT(KeyEvent.VK_RIGHT);

    private final int keyCode;

    KeyboardCommand(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }
}
