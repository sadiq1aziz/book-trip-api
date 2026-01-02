package com.vthreads.booktrip.api.config;

import com.vthreads.booktrip.api.client.*;
import com.vthreads.booktrip.api.client.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.util.concurrent.Executors;

@Configuration
public class ClientConfig {

    @Value("${spring.threads.virtual.enabled}")
    private boolean isVirtualThreadEnabled;

    @Value("${accommodation.service.url}")
    private String accomodationBaseUrl;

    @Value("${event.service.url}")
    private String eventBaseUrl;

    @Value("${local-recommendation.service.url}")
    private String recommendationBaseUrl;

    @Value("${transportation.service.url}")
    private String transportationBaseUrl;

    @Value("${weather.service.url}")
    private String weatherBaseUrl;

    @Value("${flight-search.service.url}")
    private String searchBaseUrl;

    @Value("${flight-reservation.service.url}")
    private String reservationBaseUrl;

    @Bean
    public AccomodationServiceClient accomodationServiceClient(){
        return new AccomodationServiceClient(createClient(accomodationBaseUrl));
    }

    @Bean
    public EventServiceClient eventServiceClient(){
        return new EventServiceClient(createClient(eventBaseUrl));
    }
    @Bean
    public FlightReservationServiceClient flightReservationServiceClient(){
        return new FlightReservationServiceClient(createClient(reservationBaseUrl));
    }@Bean
    public FlightSearchServiceClient flightSearchServiceClient(){
        return new FlightSearchServiceClient(createClient(searchBaseUrl));
    }
    @Bean
    public LocalRecommendationServiceClient localRecommendationServiceClient(){
        return new LocalRecommendationServiceClient(createClient(recommendationBaseUrl));
    }
    @Bean
    public TransportationServiceClient transportationServiceClient(){
        return new TransportationServiceClient(createClient(transportationBaseUrl));
    }
    @Bean
    public WeatherServiceClient weatherServiceClient(){
        return new WeatherServiceClient(createClient(weatherBaseUrl));
    }


    private RestClient createClient(String baseUrl){
       var builder = RestClient
                            .builder()
                            .baseUrl(baseUrl);
       if(isVirtualThreadEnabled){
           builder = builder
                   .requestFactory(
                           new JdkClientHttpRequestFactory(
                                   HttpClient.newBuilder()
                                           .executor(
                                                   Executors.newVirtualThreadPerTaskExecutor()).build()));
       }
       return builder.build();
    }
}
