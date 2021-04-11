package me.johara.dto;

public class UtilityTimestamp {

    private String utilityType;
    private Long timestamp;

    public UtilityTimestamp() {
    }

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
