package ru.rt.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

public class FileSerializer implements Serializer {
    File jsonFile;

    public FileSerializer(String fileName) {
        jsonFile = Paths.get(fileName).toFile();
    }

    @Override
    public void serialize(Map<String, Double> data) {
        try{
            ObjectMapper mapper = new ObjectMapper();

            mapper.writeValue(jsonFile, data);

        } catch (IOException e) {
            throw new FileProcessException(e.getMessage());
        }
    }
}
