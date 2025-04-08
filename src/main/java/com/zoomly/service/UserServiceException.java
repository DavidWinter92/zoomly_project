package com.zoomly.service;

/**
 * UserServiceException.java
 * Custom exception class for handling errors in the UserService.
 * Extends RuntimeException to provide an unchecked exception with a message and cause.
 */
public class UserServiceException extends RuntimeException {

    /**
     * Constructs a new UserServiceException with the specified detail message and cause.
     *
     * @param message The detail message for the exception.
     * @param cause The cause of the exception (can be retrieved later with {@link Throwable#getCause()}).
     */
    public UserServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
