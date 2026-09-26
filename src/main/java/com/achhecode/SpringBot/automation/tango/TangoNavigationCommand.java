package com.achhecode.SpringBot.automation.tango;

import java.awt.event.KeyEvent;

public enum TangoNavigationCommand {

    LEFT(KeyEvent.VK_LEFT),
    RIGHT(KeyEvent.VK_RIGHT),
    DOWN(KeyEvent.VK_DOWN),
    SPACE(KeyEvent.VK_SPACE);

    private final int keyCode;

    TangoNavigationCommand(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }
}