package org.imageghost.customexception;

public class NoServerException extends RuntimeException {
    public NoServerException(String message){
        super(message);
    }
}
