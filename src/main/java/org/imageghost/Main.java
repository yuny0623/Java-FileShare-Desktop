package org.imageghost;

import org.imageghost.GUIComponents.MyFrame;
import org.imageghost.Key.AsymmetricKeyGenerator;
import org.imageghost.PGPConnection.PGP;
import org.imageghost.Server.Connection;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws Exception{
        new MyFrame();


        /*
            simple test
         */
        HashMap<String, String> keyPair = AsymmetricKeyGenerator.generateKeyPair();
        String publicKey = keyPair.get("publicKey");
        String privateKey = keyPair.get("privateKey");

        PGP pgp = new PGP("","","");

        String originalPlainText = "테스트입니다.";
        String cipherText = pgp.encryptWithPrivateKey(originalPlainText, privateKey);
        String plainText = pgp.decryptWithPublicKey(cipherText, publicKey);
        System.out.println(cipherText);
        System.out.println(originalPlainText);
        System.out.println(plainText);
        // 테스트 성공이네. 휴...
    }
}
