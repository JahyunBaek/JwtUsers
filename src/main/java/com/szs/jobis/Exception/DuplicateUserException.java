package com.szs.jobis.Exception;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException() {
        super();
    }
    public DuplicateUserException(String message, Throwable cause) {
        super(message, cause);
    }
    public DuplicateUserException(String message) {
        super(message);
    }
    public DuplicateUserException(Throwable cause) {
        super(cause);
    }
}