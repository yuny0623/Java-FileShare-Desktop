package org.imageghost.ImageConverter;

import org.imageghost.AesEncryption.AESCipherMaker;
import org.imageghost.CustomException.NoKeyException;
import org.imageghost.Config;
import org.imageghost.FileController.FileTranslator;
import org.imageghost.GUIComponents.AlertGui;
import org.imageghost.Key.Keys.Key;
import org.imageghost.Key.KeyFactory;
import org.imageghost.Key.Keys.SymmetricKey;
import org.imageghost.Wallet.KeyWallet;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.UUID;

public class AESFileTranslator {

    public static String image2CipherText(String imagePath){
        SymmetricKey symmetricKey = null;
        try {
            symmetricKey = KeyWallet.getMainSymmetricKey();
        }catch(NoKeyException e){
            String cipherText = null;
            try {
                symmetricKey = KeyFactory.createSymmetricKey();
                KeyWallet.saveMainSymmetricKey(symmetricKey);
                symmetricKey = KeyWallet.getMainSymmetricKey();
                String plainTextOfFile = FileTranslator.transferFile2Text(new File(imagePath));
                cipherText = AESCipherMaker.encrypt(plainTextOfFile, (SecretKey) symmetricKey.getKey());
            } catch(Exception err){
                err.printStackTrace();
            }
            return cipherText;
        }
        String plainTextOfFile = "";
        try {
            plainTextOfFile = FileTranslator.transferFile2Text(new File(imagePath));
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }

        String cipherText = null;
        try {
            cipherText = AESCipherMaker.encrypt(plainTextOfFile, (SecretKey) symmetricKey.getKey());
        } catch (Exception e) {
            e.printStackTrace();
            new AlertGui("Cannot create Cipher Text from Image! due to:"+ e.getMessage(), false);
        }
        return cipherText;
    }

    public static Object cipherText2Image(byte[] aesCipherText) {
        Key symmetricKey = null;
        try {
            symmetricKey = KeyWallet.getMainSymmetricKey();
        } catch (NoKeyException e) {
            e.printStackTrace();
            return null;
        }
        String textOfImage = null;
        try {
            textOfImage = AESCipherMaker.decrypt(aesCipherText, (SecretKey) symmetricKey.getKey());
        } catch (Exception e) {
            new AlertGui("Cannot decrypt CipherText! due to:" + e.getMessage(), false);
            e.printStackTrace();
        }
        String uuid = UUID.randomUUID().toString();
        File file = null;
        try{
            file = FileTranslator.transferText2File(textOfImage, Config.FILE_SAVE_PATH, uuid + ".jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
}
