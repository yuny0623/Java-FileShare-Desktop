package org.imageghost.Key;

import org.imageghost.Key.Keys.ASymmetricKey;
import org.imageghost.Key.Keys.SymmetricKey;

public class KeyFactory {
    /*
        static factory method pattern for Key.
     */
    public static SymmetricKey createSymmetricKey(){
        return new SymmetricKey();
    }

    public static ASymmetricKey createAsymmetricKey(){
        return new ASymmetricKey();
    }
}
