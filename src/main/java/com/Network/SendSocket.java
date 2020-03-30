package com.Network;

import com.Objects.Discrete;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SendSocket extends Socket {
    private String ipAddr;
    private int port;
    private DataOutputStream outputStream;
    private DataInputStream dataInputStream;

    SendSocket(String ipAddr, int port) throws IOException {
        super(ipAddr, port);

        this.ipAddr = ipAddr;
        this.port = port;
    }

    void send(Discrete discrete) {
        try {
            outputStream.writeInt(discrete.getDiscreteIdx());
            outputStream.writeBoolean(discrete.getFlag());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean init() throws IOException {
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
}
