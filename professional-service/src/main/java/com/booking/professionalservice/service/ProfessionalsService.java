package com.booking.professionalservice.service;

import com.booking.professionalservice.model.dto.request.CleanerDto;
import com.booking.professionalservice.model.dto.request.VehicleDto;

import java.util.List;

public interface ProfessionalsService {

  List<VehicleDto> listVehicles();

  List<CleanerDto> listAllCleaners();

  List<CleanerDto> listCleanersByVehicle(String vehicleId);
}
