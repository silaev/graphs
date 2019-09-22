package com.silaev.graphs.exception;

/**
 * @author Konstantin Silaev on 9/20/2019
 */
public class DuplicateUserDataException extends RuntimeException {

    public DuplicateUserDataException(String errorMessage) {
        super(errorMessage);
    }

    public DuplicateUserDataException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
