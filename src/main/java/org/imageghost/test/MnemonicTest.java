package org.imageghost.test;

import org.imageghost.key.KeyFactory;
import org.imageghost.key.keys.ASymmetricKey;
import org.junit.Assert;
import org.junit.Test;

public class MnemonicTest {
    @Test
    public void privatekey_publickey_length_test(){
        // given
        ASymmetricKey aSymmetricKey = KeyFactory.createASymmetricKey();
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
