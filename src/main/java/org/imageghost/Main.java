package org.imageghost;

import org.imageghost.GUIComponents.MyFrame;


public class Main {
    public static void main(String[] args){
        try {
            new MyFrame();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
