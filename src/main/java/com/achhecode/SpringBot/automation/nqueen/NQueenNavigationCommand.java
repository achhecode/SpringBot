package com.achhecode.SpringBot.automation.nqueen;

import java.awt.event.KeyEvent;

public enum NQueenNavigationCommand {

    LEFT(KeyEvent.VK_LEFT),
    RIGHT(KeyEvent.VK_RIGHT),
    DOWN(KeyEvent.VK_DOWN),
    SPACE(KeyEvent.VK_SPACE);

    private final int keyCode;

    NQueenNavigationCommand(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }
}