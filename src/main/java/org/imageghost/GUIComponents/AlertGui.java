package org.imageghost.GUIComponents;

import javax.swing.*;

public class AlertGui {
    public AlertGui(String message){
        JOptionPane alert = new JOptionPane();
        alert.showMessageDialog(null, message); // alert
    }
}
