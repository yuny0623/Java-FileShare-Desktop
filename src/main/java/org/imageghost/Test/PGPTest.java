package org.imageghost.Test;

import org.imageghost.Key.KeyFactory;
import org.imageghost.Key.Keys.ASymmetricKey;
import org.imageghost.Key.Keys.SymmetricKey;
import org.imageghost.SecureAlgorithm.PGP.PGP;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;

public class PGPTest {
    PGP pgp;

    ASymmetricKey senderAsymmetricKey;
    ASymmetricKey receiverAsymmetricKey;

    SymmetricKey symmetricKey;
    String senderPublicKey;
    String senderPrivateKey;
    String receiverPublicKey;
    String receiverPrivateKey;

    @Before
    public void setupKeys(){
        senderAsymmetricKey = KeyFactory.createASymmetricKey();
        senderPublicKey = senderAsymmetricKey.getPublicKey();
        senderPrivateKey = senderAsymmetricKey.getPrivateKey();

        receiverAsymmetricKey = KeyFactory.createASymmetricKey();
        receiverPublicKey = receiverAsymmetricKey.getPublicKey();
        receiverPrivateKey = receiverAsymmetricKey.getPrivateKey();

        pgp = new PGP();
        pgp.setReceiverPublicKey(senderPublicKey);
        pgp.setSenderPrivateKey(senderPrivateKey);
        pgp.setReceiverPublicKey(receiverPublicKey);
        pgp.setReceiverPrivateKey(receiverPrivateKey);
    }

    @Test
    public void 전자봉투_송수신_테스트(){
        // given
        SecretKey secretKeyOriginal = KeyFactory.createSymmetricKey().getAESKey();

        // when
        String ee = pgp.createEE(secretKeyOriginal.getEncoded(), receiverPublicKey);
        byte[] byteArray = pgp.openEE(ee, receiverPrivateKey);

        // then
        Assert.assertNotNull(byteArray);
    }

    @Test
    public void 전자서명_송수신_테스트(){
        // given
        String originalMessage = "테스트입니다.";
        pgp.setPlainText(originalMessage);

        // when
        String originalMAC = pgp.generateMAC(originalMessage);
        String digitalSignature = pgp.generateDigitalSignature(originalMAC, senderPrivateKey);
        String decodedMAC = pgp.solveDigitalSignature(digitalSignature, senderPublicKey);

        // then
        Assert.assertEquals(originalMAC, decodedMAC);
    }


    @Test
    public void 메시지_body_생성_테스트(){
        // given
        String originalPlainText = "테스트입니다.";
        pgp.setPlainText(originalPlainText);

        // when
        String originalMAC = pgp.generateMAC(originalPlainText);
        String originalDigitalSignature = pgp.generateDigitalSignature(originalMAC, senderPrivateKey);
        String body = pgp.generateBody(originalPlainText, originalDigitalSignature);
        HashMap<String, String> bodyMap = pgp.bodySplitter(body);
        String receivedPlainText = bodyMap.get("receivedPlainText");
        String receivedDigitalSignature = bodyMap.get("digitalSignature");
        String hashPlainText = pgp.hashPlainText(receivedPlainText);

        // then
        Assert.assertEquals(originalMAC, hashPlainText);
        Assert.assertEquals(originalPlainText, receivedPlainText);
        Assert.assertEquals(originalDigitalSignature, receivedDigitalSignature);
    }


    @Test
    public void body를_AES키로_암호화_복호화_테스트(){
        // given
        String originalPlainText = "테스트입니다.";
        pgp.setPlainText(originalPlainText);

        // when
        String originalMAC = pgp.generateMAC(originalPlainText);
        String originalDigitalSignature = pgp.generateDigitalSignature(originalMAC, senderPrivateKey);
        String body = pgp.generateBody(originalPlainText, originalDigitalSignature);
        SecretKey secretKey = pgp.generateSymmetricKey();
        String encryptedBody = pgp.encryptBody(body, secretKey);
        String decryptedBody = pgp.decryptBody(encryptedBody, secretKey);
        HashMap<String, String> bodyMap = pgp.bodySplitter(decryptedBody);
        String receivedPlainText = bodyMap.get("receivedPlainText");
        String receivedDigitalSignature = bodyMap.get("digitalSignature");
        String hashPlainText = pgp.hashPlainText(receivedPlainText);

        // then
        Assert.assertEquals(originalMAC, hashPlainText);
        Assert.assertEquals(originalPlainText, receivedPlainText);
        Assert.assertEquals(originalDigitalSignature, receivedDigitalSignature);
    }

    @Test
    public void EE와_Body합치기_테스트(){
        // given
        String originalPlainText = "테스트입니다.";
        pgp.setPlainText(originalPlainText);

        // when
        String originalMAC = pgp.generateMAC(originalPlainText);
        String originalDigitalSignature = pgp.generateDigitalSignature(originalMAC, senderPrivateKey);
        String body = pgp.generateBody(originalPlainText, originalDigitalSignature);
        SecretKey secretKeyOriginal = pgp.generateSymmetricKey();
        String encryptedBody = pgp.encryptBody(body, secretKeyOriginal);
        String ee = pgp.createEE(secretKeyOriginal.getEncoded(), receiverPublicKey);
        String finalResult = pgp.appendEEWithBody(ee, encryptedBody);
        HashMap<String, String> dataMap = pgp.dataSplitter(finalResult);
        String receivedBody = dataMap.get("body");
        String receivedEE = dataMap.get("ee");
        String decryptedBody = pgp.decryptBody(receivedBody, secretKeyOriginal);
        HashMap<String, String> bodyMap = pgp.bodySplitter(decryptedBody);
        String receivedPlainText = bodyMap.get("receivedPlainText");
        String receivedDigitalSignature = bodyMap.get("digitalSignature");
        String receivedMAC = pgp.solveDigitalSignature(receivedDigitalSignature, senderPublicKey);
        String hashPlainText = pgp.hashPlainText(receivedPlainText);

        // then
        Assert.assertEquals(receivedMAC, hashPlainText);
        Assert.assertEquals(originalPlainText, receivedPlainText);
        Assert.assertEquals(originalDigitalSignature, receivedDigitalSignature);
    }

    @Test
    public void 전자봉투에서_키꺼내기_테스트(){
        // given
        SecretKey originalSecretKey = pgp.generateSymmetricKey();

        // when
        String EE = pgp.createEE(originalSecretKey.getEncoded(), receiverPublicKey);
        byte[] secretKeyByteArray = pgp.openEE(EE, receiverPrivateKey);
        SecretKey decryptedSecretKey = new SecretKeySpec(secretKeyByteArray, "AES");

        // then
        Assert.assertEquals(originalSecretKey, decryptedSecretKey);
    }

    @Test
    public void getBytes에서_다시_String으로_변환테스트(){
        // given
        String plainText = "테스트입니다.";

        // when
        String fixedText = new String(plainText.getBytes());

        // then
        Assert.assertEquals(plainText, fixedText);
    }

    @Test
    public void SecretKey에서_Bytes로_Bytes에서_다시_SecretKey로_복원테스트() throws Exception{
        // given
        SecretKey originalSecretKey = pgp.generateSymmetricKey();

        // when
        byte[] intermediateByteArray = originalSecretKey.getEncoded();
        String intermediateString = new String(intermediateByteArray);
        SecretKey fixedSecretKey = new SecretKeySpec(intermediateByteArray, "AES");

        // then
        Assert.assertEquals(originalSecretKey, fixedSecretKey);
    }

    @Test
    public void AES키_암복호화_테스트() throws Exception{
        // given
        SecretKey secretKey = pgp.generateSymmetricKey();

        // when
        String cipherText = pgp.encode(secretKey.getEncoded(), receiverPublicKey);
        byte[] plainText = pgp.decode(cipherText, receiverPrivateKey);
        String encodedKey = new String(secretKey.getEncoded(), "UTF-8");
        String decodedKey =  new String(plainText, "UTF-8");
        SecretKey secretKeyA = new SecretKeySpec(encodedKey.getBytes(), "AES");
        SecretKey secretKeyB = new SecretKeySpec(decodedKey.getBytes(), "AES");

        // then
        Assert.assertEquals(encodedKey, decodedKey);
        Assert.assertEquals(secretKeyA, secretKeyB);
        Assert.assertEquals(new String(secretKey.getEncoded()), new String(plainText));
    }

    @Test
    public void 전체PGP_통합테스트(){
        // given
        String originalPlainText = "테스트입니다.";
        pgp.setPlainText(originalPlainText);

        // when
        String originalMAC = pgp.generateMAC(originalPlainText);
        String originalDigitalSignature = pgp.generateDigitalSignature(originalMAC, senderPrivateKey);
        String body = pgp.generateBody(originalPlainText, originalDigitalSignature);
        SecretKey secretKeyOriginal = pgp.generateSymmetricKey();
        String encryptedBody = pgp.encryptBody(body, secretKeyOriginal);
        String ee = pgp.createEE(secretKeyOriginal.getEncoded(), receiverPublicKey);
        String finalResult = pgp.appendEEWithBody(ee, encryptedBody);
        HashMap<String, String> dataMap = pgp.dataSplitter(finalResult);
        String receivedBody = dataMap.get("body");
        String receivedEE = dataMap.get("ee");
        byte[] aesKey = pgp.openEE(receivedEE, receiverPrivateKey);
        SecretKey decryptedSecretKey = new SecretKeySpec(aesKey, "AES");
        String decryptedBody = pgp.decryptBody(receivedBody, decryptedSecretKey);
        HashMap<String, String> bodyMap = pgp.bodySplitter(decryptedBody);
        String receivedPlainText = bodyMap.get("receivedPlainText");
        String receivedDigitalSignature = bodyMap.get("digitalSignature");
        String receivedMAC = pgp.solveDigitalSignature(receivedDigitalSignature, senderPublicKey);
        String hashPlainText = pgp.hashPlainText(receivedPlainText);

        // then
        Assert.assertEquals(receivedMAC, hashPlainText);
        Assert.assertEquals(originalPlainText, receivedPlainText);
        Assert.assertEquals(originalDigitalSignature, receivedDigitalSignature);
        Assert.assertEquals(secretKeyOriginal, decryptedSecretKey);
    }
}
