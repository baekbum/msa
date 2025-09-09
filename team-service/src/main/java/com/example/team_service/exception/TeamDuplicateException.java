package com.example.team_service.exception;

public class TeamDuplicateException extends RuntimeException {
    public TeamDuplicateException(String message) {
        super(message);
    }

    public TeamDuplicateException() {
        super();
    }

    public TeamDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public TeamDuplicateException(Throwable cause) {
        super(cause);
    }

    protected TeamDuplicateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
