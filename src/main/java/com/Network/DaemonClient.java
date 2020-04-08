package com.Network;

import com.Objects.Configs;
import com.Objects.Discrete;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.SocketException;

public class DaemonClient implements Client {
    private JButton[] astsToCidsButtons;
    private JButton[] cidsToAstsButtons;
    private ReceiveSocket receiveSocket;
    private SendSocket sendSocket;
    private Configs config;
    private JLabel statusLabel;

    private static String IP_ADDR = "192.168.1.113";
    private static int RECEIVE_PORT = 2342;
    private static int SEND_PORT = 4233;


    //[inOrOut,idx,flag]
    public DaemonClient(JButton[] astsToCidsButtons, JButton[] cidsToAstsButtons, Configs config, JLabel statusLabel) throws IOException {
        this.astsToCidsButtons = astsToCidsButtons;
        this.cidsToAstsButtons = cidsToAstsButtons;
        this.config = config;

        receiveSocket = new ReceiveSocket(IP_ADDR, RECEIVE_PORT);
        sendSocket = new SendSocket(IP_ADDR, SEND_PORT);

        this.statusLabel = statusLabel;

        statusLabel.setText("Connected");
        statusLabel.setForeground(Color.GREEN);

    }

    @Override
    public void run() {
        if (receiveSocket != null) {
            try {
                if (!receiveSocket.receive(astsToCidsButtons, cidsToAstsButtons, config.getCidsToAstsArray(), config.getAstsToCidsArray(),config.getAstsToCidsChannels())) {
                    statusLabel.setText("Disconnected");
                    statusLabel.setForeground(Color.red);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                closeConnections();
            }
        }
    }

    public boolean sendDiscrete(Discrete discrete) {

        try {
            sendSocket.send(discrete);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

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