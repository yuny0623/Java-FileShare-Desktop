package org.imageghost.test;

import org.imageghost.openchat.ClientAction;
import org.imageghost.openchat.ServerAction;
import org.junit.Test;

public class TransportTest {
    @Test
    public void TCP_client_server_test() throws InterruptedException {
        Thread thread = new Thread(new ServerAction());
        thread.start();
        Thread.sleep(10000000);
    }

    @Test
    public void TCP_client_test_user1() throws InterruptedException {
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
    public void TCP_client_test_user2(){
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
