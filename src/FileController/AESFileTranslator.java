package FileController;

import Wallet.KeyWallet;
import Wallet.SymmetricKey;

import javax.crypto.SecretKey;
import java.io.File;
import java.security.NoSuchAlgorithmException;

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
                String fileString = FileTranslator.transferFile2String(new File(imagePath)); // 이미지를 Text로 변환
                cipherText = AESCipherMaker.encryptText(fileString, symmetricKey.getKey());  // Text 를 CipherText로 변환
            }catch(Exception e){
                e.printStackTrace();
            }
            return cipherText;
        }
        else {      // Main 키가 존재할 경우
            String fileString = FileTranslator.transferFile2String(new File(imagePath)); // 이미지를 Text로 변환
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
    public File AESCipherText2Image(String aesCipherText) {
        SymmetricKey symmetricKey = KeyWallet.getMainKey();
        if(symmetricKey == null){
            // 어떻게 처리할건지?
            // 복호화할때 symm key가 없으면 복호화를 못함.
        }

        String binaryData = null;
        try {
            binaryData = AESCipherMaker.decryptText(aesCipherText.getBytes(), symmetricKey.getKey());
        }catch(Exception e){
            e.printStackTrace();
        }

        File file = FileTranslator.transferString2File(binaryData, "../Image", "test.jpg");
        return file;
    }
}
