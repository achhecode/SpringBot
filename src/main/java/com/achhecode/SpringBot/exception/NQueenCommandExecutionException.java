package com.achhecode.SpringBot.exception;

public class NQueenCommandExecutionException
        extends RuntimeException {

    private final String executionId;

    public NQueenCommandExecutionException(
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