package me.johara.dto;

public class WaterDTO {

    public long milliLiters;

    public long timestamp;

    public double litresPerSecond;


    public WaterDTO() {

    }

    public WaterDTO(long timestamp, long milliLiters, double litresPerSecond) {
        this.timestamp = timestamp;
        this.milliLiters = milliLiters;
        this.litresPerSecond = litresPerSecond;
    }

    //TODO:: this needs to be replaced by a JSON obj serializer.  ATM that is not working so this is a temp work-around
    @Override
    public String toString() {
        return "{" +
                "\"timestamp\":" + timestamp +
                ", \"milliLiters\":" + milliLiters +
                ", \"litresPerSecond\":" + litresPerSecond +
                '}';
    }

}
