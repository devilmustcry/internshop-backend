package com.sandstorm.internshop.exception;

public abstract class NotFound extends RuntimeException {
    public NotFound(String message) {
        super(message);
    }
}
