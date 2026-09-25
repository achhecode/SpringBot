package com.achhecode.SpringBot.exception;

public class ZipCommandExecutionException extends RuntimeException {

    private final String executionId;

    public ZipCommandExecutionException(
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