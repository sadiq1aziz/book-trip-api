package com.vthreads.booktrip.api.dto;

import java.time.LocalDate;

public record FlightReservationRequest
        (String departure,
         String arrival,
         String flightNumber,
         LocalDate tripDate) {
}

