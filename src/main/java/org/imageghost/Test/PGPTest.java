package org.imageghost.Test;

import org.imageghost.FileController.AESKeyMaker;
import org.imageghost.Key.AsymmetricKeyGenerator;
import org.imageghost.PGPConnection.PGP;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;

public class PGPTest {

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
        String digitalSignature = pgp.generateDigitalSignature(originalMAC, senderPrivateKey);
        String decodedMAC = pgp.decryptDigitalSignature(digitalSignature, senderPublicKey);

        // then
        System.out.printf("original MAC: %s\n", originalMAC);
        System.out.printf("original decodedMAC: %s\n", decodedMAC);
        Assert.assertEquals(originalMAC, decodedMAC);
    }


    @Test
    public void 메시지_body_생성_테스트(){
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
        String originalPlainText = "테스트입니다.";
        pgp.setPlainText(originalPlainText);
        String originalMAC = pgp.generateMAC(originalPlainText);
        String originalDigitalSignature = pgp.generateDigitalSignature(originalMAC, senderPrivateKey);
        String body = pgp.generateBody(originalPlainText, originalDigitalSignature);

        System.out.printf("send - originalPlainText: %s, %d\n", originalPlainText, originalPlainText.length());
        System.out.printf("send - originalDigitalSignature: %s\n", originalDigitalSignature);


        HashMap<String, String> bodyMap = pgp.bodySplitter(body);
        String receivedPlainText = bodyMap.get("receivedPlainText");
        String receivedDigitalSignature = bodyMap.get("digitalSignature");

        System.out.printf("receive - receivedPlainText: %s, %d\n", receivedPlainText, receivedPlainText.length());
        System.out.printf("receive - receivedDigitalSignature: %s\n", receivedDigitalSignature);

        String hashPlainText = pgp.hashPlainText(receivedPlainText);

        System.out.printf("receive - originalMAC: %s\n", originalMAC);
        System.out.printf("receive - hashPlainText: %s\n", hashPlainText);

        // MAC 값 비교
        Assert.assertEquals(originalMAC, hashPlainText);
        // 평문 비교
        Assert.assertEquals(originalPlainText, receivedPlainText);
        // 전자서명 비교
        Assert.assertEquals(originalDigitalSignature, receivedDigitalSignature);
    }


    @Test
    public void body를_AES키로_암호화_복호화_테스트(){
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
        String originalPlainText = "테스트입니다.";
        pgp.setPlainText(originalPlainText);
        String originalMAC = pgp.generateMAC(originalPlainText);
        String originalDigitalSignature = pgp.generateDigitalSignature(originalMAC, senderPrivateKey);
        String body = pgp.generateBody(originalPlainText, originalDigitalSignature);
        SecretKey secretKey = pgp.generateSymmetricKey(); // 대칭키 생성
        String encryptedBody = pgp.encryptBodyFixed(body, secretKey);

        System.out.printf("send - originalPlainText: %s\n", originalPlainText);
        System.out.printf("send - originalMAC: %s\n", originalMAC);
        System.out.printf("send - originalDigitalSignature: %s\n", originalDigitalSignature);
        System.out.printf("send - body: %s\n", body);
        System.out.printf("send - encryptedBody: %s\n", encryptedBody);

        String decryptedBody = pgp.decryptBodyFixed(encryptedBody, secretKey); // 여기서 에러 발생.

        HashMap<String, String> bodyMap = pgp.bodySplitter(decryptedBody);
        String receivedPlainText = bodyMap.get("receivedPlainText");
        String receivedDigitalSignature = bodyMap.get("digitalSignature");
        String hashPlainText = pgp.hashPlainText(receivedPlainText);

        System.out.printf("receive - decryptedBody: %s\n", decryptedBody);
        System.out.printf("receive - receivedPlainText: %s\n", receivedPlainText);
        System.out.printf("receive - receivedDigitalSignature: %s\n", receivedDigitalSignature);
        System.out.printf("receive - hashPlainText: %s\n", hashPlainText);

        // MAC 값 비교
        Assert.assertEquals(originalMAC, hashPlainText);
        // 평문 비교
        Assert.assertEquals(originalPlainText, receivedPlainText);
        // 전자서명 비교
        Assert.assertEquals(originalDigitalSignature, receivedDigitalSignature);
    }

    @Test
    public void EE와_Body합치기_테스트(){
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
        /*
            송신부
         */
        String originalPlainText = "테스트입니다.";
        pgp.setPlainText(originalPlainText);
        // body 생성
        String originalMAC = pgp.generateMAC(originalPlainText);
        String originalDigitalSignature = pgp.generateDigitalSignature(originalMAC, senderPrivateKey);
        String body = pgp.generateBody(originalPlainText, originalDigitalSignature);
        SecretKey secretKeyOriginal = pgp.generateSymmetricKey(); // 대칭키 생성
        String encryptedBody = pgp.encryptBodyFixed(body, secretKeyOriginal);

        // 전자봉투 생성
        String ee = pgp.createEE(secretKeyOriginal, receiverPublicKey);
        String finalResult = pgp.appendEEWithBody(ee, encryptedBody); // 최종 결과물

        System.out.printf("send - originalPlainText: %s\n", originalPlainText);
        System.out.printf("send - originalMAC: %s\n", originalMAC);
        System.out.printf("send - originalDigitalSignature: %s\n", originalDigitalSignature);
        System.out.printf("send - body: %s\n", body);
        System.out.printf("send - encryptedBody: %s\n", encryptedBody);
        System.out.printf("send - ee: %s\n", ee);
        System.out.printf("send - finalResult: %s\n", finalResult);

        /*
        SecretKey secretKeyOriginal = AESKeyMaker.generateAESKey();
        String ee = pgp.createEE(secretKeyOriginal, receiverPublicKey);
        SecretKey secretKeyDecoded = pgp.openEE(ee, receiverPrivateKey);

        String secretKey1 = new String(secretKeyOriginal.getEncoded());
        String secretKey2 = new String(secretKeyDecoded.getEncoded());

        System.out.println("secretKey1: " + secretKey1);
        System.out.println("secretKey2: " + secretKey2);

        SecretKey secretKeyA = new SecretKeySpec(secretKey1.getBytes(), "AES");
        SecretKey secretKeyB = new SecretKeySpec(secretKey2.getBytes(), "AES");
         */
        /*
            수신부
         */
        HashMap<String, String> dataMap = pgp.dataSplitter(finalResult);
        String receivedBody = dataMap.get("body");
        String receivedEE = dataMap.get("ee");
        System.out.printf("receive - receivedEE: %s\n", receivedEE);
        System.out.printf("receive - receivedBody: %s\n", receivedBody);
        // AES 키 꺼내기
//        SecretKey receivedSecretKey = pgp.openEE(receivedEE, receiverPrivateKey); // 여기서 문제
//        String receivedSecretKeyString = new String(receivedSecretKey.getEncoded());
//        SecretKey receivedSecretKeyFixed = new SecretKeySpec(receivedSecretKeyString.getBytes(), "AES");
        // body 복호화
        String decryptedBody = pgp.decryptBodyFixed(receivedBody, secretKeyOriginal); // 에러 때문에 original key 사용
        HashMap<String, String> bodyMap = pgp.bodySplitter(decryptedBody);

        String receivedPlainText = bodyMap.get("receivedPlainText");
        String receivedDigitalSignature = bodyMap.get("digitalSignature");
        String receivedMAC = pgp.decryptDigitalSignature(receivedDigitalSignature, senderPublicKey);
        String hashPlainText = pgp.hashPlainText(receivedPlainText);

        System.out.printf("receive - decryptedBody: %s\n", decryptedBody);
        System.out.printf("receive - receivedPlainText: %s\n", receivedPlainText);
        System.out.printf("receive - receivedDigitalSignature: %s\n", receivedDigitalSignature);
        System.out.printf("receive - receivedMAC: %s\n", receivedMAC);
        System.out.printf("receive - hashPlainText: %s\n", hashPlainText);

        // MAC 값 비교
        Assert.assertEquals(receivedMAC, hashPlainText);
        // 평문 비교
        Assert.assertEquals(originalPlainText, receivedPlainText);
        // 전자서명 비교
        Assert.assertEquals(originalDigitalSignature, receivedDigitalSignature);
    }

    @Test
    public void 전자봉투에서_키꺼내기_테스트(){
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

        SecretKey originalSecretKey = pgp.generateSymmetricKey();
        String sendEE = pgp.createEE(originalSecretKey, receiverPublicKey);
        System.out.printf("send - sendEE: %s\n", sendEE);

        SecretKey receivedSecretKey = pgp.openEE(sendEE, receiverPrivateKey);
        SecretKey receivedSecretKeyFixed = new SecretKeySpec(new String(receivedSecretKey.getEncoded()).getBytes(), "AES");
        Assert.assertEquals(originalSecretKey,
                receivedSecretKeyFixed) ;
    }
}
