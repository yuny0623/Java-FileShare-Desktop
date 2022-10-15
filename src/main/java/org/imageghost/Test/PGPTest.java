package org.imageghost.Test;

import org.imageghost.FileController.AESKeyMaker;
import org.imageghost.Key.AsymmetricKeyGenerator;
import org.imageghost.PGPConnection.CutomException.InvalidMessageIntegrityException;
import org.imageghost.PGPConnection.PGP;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.util.HashMap;

public class PGPTest {
    @Test
    public void pgp구조테스트(){
        // given
        HashMap<String, String> senderKeyPair = AsymmetricKeyGenerator.generateKeyPair();
        String senderPublicKey = senderKeyPair.get("publicKey");
        String senderPrivateKey = senderKeyPair.get("privateKey");

        HashMap<String, String> receiverKeyPair = AsymmetricKeyGenerator.generateKeyPair();
        String receiverPublicKey = receiverKeyPair.get("publicKey");
        String receiverPrivateKey = receiverKeyPair.get("privateKey");

        PGP pgp = new PGP();
        pgp.setReceiverPublicKey(senderPublicKey);
        pgp.setSenderPrivateKey(senderPrivateKey);
        pgp.setReceiverPublicKey(receiverPublicKey);
        pgp.setReceiverPrivateKey(receiverPrivateKey);

        // when
        String fromData = pgp.sendData("테스트입니다.");
        System.out.println("-------------------");
        System.out.println(fromData);
        System.out.println("-------------------");

        String toData = "";
        try {
            toData = pgp.receiveData(fromData);
        }catch(InvalidMessageIntegrityException e){
            e.printStackTrace();
        }
        System.out.println("-------------------");
        System.out.println("toData: " + toData + "\n");
        System.out.println("-------------------");

        // then
        Assert.assertEquals(fromData, toData);
    }

    @Test
    public void splitter테스트(){

    }

    @Test
    public void 전자봉투테스트(){
        /*
            Alice
            1. aes 키 생성
            2. aes 키를 BOB의  public key로 잠구

            Bob:
            1. 전자봉투를 private key로 열어서 aes 키 얻어냄.
         */

        // given
        PGP pgp = new PGP();

        HashMap<String, String> receiverKeyPair = AsymmetricKeyGenerator.generateKeyPair();
        String receiverPublicKey = receiverKeyPair.get("publicKey");
        String receiverPrivateKey = receiverKeyPair.get("privateKey");

        SecretKey secretKey = AESKeyMaker.generateAESKey();
        String aesKey = new String(secretKey.getEncoded());
        // when
        String ee = pgp.createEE(secretKey, receiverPublicKey);
        String receivedAesKey = pgp.openEE(ee, receiverPrivateKey);

        System.out.printf("ee: %s\n", ee);
        System.out.printf("aesKey: %s\n", aesKey);
        System.out.printf("receivedAesKey: %s\n", receivedAesKey);

        Assert.assertEquals(aesKey, receivedAesKey);
        // then
    }
}
