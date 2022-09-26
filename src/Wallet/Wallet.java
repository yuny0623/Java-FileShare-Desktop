package Wallet;

import java.util.ArrayList;
import java.util.List;

public class Wallet {
    private List<EncKey> keySaver = new ArrayList<>();

    public void saveKey(EncKey encKey){
        this.keySaver.add(encKey);
    }
    public List<EncKey> getAllKey(){
        if(keySaver.size() == 0) {
            return this.keySaver; // 예외 처리 필요
        }else{
            return this.keySaver;
        }
    }
}
