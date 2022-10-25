package org.imageghost.Transport.TCP.Chat;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientAction {
    public ClientAction(){
    }

    public void action(){
        try{
            InetAddress ia = InetAddress.getLocalHost();
            String ipStr = ia.toString();
            String ip = ipStr.substring(ipStr.indexOf("/") + 1);
            new ClientGui(ip, 8083);
        }catch(UnknownHostException e){
            e.printStackTrace();
        }
    }
}
