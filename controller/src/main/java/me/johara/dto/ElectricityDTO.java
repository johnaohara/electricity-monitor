package me.johara.dto;

public class ElectricityDTO {

    public long timestamp;

    public double watts;

    public double amps;


    public ElectricityDTO() {

    }

    public ElectricityDTO(long timestamp, double watts, double amps) {
        this.timestamp = timestamp;
        this.watts = watts;
        this.amps = amps;
    }

    //TODO:: this needs to be replaced by a JSON obj serializer.  ATM that is not working so this is a temp work-around
    @Override
    public String toString() {
        return "{" +
                "\"timestamp\":" + timestamp +
                ", \"watts\":" + watts +
                ", \"amps\":" + amps +
                '}';
    }
}
