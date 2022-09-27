package FileController;

import ClientCustomException.NoKeyException;
import Wallet.KeyWallet;
import Wallet.SymmetricKey;

import javax.crypto.SecretKey;
import java.io.File;
import java.util.UUID;

public class AESFileTranslator {

    /*
        이미지 -> AES 암호화된 텍스트
     */
    public byte[] Image2AESCipherText(String imagePath){
        SymmetricKey symmetricKey = KeyWallet.getMainKey();
        if(symmetricKey == null){ // MainKey 가 존재하지 않을 경우
            byte[] cipherText = null;
            try {
                SecretKey secretKey = AESKeyMaker.generateAESKey(); // 새로운 AES 키 생성 
                symmetricKey = new SymmetricKey(secretKey, "Main Key1"); // 대칭키 객제 생성
                KeyWallet.saveKeyAsMainKey(symmetricKey); // 키 지갑에 Main키로 저장
                symmetricKey = KeyWallet.getMainKey();    // 저장된 Main 키를 불러오기
                String fileString = FileTranslator.transferFile2Text(new File(imagePath)); // 이미지를 Text로 변환
                cipherText = AESCipherMaker.encryptText(fileString, symmetricKey.getKey());  // Text 를 CipherText로 변환
            }catch(Exception e){
                e.printStackTrace();
            }
            return cipherText;
        }
        else {      // Main 키가 존재할 경우
            String fileString = FileTranslator.transferFile2Text(new File(imagePath)); // 이미지를 Text로 변환
            byte[] cipherText = null;
            try {
                cipherText = AESCipherMaker.encryptText(fileString, symmetricKey.getKey()); // Text를 CipherText로 변환
            } catch (Exception e) {
                e.printStackTrace();
            }
            return cipherText;
        }
    }

    /*
        AES 암호화된 텍스트 -> 이미지
     */
    public File AESCipherText2Image(byte[] aesCipherText){
        SymmetricKey symmetricKey = null;
        try {
            symmetricKey = KeyWallet.getMainKey();
        }catch(NoKeyException e){
            e.printStackTrace();
        }

        String textOfImage = null;
        try {
            textOfImage = AESCipherMaker.decryptText(aesCipherText, symmetricKey.getKey()); // CipherText 복호화 
        }catch(Exception e){
            e.printStackTrace();
        }
        String uuid = UUID.randomUUID().toString(); // 고유한 문자열 생성
        File file = FileTranslator.transferText2File(textOfImage, "../Image", uuid + ".jpg");

        return file;
    }
}
