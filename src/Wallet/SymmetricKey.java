package Wallet;

import java.util.HashMap;

public class SymmetricKey implements EncKey{
    private String AESKey;
    private String description;

    @Override
    public HashMap<String, String> getKey() {
        HashMap<String, String> keySet = new HashMap<>();
        keySet.put("AESKey", this.AESKey);
        return keySet;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
