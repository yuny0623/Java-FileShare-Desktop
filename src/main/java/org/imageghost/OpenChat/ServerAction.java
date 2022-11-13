package org.imageghost.OpenChat;

public class ServerAction implements Runnable{
    public ServerAction(){

    }

    @Override
    public void run() {
        ChatServer server = new ChatServer();
        server.giveAndTake();
    }
}
