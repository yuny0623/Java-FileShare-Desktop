package org.imageghost.Key;

import org.imageghost.Key.Key;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class SymmetricKey implements Key {
    private SecretKey AESKey;
    private String descryption;

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

    public String getDescryption() {
        return descryption;
    }

    public void setDescryption(String descryption) {
        this.descryption = descryption;
    }

    public SecretKey getAESKey(){
        return this.AESKey;
    }

    @Override
    public Object getKey() {
        return this.AESKey;
    }
}
