package com.example.team_service.exception;

public class TeamNotExistException extends RuntimeException {
    public TeamNotExistException(String message) {
        super(message);
    }

    public TeamNotExistException() {
        super();
    }

    public TeamNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public TeamNotExistException(Throwable cause) {
        super(cause);
    }

    protected TeamNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
