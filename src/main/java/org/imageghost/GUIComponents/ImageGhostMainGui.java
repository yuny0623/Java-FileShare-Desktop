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
            "I::::::::I                                                                                      GGG::::::::::::Gh:::::h                                                     ttt:::t          \n" +
            "I::::::::I                                                                                    GG:::::::::::::::Gh:::::h                                                     t:::::t          \n" +
            "II::::::II                                                                                   G:::::GGGGGGGG::::Gh:::::h                                                     t:::::t          \n" +
            "  I::::I     mmmmmmm    mmmmmmm     aaaaaaaaaaaaa     ggggggggg   ggggg    eeeeeeeeeeee     G:::::G       GGGGGG h::::h hhhhh          ooooooooooo       ssssssssss   ttttttt:::::ttttttt    \n" +
            "  I::::I   mm:::::::m  m:::::::mm   a::::::::::::a   g:::::::::ggg::::g  ee::::::::::::ee  G:::::G               h::::hh:::::hhh     oo:::::::::::oo   ss::::::::::s  t:::::::::::::::::t    \n" +
            "  I::::I  m::::::::::mm::::::::::m  aaaaaaaaa:::::a g:::::::::::::::::g e::::::eeeee:::::eeG:::::G               h::::::::::::::hh  o:::::::::::::::oss:::::::::::::s t:::::::::::::::::t    \n" +
            "  I::::I  m::::::::::::::::::::::m           a::::ag::::::ggggg::::::gge::::::e     e:::::eG:::::G    GGGGGGGGGG h:::::::hhh::::::h o:::::ooooo:::::os::::::ssss:::::stttttt:::::::tttttt    \n" +
            "  I::::I  m:::::mmm::::::mmm:::::m    aaaaaaa:::::ag:::::g     g:::::g e:::::::eeeee::::::eG:::::G    G::::::::G h::::::h   h::::::ho::::o     o::::o s:::::s  ssssss       t:::::t          \n" +
            "  I::::I  m::::m   m::::m   m::::m  aa::::::::::::ag:::::g     g:::::g e:::::::::::::::::e G:::::G    GGGGG::::G h:::::h     h:::::ho::::o     o::::o   s::::::s            t:::::t          \n" +
            "  I::::I  m::::m   m::::m   m::::m a::::aaaa::::::ag:::::g     g:::::g e::::::eeeeeeeeeee  G:::::G        G::::G h:::::h     h:::::ho::::o     o::::o      s::::::s         t:::::t          \n" +
            "  I::::I  m::::m   m::::m   m::::ma::::a    a:::::ag::::::g    g:::::g e:::::::e            G:::::G       G::::G h:::::h     h:::::ho::::o     o::::ossssss   s:::::s       t:::::t    tttttt\n" +
            "II::::::IIm::::m   m::::m   m::::ma::::a    a:::::ag:::::::ggggg:::::g e::::::::e            G:::::GGGGGGGG::::G h:::::h     h:::::ho:::::ooooo:::::os:::::ssss::::::s      t::::::tttt:::::t\n" +
            "I::::::::Im::::m   m::::m   m::::ma:::::aaaa::::::a g::::::::::::::::g  e::::::::eeeeeeee     GG:::::::::::::::G h:::::h     h:::::ho:::::::::::::::os::::::::::::::s       tt::::::::::::::t\n" +
            "I::::::::Im::::m   m::::m   m::::m a::::::::::aa:::a gg::::::::::::::g   ee:::::::::::::e       GGG::::::GGG:::G h:::::h     h:::::h oo:::::::::::oo  s:::::::::::ss          tt:::::::::::tt\n" +
            "IIIIIIIIIImmmmmm   mmmmmm   mmmmmm  aaaaaaaaaa  aaaa   gggggggg::::::g     eeeeeeeeeeeeee          GGGGGG   GGGG hhhhhhh     hhhhhhh   ooooooooooo     sssssssssss              ttttttttttt  \n" +
            "                                                               g:::::g                                                                                                                       \n" +
            "                                                   gggggg      g:::::g                                                                                                                       \n" +
            "                                                   g:::::gg   gg:::::g                                                                                                                       \n" +
            "                                                    g::::::ggg:::::::g                                                                                                                       \n" +
            "                                                     gg:::::::::::::g                                                                                                                        \n" +
            "                                                       ggg::::::ggg                                                                                                                          \n" +
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
