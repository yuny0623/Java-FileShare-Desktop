package org.imageghost.PGPConnection;
import org.imageghost.PGPConnection.CutomException.InvalidMessageIntegrityException;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;


public class PGP {

    /**
     PGP Communication Implementation in Java.
     @Date 2022-10-20
     @Author yuny0623
     @Version 1.0.0

     1. PGP - Pretty Good Privacy.
        Pretty Good Privacy (PGP) is an encryption program that provides cryptographic privacy and authentication for data communication.
        PGP is used for signing, encrypting, and decrypting texts, e-mails, files, directories, and whole disk partitions and to increase the security of e-mail communications.
        Phil Zimmermann developed PGP in 1991.
        PGP and similar software follow the OpenPGP, an open standard of PGP encryption software, standard (RFC 4880) for encrypting and decrypting data.


     2. How to use.
        - Alice(Sender):
            1. Generate MAC.
            2. Encrypt MAC with Alice's private key. This is called Digital Signature.
            3. Add plainText to the result of step2.
            4. Generate new symmetric key.
            5. Encrypt result of step3 with the symmetric key.(generate Body.)
            6. Put symmetric key into E.E called Electronic Envelope by encrypt E.E with Bob's public key.
            7. Add Body and E.E and send it to Bob.

        - Bob(Receiver):
            1. Open E.E with Bob's private key to get symmetric key from E.E. (Receiver Authentication)
            2. Encrypt Body with symmetric key. (This will get Digital Signature and PlainText.)
            3. Encrypt Digital Signature via Alice's public key. and get MAC.(Sender Authentication)
            4. Hash the original plainText to compare with received MAC.
            5. compare the hashed PlainText and received mac. (If it is same then message integrity has guranteed.)


     3. Get more information about PGP.
        Link: https://en.wikipedia.org/wiki/Pretty_Good_Privacy

     */

    private String plainText;
    private String senderPublicKey;
    private String senderPrivateKey;
    private String receiverPublicKey;
    private String receiverPrivateKey;

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


    public String generateDigitalSignature(String MAC, String senderPrivateKey){
        return encryptWithPrivateKey(MAC, senderPrivateKey);
    }

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

            encryptedText = Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
        }catch(Exception e){
            e.printStackTrace();
        }
        return encryptedText;
    }

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

    public SecretKey generateSymmetricKey(){
        KeyGenerator generator = null;
        try {
            generator = KeyGenerator.getInstance("AES");
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        generator.init(128);
        SecretKey secKey = generator.generateKey();
        return secKey;
    }

    public String encryptBodyFixed(String body, SecretKey secretKey){
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] cipherText = cipher.doFinal(body.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception e) {
            throw new RuntimeException("Error occured while encrypting data", e);
        }
    }

    public String decryptBodyFixed(String encryptedBody, SecretKey secretKey){
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] cipherText = cipher.doFinal(Base64.getDecoder().decode(encryptedBody));
            return new String(cipherText);
        } catch (Exception e) {
            throw new RuntimeException("Error occured while decrypting data", e);
        }
    }

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

    public String decryptDigitalSignature(String digitalSignature, String senderPublicKey){
        return solveDigitalSignature(digitalSignature, senderPublicKey);
    }

    public String hashPlainText(String receivedPlainText){
        return generateMAC(receivedPlainText);
    }

    public boolean compareMAC(String receivedMAC, String generatedMAC){
        return receivedMAC.equals(generatedMAC);
    }

    public String createEE(byte[] secretKeyArray, String receiverPublicKey){
        return encode(secretKeyArray, receiverPublicKey);
    }

    public byte[] openEE(String cipherText, String receiverPrivateKey){
        return decode(cipherText, receiverPrivateKey);
    }


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

    public String send(String plainText){
        String originalMAC = this.generateMAC(plainText);
        String originalDigitalSignature = this.generateDigitalSignature(originalMAC, senderPrivateKey);
        String body = this.generateBody(plainText, originalDigitalSignature);
        SecretKey secretKeyOriginal = this.generateSymmetricKey();
        String encryptedBody = this.encryptBodyFixed(body, secretKeyOriginal);
        String ee = this.createEE(secretKeyOriginal.getEncoded(), receiverPublicKey);
        String finalResult = this.appendEEWithBody(ee, encryptedBody);
        return finalResult;
    }

    public String receive(String cipherText) throws Exception{
        HashMap<String, String> dataMap = this.dataSplitter(cipherText);
        String receivedBody = dataMap.get("body");
        String receivedEE = dataMap.get("ee");
        System.out.printf("receive - receivedEE: %s\n", receivedEE);
        System.out.printf("receive - receivedBody: %s\n", receivedBody);

        byte[] aesKey = this.openEE(receivedEE, receiverPrivateKey);
        SecretKey decryptedSecretKey = new SecretKeySpec(aesKey, "AES");

        // body 복호화
        String decryptedBody = this.decryptBodyFixed(receivedBody, decryptedSecretKey);
        HashMap<String, String> bodyMap = this.bodySplitter(decryptedBody);

        String receivedPlainText = bodyMap.get("receivedPlainText");
        String receivedDigitalSignature = bodyMap.get("digitalSignature");
        String receivedMAC = this.decryptDigitalSignature(receivedDigitalSignature, senderPublicKey); // sender authentication
        String hashPlainText = this.hashPlainText(receivedPlainText);
        if(this.compareMAC(receivedMAC, hashPlainText)){
            return receivedPlainText;
        }else{
            throw new InvalidMessageIntegrityException("Invalid Message Integrity. Message has been changed while transportation.");
        }
    }
}






