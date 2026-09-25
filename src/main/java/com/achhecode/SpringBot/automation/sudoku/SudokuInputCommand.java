package com.achhecode.SpringBot.automation.sudoku;

public record SudokuInputCommand(
        SudokuCommand number,
        SudokuNavigationCommand navigation
) {

    public static SudokuInputCommand number(SudokuCommand command) {
        return new SudokuInputCommand(command, null);
    }

    public static SudokuInputCommand move(
            SudokuNavigationCommand command
    ) {
        return new SudokuInputCommand(null, command);
    }

    public boolean isNumber() {
        return number != null;
    }

    public boolean isNavigation() {
        return navigation != null;
    }

    public int getKeyCode() {

        if (number != null) {
            return number.getKeyCode();
        }

        if (navigation != null) {
            return navigation.getKeyCode();
        }

        throw new IllegalStateException(
                "SudokuInputCommand contains neither number nor navigation command"
        );
    }
}