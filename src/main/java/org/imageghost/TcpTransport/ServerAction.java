package org.imageghost.TcpTransport;

public class ServerAction {
    public ServerAction(){
    }

    public void action(){
        ChatServer server = new ChatServer();
        server.giveAndTake();
    }
}
