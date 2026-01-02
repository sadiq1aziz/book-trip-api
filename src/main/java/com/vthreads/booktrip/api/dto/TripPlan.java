package com.vthreads.booktrip.api.dto;

import java.util.List;

public record TripPlan(String airportCode,
                       List<Accomodation> accomodationList,
                       Weather weather,
                       List<Event> events,
                       LocalRecommendations localRecommendations,
                       Transportation transportation) {
}
