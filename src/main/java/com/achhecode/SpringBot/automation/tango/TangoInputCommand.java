package com.achhecode.SpringBot.automation.tango;

public record TangoInputCommand(
        TangoCommand command,
        TangoNavigationCommand navigation
) {

    public static TangoInputCommand command(TangoCommand command) {
        return new TangoInputCommand(command, null);
    }

    public static TangoInputCommand move(
            TangoNavigationCommand navigation
    ) {
        return new TangoInputCommand(null, navigation);
    }

    public int getKeyCode() {

        if (navigation != null) {
            return navigation.getKeyCode();
        }

        throw new IllegalStateException(
                "Command does not have a single keyCode"
        );
    }
}