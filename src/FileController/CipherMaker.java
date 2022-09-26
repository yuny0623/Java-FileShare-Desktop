package FileController;

import Wallet.SymmetricKey;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class CipherMaker {

    /*
        AES 키 생성기
     */
    public static SecretKey generateAESKey() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES");   // AES Key Generator 객체 생성
        generator.init(128);    // AES Key size 지정
        SecretKey secKey = generator.generateKey();     // AES 암호화 알고리즘에서 사용할 대칭키 생성
        return secKey;
    }

    /*
        AES 암호화
     */
    public static byte[] encryptText(String plainText, SecretKey secKey) throws Exception {
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());    // 암호문 생성
        return byteCipherText;
    }

    /*
        AES 복호화
     */
    public static String decryptText(byte[] byteCipherText, SecretKey secKey) throws Exception {
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, secKey);    // 복호화 모드 초기화
        byte[] bytePlainText = aesCipher.doFinal(byteCipherText);   // 암호문 -> 평문으로 복호화
        return new String(bytePlainText);
    }
}
