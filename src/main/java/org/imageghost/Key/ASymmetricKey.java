package org.imageghost.Key;

import org.imageghost.AsymmetricKey.AsymmetricKeyMaker;
import org.imageghost.Key.Key;

import javax.net.ssl.KeyManager;
import java.security.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

public class ASymmetricKey implements Key{
    private final int KEY_SIZE = 2048;

    private HashMap<String, String> keyMap = new HashMap<>();
    private String descryption;

    private String publicKey;
    private String privateKey;

    public ASymmetricKey(){
        try {
            SecureRandom secureRandom = new SecureRandom();
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(KEY_SIZE, secureRandom);
            KeyPair keyPair = keyPairGenerator.genKeyPair();

            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            String stringPublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            String stringPrivateKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());

            this.publicKey = stringPublicKey;
            this.privateKey = stringPrivateKey;

            keyMap.put("publicKey", stringPublicKey);
            keyMap.put("privateKey", stringPrivateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDescryption(String descryption) {
        this.descryption = descryption;
    }

    public String getDescryption() {
        return descryption;
    }

    public String getPublicKey(){
        return this.publicKey;
    }

    public String getPrivateKey(){
        return this.privateKey;
    }

    @Override
    public Object getKey() {
        return this.keyMap;
    }
}
