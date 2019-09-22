package com.silaev.graphs.exception;

/**
 * @author Konstantin Silaev on 9/20/2019
 */
public class UserDataNotFoundException extends RuntimeException {

    public UserDataNotFoundException(String errorMessage) {
        super(errorMessage);
    }

    public UserDataNotFoundException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
