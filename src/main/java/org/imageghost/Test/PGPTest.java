package org.imageghost.Test;

import org.imageghost.Key.AsymmetricKeyGenerator;
import org.imageghost.PGPConnection.CutomException.InvalidMessageIntegrityException;
import org.imageghost.PGPConnection.PGP;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class PGPTest {
    @Test
    public void pgp구조테스트(){
        // given
        HashMap<String, String> senderKeyPair = AsymmetricKeyGenerator.generateKeyPair();
        String senderPublicKey = senderKeyPair.get("publicKey");
        String senderPrivateKey = senderKeyPair.get("privateKey");

        HashMap<String, String> receiverKeyPair = AsymmetricKeyGenerator.generateKeyPair();
        String receiverPublicKey = receiverKeyPair.get("publicKey");
        String receiverPrivateKey = receiverKeyPair.get("privateKey");

        PGP pgp = new PGP();
        pgp.setReceiverPublicKey(senderPublicKey);
        pgp.setSenderPrivateKey(senderPrivateKey);
        pgp.setReceiverPublicKey(receiverPublicKey);
        pgp.setReceiverPrivateKey(receiverPrivateKey);

        // when
        String fromData = pgp.sendData("테스트입니다.");
        System.out.println("-------------------");
        System.out.println("fromData: " + fromData);
        System.out.println("-------------------");

        String toData = "";
        try {
            toData = pgp.receiveData(fromData);
        }catch(InvalidMessageIntegrityException e){
            e.printStackTrace();
        }
        System.out.println("-------------------");
        System.out.println("toData: " + toData);
        System.out.println("-------------------");

        // then
        Assert.assertEquals(fromData, toData);
    }

    @Test
    public void splitter테스트(){

    }
}
