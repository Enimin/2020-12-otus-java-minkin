package ru.rt.dataprocessor;

import ru.rt.model.Measurement;

import java.util.List;

public interface Loader {

    List<Measurement> load();
}
