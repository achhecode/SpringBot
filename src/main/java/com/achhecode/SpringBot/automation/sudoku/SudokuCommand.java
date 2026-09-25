package com.achhecode.SpringBot.automation.sudoku;

import java.awt.event.KeyEvent;

public enum SudokuCommand {

    ONE(1, KeyEvent.VK_1),
    TWO(2, KeyEvent.VK_2),
    THREE(3, KeyEvent.VK_3),
    FOUR(4, KeyEvent.VK_4),
    FIVE(5, KeyEvent.VK_5),
    SIX(6, KeyEvent.VK_6),
    SEVEN(7, KeyEvent.VK_7),
    EIGHT(8, KeyEvent.VK_8),
    NINE(9, KeyEvent.VK_9);

    private final int digit;
    private final int keyCode;

    SudokuCommand(int digit, int keyCode) {
        this.digit = digit;
        this.keyCode = keyCode;
    }

    public int getDigit() {
        return digit;
    }

    public int getKeyCode() {
        return keyCode;
    }
}