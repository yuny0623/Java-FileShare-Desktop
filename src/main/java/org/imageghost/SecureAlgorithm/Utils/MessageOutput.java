package org.imageghost.SecureAlgorithm.Utils;

public class MessageOutput {
    String plainText;
    boolean receive;
    boolean error;
    String errorMessage;

    public MessageOutput(String plainText, boolean receive, boolean error, String errorMessage){
        this.plainText = plainText;
        this.receive = receive;
        this.error = error;
        this.errorMessage = errorMessage;
    }

    public String getPlainText() {
        return plainText;
    }

    public boolean isReceive() {
        return receive;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isError() {
        return error;
    }
}
