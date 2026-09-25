package com.achhecode.SpringBot.exception;

public class SudokuCommandExecutionException extends RuntimeException {

    private final String executionId;

    public SudokuCommandExecutionException(
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