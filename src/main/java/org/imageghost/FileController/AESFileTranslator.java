package org.imageghost.FileController;

import org.imageghost.ClientCustomException.NoKeyException;
import org.imageghost.Config;
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
        SymmetricKey symmetricKey = null;
        try {
            symmetricKey = KeyWallet.getMainKeyForSymmetricKey();
        }catch(NoKeyException e){ // if no main key exist
            e.printStackTrace();
            String cipherText = null;
            try {
                SecretKey secretKey = AESKeyMaker.generateAESKey(); // secretKey 키 생성
                symmetricKey = new SymmetricKey(secretKey, "Main Key"); // 대칭키 생성
                KeyWallet.saveKeyAsMainKeyForSymmetricKey(symmetricKey); // 키 지갑에 Main키로 저장
                symmetricKey = KeyWallet.getMainKeyForSymmetricKey();    // 저장된 Main 키를 불러오기
                String plainTextOfFile = FileTranslator.transferFile2Text(new File(imagePath)); // 이미지를 Text로 변환
                cipherText = AESCipherMaker.encryptText(plainTextOfFile, symmetricKey.getKey());  // Text 를 CipherText로 변환
            } catch(Exception err){
                err.printStackTrace();
                MyFrame.showAlert("Cannot create Cipher Text from Image!"); // alert
            }
            return cipherText;
        }
        // if main key exist
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

    /*
        AES 암호화된 텍스트 -> 이미지 변환
     */
    public static Object AESCispherText2Image(byte[] aesCipherText){
        SymmetricKey symmetricKey = null;
        try {
            symmetricKey = KeyWallet.getMainKeyForSymmetricKey();
        }catch(NoKeyException e){
            e.printStackTrace();
            // decrypt 시에 main key가 없을 경우 decrypt 불가능.
            MyFrame.showAlert("No Main Key Existing!");
            return null;
        }

        // 정상 진행
        String textOfImage = null;
        try {
            textOfImage = AESCipherMaker.decryptText(aesCipherText, symmetricKey.getKey()); // CipherText 복호화
        }catch(Exception e){
            e.printStackTrace();
            MyFrame.showAlert("Cannot decrypt CipherText!");
        }
        String uuid = UUID.randomUUID().toString(); // 고유한 문자열 생성
        File file = FileTranslator.transferText2File(textOfImage, Config.FILE_SAVE_PATH, uuid + ".jpg"); // 랜덤 이미지명으로 생성
        return file;
    }
}
