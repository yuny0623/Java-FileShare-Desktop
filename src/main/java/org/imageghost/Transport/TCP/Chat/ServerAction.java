package org.imageghost.Transport.TCP.Chat;

public class ServerAction {
    public ServerAction(){
    }

    public void action(){
        ChatServer server = new ChatServer();
        server.giveAndTake();
    }
}
