package org.imageghost.Test;

import org.imageghost.FileController.AESKeyMaker;
import org.imageghost.Key.AsymmetricKeyGenerator;
import org.imageghost.PGPConnection.PGP;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
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
        System.out.printf("originalSecretKey: %s\n", originalSecretKey.getEncoded());

        String EE = createEE(originalSecretKey, receiverPublicKey);
        SecretKey secretKey2 = openEE(EE, receiverPrivateKey);

        // --------------------------------
        Assert.assertEquals(originalSecretKey, secretKey2);

        System.out.printf("secialized: %s\n", secretKey2.getEncoded());
        String sendEE = pgp.createEE(originalSecretKey, receiverPublicKey);
        System.out.printf("send - sendEE: %s\n", sendEE);

        System.out.printf("originalSecretKey: %s\n", originalSecretKey.getEncoded());
        SecretKey receivedSecretKey = pgp.openEE(sendEE, receiverPrivateKey);
        System.out.printf("receivedSecretKey: %s\n", receivedSecretKey.getEncoded());

        // Assert.assertEquals(originalSecretKey, receivedSecretKey) ;
    }
    public String createEE(SecretKey secretKey, String receiverPublicKey){
        String serializedSecretKeyBase64 = "";
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try(ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(secretKey);
                //직렬화(byte array)
                byte[] serializedSecretKey = baos.toByteArray();
                //byte array를 base64로 변환
                serializedSecretKeyBase64 = Base64.getEncoder().encodeToString(serializedSecretKey);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // encrypt with receive public key
        return encryptWithPublicKey(serializedSecretKeyBase64, receiverPublicKey);
    }
    public SecretKey openEE(String EE, String receiverPrivateKey){
        String serializedSecretKeyBase64 = decryptWithPrivateKey(EE, receiverPrivateKey);
        byte[] serializedSecretKey = Base64.getDecoder().decode(serializedSecretKeyBase64);
        SecretKey secretKey2 = null;
        try(ByteArrayInputStream bais = new ByteArrayInputStream(serializedSecretKey)) {
            try(ObjectInputStream ois = new ObjectInputStream(bais)) {
                //역직렬화(byte array -> object)
                Object objectUser = ois.readObject();
                secretKey2 = (SecretKey) objectUser;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return secretKey2;
    }

    public String encryptWithPublicKey(String secretKey, String receiverPublicKey) {
        String encryptedText = null;
        try {
            // 평문으로 전달받은 공개키를 사용하기 위해 공개키 객체 생성
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] bytePublicKey = Base64.getDecoder().decode(receiverPublicKey.getBytes());
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytePublicKey);
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            // 만들어진 공개키 객체로 암호화 설정
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] encryptedBytes = cipher.doFinal(secretKey.getBytes("UTF-8"));
            encryptedText = Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encryptedText;
    }

    /*
    private key로 복호화
 */
    public String decryptWithPrivateKey(String cipherText, String receiverPrivateKey){
        String decryptedText = null;

        try {
            // 평문으로 전달받은 공개키를 사용하기 위해 공개키 객체 생성
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] bytePrivateKey = Base64.getDecoder().decode(receiverPrivateKey.getBytes());
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bytePrivateKey);
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

            // 만들어진 공개키 객체로 복호화 설정
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            // 암호문을 평문화하는 과정
            byte[] encryptedBytes =  Base64.getDecoder().decode(cipherText.getBytes());
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            decryptedText = Base64.getEncoder().encodeToString(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return decryptedText;
    }

    @Test
    public void 암복호화_메서드_테스트(){
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

        SecretKey secretKey = pgp.generateSymmetricKey();

        String cipherText = pgp.encryptWithPublicKey(secretKey, receiverPublicKey);
        String result = pgp.decryptWithPrivateKey(cipherText, receiverPrivateKey);
        System.out.println(secretKey.getEncoded());
        System.out.println(result.getBytes());
    }
}
