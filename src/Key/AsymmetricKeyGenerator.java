package Key;

import ClientCustomException.NoKeyException;

import java.security.*;
import java.util.Base64;
import java.util.HashMap;

public class AsymmetricKeyGenerator {
    static final int KEY_SIZE = 2048;
    private String privateKey;
    private String publicKey;

    public HashMap<String, String> generateKeyPair(){
        HashMap<String, String> keySet = new HashMap<>();
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

            keySet.put("publicKey", stringPublicKey);
            keySet.put("privateKey", stringPrivateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return keySet;
    }

    public String getPublicKey(){
        if(this.publicKey != null){
            return this.publicKey;
        }else{
            throw new NoKeyException("Public Key not existed.");
        }
   }

    public String getPrivateKey(){
        if(this.privateKey != null) {
            return this.privateKey;
        }else{
            throw new NoKeyException("Private Key not existed.");
        }
    }
}
