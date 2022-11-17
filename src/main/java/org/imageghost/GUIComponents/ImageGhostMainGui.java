package org.imageghost.GUIComponents;

import org.imageghost.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;

public class ImageGhostMainGui extends JFrame implements ActionListener {

    Container container = getContentPane();
    JButton clientButton = new JButton("Client");
    JButton serverButton = new JButton("Server");
    JPanel buttonPanel = new JPanel();
    String mainScreen = "\n" +
            "                                                                                                                                                                                             \n" +
            "                                                                                                                                                                                             \n" +
            "IIIIIIIIII                                                                                         GGGGGGGGGGGGGhhhhhhh                                                        tttt          \n" +
            "I0000000I                                                                                      GGG000000000000Gh00000h                                                     ttt000t          \n" +
            "I00000000I                                                                                    GG000000000000000Gh00000h                                                     t00000t          \n" +
            "II000000II                                                                                   G00000GGGGGGGG0000Gh00000h                                                     t00000t          \n" +
            "  I0000I     mmmmmmm    mmmmmmm     aaaaaaaaaaaaa     ggggggggg   ggggg    eeeeeeeeeeee     G00000G       GGGGGG h0000h hhhhh          ooooooooooo       ssssssssss   ttttttt00000ttttttt    \n" +
            "  I0000I   mm0000000m  m0000000mm   a000000000000a   g000000000ggg0000g  ee000000000000ee  G00000G               h0000hh00000hhh     oo00000000000oo   ss0000000000s  t00000000000000000t    \n" +
            "  I0000I  m0000000000mm0000000000m  aaaaaaaaa00000a g00000000000000000g e000000eeeee00000eeG00000G               h00000000000000hh  o000000000000000oss0000000000000s t00000000000000000t    \n" +
            "  I0000I  m0000000000000000000000m           a0000ag000000ggggg000000gge000000e     e00000eG00000G    GGGGGGGGGG h0000000hhh000000h o00000ooooo00000os000000ssss00000stttttt0000000tttttt    \n" +
            "  I0000I  m00000mmm000000mmm00000m    aaaaaaa00000ag00000g     g00000g e0000000eeeee000000eG00000G    G00000000G h000000h   h000000ho0000o     o0000o s00000s  ssssss       t00000t          \n" +
            "  I0000I  m0000m   m0000m   m0000m  aa000000000000ag00000g     g00000g e00000000000000000e G00000G    GGGGG0000G h00000h     h00000ho0000o     o0000o   s000000s            t00000t          \n" +
            "  I0000I  m0000m   m0000m   m0000m a0000aaaa000000ag00000g     g00000g e000000eeeeeeeeeee  G00000G        G0000G h00000h     h00000ho0000o     o0000o      s000000s         t00000t          \n" +
            "  I0000I  m0000m   m0000m   m0000ma0000a    a00000ag000000g    g00000g e0000000e            G00000G       G0000G h00000h     h00000ho0000o     o0000ossssss   s00000s       t00000t    tttttt\n" +
            "II000000IIm0000m   m0000m   m0000ma0000a    a00000ag0000000ggggg00000g e00000000e            G00000GGGGGGGG0000G h00000h     h00000ho00000ooooo00000os00000ssss000000s      t000000tttt00000t\n" +
            "I00000000Im0000m   m0000m   m0000ma00000aaaa000000a g0000000000000000g  e00000000eeeeeeee     GG000000000000000G h00000h     h00000ho000000000000000os00000000000000s       tt00000000000000t\n" +
            "I00000000Im0000m   m0000m   m0000m a0000000000aa000a gg00000000000000g   ee0000000000000e       GGG000000GGG000G h00000h     h00000h oo00000000000oo  s00000000000ss          tt00000000000tt\n" +
            "IIIIIIIIIImmmmmm   mmmmmm   mmmmmm  aaaaaaaaaa  aaaa   gggggggg000000g     eeeeeeeeeeeeee          GGGGGG   GGGG hhhhhhh     hhhhhhh   ooooooooooo     sssssssssss              ttttttttttt  \n" +
            "                                                               g00000g                                                                                                                       \n" +
            "                                                   gggggg      g00000g                                                                                                                       \n" +
            "                                                   g00000gg   gg00000g                                                                                                                       \n" +
            "                                                    g000000ggg0000000g                                                                                                                       \n" +
            "                                                     gg0000000000000g                                                                                                                        \n" +
            "                                                       ggg000000ggg                                                                                                                          \n" +
            "                                                          gggggg                                                                                                                             \n";
    JTextPane jTextPane = new JTextPane();

    public ImageGhostMainGui(){
        setTitle("ImageGhost");
        setSize(550, 550);
        setLocation(400, 400);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        container.setLayout(new BorderLayout());

        jTextPane.setEditable(false);
        jTextPane.setText(mainScreen);

        buttonPanel.add("East", clientButton);
        buttonPanel.add("West", serverButton);
        container.add("Center", jTextPane);
        container.add("South", buttonPanel);

        clientButton.addActionListener(this);
        serverButton.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == clientButton){
            try {
                InetAddress ia = InetAddress.getLocalHost();
                String ipStr = ia.toString();
                String ip = ipStr.substring(ipStr.indexOf("/") + 1);
                new ClientGui(ip, Config.TCP_IP_CONNECTION_DEFAULT_PORT);
            }catch(Exception err){
                err.printStackTrace();
            }
        }
        else if(e.getSource() == serverButton){
            new ServerGui();
        }
    }
}
