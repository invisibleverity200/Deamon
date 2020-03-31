package com.UI;

import com.Network.Client;
import com.Network.DaemonClient;
import com.Objects.Config;
import com.Objects.Configs;
import com.Objects.Discrete;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;

public class GUI extends JFrame {
    private int nameType = 1;
    private Configs config;
    private Client daemonClient;
    private Thread clientThread;

    public GUI() {
        config = new Config();
        final JButton[][] buttonsUpLink = {null};
        final JButton[][] buttonsDownLink = {null};

        buttonsUpLink[0] = getUpLinkButtons(config.getUpLinkChannels());
        buttonsDownLink[0] = getUpLinkButtons(config.getDownLinkChannels());

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
        });
        downLinkWindow.addActionListener((ActionEvent e) -> {
            if (buttonsDownLink[0] == null) {
                buttonsDownLink[0] = getDownLinkButtons(config.getDownLinkChannels());
                setUpContentPane(buttonsDownLink[0], contentPanel);
            } else {
                setUpContentPane(buttonsDownLink[0], contentPanel);
            }
        });

        JMenu operations = new JMenu("Operations");

        JMenuItem quit = new JMenuItem("Quit");
        quit.addActionListener((ActionEvent e) -> {
            if (daemonClient != null) daemonClient.closeConnections();
            if (clientThread != null) clientThread.stop();
            System.exit(1);
        });

        operations.add(quit);

        JLabel statusLabel = new JLabel("       Not Connected");
        statusLabel.setForeground(Color.red);

        menuBar.add(operations);
        menuBar.add(statusLabel);

        channelNameTypes.add(upLinkWindow);
        channelNameTypes.add(downLinkWindow);

        setUpContentPane(buttonsUpLink[0], contentPanel);

        setContentPane(contentPanel);

        setJMenuBar(menuBar);

        pack();

        setTitle("DeamonGUI");

        setVisible(true);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        while (createClient(buttonsUpLink, buttonsDownLink, true, statusLabel)) ;

    }

    private boolean createClient(JButton[][] buttonsUpLink, JButton[][] buttonsDownLink, boolean failed, JLabel statusLabel) {
        try {
            daemonClient = new DaemonClient(buttonsUpLink[0], buttonsDownLink[0], config, statusLabel);
        } catch (IOException e) {
            int returnVal = JOptionPane.showConfirmDialog(null,
                    "Cant Connect to Server pls try again\nretry?", "An Error occurred",
                    JOptionPane.YES_NO_OPTION);
            if (returnVal == JOptionPane.YES_OPTION) {
                return true;
            } else {
                if (daemonClient != null) daemonClient.closeConnections();
                System.exit(1);
            }
        }
        if (!failed) {
            clientThread = new Thread(daemonClient);
            clientThread.start();
            JOptionPane.showMessageDialog(null,
                    "Connected to Server", "An Error occurred",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
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
        if (buttons.length * 300 >= 650) {
            setSize(buttons.length * 300, buttons.length / 10 * 20 + 100);
        } else {
            setSize(650, buttons.length / 10 * 20 + 100);
        }
        revalidate();
        pack();
        repaint();
    }
}
