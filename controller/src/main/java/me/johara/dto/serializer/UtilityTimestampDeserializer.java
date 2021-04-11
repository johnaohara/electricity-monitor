package me.johara.dto.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.johara.dto.UtilityTimestamp;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class UtilityTimestampDeserializer implements Deserializer {
    @Override
    public void configure(Map configs, boolean isKey) {

    }

    @Override
    public Object deserialize(String topic, byte[] bytes) {
        ObjectMapper mapper = new ObjectMapper();
        UtilityTimestamp utilityTimestamp = null;
        try {
            utilityTimestamp = mapper.readValue(bytes, UtilityTimestamp.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return utilityTimestamp;
    }

    @Override
    public void close() {

    }
}

