package me.johara.controller;

import me.johara.dto.ElectricityDTO;
import me.johara.dto.UtilityTimestamp;
import me.johara.dto.WaterDTO;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/utility")
@ApplicationScoped
public class UtilityController {
/*

    @Inject
    @Channel("utility")
    Emitter<UtilityTimestamp> utilityTimestampEmitter;

*/

    @Inject
    @Channel("electricity")
    Publisher<ElectricityDTO> electricity;


    @Inject
    @Channel("water")
    Publisher<WaterDTO> water;

    @GET
    @Path("/electricity")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType("text/plain")
    public Publisher<ElectricityDTO> electricityStream() {
        return electricity;
    }

    @GET
    @Path("/water")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType("text/plain")
    public Publisher<WaterDTO> waterStream() {
        return water;
    }

    /*
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/electricity")
    public void electricityReading(String timestamp) {

        UtilityTimestamp utilityTimestamp = new UtilityTimestamp("ELECTRICITY", Long.parseLong(timestamp));

        System.out.printf("Electricity timestamp: %s\n", timestamp);

        utilityTimestampEmitter.send(utilityTimestamp);

    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/water")
    public void waterReading(String timestamp) {

        UtilityTimestamp utilityTimestamp = new UtilityTimestamp("WATER", Long.parseLong(timestamp));

        System.out.printf("Water timestamp: %s\n", timestamp);

        utilityTimestampEmitter.send(utilityTimestamp);

    }

*/
}
