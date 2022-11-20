package org.imageghost.SecureAlgorithm.Utils;

public class MessageOutput {
    String plainText;
    boolean integrity;
    boolean error;
    String errorMessage;

    public MessageOutput(String plainText, boolean integrity, boolean error, String errorMessage){
        this.plainText = plainText;
        this.integrity = integrity;
        this.error = error;
        this.errorMessage = errorMessage;
    }

    public String getPlainText() {
        return plainText;
    }

    public boolean isIntegrity() {
        return integrity;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isError() {
        return error;
    }
}
