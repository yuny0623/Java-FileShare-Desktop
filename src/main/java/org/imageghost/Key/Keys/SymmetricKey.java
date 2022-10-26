package org.imageghost.Key.Keys;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class SymmetricKey implements Key {
    private SecretKey AESKey;
    private String content;

    public SymmetricKey(){
        KeyGenerator generator = null;
        try {
            generator = KeyGenerator.getInstance("AES");   // AES Key Generator 객체 생성
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        generator.init(128);    // AES Key size 지정
        SecretKey secKey = generator.generateKey();     // AES 암호화 알고리즘에서 사용할 대칭키 생성
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
