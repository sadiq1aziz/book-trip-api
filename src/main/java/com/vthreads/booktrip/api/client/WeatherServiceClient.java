package com.vthreads.booktrip.api.client;

import com.vthreads.booktrip.api.dto.Weather;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

public class WeatherServiceClient extends BaseServiceClient {

    public WeatherServiceClient(RestClient client) {
        super(client);
    }

    public Weather fetchWeather(String airportCode){
        return this.fetchResponse("{airportCode}"
                , new ParameterizedTypeReference<Weather>(){}
                , airportCode);
    }
}
