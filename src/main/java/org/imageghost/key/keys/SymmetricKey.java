package org.imageghost.key.keys;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class SymmetricKey implements Key {
    private SecretKey AESKey;
    private String content;
    public SymmetricKey(){
        KeyGenerator generator = null;
        try {
            generator = KeyGenerator.getInstance("AES");
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        generator.init(128);
        SecretKey secKey = generator.generateKey();
        this.AESKey = secKey;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SecretKey getAESKey(){
        return this.AESKey;
    }

    @Override
    public Object getKey() {
        return this.AESKey;
    }
}
