package org.imageghost.openchat;

import org.imageghost.Config;
import org.imageghost.guicomponents.ClientGui;
import org.imageghost.wallet.KeyWallet;

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
            KeyWallet keyWallet = new KeyWallet();
            keyWallet.init();
            new ClientGui(ip, Config.TCP_IP_CONNECTION_DEFAULT_PORT);
        }catch(UnknownHostException e){
            e.printStackTrace();
        }
    }
}
