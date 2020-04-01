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
import java.util.concurrent.atomic.AtomicInteger;

public class GUI extends JFrame {
    private int nameType = 1;
    private Configs config;
    private Client daemonClient;
    private Thread clientThread;

    public GUI() {
        config = new Config();
        final JButton[][] buttonsUpLink = {null};
        final JButton[][] buttonsDownLink = {null};
        AtomicInteger currentSide = new AtomicInteger();

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
            currentSide.set(0);
            if (buttonsUpLink[0] == null) {
                buttonsUpLink[0] = getUpLinkButtons(config.getUpLinkChannels());
                setUpContentPane(buttonsUpLink[0], contentPanel);
            } else {
                setUpContentPane(buttonsUpLink[0], contentPanel);
            }
        });
        downLinkWindow.addActionListener((ActionEvent e) -> {
            currentSide.set(1);
            if (buttonsDownLink[0] == null) {
                buttonsDownLink[0] = getDownLinkButtons(config.getDownLinkChannels());
                setUpContentPane(buttonsDownLink[0], contentPanel);
            } else {
                setUpContentPane(buttonsDownLink[0], contentPanel);
            }
        });

        JMenu operations = new JMenu("Operations");
        JMenu nameSets = new JMenu("Name Sets");

        JMenuItem nameSet1 = new JMenuItem("Name set 1");
        nameSet1.addActionListener((ActionEvent e) -> changeNameSet(currentSide.get() == 0 ? buttonsUpLink[0] : buttonsDownLink[0], 0, currentSide.get()));

        JMenuItem nameSet2 = new JMenuItem("Name set 2");
        nameSet2.addActionListener((ActionEvent e) -> changeNameSet(currentSide.get() == 0 ? buttonsUpLink[0] : buttonsDownLink[0], 1, currentSide.get()));

        JMenuItem nameSet3 = new JMenuItem("Name set 3");
        nameSet3.addActionListener((ActionEvent e) -> changeNameSet(currentSide.get() == 0 ? buttonsUpLink[0] : buttonsDownLink[0], 2, currentSide.get()));

        JMenuItem quit = new JMenuItem("Quit");
        quit.addActionListener((ActionEvent e) -> {
            if (daemonClient != null) daemonClient.closeConnections();
            if (clientThread != null) clientThread.stop();
            System.exit(1);
        });

        nameSets.add(nameSet1);
        nameSets.add(nameSet2);
        nameSets.add(nameSet3);

        operations.add(quit);

        JLabel statusLabel = new JLabel("       Not Connected");
        statusLabel.setForeground(Color.red);

        menuBar.add(nameSets);
        menuBar.add(operations);
        menuBar.add(statusLabel);

        channelNameTypes.add(upLinkWindow);
        channelNameTypes.add(downLinkWindow);

        setContentPane(contentPanel);

        setJMenuBar(menuBar);

        pack();

        setTitle("DeamonGUI");

        setVisible(true);

        setUpContentPane(buttonsUpLink[0], contentPanel);

        if (buttonsUpLink.length * 300 >= 650) {
            setSize(buttonsUpLink.length * 300, buttonsUpLink.length / 10 * 20 + 100);
        } else {
            setSize(650, buttonsUpLink.length / 10 * 20 + 100);
            System.out.println("not enought button\n");
        }

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
                if (thisButton.getFlag()) {
                    thisButton.setFlag(false);
                } else {
                    thisButton.setFlag(true);
                }
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
                if (thisButton.getFlag()) {
                    thisButton.setFlag(false);
                } else {
                    thisButton.setFlag(true);
                }
                daemonClient.sendDiscrete(thisButton);
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
            setSize(650, buttons.length / 10 * 20 + 100); //FIXME setSize is not working correctly!
            System.out.println("not enought button\n");
        }
        revalidate();
        pack();
        repaint();
    }

    private void changeNameSet(JButton[] buttons, int nameSetId, int sideIdx) {
        if (sideIdx == 0) {
            for (int index = 0; index < buttons.length; index++) {
                buttons[index].setText(config.getUpLinkChannels().get(index).getAttributes()[nameSetId + 1].substring(1, config.getUpLinkChannels().get(index).getAttributes()[nameSetId + 1].length() - 1));
            }
        } else {
            for (int index = 0; index < buttons.length; index++) {
                buttons[index].setText(config.getDownLinkChannels().get(index).getAttributes()[nameSetId + 1].substring(1, config.getDownLinkChannels().get(index).getAttributes()[nameSetId + 1].length() - 1));
            }
        }
    }
}
