package com.UI;

import com.Network.Client;
import com.Network.DaemonClient;
import com.Objects.Config;
import com.Objects.Configs;
import com.Objects.Discrete;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class GUI extends JFrame {
    private int nameType = 1;
    private Configs config;
    private Client daemonClient;

    public GUI() {
        config = new Config();
        final JButton[][] buttonsUpLink = {null};
        final JButton[][] buttonsDownLink = {null};

        buttonsUpLink[0] = getUpLinkButtons(config.getUpLinkChannels());
        buttonsDownLink[0] = getUpLinkButtons(config.getDownLinkChannels());

        daemonClient = new DaemonClient(buttonsUpLink[0], buttonsDownLink[0], config);
        if(!daemonClient.init()){
            //TODO show error message!!!
        }

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(0, 10));

        JMenuBar menuBar = new JMenuBar();

        JMenu channelNameTypes = new JMenu("Channel Names");

        menuBar.add(channelNameTypes);

        JMenuItem upLinkWindow = new JMenuItem("Up Link");
        JMenuItem downLinkWindow = new JMenuItem("Down Link");

        upLinkWindow.addActionListener((ActionEvent e) -> {
            if (buttonsUpLink[0] == null) {
                buttonsUpLink[0] = getUpLinkButtons(config.getUpLinkChannels());
                setUpContentPane(buttonsUpLink[0], contentPanel);
            } else {
                setUpContentPane(buttonsUpLink[0], contentPanel);
            }
            setSize(config.getUpLinkChannels().size() * 150, config.getUpLinkChannels().size() / 10 * 20 + 100);
        });
        downLinkWindow.addActionListener((ActionEvent e) -> {
            if (buttonsDownLink[0] == null) {
                buttonsDownLink[0] = getDownLinkButtons(config.getDownLinkChannels());
                setUpContentPane(buttonsDownLink[0], contentPanel);
            } else {
                setUpContentPane(buttonsDownLink[0], contentPanel);
            }
            setSize(config.getDownLinkChannels().size() * 150, config.getDownLinkChannels().size() / 10 * 20 + 100);
        });

        channelNameTypes.add(upLinkWindow);
        channelNameTypes.add(downLinkWindow);

        setUpContentPane(buttonsDownLink[0], contentPanel);

        setContentPane(contentPanel);

        setJMenuBar(menuBar);

        pack();

        setTitle("DeamonGUI");

        setVisible(true);

        setSize(config.getUpLinkChannels().size() * 150, config.getUpLinkChannels().size() / 10 * 20 + 100);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    private JButton[] getUpLinkButtons(ArrayList<Discrete> discretes) {
        int i = 0;
        JButton[] buttons = new JButton[discretes.size()];
        int index = 0;
        for (Discrete discrete : discretes) {
            buttons[index] = new JButton(discrete.getAttributes()[nameType].substring(1, discrete.getAttributes()[nameType].length() - 1));
            buttons[index].setBackground(Color.ORANGE);
            int finalI = i;
            buttons[index].addActionListener((ActionEvent e) -> {
                int x = finalI;
                if (config.getUpLinkChannels().get(x).getFlag()) {
                    config.getUpLinkChannels().get(x).setFlag(false);
                } else {
                    config.getUpLinkChannels().get(x).setFlag(true);
                }
                Discrete thisButton = config.getUpLinkChannels().get(x);
                daemonClient.sendDiscrete(thisButton);
            });
            i++;
            index++;
        }

        return buttons;
    }

    private JButton[] getDownLinkButtons(ArrayList<Discrete> discretes) {
        int i = 0;
        JButton[] buttons = new JButton[discretes.size()];
        int index = 0;
        for (Discrete discrete : discretes) {
            buttons[index] = new JButton(discrete.getAttributes()[nameType].substring(1, discrete.getAttributes()[nameType].length() - 1));
            buttons[index].setBackground(Color.ORANGE);
            int finalI = i;
            buttons[index].addActionListener((ActionEvent e) -> {
                int x = finalI;
                if (config.getDownLinkChannels().get(x).getFlag()) {
                    config.getDownLinkChannels().get(x).setFlag(false);
                } else {
                    config.getDownLinkChannels().get(x).setFlag(true);
                }
                Discrete thisButton = config.getDownLinkChannels().get(x);
                System.out.println(x);
                //TODO write methode
            });
            i++;
            index++;
        }

        return buttons;
    }

    private void setUpContentPane(JButton[] buttons, JPanel contentPane) {
        contentPane.removeAll();
        if (buttons.length >= 10) {
            contentPane.setLayout(new GridLayout(0, 10));
        } else {
            contentPane.setLayout(new GridLayout(0, buttons.length));
        }
        for (JButton button : buttons) {
            contentPane.add(button);
        }
        revalidate();
        pack();
        repaint();
    }
}
