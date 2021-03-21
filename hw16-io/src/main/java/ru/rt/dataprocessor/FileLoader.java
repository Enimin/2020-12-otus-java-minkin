package ru.rt.dataprocessor;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ru.rt.model.Measurement;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FileLoader implements Loader {
    InputStream inputStream;

    public FileLoader(String fileName) {
        inputStream = getClass().getClassLoader().getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new FileProcessException(String.format("Файл %s не найден!", fileName));
        }
    }

    @Override
    public List<Measurement> load() {
        List<Measurement> measurements = null;

        try {
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule("MeasurementDeserializer", new Version(1, 0, 0, null, null, null));
            module.addDeserializer(Measurement.class, new MeasurementDeserializer());
            mapper.registerModule(module);

            measurements = mapper.readValue(inputStream, new TypeReference<List<Measurement>>(){});
        } catch (IOException e) {
            throw new FileProcessException(e.getMessage());
        }
        return measurements;
    }
}
