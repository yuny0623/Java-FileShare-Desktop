package Wallet;

import java.util.ArrayList;
import java.util.List;

public class Wallet {
    private static List<EncKey> keySaver = new ArrayList<>();

    public void saveKey(EncKey encKey){
        this.keySaver.add(encKey);
    }

    public List<EncKey> getAllKey(){
        return keySaver;
    }
}
