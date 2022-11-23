package org.imageghost.key;

import org.imageghost.key.keys.ASymmetricKey;
import org.imageghost.key.keys.SymmetricKey;

public class KeyFactory {
    public static SymmetricKey createSymmetricKey(){
        return new SymmetricKey();
    }
    public static ASymmetricKey createASymmetricKey(){
        return new ASymmetricKey();
    }
}
