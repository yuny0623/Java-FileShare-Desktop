package org.imageghost.ClientCustomException;

public class InvalidMessageIntegrityException extends Exception{
    public InvalidMessageIntegrityException(String message){
        super(message);
    }
}
