package Wallet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KeyWallet {
    private static HashMap<String, EncKey> keyMap = new HashMap<>(); // AES 키 전용 지갑
    private static int number = 0;

    public static void saveKey(EncKey encKey){
        keyMap.put(String.valueOf(++number), encKey);
    }
    public static void saveKeyAsMainKey(EncKey encKey){
        keyMap.put("MainKey", encKey);
    }

    public static HashMap<String, EncKey> getAllKey(){
        return keyMap;
    }

    public static EncKey getMainKey(){
        return keyMap.get("Main");
    }
}
