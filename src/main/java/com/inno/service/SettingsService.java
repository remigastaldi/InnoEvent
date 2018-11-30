package com.inno.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;



public class SettingsService {

    private FileWriter _out = null;

    public SettingsService() {
        try {
            String rootPath = getClass().getResource("").getPath();
            Path appConfigPath = Paths.get(rootPath + "/properties/app.json");
            Files.createDirectories(appConfigPath.getParent());
            if (!Files.exists(appConfigPath))
                Files.createFile(appConfigPath);
            Files.write(appConfigPath, ("{}").getBytes());

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed when load properties");
        }
    }

    // public String get(String key) {
    //     // return appProps.getProperty(key);
    // }

    // public void set(String key, String value) {
    //     // appProps.setProperty(key, value);
    //     save();
    // }

    // private void save() {
    //     try {
    //         // appProps.store(_out, "save");
    //     } catch (IOException err) {
    //         // TODO: ad
    //     }
    // }
}