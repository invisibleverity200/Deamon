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

    public Config() {
        readConfigFile();
    }

    public ArrayList<Discrete> getUpLinkChannels() {
        return upLinkChannels;
    }

    public ArrayList<Discrete> getDownLinkChannels() {
        return downLinkChannels;
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


            fillArray(this.upLinkChannels, upLinkChannels);
            fillArray(this.downLinkChannels, downLinkChannels);

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

    private void fillArray(ArrayList<Discrete> arrayList, JsonArray channels) {
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
    }

    private void close(InputStream inputStream, InputStream inputStream2, JsonReader jsonReader, JsonReader jsonReader2) throws IOException {
        if (inputStream != null) inputStream.close();
        if (inputStream2 != null) inputStream2.close();
        if (jsonReader != null) jsonReader.close();
        if (jsonReader2 != null) jsonReader2.close();
    }
}
