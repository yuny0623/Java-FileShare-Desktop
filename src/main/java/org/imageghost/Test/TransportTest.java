package org.imageghost.Test;

import org.imageghost.Transport.TCP.Chat.ClientAction;
import org.imageghost.Transport.TCP.Chat.ServerAction;
import org.junit.Test;

public class TransportTest {

    @Test
    public void udp클라이언트_서버_테스트() throws InterruptedException {
        new Thread(() -> {
            ServerAction serverAction = new ServerAction();
            serverAction.action();
        }).start();
        Thread.sleep(10000000);
    }

    @Test
    public void TCP_클라이언트_테스트_유저1() throws InterruptedException {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ClientAction clientAction = new ClientAction();
            clientAction.action();
        }).start();
        Thread.sleep(10000000);
    }

    @Test
    public void TCP_클라이언트_테스트_유저2() throws InterruptedException {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ClientAction clientAction = new ClientAction();
            clientAction.action();
        }).start();
        Thread.sleep(10000000);
    }
}
