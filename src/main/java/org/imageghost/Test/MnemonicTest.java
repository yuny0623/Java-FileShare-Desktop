package org.imageghost.Test;

import org.imageghost.Key.KeyFactory;
import org.imageghost.Key.Keys.ASymmetricKey;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class MnemonicTest {
    @Test
    public void privatekey_publickey_길이_테스트(){
        // given
        ASymmetricKey aSymmetricKey = KeyFactory.createAsymmetricKey();
        String privateKey = aSymmetricKey.getPrivateKey();
        String publicKey = aSymmetricKey.getPublicKey();

        // when
        int privateKeyLength = privateKey.length();
        int publicKeyLength = publicKey.length();

        // then
        Assert.assertEquals(2048, privateKeyLength);
        Assert.assertEquals(2048, publicKeyLength);
    }
}
