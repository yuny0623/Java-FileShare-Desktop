package org.imageghost.PGPConnection;

import org.imageghost.PGPConnection.CutomException.InvalidMessageIntegrityException;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;

public class PGP {
    /*
        PGP Communication Implementation - by yuny0623
     */

    /*
        Alice
     */
    private String plainText;
    private String senderPublicKey;
    private String senderPrivateKey;
    private String receiverPublicKey;
    private String receiverPrivateKey;

    /*
        Bob
     */
    private String receivedPlainText;
    private String decryptedMAC;
    private String result;
    private String digitalSignature;
    private String body;
    private String ee;
    private String aesKey;

    private String SENDER_AES_KEY;
    private String RECEIVER_AES_KEY;

    public PGP(){

    }

    public PGP(String plainText, String senderPublicKey, String senderPrivateKey, String receiverPublicKey, String receiverPrivateKey){
        this.plainText = plainText;
        this.senderPublicKey = senderPublicKey;
        this.senderPrivateKey = senderPrivateKey;
        this.receiverPublicKey = receiverPublicKey;
        this.receiverPrivateKey = receiverPrivateKey;
    }

    public void setPlainText(String plainText){
        this.plainText = plainText;
    }
    public void setSenderPublicKey(String senderPublicKey){
        this.senderPublicKey = senderPublicKey;
    }

    public void setSenderPrivateKey(String senderPrivateKey){
        this.senderPrivateKey = senderPrivateKey;
    }

    public void setReceiverPublicKey(String receiverPublicKey){
        this.receiverPublicKey = receiverPublicKey;
    }

    public void setReceiverPrivateKey(String receiverPrivateKey){
        this.receiverPrivateKey = receiverPrivateKey;
    }


    /*
        How to use?

        Alice:
           1. generate MAC.
           2. encrypt MAC with Alice's private key.
           3. add original message to result of 2 step.
           4. generate new symmetric key
           5. encrypt result of 3 step with the result of 4 step. the symmetric key.
           6. put symmetric key in E.E (encrypt E.E with Bob's public key).
           7. add all result and send to Bob.

        Bob:
           1. open EE with Bob's private key to get symmetric key from EE. (receiver authentication)
           2. encrypt message body with symmetric key from 1 step.
           3. encrypt digital signature via Alice's public key (sender authentication)
           4. hash the original plainText to create mac
           5. compare the 3 step's mac and received mac (message integrity)
     */

    public String generateMAC(String plainText) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(plainText.getBytes());
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return new String(md.digest());
    }

    /*
        Alice 2. MAC 암호화
     */
    public String generateDigitalSignature(String MAC, String senderPrivateKey){
        return encryptWithPrivateKey(MAC, senderPrivateKey);
    }

    /*
        private key로 전자서명에 사인
     */
    public String encryptWithPrivateKey(String plainText, String senderPrivateKey) {
        PrivateKey privateKey;
        String encryptedText = "";

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] bytePrivateKey = Base64.getDecoder().decode(senderPrivateKey.getBytes());
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bytePrivateKey);
            privateKey = keyFactory.generatePrivate(privateKeySpec);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);

            // 평문 private key 로 암호화
            encryptedText = Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
        }catch(Exception e){
            e.printStackTrace();
        }
        return encryptedText;
    }

    /*
        public key로 전자서명 풀기
     */
    public String solveDigitalSignature(String cipherText, String senderPublicKey) {
        PublicKey publicKey;
        String decryptedText = "";
        try{
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] bytePublicKey = Base64.getDecoder().decode(senderPublicKey.getBytes());
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytePublicKey);
            publicKey = keyFactory.generatePublic(publicKeySpec);

            Cipher cipher = Cipher.getInstance("RSA");

            cipher.init(Cipher.DECRYPT_MODE, publicKey);

            // 암호문 public key 로 복호화
            decryptedText = new String(cipher.doFinal(Base64.getDecoder().decode(cipherText.getBytes())));
        }catch(Exception e){
            e.printStackTrace();
        }
        return decryptedText;
    }

    /*
        Alice 3. 전자서명과 메시지 원본 합치기
     */
    public String generateBody(String plainText, String digitalSignature){
        StringBuffer sb = new StringBuffer();
        sb.append("-----BEGIN PLAIN TEXT-----\n");
        sb.append(plainText);
        sb.append("\n-----END PLAIN TEXT-----\n");
        sb.append("-----BEGIN DIGITAL SIGNATURE-----\n");
        sb.append(digitalSignature);
        sb.append("\n-----END DIGITAL SIGNATURE-----\n");
        return sb.toString();
    }

    /*
        Alice 4. 대칭키 생성
     */
    public SecretKey generateSymmetricKey(){
        KeyGenerator generator = null;
        try {
            generator = KeyGenerator.getInstance("AES");   // AES Key Generator 객체 생성
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        generator.init(128);    // AES Key size 지정
        SecretKey secKey = generator.generateKey();     // AES 암호화 알고리즘에서 사용할 대칭키 생성
        return secKey;
    }

    /*
        Recommended
     */
    public String encryptBodyFixed(String body, SecretKey secretKey){
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] cipherText = cipher.doFinal(body.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error occured while encrypting data", e);
        }
    }
    /*
        Recommended
     */
    public String decryptBodyFixed(String encryptedBody, SecretKey secretKey){
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] cipherText = cipher.doFinal(Base64.getDecoder().decode(encryptedBody));
            return new String(cipherText);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error occured while decrypting data", e);
        }
    }

    public String decryptBodyFixed2(String secretKey, String encryptedBody){
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);

        try {
            Cipher cipher = Cipher.getInstance("AES");
            // rebuild key using SecretKeySpec
            SecretKey originalKey = new SecretKeySpec(Arrays.copyOf(decodedKey, 16), "AES");
            cipher.init(Cipher.DECRYPT_MODE, originalKey);
            byte[] cipherText = cipher.doFinal(Base64.getDecoder().decode(encryptedBody));
            return new String(cipherText);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error occured while decrypting data", e);
        }
    }
    public String encryptedBodyFixed2(String secretKey, String body){
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);

        try {
            Cipher cipher = Cipher.getInstance("AES");
            // rebuild key using SecretKeySpec
            SecretKey originalKey = new SecretKeySpec(Arrays.copyOf(decodedKey, 16), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, originalKey);
            byte[] cipherText = cipher.doFinal(body.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error occured while encrypting data", e);
        }
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

            byte[] encryptedBytes = cipher.doFinal(secretKey.getBytes());
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

    /*
        Alice 8. 결과물과 전자봉투 합치기
     */
    public String appendEEWithBody(String EE, String body){
        StringBuffer sb = new StringBuffer();
        sb.append("-----BEGIN BODY-----\n");
        sb.append(body);
        sb.append("\n-----END BODY-----\n");
        sb.append("-----BEGIN EE-----\n");
        sb.append(EE);
        sb.append("\n-----END EE-----\n");
        return sb.toString();
    }


    /*
        1. open EE with Bob's private key to get symmetric key from EE. (receiver authentication)
           2. encrypt message body with symmetric key from 1 step.
           3. encrypt digital signature via Alice's public key (sender authentication)
           4. hash the original plainText to create mac
           5. compare the 3 step's mac and received mac (message integrity)
     */

    /*
        전달받은 데이터를 body와 ee로 분할
     */
    public HashMap<String, String> dataSplitter(String message){
        String bodyString = "-----BEGIN BODY-----\n";
        String eeString = "-----BEGIN EE-----\n";

        int bodyBeginIndex = message.indexOf("-----BEGIN BODY-----\n");
        int bodyEndIndex = message.indexOf("\n-----END BODY-----\n");
        int eeBeginIndex = message.indexOf("-----BEGIN EE-----\n");
        int eeEndIndex = message.indexOf("\n-----END EE-----\n");

        String body = message.substring(bodyBeginIndex + bodyString.length(), bodyEndIndex);
        String ee = message.substring(eeBeginIndex + eeString.length(), eeEndIndex);

        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("ee", ee);
        dataMap.put("body", body);
        return dataMap;

    }

    /*
        전달받은 body를 plainText와 DigitalSignature로 분할
     */
    public HashMap<String, String> bodySplitter(String body){
        String plainTextString = "-----BEGIN PLAIN TEXT-----\n";
        String digitalSignatureString = "-----BEGIN DIGITAL SIGNATURE-----\n";

        int plainTextBeginIndex = body.indexOf("-----BEGIN PLAIN TEXT-----\n");
        int plainTextEndIndex = body.indexOf("\n-----END PLAIN TEXT-----\n");
        int digitalSignatureBeginIndex = body.indexOf("-----BEGIN DIGITAL SIGNATURE-----\n");
        int digitalSignatureEndIndex = body.indexOf("\n-----END DIGITAL SIGNATURE-----\n");

        String receivedPlainText = body.substring(plainTextBeginIndex + plainTextString.length(), plainTextEndIndex);
        String digitalSignature = body.substring(digitalSignatureBeginIndex + digitalSignatureString.length(), digitalSignatureEndIndex);

        HashMap<String, String> bodyMap = new HashMap<>();
        bodyMap.put("receivedPlainText", receivedPlainText);
        bodyMap.put("digitalSignature", digitalSignature);
        return bodyMap;
    }


    /*
        Bob: DigitalSignature 를 Alice의 public key로 열기
     */
    public String decryptDigitalSignature(String digitalSignature, String senderPublicKey){
        return solveDigitalSignature(digitalSignature, senderPublicKey);
    }

    /*
        SHA-256 사용해서 MAC 생성
     */
    public String hashPlainText(String receivedPlainText){
        return generateMAC(receivedPlainText);
    }

    /*
        mac 값 비교
     */
    public boolean compareMAC(String receivedMAC, String generatedMAC){
        return receivedMAC.equals(generatedMAC);
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
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] bytePublicKey = Base64.getDecoder().decode(stringPublicKey.getBytes());
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytePublicKey);
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

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
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] bytePrivateKey = Base64.getDecoder().decode(stringPrivateKey.getBytes());
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bytePrivateKey);
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] byteEncryptedData = Base64.getDecoder().decode(encryptedData.getBytes());
            byteDecryptedData = cipher.doFinal(byteEncryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteDecryptedData;
    }
}
