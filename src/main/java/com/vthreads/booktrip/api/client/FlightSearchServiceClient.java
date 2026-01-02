package com.vthreads.booktrip.api.client;

import com.vthreads.booktrip.api.dto.Flight;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

public class FlightSearchServiceClient extends BaseServiceClient {

    public FlightSearchServiceClient(RestClient client) {
        super(client);
    }

    public List<Flight> fetchFlights(String arrival, String departure){
        return client.get()
                .uri("{arrival}/{departure}",arrival, departure)
                .retrieve()
                .body(new ParameterizedTypeReference<List<Flight>>() {
                });
    }
}
