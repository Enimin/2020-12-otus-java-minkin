package ru.rt.dataprocessor;

import ru.rt.model.Measurement;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        Map<String, Double> map = new TreeMap<>();
        data.stream()
            .collect(Collectors.groupingBy(Measurement::getName,
                                           Collectors.summingDouble(Measurement::getValue)))
            .forEach(map::put);

        return map;
    }

}
