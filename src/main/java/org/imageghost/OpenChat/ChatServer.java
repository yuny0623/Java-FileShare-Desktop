package org.imageghost.OpenChat;

import org.imageghost.Config;
import org.imageghost.SecureAlgorithm.Utils.RSAUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatServer {
    ServerSocket serverSocket;
    Socket socket;
    List<Thread> list;
    static HashMap<String, String> publicKeyList;
    static HashMap<String, Thread> threadList;

    public ChatServer(){
        list = new ArrayList<Thread>();
        publicKeyList = new HashMap<>();
        threadList = new HashMap<>();

        System.out.println("서버가 시작되었습니다.");
    }

    public void giveAndTake(){
        try{
            serverSocket = new ServerSocket(Config.TCP_IP_CONNECTION_DEFAULT_PORT);
            serverSocket.setReuseAddress(true);

            while(true){
                socket = serverSocket.accept();
                ServerSocketThread thread = new ServerSocketThread(this, socket);
                // added.
                threadList.put(socket.getInetAddress().toString(), thread);
                // -----
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
        System.out.println("Client 1명 이탈. 총" + list.size() + "명");
    }

    public synchronized void broadCasting(String str){
        for(int i = 0; i < list.size(); i++){
            ServerSocketThread thread = (ServerSocketThread) list.get(i);
            thread.sendMessage(str);
        }
    }

    public synchronized  void sendMessageTo(String str, String publicKey){
        ServerSocketThread thread = (ServerSocketThread) threadList.get(publicKey);
        thread.sendMessage(str);
    }
}
