package com.vthreads.booktrip.api.service;

import com.vthreads.booktrip.api.client.*;
import com.vthreads.booktrip.api.client.*;
import com.vthreads.booktrip.api.dto.TripPlan;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
public class TripPlanService {
    private static final Logger logger = LoggerFactory.getLogger(TripPlanService.class);
    private final AccomodationServiceClient accomodationServiceClient;
    private final EventServiceClient eventServiceClient;
    private final FlightReservationServiceClient flightReservationServiceClient;
    private final FlightSearchServiceClient flightSearchServiceClient;
    private final LocalRecommendationServiceClient localRecommendationServiceClient;
    private final TransportationServiceClient transportationServiceClient;
    private final WeatherServiceClient weatherServiceClient;
    private final ExecutorService executorService;

    public TripPlan getTripPlan(String airportCode){
        // assign vars against the future objects returned from the executor services
        var accommodation = executorService.submit(() -> {
            logger.info("Thread (accommodation): {}", Thread.currentThread());
            return accomodationServiceClient.fetchAccomodationDetails(airportCode);
        });

        var events = executorService.submit(() -> {
            logger.info("Thread (events): {}", Thread.currentThread());
            return eventServiceClient.fetchEventDetails(airportCode);
        });

        var recommendations = executorService.submit(() -> {
            logger.info("Thread (recommendations): {}", Thread.currentThread());
            return localRecommendationServiceClient.fetchLocalRecommendations(airportCode);
        });

        var weather = executorService.submit(() -> {
            logger.info("Thread (weather): {}", Thread.currentThread());
            return weatherServiceClient.fetchWeather(airportCode);
        });

        var transportation = executorService.submit(() -> {
            logger.info("Thread (transportation): {}", Thread.currentThread());
            return transportationServiceClient.fetchTransportation(airportCode);
        });

        return new TripPlan(airportCode,
                getOrDefaultReturn(accommodation,Collections.emptyList()),
                getOrDefaultReturn(weather, null),
                getOrDefaultReturn(events, Collections.emptyList()),
                getOrDefaultReturn(recommendations, null),
                getOrDefaultReturn(transportation, null));
    }

    private <R> R getOrDefaultReturn(Future<R> future, R defaultReturn){
        try {
            logger.info("Calling Rest Client:  {}", Thread.currentThread().getName());
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            logger.error("fetching from future object interrupted or unable to execute: {}", e.getMessage());
        } catch (Exception e){
            logger.error("Exception fetching from future object: {}", e.getMessage());
        }
        return defaultReturn;
    }


}
