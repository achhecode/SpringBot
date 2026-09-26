package com.achhecode.SpringBot.exception;

public class TangoCommandExecutionException
        extends RuntimeException {

    private final String executionId;

    public TangoCommandExecutionException(
            String message,
            String executionId,
            Throwable cause
    ) {

        super(message, cause);

        this.executionId = executionId;
    }

    public String getExecutionId() {
        return executionId;
    }
}