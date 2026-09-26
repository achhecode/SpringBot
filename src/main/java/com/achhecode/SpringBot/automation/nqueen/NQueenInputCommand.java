package com.achhecode.SpringBot.automation.nqueen;

public record NQueenInputCommand(
        NQueenNavigationCommand command
) {

    public static NQueenInputCommand of(
            NQueenNavigationCommand command
    ) {
        return new NQueenInputCommand(command);
    }

    public int getKeyCode() {
        return command.getKeyCode();
    }
}