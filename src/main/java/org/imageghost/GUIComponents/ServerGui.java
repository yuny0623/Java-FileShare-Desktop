package org.imageghost.GUIComponents;

import org.imageghost.TcpTransport.ServerAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerGui extends JFrame implements ActionListener{
    Container container = getContentPane();
    JTextArea jTextArea = new JTextArea();
    JScrollPane scrollPane = new JScrollPane(jTextArea);
    JButton serverStartButton = new JButton("Server Start");
    public ServerGui(){
        setTitle("ServerLog");
        setSize(550, 400);
        setLocation(400, 400);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        container.setLayout(new BorderLayout());

        container.add("Center", scrollPane);
        container.add("South", serverStartButton);

        serverStartButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == serverStartButton) {
//            ServerAction serverAction = new ServerAction();
//            serverAction.action();
            Thread thread = new Thread(new ServerAction());
            thread.start();
        }
    }

}
