package org.imageghost.Transport;

public class ServerAction {
    public ServerAction(){
    }

    public void action(){
        ChatServer server = new ChatServer();
        server.giveAndTake();
    }
}
