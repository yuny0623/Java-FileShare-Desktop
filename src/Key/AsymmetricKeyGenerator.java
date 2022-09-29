package Key;

import ClientCustomException.NoKeyException;

import java.security.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

public class AsymmetricKeyGenerator {
    private static final int KEY_SIZE = 2048;
    private static String privateKey;
    private static String publicKey;

    public static List<String> generateKeyPair(){
        List<String> keyList = new ArrayList<>();
        try {
            SecureRandom secureRandom = new SecureRandom();
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(KEY_SIZE, secureRandom);
            KeyPair keyPair = keyPairGenerator.genKeyPair();

            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            String stringPublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            String stringPrivateKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());

            AsymmetricKeyGenerator.publicKey = stringPublicKey;
            AsymmetricKeyGenerator.privateKey = stringPrivateKey;

            keyList.add(stringPublicKey);
            keyList.add(stringPrivateKey);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return keyList;
    }

    public static String getPublicKey(){
        if(AsymmetricKeyGenerator.publicKey != null){
            return AsymmetricKeyGenerator.publicKey;
        }else{
            throw new NoKeyException("Public Key not existed.");
        }
   }

    public static String getPrivateKey(){
        if(AsymmetricKeyGenerator.privateKey != null) {
            return AsymmetricKeyGenerator.privateKey;
        }else{
            throw new NoKeyException("Private Key not existed.");
        }
    }
}
