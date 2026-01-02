package com.vthreads.booktrip.api.client;


import com.vthreads.booktrip.api.dto.Accomodation;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

public class AccomodationServiceClient extends BaseServiceClient {

    public AccomodationServiceClient(RestClient client) {
        super(client);
    }

    public List<Accomodation> fetchAccomodationDetails(String airportCode){
       return this.fetchResponseList("{airportCode}",
               new ParameterizedTypeReference<List<Accomodation>>(){},
               airportCode);
    }
}
