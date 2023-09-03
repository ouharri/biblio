package com.biblio.helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class envLoader {
    private final Map<String, String> envVariables = new HashMap<>();

    public envLoader() {
        this.loadEnv();
    }

    private void loadEnv() {

        String filePath = ".env";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    envVariables.put(key, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String get(String key) {
        return envVariables.get(key);
    }
}
