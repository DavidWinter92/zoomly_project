package com.zoomly.service;

/**
 * UserServiceException.java
 * Custom exception class for handling errors in the UserService.
 * Extends RuntimeException to provide an unchecked exception with a message and cause.
 */

public class UserServiceException extends RuntimeException {
    public UserServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}