package me.johara.dto.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class UtilityTimestampSerializer implements Serializer {
    @Override
    public void configure(Map configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String s, Object o) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            retVal = objectMapper.writeValueAsString(o).getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;    }

    @Override
    public byte[] serialize(String topic, Headers headers, Object data) {
        return new byte[0];
    }

    @Override
    public void close() {

    }
}
