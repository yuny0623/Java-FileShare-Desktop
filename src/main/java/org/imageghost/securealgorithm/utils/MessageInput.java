package org.imageghost.securealgorithm.utils;

public class MessageInput {
    String cipherText;
    boolean send;
    boolean error;
    String errorMessage;
    public MessageInput(String cipherText, boolean send, boolean error, String errorMessage){
        this.cipherText = cipherText;
        this.send = send;
        this.error = error;
        this.errorMessage = errorMessage;
    }

    public String getCipherText() {
        return cipherText;
    }

    public boolean isSend() {
        return send;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isError() {
        return error;
    }
}
