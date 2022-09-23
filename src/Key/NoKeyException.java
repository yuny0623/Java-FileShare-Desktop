package Key;

public class NoKeyException extends RuntimeException{
    NoKeyException(){

    }
    NoKeyException(String message){
        super(message);
    }
}
