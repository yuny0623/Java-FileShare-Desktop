package org.imageghost.ClientCustomException;

public class NoServerException extends RuntimeException {
    public NoServerException(String message){
        super(message);
    }
}