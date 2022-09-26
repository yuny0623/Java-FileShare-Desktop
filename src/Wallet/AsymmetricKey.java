package Wallet;

import java.util.ArrayList;
import java.util.List;

public class AsymmetricKey implements EncKey{
    private String publicKey;
    private String privateKey;
    private String description;


    @Override
    public String getKey() {
        List<String> keyList = new ArrayList<>();
        keyList.add(publicKey);
        keyList.add(privateKey);
        return keyList.toString();
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
