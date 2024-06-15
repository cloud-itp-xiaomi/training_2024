package org.example.log;

import com.google.gson.*;
import org.example.rest.DataSender;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConfigReader {
    private final String CONFIG_FILE_PATH = "config.json";

    private final DataSender dataSender;
    private JsonObject config;

    public ConfigReader(DataSender dataSender) {
        this.dataSender  = dataSender;
        this.config = readConfig();
    }

    public List<String> getFileList() {
        return getLogFilePaths(config);
    }

    public long getLastReadPosition() {
        return config.has("lastReadPosition") ? config.get("lastReadPosition").getAsLong() : 0;
    }

    public void updateLastReadPosition(long position) {
        config.addProperty("lastReadPosition", position);
        writeConfig(config);
    }


    public void beginTask() {
        List<String> list = getFileList();
        ExecutorService executorService = Executors.newFixedThreadPool(list.size());
        for (String filePath : list) {
            new Thread(() -> new PeriodicFileWriter(filePath)).start();
        }
        for (String filePath : list) {
            LogWatching lw = new LogWatching(filePath, dataSender, this);
            lw.setLastReadPosition(getLastReadPosition());
            executorService.submit(lw::startWatching);
        }
    }

    private JsonObject readConfig() {
        try (Reader reader = new InputStreamReader(
                Objects.requireNonNull(ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE_PATH)))) {
            JsonParser jsonParser = new JsonParser();
            return jsonParser.parse(reader).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonObject();
        }
    }

    private void writeConfig(JsonObject config) {
        try (Writer writer = new FileWriter(
                Objects.requireNonNull(ConfigReader.class.getClassLoader().getResource(CONFIG_FILE_PATH)).getFile())) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> getLogFilePaths(JsonObject config) {
        List<String> logFilePaths = new ArrayList<>();
        if (config.has("files")) {
            JsonArray filesArray = config.getAsJsonArray("files");
            for (int i = 0; i < filesArray.size(); i++) {
                logFilePaths.add(filesArray.get(i).getAsString());
            }
        }
        return logFilePaths;
    }
}

