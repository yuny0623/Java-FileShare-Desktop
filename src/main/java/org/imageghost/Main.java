package org.imageghost;

import org.imageghost.GUIComponents.AlertGui;
import org.imageghost.GUIComponents.MainGui;


public class Main {
    public static void main(String[] args){
        try {
            new MainGui();
        } catch(Exception e){
            new AlertGui("Filed while loading due to: " + e.getMessage(), false);
            e.printStackTrace();
        }
    }
}
