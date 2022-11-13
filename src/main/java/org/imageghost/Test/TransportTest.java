package org.imageghost.Test;

import org.imageghost.OpenChat.ClientAction;
import org.imageghost.OpenChat.ServerAction;
import org.junit.Test;

public class TransportTest {

    @Test
    public void TCP_클라이언트_서버_테스트() throws InterruptedException {
        Thread thread = new Thread(new ServerAction());
        thread.start();
        Thread.sleep(10000000);
    }

    @Test
    public void TCP_클라이언트_테스트용_유저1() throws InterruptedException {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ClientAction clientAction = new ClientAction();
            clientAction.action();
        }).start();
        try {
            Thread.sleep(10000000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    @Test
    public void TCP_클라이언트_테스트용_유저2(){
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ClientAction clientAction = new ClientAction();
            clientAction.action();
        }).start();
        try {
            Thread.sleep(10000000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
