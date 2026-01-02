package com.vthreads.booktrip.api.dto;

import java.time.LocalDate;
import java.util.List;

public record TripResevationRequest(String departure,
                                    String arrival,
                                    LocalDate date) {
}
