package org.imageghost.GUIComponents;

import org.imageghost.TcpTransport.ClientAction;
import org.imageghost.TcpTransport.ServerAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class FixedMainGui extends JFrame implements ActionListener {

    Container container = getContentPane();
    JButton clientButton = new JButton("Client");
    JButton serverButton = new JButton("Server");
    JPanel buttonPanel = new JPanel();

    JTextField textField = new JTextField();

    public FixedMainGui(){
        setTitle("ImageGhost");
        setSize(550, 400);
        setLocation(400, 400);
        setVisible(true);
        container.setLayout(new BorderLayout());

        buttonPanel.add("East", clientButton);
        buttonPanel.add("West", serverButton);
        container.add("Center", textField);
        container.add("South", buttonPanel);

        clientButton.addActionListener(this);
        serverButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == clientButton){
            serverButton.setEnabled(false);
            ClientAction clientAction = new ClientAction();
            clientAction.action();
        }
        else if(e.getSource() == serverButton){
            clientButton.setEnabled(false);
            ServerAction serverAction = new ServerAction();
            serverAction.action();
        }
    }
}
