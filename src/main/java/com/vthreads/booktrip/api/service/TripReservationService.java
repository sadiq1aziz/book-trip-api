package com.vthreads.booktrip.api.service;

import com.vthreads.booktrip.api.client.FlightReservationServiceClient;
import com.vthreads.booktrip.api.client.FlightSearchServiceClient;
import com.vthreads.booktrip.api.dto.Flight;
import com.vthreads.booktrip.api.dto.FlightReservationRequest;
import com.vthreads.booktrip.api.dto.FlightReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class TripReservationService {

    private final FlightReservationServiceClient flightReservationServiceClient;
    private final FlightSearchServiceClient flightSearchServiceClient;

    public FlightReservationResponse reserveFlight(FlightReservationRequest flightReservationRequest){
        var flights = this.flightSearchServiceClient.fetchFlights(flightReservationRequest.arrival(), flightReservationRequest.departure());
        var bestFlightDeal = flights.stream().min(Comparator.comparingInt(Flight::price));
        var finalFlightDeal = bestFlightDeal.orElseThrow(() -> new RuntimeException("Flight not found"));
        FlightReservationRequest reservationRequest = new FlightReservationRequest(flightReservationRequest.departure(),
                                                            flightReservationRequest.arrival(),
                                                            finalFlightDeal.flightNumber(),
                                                            finalFlightDeal.date());
        return this.flightReservationServiceClient.fetchFlightReservationConfirmation(reservationRequest);
    }
}
