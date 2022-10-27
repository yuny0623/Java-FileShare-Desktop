package org.imageghost.AesEncryption;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class AESCipherMaker {
    public static String encrypt(String plainText, SecretKey secretKey) throws Exception {
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());    // 암호문 생성
        return new String(byteCipherText);
    }

    public static String decrypt(byte[] byteCipherText, SecretKey secKey) throws Exception {
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, secKey);    // 복호화 모드 초기화
        byte[] bytePlainText = aesCipher.doFinal(byteCipherText);   // 암호문 -> 평문으로 복호화
        return new String(bytePlainText);
    }
}
