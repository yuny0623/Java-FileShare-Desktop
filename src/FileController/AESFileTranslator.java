package FileController;

import Wallet.KeyWallet;
import Wallet.SymmetricKey;

import javax.crypto.SecretKey;
import java.io.File;
import java.security.NoSuchAlgorithmException;

public class AESFileTranslator {
    /*
        이미지 -> AES암호화된 텍스트
     */
    public String Image2AESCipherText(String imagePath){
        SymmetricKey symmetricKey = KeyWallet.getMainKey();
        if(symmetricKey == null){ // MainKey 가 없을 경우
            byte[] cipherText = null;
            try {
                SecretKey secretKey = AESKeyMaker.generateAESKey();
                symmetricKey = new SymmetricKey(secretKey, "Main Key1"); // 새롭게 생성
                KeyWallet.saveKeyAsMainKey(symmetricKey);
                symmetricKey = KeyWallet.getMainKey();
                String fileString = FileTranslator.transferFile2String(new File(imagePath));
                cipherText = AESCipherMaker.encryptText(fileString, symmetricKey.getKey());
            }catch(Exception e){
                e.printStackTrace();
            }
            return cipherText.toString();
        }
        else {
            String fileString = FileTranslator.transferFile2String(new File(imagePath));
            byte[] cipherText = null;
            try {
                cipherText = AESCipherMaker.encryptText(fileString, symmetricKey.getKey());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return cipherText.toString();
        }
    }

    /*
        AES암호화된 텍스트 -> 이미지
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
