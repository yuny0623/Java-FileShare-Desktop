package org.imageghost.SecureAlgorithm.Utils;

public class MessageInput {
    String cipherText;
    boolean send;

    public MessageInput(String cipherText, boolean send){
        this.cipherText = cipherText;
        this.send = send;
    }

    public String getCipherText() {
        return cipherText;
    }

    public boolean isSend() {
        return send;
    }
}
