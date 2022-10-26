package org.imageghost.SecureAlgorithm.Utils;

public class MessageOutput {
    String plainText;
    boolean receive;

    public MessageOutput(String plainText, boolean receive){
        this.plainText = plainText;
        this.receive = receive;
    }

    public String getPlainText() {
        return plainText;
    }

    public boolean isReceive() {
        return receive;
    }
}
