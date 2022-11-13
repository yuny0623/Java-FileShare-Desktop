package org.imageghost.OpenChat;

import org.imageghost.Config;
import org.imageghost.GUIComponents.ClientGui;
import org.imageghost.Wallet.KeyWallet;

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
