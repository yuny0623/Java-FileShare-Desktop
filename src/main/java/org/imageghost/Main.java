package org.imageghost;

import org.imageghost.GUIComponents.MyFrame;
import org.imageghost.Transport.TCP.Chat.ClientAction;
import org.imageghost.Transport.TCP.Chat.ServerAction;

public class Main {
    public static void main(String[] args){
        try {
            new MyFrame();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
