package com.vthreads.booktrip.api.controller;

import com.vthreads.booktrip.api.dto.FlightReservationRequest;
import com.vthreads.booktrip.api.dto.FlightReservationResponse;
import com.vthreads.booktrip.api.dto.TripPlan;
import com.vthreads.booktrip.api.service.TripPlanService;
import com.vthreads.booktrip.api.service.TripReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("trip")
public class TripPlanController {

    private final TripPlanService tripPlanService;
    private final TripReservationService tripReservationService;

    @GetMapping("{airportCode}")
    public ResponseEntity<TripPlan> getTripPlan(@PathVariable String airportCode){
        var tripPlan = this.tripPlanService.getTripPlan(airportCode);
        return new ResponseEntity<>(tripPlan, HttpStatus.OK);
    }

    @PostMapping("reserve")
    public ResponseEntity<FlightReservationResponse> reserve(@RequestBody FlightReservationRequest flightReservationRequest){
        var FlightReservationResponse = this.tripReservationService.reserveFlight(flightReservationRequest);
        return new ResponseEntity<>(FlightReservationResponse, HttpStatus.OK);
    }


}
