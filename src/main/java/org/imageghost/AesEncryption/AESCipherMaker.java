package org.imageghost.AesEncryption;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.util.Base64;

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

    /*
        Could get Error when using encrypt method of IllegalBlockSizeException.
        use encryptWithBase64 method instead.
     */
    public static String encryptWithBase64(String body, SecretKey secretKey){
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] cipherText = cipher.doFinal(body.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception e) {
            throw new RuntimeException("Error occured while encrypting data", e);
        }
    }

    /*
        Could get Error when using decrypt method of IllegalBlockSizeException.
        use decryptWithBase64 method instead.
    */
    public static String decryptWithBase64(String encryptedBody, SecretKey secretKey){
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] cipherText = cipher.doFinal(Base64.getDecoder().decode(encryptedBody));
            return new String(cipherText);
        } catch (Exception e) {
            throw new RuntimeException("Error occured while decrypting data", e);
        }
    }
}
