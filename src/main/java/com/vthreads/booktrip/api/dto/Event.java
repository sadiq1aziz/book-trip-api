package com.vthreads.booktrip.api.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Event(String name, String description, LocalDate date) {
}

