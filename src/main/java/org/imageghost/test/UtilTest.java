package org.imageghost.test;

import org.imageghost.key.KeyFactory;
import org.imageghost.key.keys.ASymmetricKey;
import org.imageghost.securealgorithm.utils.RSAUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

public class UtilTest {
    @Test
    public void contains_method_test(){
        // given
        String temp = "안녕하세요. 테스트 문자열입니다. 반갑습니다.";

        // when
        String subString = "요. 테스트 문";

        // then
        Assert.assertTrue(temp.contains(subString));
    }
    @Test
    public void RSAUtil_encryption_decryption_test() throws UnsupportedEncodingException {
        // given
        ASymmetricKey aSymmetricKey = KeyFactory.createASymmetricKey();
        String privateKey = aSymmetricKey.getPrivateKey();
        String publicKey = aSymmetricKey.getPublicKey();
        String plainText = "This is plainText.";

        // when
        String cipherText = RSAUtil.encode(plainText.getBytes(), publicKey);
        byte[] decryptedText = RSAUtil.decode(cipherText, privateKey);
        String fixedDecryptedText = new String(decryptedText, "UTF-8");
        System.out.println(fixedDecryptedText);

        // then
        Assert.assertEquals(plainText, fixedDecryptedText);
    }
}
