package com.Objects;

import com.Utils.MySortAlgorithim;
import com.Utils.SortAlgorithim;

import javax.json.*;
import javax.json.stream.JsonParsingException;
import java.io.*;
import java.util.ArrayList;

public class Config implements Configs {
    private ArrayList<Discrete> astsToCidsChannels = new ArrayList<>(); //Discrete
    private ArrayList<Discrete> cidsToAstsChannels = new ArrayList<>();
    private int[][] posArrays = new int[2][];
    private String ip;

    public String getIp() {
        System.out.println("ip: " + ip);
        return ip;
    }

    public Config() {
        readConfigFile();
    }

    public ArrayList<Discrete> getAstsToCidsChannels() {
        return astsToCidsChannels;
    }

    public ArrayList<Discrete> getCidsToAstsChannels() {
        return cidsToAstsChannels;
    }

    public int[] getAstsToCidsArray() {
        return posArrays[0];
    }

    public int[] getCidsToAstsArray() {
        return posArrays[1];
    }

    public void updateConfig() {
        astsToCidsChannels = new ArrayList<>();
        cidsToAstsChannels = new ArrayList<>();
        readConfigFile();
    }

    //192.168.1.108
    private void readConfigFile() {
        InputStream inputStream = null;
        JsonReader reader = null;
        InputStream inputStreamSelectedFile = null;
        JsonReader reader1 = null;
        try {
            inputStream = new FileInputStream("config.json");
            reader = Json.createReader(inputStream);

            JsonObject config = reader.readObject();

            JsonArray astsToCidsChannels = config.getJsonArray("UplinkChannels");
            JsonArray cidsToAstsChannels = config.getJsonArray("DownlinkChannels");
            String ip = config.getString("Ip");
            if (!checkIp(ip)) {
                System.out.println("Not valid Ip address!");
            } else {
                this.ip = ip;
            }


            fillArray(this.astsToCidsChannels, astsToCidsChannels, 0);
            fillArray(this.cidsToAstsChannels, cidsToAstsChannels, 1);
            System.out.println(posArrays[0].length);
            System.out.println(posArrays[1].length);

        } catch (FileNotFoundException | JsonParsingException | NumberFormatException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            try {
                close(inputStream, inputStreamSelectedFile, reader, reader1);
            } catch (IOException e) {
                System.out.println("\u001B[31m" + "ERROR: " + e.getMessage());
            }
        }
    }

    private void fillArray(ArrayList<Discrete> arrayList, JsonArray channels, int sideIdx) {//FIXME something is wrong here
        SortAlgorithim algorithim = new MySortAlgorithim();
        for (JsonValue channel : channels) {
            JsonArray temp = channel.asJsonArray();
            int index = 0;
            String[] channelAttributes = new String[5];
            for (JsonValue channelAttribute : temp) {
                channelAttributes[index] = channelAttribute.toString();
                index++;
            }
            arrayList.add(new Discrete(channelAttributes));
        }

        arrayList = algorithim.sort(arrayList);
        for (int i = 0; i < arrayList.size(); i++) {
            System.out.println("discrete IDX at: " + (i + 1) + "  " + arrayList.get(i).getDiscreteIdx());
        }
        fillPosArray(sideIdx, sideIdx == 0 ? astsToCidsChannels : cidsToAstsChannels);
    }

    private void fillPosArray(int sideIdx, ArrayList<Discrete> arrayList) {
        boolean tempFlag = false;
        int posArrayIdx = 0;
        if (sideIdx == 1) posArrayIdx = 1;
        posArrays[posArrayIdx] = new int[arrayList.get(arrayList.size() - 1).getDiscreteIdx() + 1];//FIXME possible bug the discrete idx´s need to start iwth zero!
        for (int indexPosArray = 0; indexPosArray < posArrays[posArrayIdx].length; indexPosArray++) {
            for (int index = 0; index < arrayList.size(); index++) {
                if (indexPosArray == arrayList.get(index).getDiscreteIdx()) {
                    posArrays[posArrayIdx][indexPosArray] = index;
                    tempFlag = true;
                }
            }
            if (!tempFlag) {
                posArrays[posArrayIdx][indexPosArray] = -1;
            }
        }
    }

    private void close(InputStream inputStream, InputStream inputStream2, JsonReader jsonReader, JsonReader jsonReader2) throws IOException {
        if (inputStream != null) inputStream.close();
        if (inputStream2 != null) inputStream2.close();
        if (jsonReader != null) jsonReader.close();
        if (jsonReader2 != null) jsonReader2.close();
    }

    private boolean checkIp(String ip) {
        String[] ipParts = ip.split("\\.");
        if (ipParts.length != 4) {
            return false;
        }
        for (int i = 0; i < ipParts.length; i++) {
            try {
                int temp = Integer.parseInt(ipParts[i]);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }
}
