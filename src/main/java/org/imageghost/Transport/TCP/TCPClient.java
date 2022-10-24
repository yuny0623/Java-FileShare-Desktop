package org.imageghost.Transport.TCP;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPClient {
    static String SERVER_IP = "127.0.0.1";
    static int SERVER_PORT = 1225;
    static String MESSAGE_TO_SERVER = "Hi, Server";

    public String send(){
        Socket socket = null;
        String response = null;
        try{socket = new Socket(SERVER_IP, SERVER_PORT);
            System.out.println("socket 연결");

            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();

            os.write(MESSAGE_TO_SERVER.getBytes());
            os.flush();

            byte[] data = new byte[16];
            int n = is.read(data);
            String resultFromServer = new String(data, 0, n);
            System.out.println(resultFromServer);

            socket.close();
            response = resultFromServer;
        } catch(UnknownHostException e){
            e.printStackTrace();
        } catch(IOException e)
        {
            e.printStackTrace();
        }
        return response;
    }
}
