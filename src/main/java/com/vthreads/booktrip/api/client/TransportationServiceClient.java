package com.vthreads.booktrip.api.client;

import com.vthreads.booktrip.api.dto.Transportation;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

public class TransportationServiceClient extends BaseServiceClient {

    public TransportationServiceClient(RestClient client) {
        super(client);
    }

    public Transportation fetchTransportation(String airportCode){
        return this.fetchResponse("{airportCode}"
                , new ParameterizedTypeReference<Transportation>(){}
                , airportCode);
    }
}
