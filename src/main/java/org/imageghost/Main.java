package org.imageghost;

import org.imageghost.GUIComponents.MainGui;


public class Main {
    public static void main(String[] args){
        try {
            new MainGui();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
