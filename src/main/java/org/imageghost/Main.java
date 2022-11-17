package org.imageghost;

import org.imageghost.GUIComponents.AlertGui;
import org.imageghost.GUIComponents.ImageGhostMainGui;

public class Main {
    public static void main(String[] args){
        try {
            new ImageGhostMainGui();
        } catch(Exception e){
            new AlertGui("Filed while loading. due to0 " + e.getMessage(), true);
            e.printStackTrace();
        }
    }
}
