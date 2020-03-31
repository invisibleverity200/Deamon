package com.Objects;

import com.Utils.MySortAlgorithim;
import com.Utils.SortAlgorithim;

import javax.json.*;
import javax.json.stream.JsonParsingException;
import java.io.*;
import java.util.ArrayList;

public class Config implements Configs {
    private ArrayList<Discrete> upLinkChannels = new ArrayList<>(); //Discrete
    private ArrayList<Discrete> downLinkChannels = new ArrayList<>();
    private int[] upLinkPosArray;
    private int[] downLinkPosArray;

    public Config() {
        readConfigFile();
    }

    public ArrayList<Discrete> getUpLinkChannels() {
        return upLinkChannels;
    }

    public ArrayList<Discrete> getDownLinkChannels() {
        return downLinkChannels;
    }

    public int[] getUpLinkPosArray() {
        return upLinkPosArray;
    }

    public int[] getDownLinkPosArray() {
        return downLinkPosArray;
    }

    public void updateConfig() {
        upLinkChannels = new ArrayList<>();
        downLinkChannels = new ArrayList<>();
        readConfigFile();
    }

    private void readConfigFile() {
        InputStream inputStream = null;
        JsonReader reader = null;
        InputStream inputStreamSelectedFile = null;
        JsonReader reader1 = null;
        try {
            inputStream = new FileInputStream("config.json");
            reader = Json.createReader(inputStream);

            JsonObject config = reader.readObject();

            JsonArray upLinkChannels = config.getJsonArray("UplinkChannels");
            JsonArray downLinkChannels = config.getJsonArray("DownlinkChannels");


            fillArray(this.upLinkChannels, upLinkChannels, upLinkPosArray);
            fillArray(this.downLinkChannels, downLinkChannels, downLinkPosArray);

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

    private void fillArray(ArrayList<Discrete> arrayList, JsonArray channels, int[] posArray) {
        boolean tempFlag = false;
        SortAlgorithim algorithim = new MySortAlgorithim();
        for (JsonValue channel : channels) {
            JsonArray temp = channel.asJsonArray();
            int index = 0;
            String[] channelAttributes = new String[4];
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
        posArray = new int[arrayList.get(arrayList.size() - 1).getDiscreteIdx()];
        for (int indexPosArray = 0; indexPosArray < posArray.length; indexPosArray++) {
            for (int index = 0; index < arrayList.size(); index++) {
                if (index == arrayList.get(index).getDiscreteIdx()) {
                    posArray[indexPosArray] = index;
                    tempFlag = true;
                }
            }
            if (!tempFlag) {
                posArray[indexPosArray] = -1;
            }
        }
    }

    private void close(InputStream inputStream, InputStream inputStream2, JsonReader jsonReader, JsonReader jsonReader2) throws IOException {
        if (inputStream != null) inputStream.close();
        if (inputStream2 != null) inputStream2.close();
        if (jsonReader != null) jsonReader.close();
        if (jsonReader2 != null) jsonReader2.close();
    }
}
