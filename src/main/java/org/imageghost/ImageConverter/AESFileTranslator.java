package org.imageghost.ImageConverter;

import org.imageghost.AesEncryption.AESCipherMaker;
import org.imageghost.CustomException.NoKeyException;
import org.imageghost.Config;
import org.imageghost.FileController.FileTranslator;
import org.imageghost.GUIComponents.MainGui;
import org.imageghost.Key.Keys.Key;
import org.imageghost.Key.KeyFactory;
import org.imageghost.Key.Keys.SymmetricKey;
import org.imageghost.Wallet.KeyWallet;

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
                symmetricKey = KeyFactory.createSymmetricKey(); // 대칭키 생성
                KeyWallet.saveKeyAsMainKeyForSymmetricKey(symmetricKey); // 키 지갑에 Main키로 저장
                symmetricKey = KeyWallet.getMainKeyForSymmetricKey();    // 저장된 Main 키를 불러오기
                String plainTextOfFile = FileTranslator.transferFile2Text(new File(imagePath)); // 이미지를 Text로 변환
                cipherText = AESCipherMaker.encryptText(plainTextOfFile, (SecretKey) symmetricKey.getKey());  // Text 를 CipherText로 변환
            } catch(Exception err){
                err.printStackTrace();
                MainGui.showAlert("Cannot create Cipher Text from Image!"); // alert
            }
            return cipherText;
        }
        // if main key exist
        String plainTextOfFile = FileTranslator.transferFile2Text(new File(imagePath)); // 이미지를 Text로 변환
        String cipherText = null;
        try {
            cipherText = AESCipherMaker.encryptText(plainTextOfFile, (SecretKey) symmetricKey.getKey()); // Text를 CipherText로 변환
        } catch (Exception e) {
            e.printStackTrace();
            MainGui.showAlert("Cannot create Cipher Text from Image!"); // alert
        }
        return cipherText;
    }

    /*
        AES 암호화된 텍스트 -> 이미지 변환
        (decrypt 시에 main key가 없을 경우 decrypt 불가능.)
     */
    public static Object AESCispherText2Image(byte[] aesCipherText){ // return type changed from File to Object
        Key symmetricKey = null;
        try {
            symmetricKey = KeyWallet.getMainKeyForSymmetricKey();
        }catch(NoKeyException e){
            e.printStackTrace();
            MainGui.showAlert("No Main Key Existing!");
            return null;            // decrypt 시에 main key가 없을 경우 decrypt 불가능.
        }

        // 정상 진행
        String textOfImage = null;
        try {
            textOfImage = AESCipherMaker.decryptText(aesCipherText, (SecretKey) symmetricKey.getKey()); // CipherText 복호화
        }catch(Exception e){
            e.printStackTrace();
            MainGui.showAlert("Cannot decrypt CipherText!");
        }
        String uuid = UUID.randomUUID().toString(); // 고유한 문자열 생성
        File file = FileTranslator.transferText2File(textOfImage, Config.FILE_SAVE_PATH, uuid + ".jpg"); // 랜덤 이미지명으로 생성
        return file;
    }
}
