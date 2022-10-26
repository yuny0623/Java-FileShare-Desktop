package org.imageghost.TcpTransport;

import org.imageghost.Config;
import org.imageghost.GUIComponents.ClientGui;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientAction {
    public ClientAction(){
    }
    public void action(){
        try{
            InetAddress ia = InetAddress.getLocalHost();
            String ipStr = ia.toString();
            // ia.toString() -> "DESKTOP-5QRSVM2/192.168.70.1"
            String ip = ipStr.substring(ipStr.indexOf("/") + 1);
            new ClientGui(ip, Config.TCP_IP_CONNECTION_DEFAULT_PORT);
        }catch(UnknownHostException e){
            e.printStackTrace();
        }
    }
}
