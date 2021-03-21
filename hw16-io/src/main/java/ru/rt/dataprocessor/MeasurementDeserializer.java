package ru.rt.dataprocessor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ru.rt.model.Measurement;

import java.io.IOException;

public class MeasurementDeserializer extends StdDeserializer<Measurement> {

        public MeasurementDeserializer() {
            this(null);
        }

        public MeasurementDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Measurement deserialize(JsonParser parser, DeserializationContext deserializer) {
            ObjectCodec codec = parser.getCodec();
            JsonNode node = null;
            String name = null;
            double value = 0;

            try {
                node = codec.readTree(parser);

                JsonNode nameNode = node.get("name");
                name = nameNode.asText();

                JsonNode valueNode = node.get("value");
                value = valueNode.asDouble();
            } catch (IOException e) {
                throw new FileProcessException(e.getMessage());
            }

            return new Measurement(name, value);
        }
    }
