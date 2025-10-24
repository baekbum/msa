package com.example.user_service.exception;

public class UserProfileException extends RuntimeException {
    public UserProfileException(String message) {
        super(message);
    }

    public UserProfileException() {
        super();
    }

    public UserProfileException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserProfileException(Throwable cause) {
        super(cause);
    }

    protected UserProfileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
