package me.johara.picocli.dto;

public class UtilityTimestamp {

    private final String utilityType;
    private final Long timestamp;

    public UtilityTimestamp(String utilityType, Long timestamp) {
        this.utilityType = utilityType;
        this.timestamp = timestamp;
    }

    public String getUtilityType() {
        return utilityType;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
