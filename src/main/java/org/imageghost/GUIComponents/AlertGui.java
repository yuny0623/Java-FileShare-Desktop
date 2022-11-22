package org.imageghost.GUIComponents;

import javax.swing.*;

public class AlertGui {
    public AlertGui(String message, boolean isMain){
        JOptionPane alert = new JOptionPane();
        alert.showMessageDialog(null, message);
        int result = JOptionPane.showConfirmDialog(null, message, "Confirm Exit", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION && isMain) {
            System.exit(0);
        }
    }
}
