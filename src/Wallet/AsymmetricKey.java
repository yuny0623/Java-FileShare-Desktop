package Wallet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AsymmetricKey implements EncKey{
    private String publicKey;
    private String privateKey;
    private String description;


    @Override
    public HashMap<String, String> getKey() {
        HashMap<String, String> keySet = new HashMap<>();
        keySet.put("publicKey", this.publicKey);
        keySet.put("privateKey", this.privateKey);
        return keySet;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
