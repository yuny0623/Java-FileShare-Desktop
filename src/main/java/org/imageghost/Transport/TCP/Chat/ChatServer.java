package org.imageghost.Transport.TCP.Chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    ServerSocket serverSocket;
    Socket socket;
    List<Thread> list;

    public ChatServer(){
        list = new ArrayList<Thread>();
        System.out.println("서버가 시작되었습니다.");
    }

    public void giveAndTake(){
        try{
            serverSocket = new ServerSocket(8083);
            serverSocket.setReuseAddress(true);

            while(true){
                socket = serverSocket.accept();
                ServerSocketThread thread = new ServerSocketThread(this, socket);
                addClient(thread);
                thread.start();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private synchronized void addClient(ServerSocketThread thread){
        list.add(thread);
        System.out.println("Client 1명 입장. 총 " + list.size() + "명");
    }

    public synchronized void removeClient(Thread thread){
        list.remove(thread);
        System.out.println("Client 1명 입장. 총" + list.size() + "명");
    }

    public synchronized void broadCasting(String str){
        for(int i = 0; i < list.size(); i++){
            ServerSocketThread thread = (ServerSocketThread) list.get(i);
            thread.sendMessage(str);
        }
    }
}
