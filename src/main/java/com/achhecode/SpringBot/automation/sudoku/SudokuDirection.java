package com.achhecode.SpringBot.automation.sudoku;

import java.awt.event.KeyEvent;

public enum SudokuDirection {

    LEFT(KeyEvent.VK_LEFT),
    RIGHT(KeyEvent.VK_RIGHT),
    UP(KeyEvent.VK_UP),
    DOWN(KeyEvent.VK_DOWN);

    private final int keyCode;

    SudokuDirection(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }
}