package org.imageghost.AesEncryption;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class AESCipherMaker {
    public static String encrypt(String plainText, SecretKey secretKey) throws Exception {
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());
        return new String(byteCipherText);
    }
    public static String decrypt(byte[] byteCipherText, SecretKey secKey) throws Exception {
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, secKey);
        byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
        return new String(bytePlainText);
    }
}
