package com.inno.service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SettingsService {

    private HashMap<String, Object> _db = new HashMap<>();

    public SettingsService() {
        try {
            File file = new File(System.getProperty("user.home") + "/.innoevent/config.json");

            // if file does not exists, then create it
            if (!file.exists()) {
                file.getParentFile().mkdir();
                file.createNewFile();
            }
            Reader reader = new FileReader(file.getAbsolutePath());
            Gson gson = new Gson();

            @SuppressWarnings("unchecked")
            HashMap<String, Object> map = gson.fromJson(reader, HashMap.class);
            if (map != null) {
                this._db = map;
            }
            
        } catch (Exception e) {
            //TODO: handle exception
        }

    }

    public Object get(String key) {
        return _db.get(key);
    }

    public void set(String key, Object value) {
        _db.put(key, value);
        save();
    }

    private void save() {
        try (Writer writer = new FileWriter(System.getProperty("user.home") + "/.innoevent/config.json")) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(_db, writer);
        } catch(IOException e) {

        }
    }

	public boolean has(String key) {
		return _db.get(key) != null;
	}
}