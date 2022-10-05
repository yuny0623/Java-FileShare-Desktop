package org.imageghost.ClientCustomException;

public class NoKeyException extends RuntimeException{
    public NoKeyException(String message){
        super(message);
    }
}
