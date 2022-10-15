package org.imageghost.Test;

import org.imageghost.Key.AsymmetricKeyGenerator;
import org.imageghost.PGPConnection.PGP;
import org.junit.Test;

import java.util.HashMap;

public class PGPTest {
    @Test
    void pgp구조테스트(){
        PGP pgp = new PGP();

        HashMap<String, String> senderKeyPair = AsymmetricKeyGenerator.generateKeyPair();
        String senderPublicKey = senderKeyPair.get("publicKey");
        String senderPrivateKey = senderKeyPair.get("privateKey");

        HashMap<String, String> receiverKeyPair = AsymmetricKeyGenerator.generateKeyPair();
        String receiverPublicKey = receiverKeyPair.get("publicKey");
        String receiverPrivateKey = receiverKeyPair.get("privateKey");

        pgp.setPlainText(" 테스트입니다.");
        pgp.setReceiverPublicKey(senderPublicKey);
        pgp.setSenderPrivateKey(senderPrivateKey);

        pgp.setReceiverPublicKey(receiverPublicKey);
        pgp.setReceiverPrivateKey(receiverPrivateKey);
    }
}
