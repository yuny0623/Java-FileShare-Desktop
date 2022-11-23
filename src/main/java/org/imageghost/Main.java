package org.imageghost;

import org.imageghost.guicomponents.AlertGui;
import org.imageghost.guicomponents.ImageGhostMainGui;

public class Main {
    public static void main(String[] args){
        try {
            new ImageGhostMainGui();
        } catch(Exception e){
            new AlertGui("Filed while loading. due to: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }
}
