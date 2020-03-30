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
    int nameType = 1;

    public GUI() {
        Client daemonClient = new DaemonClient();
        final JButton[][] buttonsUpLink = {null};
        final JButton[][] buttonsDownLink = {null};
        Configs config = new Config();

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(0, 10));

        JMenuBar menuBar = new JMenuBar();

        JMenu channelNameTypes = new JMenu("Channel Names");

        menuBar.add(channelNameTypes);

        JMenuItem upLinkWindow = new JMenuItem("Up Link");
        JMenuItem downLinkWindow = new JMenuItem("Down Link");

        upLinkWindow.addActionListener((ActionEvent e) -> {
            if (buttonsUpLink[0] == null) {
                buttonsUpLink[0] = getButtons(config.getUpLinkChannels());
                setUpContentPane(buttonsUpLink[0], contentPanel);
            } else {
                setUpContentPane(buttonsUpLink[0], contentPanel);
            }
            setSize(config.getUpLinkChannels().size() * 150, config.getUpLinkChannels().size() / 10 * 20 + 100);
        });
        downLinkWindow.addActionListener((ActionEvent e) -> {
            if (buttonsDownLink[0] == null) {
                buttonsDownLink[0] = getButtons(config.getDownLinkChannels());
                setUpContentPane(buttonsDownLink[0], contentPanel);
            } else {
                setUpContentPane(buttonsDownLink[0], contentPanel);
            }
            setSize(config.getDownLinkChannels().size() * 150, config.getDownLinkChannels().size() / 10 * 20 + 100);
        });

        channelNameTypes.add(upLinkWindow);
        channelNameTypes.add(downLinkWindow);

        setUpContentPane(getButtons(config.getUpLinkChannels()), contentPanel);

        setContentPane(contentPanel);

        setJMenuBar(menuBar);

        pack();

        setTitle("DeamonGUI");

        setVisible(true);

        setSize(config.getUpLinkChannels().size() * 150, config.getUpLinkChannels().size() / 10 * 20 + 100);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    private JButton[] getButtons(ArrayList<Discrete> discretes) {
        JButton[] buttons = new JButton[discretes.size()];
        int index = 0;
        for (Discrete discrete : discretes) {
            buttons[index] = new JButton(discrete.getAttributes()[nameType].substring(1, discrete.getAttributes()[nameType].length() - 1));
            buttons[index].setBackground(Color.ORANGE);
            buttons[index].addActionListener((ActionEvent e) -> {
                System.out.println("test");
                //TODO write methode
            });
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
