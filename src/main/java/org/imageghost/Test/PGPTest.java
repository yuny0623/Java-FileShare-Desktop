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
        senderAsymmetricKey = KeyFactory.createAsymmetricKey();
        senderPublicKey = senderAsymmetricKey.getPublicKey();
        senderPrivateKey = senderAsymmetricKey.getPrivateKey();

        receiverAsymmetricKey = KeyFactory.createAsymmetricKey();
        receiverPublicKey = receiverAsymmetricKey.getPublicKey();
        receiverPrivateKey = receiverAsymmetricKey.getPrivateKey();

        pgp = new PGP();
        pgp.setReceiverPublicKey(senderPublicKey);
        pgp.setSenderPrivateKey(senderPrivateKey);
        pgp.setReceiverPublicKey(receiverPublicKey);
        pgp.setReceiverPrivateKey(receiverPrivateKey);
    }

    @Test
    public void 전자봉투_송수신_테스트(){ // fail -> success 로 바꿔 봅시다.
        // given

        System.out.printf("receiverPublicKey: %s\n", receiverPublicKey);
        System.out.printf("receiverPrivateKey: %s\n", receiverPrivateKey);

        // when
        SecretKey secretKeyOriginal = KeyFactory.createSymmetricKey().getAESKey();
        String ee = pgp.createEE(secretKeyOriginal.getEncoded(), receiverPublicKey);
        byte[] byteArray = pgp.openEE(ee, receiverPrivateKey);

        // then
    }

    @Test
    public void 전자서명_송수신_테스트(){ // success
        // given
        // when
        String originalMessage = "테스트입니다.";
        pgp.setPlainText(originalMessage);
        String originalMAC = pgp.generateMAC(originalMessage);
        String digitalSignature = pgp.generateDigitalSignature(originalMAC, senderPrivateKey);
        String decodedMAC = pgp.solveDigitalSignature(digitalSignature, senderPublicKey);

        // then
        System.out.printf("original MAC: %s\n", originalMAC);
        System.out.printf("original decodedMAC: %s\n", decodedMAC);
        Assert.assertEquals(originalMAC, decodedMAC);
    }


    @Test
    public void 메시지_body_생성_테스트(){ // success
        // given

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

        // when
        String originalPlainText = "테스트입니다.";
        pgp.setPlainText(originalPlainText);
        String originalMAC = pgp.generateMAC(originalPlainText);
        String originalDigitalSignature = pgp.generateDigitalSignature(originalMAC, senderPrivateKey);
        String body = pgp.generateBody(originalPlainText, originalDigitalSignature);
        SecretKey secretKey = pgp.generateSymmetricKey(); // 대칭키 생성
        String encryptedBody = pgp.encryptBody(body, secretKey);

        System.out.printf("send - originalPlainText: %s\n", originalPlainText);
        System.out.printf("send - originalMAC: %s\n", originalMAC);
        System.out.printf("send - originalDigitalSignature: %s\n", originalDigitalSignature);
        System.out.printf("send - body: %s\n", body);
        System.out.printf("send - encryptedBody: %s\n", encryptedBody);

        String decryptedBody = pgp.decryptBody(encryptedBody, secretKey);

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
        String encryptedBody = pgp.encryptBody(body, secretKeyOriginal);

        // 전자봉투 생성
        String ee = pgp.createEE(secretKeyOriginal.getEncoded(), receiverPublicKey);
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
        String decryptedBody = pgp.decryptBody(receivedBody, secretKeyOriginal);
        HashMap<String, String> bodyMap = pgp.bodySplitter(decryptedBody);

        String receivedPlainText = bodyMap.get("receivedPlainText");
        String receivedDigitalSignature = bodyMap.get("digitalSignature");
        String receivedMAC = pgp.solveDigitalSignature(receivedDigitalSignature, senderPublicKey); // sender authentication
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
        SecretKey originalSecretKey = pgp.generateSymmetricKey();
        System.out.printf("originalSecretKey: %s\n", originalSecretKey.getEncoded());

        String EE = pgp.createEE(originalSecretKey.getEncoded(), receiverPublicKey);
        byte[] secretKeyByteArray = pgp.openEE(EE, receiverPrivateKey);

        SecretKey decryptedSecretKey = new SecretKeySpec(secretKeyByteArray, "AES");
        Assert.assertEquals(originalSecretKey, decryptedSecretKey);
    }

    @Test
    public void getBytes에서_다시_String으로_변환테스트(){
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
    public void AES키_암복호화_테스트() throws Exception{

        SecretKey secretKey = pgp.generateSymmetricKey();
        String cipherText = pgp.encode(secretKey.getEncoded(), receiverPublicKey);
        byte[] plainText = pgp.decode(cipherText, receiverPrivateKey);

        String a = new String(secretKey.getEncoded(), "UTF-8");
        String b =  new String(plainText, "UTF-8");

        SecretKey secretKeyA = new SecretKeySpec(a.getBytes(), "AES");
        SecretKey secretKeyB = new SecretKeySpec(b.getBytes(), "AES");

        Assert.assertEquals(secretKeyA, secretKeyB);
        // UTF-8로 동시에 변환하니까 된다!
        Assert.assertEquals(new String(secretKey.getEncoded()), new String(plainText));
    }

    @Test
    public void 전체PGP_통합테스트(){
        // given

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
        String encryptedBody = pgp.encryptBody(body, secretKeyOriginal);

        // 전자봉투 생성
        String ee = pgp.createEE(secretKeyOriginal.getEncoded(), receiverPublicKey);
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

        byte[] aesKey = pgp.openEE(receivedEE, receiverPrivateKey);
        SecretKey decryptedSecretKey = new SecretKeySpec(aesKey, "AES");

        // body 복호화
        String decryptedBody = pgp.decryptBody(receivedBody, decryptedSecretKey);
        HashMap<String, String> bodyMap = pgp.bodySplitter(decryptedBody);

        String receivedPlainText = bodyMap.get("receivedPlainText");
        String receivedDigitalSignature = bodyMap.get("digitalSignature");
        String receivedMAC = pgp.solveDigitalSignature(receivedDigitalSignature, senderPublicKey); // sender authentication
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
        // 전자봉투에서 얻어낸 대칭키가 original key와 같은지 비교
        Assert.assertEquals(secretKeyOriginal, decryptedSecretKey);
    }
}
