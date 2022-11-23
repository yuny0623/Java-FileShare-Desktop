package org.imageghost.test;

import org.imageghost.aesencryption.AESCipherMaker;
import org.imageghost.key.KeyFactory;
import org.imageghost.key.keys.ASymmetricKey;
import org.imageghost.key.keys.SymmetricKey;
import org.imageghost.securealgorithm.pgp.PGP;
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
    public void symmetricKey_encryption_decryption_test(){
        // given
        String plainText = "This is test.";
        String receivedPlainText = "";
        SecretKey commonSecretKey = KeyFactory.createSymmetricKey().getAESKey();

        // when
        String cipherText = AESCipherMaker.encryptWithBase64(plainText, commonSecretKey);
        receivedPlainText = AESCipherMaker.decryptWithBase64(cipherText, commonSecretKey);

        // then
        Assert.assertEquals(plainText, receivedPlainText);
    }

    @Test
    public void EE_trasfer_test1(){
        // given
        SecretKey secretKeyOriginal = KeyFactory.createSymmetricKey().getAESKey();

        // when
        String ee = pgp.createEE(secretKeyOriginal.getEncoded(), receiverPublicKey);
        byte[] byteArray = pgp.openEE(ee, receiverPrivateKey);

        // then
        Assert.assertNotNull(byteArray);
    }

    @Test
    public void EE_transfer_test2(){
        // given
        String originalMessage = "This is test.";
        pgp.setPlainText(originalMessage);

        // when
        String originalMAC = pgp.generateMAC(originalMessage);
        String digitalSignature = pgp.generateDigitalSignature(originalMAC, senderPrivateKey);
        String decodedMAC = pgp.solveDigitalSignature(digitalSignature, senderPublicKey);

        // then
        Assert.assertEquals(originalMAC, decodedMAC);
    }

    @Test
    public void message_body_generation_test(){
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
    public void body_to_AESkey_encryption_decryption_test(){
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
    public void EE_concat_with_Body_test(){
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
    public void get_key_from_EE_test(){
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
    public void getBytes_to_String_conversion_test(){
        // given
        String plainText = "테스트입니다.";

        // when
        String fixedText = new String(plainText.getBytes());

        // then
        Assert.assertEquals(plainText, fixedText);
    }

    @Test
    public void secretKey_to_Bytes_and_Bytes_to_SecretKey_recover_test() throws Exception{
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
    public void get_received_AESKey_test() throws Exception{
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
    public void PGP_test(){
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
