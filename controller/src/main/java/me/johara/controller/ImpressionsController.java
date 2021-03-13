package me.johara.controller;

import me.johara.dto.ElectricityDTO;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/impressions")
public class ImpressionsController {

    @Inject
    @Channel("watts")
    Publisher<ElectricityDTO> watts;

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType("text/plain")
    public Publisher<ElectricityDTO> stream() {
        return watts;
    }
}
