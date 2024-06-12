package org.example.log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.rest.DataSender;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConfigReader {
    private final String CONFIG_FILE_PATH = "config.json";

    private final DataSender dataSender;

    public ConfigReader(DataSender dataSender) {
        this.dataSender  = dataSender;
    }

    public List<String> getFileList() {
        JsonObject config = readConfig();
        return getLogFilePaths(config);
    }

    public void beginTask() {
        List<String> list = getFileList();
        ExecutorService executorService = Executors.newFixedThreadPool(list.size());
        for (String filePath : list) {
            new Thread(() -> new PeriodicFileWriter(filePath)).start();
        }
        for (String filePath : list) {
            LogWatching lw = new LogWatching(filePath, dataSender);
            executorService.submit(lw::watchLogFile);
        }
    }

    private JsonObject readConfig() {
        try (Reader reader = new InputStreamReader(
                Objects.requireNonNull(ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE_PATH)))) {
            JsonParser jsonParser = new JsonParser();
            return jsonParser.parse(reader).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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

