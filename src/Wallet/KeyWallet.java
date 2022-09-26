package Wallet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KeyWallet {
    private static HashMap<String, SymmetricKey> keyMap = new HashMap<>(); // AES 키 전용 지갑
    private static int number = 0;

    public static void saveKey(SymmetricKey encKey){
        keyMap.put(String.valueOf(++number), encKey);
    }
    public static void saveKeyAsMainKey(SymmetricKey encKey){
        keyMap.put("MainKey", encKey);
    }

    public static HashMap<String, SymmetricKey> getAllKey(){
        return keyMap;
    }

    public static SymmetricKey getMainKey(){
        return keyMap.get("Main");
    }
}
