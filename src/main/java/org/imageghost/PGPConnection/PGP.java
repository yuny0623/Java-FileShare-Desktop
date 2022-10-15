package org.imageghost.PGPConnection;

import javax.crypto.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
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

    /*
        Bob
     */
    private String mac;
    private String message;
    private String body;
    private String ee;

    public PGP(){

    }

    public PGP(String plainText, String senderPublicKey, String senderPrivateKey, String receiverPublicKey){
        this.plainText = plainText;
        this.senderPublicKey = senderPublicKey;
        this.senderPrivateKey = senderPrivateKey;
        this.receiverPublicKey = receiverPublicKey;
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

    /*
        Alice 1: MAC 생성
     */
    public String generateMAC(String plainText) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(plainText.getBytes());
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return bytesToHex(md.digest());
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    /*
        Alice 2. MAC 암호화
     */
    public String encryptMAC(String MAC){
        return encryptWithPrivateKey(MAC, this.senderPrivateKey);
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
    public String decryptWithPublicKey(String cipherText, String senderPublicKey) {
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
    public String concatResult(String plainText, String digitalSignature){
        StringBuffer sb = new StringBuffer();
        sb.append("-----BEGIN PLAIN TEXT-----\n");
        sb.append(plainText);
        sb.append("-----END PLAIN TEXT-----\n");
        sb.append("-----BEGIN DIGITAL SIGNATURE-----\n");
        sb.append(digitalSignature);
        sb.append("-----END DIGITAL SIGNATURE-----\n");
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
        Alice 5. 내용물을 대칭키로 암호화
     */
    public String encryptBody(String body, SecretKey secretKey){
        String encryptedData = "";
        try {
            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] byteCipherText = aesCipher.doFinal(body.getBytes());    // 암호문 생성
            encryptedData = new String(byteCipherText);
        }catch(Exception e){
            e.printStackTrace();
        }
        return encryptedData;
    }

    /*
        Alice 6. 전자봉투 생성
     */
    public String createEE(SecretKey secretKey, String receiverPublicKey){
        return encryptWithPublicKey(secretKey.getEncoded().toString(), receiverPublicKey);
    }

    /*
        public key로 암호화
     */
    private String encryptWithPublicKey(String data, String receiverPublicKey) {
        String encryptedData = null;
        try{
            // Public key 만들기
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] bytePublicKey = Base64.getDecoder().decode(receiverPublicKey.getBytes());
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytePublicKey);
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            // 암호화 모드 설정
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(cipher.ENCRYPT_MODE, publicKey);

            // plainText 암호화
            byte[] byteEncryptedData = cipher.doFinal(data.getBytes());
            encryptedData = Base64.getEncoder().encodeToString(byteEncryptedData);
        }catch(Exception e){
            e.printStackTrace();
        }
        return encryptedData;
    }

    /*
        private key로 복호화
     */
    private String decryptWithPrivateKey(String cipherText, String receiverPrivateKey){
        String decryptedData = null;
        try {
            // Private Key 객체 생성
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] bytePrivateKey = Base64.getDecoder().decode(receiverPrivateKey.getBytes());
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bytePrivateKey);
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

            // 암호화 모드 설정
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            // 암호문 복호화
            byte[] byteEncryptedData = Base64.getDecoder().decode(cipherText.getBytes());
            byte[] byteDecryptedData = cipher.doFinal(byteEncryptedData);
            decryptedData = new String(byteDecryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedData;
    }

    /*
        Alice 8. 결과물과 전자봉투 합치기
     */
    public String generateFinalResult(String body, String EE){
        StringBuffer sb = new StringBuffer();
        sb.append("-----BEGIN BODY-----\n");
        sb.append(body);
        sb.append("-----END BODY-----\n");
        sb.append("-----BEGIN EE-----\n");
        sb.append(EE);
        sb.append("-----END EE-----\n");
        return sb.toString();
    }


    /*
        데이터 보내기 - PGP 순방향 프로세스
     */
    public String sendData(String plainText){
        String mac = generateMAC(plainText);
        String digitalSignature = encryptMAC(mac);
        String body = concatResult(this.plainText,digitalSignature);
        SecretKey secretKey = generateSymmetricKey();
        String result = encryptBody(body, secretKey);
        String finalResult = createEE(secretKey, this.receiverPublicKey);
        return finalResult;
    }

    /*
        데이터 받기 - PGP 역방향 프로세스
     */
    public String receiveData(String cipherText){
        return new String("");
    }

    /*
        1. open EE with Bob's private key to get symmetric key from EE. (receiver authentication)
           2. encrypt message body with symmetric key from 1 step.
           3. encrypt digital signature via Alice's public key (sender authentication)
           4. hash the original plainText to create mac
           5. compare the 3 step's mac and received mac (message integrity)
     */

    /*
        Bob 1:
     */
    public HashMap<String, String> dataSplitter(String message){
        String[] lines = message.split(System.getProperty("line.separator"));
        HashMap<String, String> splitedData = new HashMap<>();
        boolean beginBody = false;
        boolean endBody = false;
        boolean beginEE = false;
        boolean endEE = false;
        StringBuffer bodyBuffer = new StringBuffer();
        StringBuffer eeBuffer = new StringBuffer();
        for(int i = 0; i < lines.length; i++){
            if(lines[i].contains("BEGIN BODY")){
                beginBody = true;
                continue;
            }
            if(lines[i].contains("END BODY")){
                beginBody = false;
                endBody = true;
                continue;
            }
            if(lines[i].contains("BEGIN EE")){
                endBody = false;
                beginEE = true;
                continue;
            }
            if(lines[i].contains("END EE")){
                endBody = false;
                endEE = true;
                continue;
            }
            if(beginBody){
                bodyBuffer.append(lines[i]);
            }
            if(beginEE){
                eeBuffer.append(lines[i]);
            }
            if(endEE){
                break;
            }
        }
        splitedData.put("messageBody", bodyBuffer.toString());
        splitedData.put("EE", eeBuffer.toString());
        return splitedData;
    }

    public String openEE(String receiverPrivateKey){
        return new String("");
    }
    /*
        Bob 2:
     */
    public String encryptMessageBody(String body){
        return new String("");
    }

    public String encryptDigitalSignature(String digitalSignature, String senderPublicKey){
        return new String("");
    }

    /*
        SHA-256 사용해서 MAC 생성
     */
    public String hashPlainText(String plainText){
        return new String("");
    }

    /*
        mac 값 비교
     */
    public boolean compareMAC(String receivedMAC, String generatedMAC){
        return receivedMAC.equals(generatedMAC);
    }

}
