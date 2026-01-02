package com.vthreads.booktrip.api.client;

import com.vthreads.booktrip.api.dto.LocalRecommendations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

public class LocalRecommendationServiceClient extends BaseServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(LocalRecommendationServiceClient.class);
    public LocalRecommendationServiceClient(RestClient client) {
        super(client);
    }

    public LocalRecommendations fetchLocalRecommendations(String airportCode){
        return this.fetchResponse("{airportCode}"
                , new ParameterizedTypeReference<LocalRecommendations>(){}
                , airportCode);
    }
}
