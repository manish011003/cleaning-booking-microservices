package com.booking.professionalservice.model.dto.request;

import java.util.List;

public record VehicleDto(String id, String code, String licensePlate, List<CleanerDto> cleaners) {}
