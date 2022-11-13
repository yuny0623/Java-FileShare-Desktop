package org.imageghost.TcpTransport;

public class ServerAction implements Runnable{
    public ServerAction(){
    }

    public void action(){
        ChatServer server = new ChatServer();
        server.giveAndTake();
    }

    @Override
    public void run() {
        action();
    }
}
