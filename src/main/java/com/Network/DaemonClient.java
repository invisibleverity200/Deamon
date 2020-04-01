package com.Network;

import com.Objects.Configs;
import com.Objects.Discrete;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class DaemonClient implements Client {
    private JButton[] upLinkButtons;
    private JButton[] downLinkButtons;
    private ReceiveSocket receiveSocket;
    private SendSocket sendSocket;
    private Configs config;

    private static String IP_ADDR = "192.168.1.204";
    private static int RECEIVE_PORT = 31242;
    private static int SEND_PORT = 1272;


    //[inOrOut,idx,flag]
    public DaemonClient(JButton[] upLinkButtons, JButton[] downLinkButtons, Configs config, JLabel statusLabel) throws IOException {
        this.upLinkButtons = upLinkButtons;
        this.downLinkButtons = downLinkButtons;
        this.config = config;

        receiveSocket = new ReceiveSocket(IP_ADDR, RECEIVE_PORT);
        sendSocket = new SendSocket(IP_ADDR, SEND_PORT);

        statusLabel.setText("Connected");
        statusLabel.setForeground(Color.GREEN);

    }

    @Override
    public void run() {
        if (receiveSocket != null) {
            try {
                receiveSocket.receive(upLinkButtons, downLinkButtons, config.getDownLinkPosArray(), config.getUpLinkPosArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean sendDiscrete(Discrete discrete) {
        sendSocket.send(discrete);
        return true;
    }

    public void closeConnections() {
        try {
            if (sendSocket != null) sendSocket.closeConnection();
            if (sendSocket != null) receiveSocket.closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}