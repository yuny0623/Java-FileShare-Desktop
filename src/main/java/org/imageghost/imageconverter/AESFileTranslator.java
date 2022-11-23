package org.imageghost.imageconverter;

import org.imageghost.aesencryption.AESCipherMaker;
import org.imageghost.customexception.NoKeyException;
import org.imageghost.Config;
import org.imageghost.filecontroller.FileTranslator;
import org.imageghost.guicomponents.AlertGui;
import org.imageghost.key.keys.Key;
import org.imageghost.key.KeyFactory;
import org.imageghost.key.keys.SymmetricKey;
import org.imageghost.wallet.KeyWallet;

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
