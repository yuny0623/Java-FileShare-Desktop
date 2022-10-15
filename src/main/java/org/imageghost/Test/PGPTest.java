package org.imageghost.Test;

import org.imageghost.FileController.AESKeyMaker;
import org.imageghost.Key.AsymmetricKeyGenerator;
import org.imageghost.PGPConnection.CutomException.InvalidMessageIntegrityException;
import org.imageghost.PGPConnection.PGP;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.sql.SQLOutput;
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
        System.out.println("-----send Data---------");
        System.out.println(fromData);
        System.out.println("-------------------");

        String toData = "";
        try {
            toData = pgp.receiveData(fromData);
        }catch(InvalidMessageIntegrityException e){
            e.printStackTrace();
        }
        System.out.println("------receive Data-------");
        System.out.println("toData: " + toData + "\n");
        System.out.println("-------------------");

        // then
        Assert.assertEquals(fromData, toData);
    }

    @Test
    public void splitter테스트(){
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

    /*
            Alice
            1. aes 키 생성
            2. aes 키를 BOB의  public key로 잠구

            Bob:
            1. 전자봉투를 private key로 열어서 aes 키 얻어냄.
         */
    @Test
    public void 전자봉투테스트(){
        // given
        PGP pgp = new PGP();
        HashMap<String, String> receivedKeyPair = AsymmetricKeyGenerator.generateKeyPair();
        String receiverPublicKey = receivedKeyPair.get("publicKey");
        String receiverPrivateKey = receivedKeyPair.get("privateKey");

        SecretKey secretKey = AESKeyMaker.generateAESKey();
        String aesKey = new String(secretKey.getEncoded());
        // when
        String ee = pgp.createEE(secretKey, receiverPublicKey);
        SecretKey secretKey1 = pgp.openEE(ee, receiverPrivateKey);

        // then
        System.out.printf("ee: %s\n", ee);
        System.out.printf("aesKey: %s\n", aesKey);
        System.out.printf("receivedAesKey: %s\n", new String(secretKey1.getEncoded()));
        String secretKeyString = new String(secretKey1.getEncoded());

        SecretKey secretKeyA = new SecretKeySpec(aesKey.getBytes(),"AES");
        SecretKey secretKeyB = new SecretKeySpec(secretKeyString.getBytes(), "AES");
        Assert.assertEquals(aesKey, new String(secretKey1.getEncoded()));

        System.out.printf("A: %s\n", secretKeyA.getEncoded());
        System.out.printf("B: %s\n", secretKeyB.getEncoded());
        Assert.assertEquals(new String(secretKeyA.getEncoded()), new String(secretKeyB.getEncoded()));


    }
}
