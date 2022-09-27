package FileController;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class AESCipherMaker {

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
