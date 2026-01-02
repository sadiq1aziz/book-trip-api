package com.vthreads.booktrip.api.client;

import com.vthreads.booktrip.api.dto.FlightReservationRequest;
import com.vthreads.booktrip.api.dto.FlightReservationResponse;
import org.springframework.web.client.RestClient;

public class FlightReservationServiceClient extends BaseServiceClient {

    public FlightReservationServiceClient(RestClient client) {
        super(client);
    }

    public FlightReservationResponse fetchFlightReservationConfirmation(FlightReservationRequest flightReservationRequest){
       return client.post()
                .body(flightReservationRequest)
                .retrieve()
                .body(FlightReservationResponse.class);
    }
}
