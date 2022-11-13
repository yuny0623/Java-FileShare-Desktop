package org.imageghost.GUIComponents;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FixedMainGui extends JFrame implements ActionListener {
    
    public FixedMainGui(){
        setTitle("ImageGhost");
        setSize(550, 400);
        setLocation(400, 400);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
