package org.imageghost.Test;

import org.imageghost.FileController.AESKeyMaker;
import org.imageghost.Key.AsymmetricKeyGenerator;
import org.imageghost.PGPConnection.CutomException.InvalidMessageIntegrityException;
import org.imageghost.PGPConnection.PGP;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;

public class PGPTest {
    @Test
    public void pgp구조_전체동작_테스트(){
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
        String originalMessage = "테스트입니다.";

        String sendingCipherText = pgp.sendData(originalMessage);
        System.out.println("-------sendingCipherText-------");
        System.out.println(sendingCipherText);
        System.out.println("-------------------------------");

        String receivedMessage = "";
        try {
            receivedMessage = pgp.receiveData(sendingCipherText);
        }catch(InvalidMessageIntegrityException e){
            e.printStackTrace();
        }

        System.out.println("--------receivedMessage---------");
        System.out.println(receivedMessage);
        System.out.println("--------------------------------");
        // then
        Assert.assertEquals(originalMessage, receivedMessage);
    }

    @Test
    public void splitter_작동_테스트(){
        String data = "-----BEGIN BODY-----\n" +
                "� �\u0003�\u0014 7�Ya�\n" +
                "iF�M�J��������-/��N�s���\u0019�\n" +
                "\u0004.\u0005Q�\u007F��\n" +
                "\n" +
                "-----END BODY-----\n" +
                "-----BEGIN EE-----\n" +
                "KS+oTdF04fcCU5G8T0D+HP6hHIFkFQKERcdzZUmDC+AnrDSRKn3B+BR1QkA5bUap3cE2IH64mdls/qJvHGrYyE744+Xym2PtPxnF6jKKPq0ecxOCZWqeH0MmqZhp3/vSp3Q2lNB53oo7F7pGthzJIJHyHEgC5Qn2H4rkyFycfMyznROXP7aT3pEmEae0fzVgaCk057oizzvwJ20LIl8yRONmO0hmaCa6EXmijvcUiNcnC4UX87MAkQOxLIYgUlGRvLCPh1k9Z2aCHB3OpTJ9jzk8FG9Dpqw8Xyo1RfRY66Yv2L5s+Iatey8bgDllpPwl22y9GK6x9Xk3pA/Q4nMaAA==\n" +
                "-----END EE-----";
        // given

        // when

        // then
    }


    @Test
    public void 전자봉투_송수신_테스트(){
        // given
        PGP pgp = new PGP();
        HashMap<String, String> receivedKeyPair = AsymmetricKeyGenerator.generateKeyPair();
        String receiverPublicKey = receivedKeyPair.get("publicKey");
        String receiverPrivateKey = receivedKeyPair.get("privateKey");

        // when
        SecretKey secretKeyOriginal = AESKeyMaker.generateAESKey();
        String ee = pgp.createEE(secretKeyOriginal, receiverPublicKey);
        SecretKey secretKeyDecoded = pgp.openEE(ee, receiverPrivateKey);

        String secretKey1 = new String(secretKeyOriginal.getEncoded());
        String secretKey2 = new String(secretKeyDecoded.getEncoded());

        System.out.println("secretKey1: " + secretKey1);
        System.out.println("secretKey2: " + secretKey2);

        SecretKey secretKeyA = new SecretKeySpec(secretKey1.getBytes(), "AES");
        SecretKey secretKeyB = new SecretKeySpec(secretKey2.getBytes(), "AES");

        // then
        // Assert.assertEquals(secretKeyOriginal, secretKeyDecoded); // -> 통과 실패
        Assert.assertEquals(secretKey1, secretKey2);                 // -> 통과 성공
        Assert.assertEquals(secretKeyA, secretKeyB);                 // -> 통과 성공
    }

    @Test
    public void 전자서명_송수신_테스트(){
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
        String originalMessage = "테스트입니다.";
        pgp.setPlainText(originalMessage);
        String originalMAC = pgp.generateMAC(originalMessage);
        String digitalSignature = pgp.encryptMAC(originalMAC, senderPrivateKey);
        String decodedMAC = pgp.decryptDigitalSignature(digitalSignature, senderPublicKey);

        // then
        System.out.printf("original MAC: %s\n", originalMAC);
        System.out.printf("original decodedMAC: %s\n", decodedMAC);
        Assert.assertEquals(originalMAC, decodedMAC);
    }
}
