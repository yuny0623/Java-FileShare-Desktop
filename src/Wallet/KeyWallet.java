package Wallet;

import ClientCustomException.NoKeyException;

import java.util.HashMap;

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

    public static SymmetricKey getMainKey() throws NoKeyException{
        SymmetricKey findSymmetricKey =  keyMap.get("Main");
        if(findSymmetricKey == null){
            throw new NoKeyException("No Main Symmetric Key");
        }else{
            return findSymmetricKey;
        }
    }

    public static void deleteAllKeys(){
        keyMap.clear();
        number = 0;
    }
}
