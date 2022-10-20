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
    public void 전자봉투_송수신_테스트(){ // fail -> success 로 바꿔 봅시다.
        // given
        PGP pgp = new PGP();
        HashMap<String, String> receivedKeyPair = AsymmetricKeyGenerator.generateKeyPair();
        String receiverPublicKey = receivedKeyPair.get("publicKey");
        String receiverPrivateKey = receivedKeyPair.get("privateKey");

        System.out.printf("receiverPublicKey: %s\n", receiverPublicKey);
        System.out.printf("receiverPrivateKey: %s\n", receiverPrivateKey);

        // when
        SecretKey secretKeyOriginal = AESKeyMaker.generateAESKey();
        String ee = pgp.createEE(secretKeyOriginal, receiverPublicKey);
        SecretKey secretKeyDecoded = pgp.openEE(ee, receiverPrivateKey);

        // then
        System.out.printf("secretKeyOriginal: %s \n", secretKeyOriginal.getEncoded());
        System.out.printf("secretKeyDecoded: %s \n", secretKeyDecoded.getEncoded());

        Assert.assertEquals(secretKeyOriginal, secretKeyDecoded);
    }

    @Test
    public void 전자서명_송수신_테스트(){ // success
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
    public void 메시지_body_생성_테스트(){ // success
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

        String hashPlainText = pgp.hashPlainText(receivedPlainText); // 받은 평문 해시화

        System.out.printf("receive - originalMAC: %s\n", originalMAC);
        System.out.printf("receive - hashPlainText: %s\n", hashPlainText);

        // then
        // MAC 값 비교
        Assert.assertEquals(originalMAC, hashPlainText);
        // 평문 비교
        Assert.assertEquals(originalPlainText, receivedPlainText);
        // 전자서명 비교
        Assert.assertEquals(originalDigitalSignature, receivedDigitalSignature);
    }


    @Test
    public void body를_AES키로_암호화_복호화_테스트(){ // success
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

        String decryptedBody = pgp.decryptBodyFixed(encryptedBody, secretKey);

        HashMap<String, String> bodyMap = pgp.bodySplitter(decryptedBody);
        String receivedPlainText = bodyMap.get("receivedPlainText");
        String receivedDigitalSignature = bodyMap.get("digitalSignature");
        String hashPlainText = pgp.hashPlainText(receivedPlainText);

        System.out.printf("receive - decryptedBody: %s\n", decryptedBody);
        System.out.printf("receive - receivedPlainText: %s\n", receivedPlainText);
        System.out.printf("receive - receivedDigitalSignature: %s\n", receivedDigitalSignature);
        System.out.printf("receive - hashPlainText: %s\n", hashPlainText);

        // then
        // MAC 값 비교
        Assert.assertEquals(originalMAC, hashPlainText);
        // 평문 비교
        Assert.assertEquals(originalPlainText, receivedPlainText);
        // 전자서명 비교
        Assert.assertEquals(originalDigitalSignature, receivedDigitalSignature);
    }

    @Test
    public void EE와_Body합치기_테스트(){ // success
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
            수신부
         */
        HashMap<String, String> dataMap = pgp.dataSplitter(finalResult);
        String receivedBody = dataMap.get("body");
        String receivedEE = dataMap.get("ee");
        System.out.printf("receive - receivedEE: %s\n", receivedEE);
        System.out.printf("receive - receivedBody: %s\n", receivedBody);

        // body 복호화
        String decryptedBody = pgp.decryptBodyFixed(receivedBody, secretKeyOriginal);
        HashMap<String, String> bodyMap = pgp.bodySplitter(decryptedBody);

        String receivedPlainText = bodyMap.get("receivedPlainText");
        String receivedDigitalSignature = bodyMap.get("digitalSignature");
        String receivedMAC = pgp.decryptDigitalSignature(receivedDigitalSignature, senderPublicKey); // sender authentication
        String hashPlainText = pgp.hashPlainText(receivedPlainText);

        System.out.printf("receive - decryptedBody: %s\n", decryptedBody);
        System.out.printf("receive - receivedPlainText: %s\n", receivedPlainText);
        System.out.printf("receive - receivedDigitalSignature: %s\n", receivedDigitalSignature);
        System.out.printf("receive - receivedMAC: %s\n", receivedMAC);
        System.out.printf("receive - hashPlainText: %s\n", hashPlainText);

        // then
        // MAC 값 비교
        Assert.assertEquals(receivedMAC, hashPlainText);
        // 평문 비교
        Assert.assertEquals(originalPlainText, receivedPlainText);
        // 전자서명 비교
        Assert.assertEquals(originalDigitalSignature, receivedDigitalSignature);
    }

    @Test
    public void 전자봉투에서_키꺼내기_테스트(){ // fail
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

        String EE = createEE(originalSecretKey.getEncoded(), receiverPublicKey);
        byte[] secretKeyByteArray = openEE(EE, receiverPrivateKey);

        SecretKey decryptedSecretKey = new SecretKeySpec(secretKeyByteArray, "AES");
        Assert.assertEquals(originalSecretKey, decryptedSecretKey);
    }

    public String encryptWithPublicKeyToSting(String secretKey, String receiverPublicKey) {
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


    @Test
    public void 암복호화_메서드_테스트(){
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
        SecretKey originalSecretKey = pgp.generateSymmetricKey(); // 대칭키 생성
        String plainText = new String(originalSecretKey.getEncoded());

        String cipherText = encode(plainText.getBytes(), receiverPublicKey);
        byte[] decryptedCipherText = decode(cipherText, receiverPrivateKey);

        System.out.printf("plainText: %s\n", plainText);
        System.out.printf("cipherText: %s\n", cipherText);
        System.out.printf("decryptedCipherText: %s\n", decryptedCipherText);

        // then
        Assert.assertEquals(plainText, decryptedCipherText);
    }

    @Test
    public void getBytes에서_다시_String으로변환테스트(){
        // given
        String plainText = "테스트입니다.";
        // when
        String fixedText = new String(plainText.getBytes());

        System.out.printf("plainText: %s\n", plainText);
        System.out.printf("fixedText: %s\n", fixedText);

        // then
        Assert.assertEquals(plainText, fixedText);
    }

    @Test
    public void SecretKey에서_Bytes로_Bytes에서_다시_SecretKey로_복원테스트() throws Exception{
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

        SecretKey originalSecretKey = pgp.generateSymmetricKey();
        // when
        byte[] intermediateByteArray = originalSecretKey.getEncoded();
        String intermediateString = new String(intermediateByteArray);

        /*
            intermediateByteArray: [B@4e04a765
            intermediateString: � �!��v�Ik �~q

            결과가 위처럼 나왔는데 new String 으로 String으로 만들어준다고 해서 byte로 출력되는 문자열이 그대로 나오는게 아니라
            변환되서 나오는걸 확인할 수 있음.

            결과는 new SecretKeySpec에 전달할때는 SecretKey를 getEncoded 한 byte array 를 줘야만 원래 SecretKey로 복원됨을 알 수 있음.
         */
        System.out.printf("intermediateByteArray: %s\n", intermediateByteArray);
        System.out.printf("intermediateString: %s\n", intermediateString);
        // then
        SecretKey fixedSecretKey = new SecretKeySpec(intermediateByteArray, "AES");
        System.out.printf("originalSecretKey: %s\n", originalSecretKey.getEncoded());
        System.out.printf("fixedSecretKey: %s\n", fixedSecretKey.getEncoded());

        Assert.assertEquals(originalSecretKey, fixedSecretKey);
    }

    @Test
    public void AES키_암복호화_테스트()throws Exception{
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
        String cipherText = encode(secretKey.getEncoded(), receiverPublicKey);
        byte[] plainText = decode(cipherText, receiverPrivateKey);

        String a = new String(secretKey.getEncoded(), "UTF-8");
        String b =  new String(plainText, "UTF-8");

        SecretKey secretKeyA = new SecretKeySpec(a.getBytes(), "AES");
        SecretKey secretKeyB = new SecretKeySpec(b.getBytes(), "AES");

        Assert.assertEquals(secretKeyA, secretKeyB);
        // UTF-8로 동시에 변환하니까 된다!
        Assert.assertEquals(new String(secretKey.getEncoded()), new String(plainText));
    }

    public String createEE(byte[] secretKeyArray, String receiverPublicKey){
        return encode(secretKeyArray, receiverPublicKey);
    }
    public byte[] openEE(String cipherText, String receiverPrivateKey){
        return decode(cipherText, receiverPrivateKey);
    }


    /**
     * 암호화
     */
    public String encode(byte[] plainData, String stringPublicKey) {
        String encryptedData = null;
        try {
            //평문으로 전달받은 공개키를 공개키객체로 만드는 과정
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] bytePublicKey = Base64.getDecoder().decode(stringPublicKey.getBytes());
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytePublicKey);
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            //만들어진 공개키객체를 기반으로 암호화모드로 설정하는 과정
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            //평문을 암호화하는 과정
            byte[] byteEncryptedData = cipher.doFinal(plainData);
            encryptedData = Base64.getEncoder().encodeToString(byteEncryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedData;
    }

    /**
     * 복호화
     */
    public byte[] decode(String encryptedData, String stringPrivateKey) {
        String decryptedData = null;
        byte[] byteDecryptedData = null;

        try {
            //평문으로 전달받은 개인키를 개인키객체로 만드는 과정
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] bytePrivateKey = Base64.getDecoder().decode(stringPrivateKey.getBytes());
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bytePrivateKey);
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

            //만들어진 개인키객체를 기반으로 암호화모드로 설정하는 과정
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            //암호문을 평문화하는 과정
            byte[] byteEncryptedData = Base64.getDecoder().decode(encryptedData.getBytes());
            byteDecryptedData = cipher.doFinal(byteEncryptedData);
            // decryptedData = new String(byteDecryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return decryptedData;
        return byteDecryptedData;
    }
}
