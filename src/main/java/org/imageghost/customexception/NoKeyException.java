package org.imageghost.customexception;

public class NoKeyException extends RuntimeException{
    public NoKeyException(String message){
        super(message);
    }
}
