package com.Network;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ReceiveSocket extends Socket {
    private String ipAddr;
    private int port;
    private DataOutputStream outputStream;
    private DataInputStream dataInputStream;

    //[inOrOut,idx,flag]
    ReceiveSocket(String ipAddr, int port) throws IOException {
        super(ipAddr, port);

        this.ipAddr = ipAddr;
        this.port = port;

        init();
    }

    private boolean init() throws IOException {
        try {
            outputStream = new DataOutputStream(this.getOutputStream());
            dataInputStream = new DataInputStream(this.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            closeConnection();
            return false;
        }
        return true;
    }

    public void closeConnection() throws IOException {
        if (this != null) this.close();
        if (outputStream != null) outputStream.close();
        if (dataInputStream != null) dataInputStream.close();
    }

    //[inOrOut,idx,flag]
    boolean receive(JButton[] astsToCidsButtons, JButton[] cidsToAstsButtons, int[] posArrayCidsToAsts, int[] posArrayAstsToCids) throws IOException {
        try {
            outputStream.write(1);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        while (true) { //TODO write method
            if (dataInputStream.available() >= Integer.BYTES * 3) {//FIXME possible bug
                if (dataInputStream.readInt() == 1) {
                    int idx = dataInputStream.readInt();
                    boolean flag = dataInputStream.readBoolean();
                    if (flag) {
                        cidsToAstsButtons[posArrayCidsToAsts[idx]].setBackground(Color.GREEN);
                    } else {
                        cidsToAstsButtons[posArrayCidsToAsts[idx]].setBackground(Color.orange);
                    }
                } else {
                    int idx = dataInputStream.readInt();
                    boolean flag = dataInputStream.readBoolean();
                    if (flag) {
                        int tempIdx = posArrayAstsToCids[idx];
                        if (tempIdx != -1) astsToCidsButtons[tempIdx].setBackground(Color.GREEN);
                    } else {
                        int tempIdx = posArrayAstsToCids[idx];
                        if (tempIdx != -1) astsToCidsButtons[posArrayAstsToCids[idx]].setBackground(Color.orange);
                    }
                }
            }
        }
    }
}
