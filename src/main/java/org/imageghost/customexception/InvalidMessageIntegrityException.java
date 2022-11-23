package org.imageghost.customexception;

public class InvalidMessageIntegrityException extends Exception{
    public InvalidMessageIntegrityException(String message){
        super(message);
    }
}
