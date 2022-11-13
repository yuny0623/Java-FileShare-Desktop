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
    JTextArea jTextArea = new JTextArea();
    JScrollPane scrollPane = new JScrollPane(jTextArea);

    public FixedMainGui(){
        setTitle("ImageGhost");
        setSize(550, 400);
        setLocation(400, 400);
        setVisible(true);
        container.setLayout(new BorderLayout());

        buttonPanel.add("East", clientButton);
        buttonPanel.add("West", serverButton);
        container.add("Center", scrollPane);
        container.add("South", buttonPanel);

        clientButton.addActionListener(this);
        serverButton.addActionListener(this);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == clientButton){
            serverButton.setEnabled(false);
            ClientAction clientAction = new ClientAction();
            clientAction.action();
        }
        else if(e.getSource() == serverButton){
            PrintStream printStream = new PrintStream(new CustomOutputStream(jTextArea));
            System.setOut(printStream);
            System.setErr(printStream);

            clientButton.setEnabled(false);
            ServerAction serverAction = new ServerAction();
            serverAction.action();
        }
    }
}
