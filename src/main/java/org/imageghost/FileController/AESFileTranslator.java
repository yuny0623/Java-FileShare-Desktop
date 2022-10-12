package org.imageghost.FileController;

import org.imageghost.ClientCustomException.NoKeyException;
import org.imageghost.GUIComponents.MyFrame;
import org.imageghost.Wallet.KeyWallet;
import org.imageghost.Wallet.SymmetricKey;

import javax.crypto.SecretKey;
import java.io.File;
import java.util.UUID;

public class AESFileTranslator {

    /*
        이미지 -> AES 암호화된 텍스트
     */
    public static String Image2AESCipherText(String imagePath){
        SymmetricKey symmetricKey = KeyWallet.getMainKeyForSymmetricKey();
        if(symmetricKey == null){ // MainKey 가 존재하지 않을 경우
            String cipherText = null;
            try {
                SecretKey secretKey = AESKeyMaker.generateAESKey(); // secretKey 키 생성
                symmetricKey = new SymmetricKey(secretKey, "Main Key"); // 대칭키 생성
                KeyWallet.saveKeyAsMainKeyForSymmetricKey(symmetricKey); // 키 지갑에 Main키로 저장
                symmetricKey = KeyWallet.getMainKeyForSymmetricKey();    // 저장된 Main 키를 불러오기
                String plainTextOfFile = FileTranslator.transferFile2Text(new File(imagePath)); // 이미지를 Text로 변환
                cipherText = AESCipherMaker.encryptText(plainTextOfFile, symmetricKey.getKey());  // Text 를 CipherText로 변환
            } catch(Exception e){
                e.printStackTrace();
                MyFrame.showAlert("Cannot create Cipher Text from Image!"); // alert
            }
            return cipherText;
        }
        else {      // Main 키가 존재할 경우
            String plainTextOfFile = FileTranslator.transferFile2Text(new File(imagePath)); // 이미지를 Text로 변환
            String cipherText = null;
            try {
                cipherText = AESCipherMaker.encryptText(plainTextOfFile, symmetricKey.getKey()); // Text를 CipherText로 변환
            } catch (Exception e) {
                e.printStackTrace();
                MyFrame.showAlert("Cannot create Cipher Text from Image!"); // alert
            }
            return cipherText;
        }
    }

    /*
        AES 암호화된 텍스트 -> 이미지 변환
     */
    public static File AESCipherText2Image(byte[] aesCipherText){
        SymmetricKey symmetricKey = null;
        try {
            symmetricKey = KeyWallet.getMainKeyForSymmetricKey();
        }catch(NoKeyException e){
            e.printStackTrace();
            MyFrame.showAlert("No Main Key Existing!");
        }
        String textOfImage = null;
        try {
            textOfImage = AESCipherMaker.decryptText(aesCipherText, symmetricKey.getKey()); // CipherText 복호화
        }catch(Exception e){
            e.printStackTrace();
            MyFrame.showAlert("Cannot decrypt CipherText!");
        }

        String uuid = UUID.randomUUID().toString(); // 고유한 문자열 생성
        File file = FileTranslator.transferText2File(textOfImage, "../Image", uuid + ".jpg"); // 랜덤 이미지명으로 생성
        return file;
    }
}
