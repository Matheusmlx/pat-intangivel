package br.com.azi.patrimoniointangivel.integrationtest.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileHelper {

    public static String getJson(String sistema, String fileName) {
        try {
            return new String(Files.readAllBytes(
                    Paths.get(FileHelper.class.getClassLoader().getResource(String.format("%s/%s/%s", "integration-responses", sistema, fileName)).getPath())
            ));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

