package com.vthreads.booktrip.api.client;


import com.vthreads.booktrip.api.dto.Event;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

public class EventServiceClient extends BaseServiceClient {

    public EventServiceClient(RestClient client) {
        super(client);
    }

    public List<Event> fetchEventDetails(String airportCode){
       return this.fetchResponseList("{airportCode}",
               new ParameterizedTypeReference<List<Event>>(){},
               airportCode);
    }
}
