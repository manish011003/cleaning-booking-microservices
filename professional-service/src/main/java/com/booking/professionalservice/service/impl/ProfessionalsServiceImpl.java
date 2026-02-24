package com.booking.professionalservice.service.impl;

import com.booking.professionalservice.model.dto.request.CleanerDto;
import com.booking.professionalservice.model.dto.request.VehicleDto;
import com.booking.professionalservice.model.entity.CleanerEntity;
import com.booking.professionalservice.model.entity.VehicleEntity;
import com.booking.professionalservice.repository.CleanerRepository;
import com.booking.professionalservice.repository.VehicleRepository;
import com.booking.professionalservice.service.ProfessionalsService;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProfessionalsServiceImpl implements ProfessionalsService {

  private final VehicleRepository vehicleRepository;
  private final CleanerRepository cleanerRepository;

  public ProfessionalsServiceImpl(VehicleRepository vehicleRepository, CleanerRepository cleanerRepository) {
    this.vehicleRepository = vehicleRepository;
    this.cleanerRepository = cleanerRepository;
  }

  @Override
  @Cacheable(cacheNames = "professionals-vehicles")
  public List<VehicleDto> listVehicles() {
    return vehicleRepository.findAll().stream()
        .map(this::toVehicleDto)
        .toList();
  }

  @Override
  @Cacheable(cacheNames = "professionals-cleaners")
  public List<CleanerDto> listAllCleaners() {
    return cleanerRepository.findAll().stream()
        .map(this::toCleanerDto)
        .toList();
  }

  @Override
  @Cacheable(cacheNames = "professionals-cleaners-by-vehicle", key = "#vehicleId.toString()")
  public List<CleanerDto> listCleanersByVehicle(String vehicleId) {
    return cleanerRepository.findByVehicle_Id(vehicleId).stream()
        .map(this::toCleanerDto)
        .toList();
  }

  private VehicleDto toVehicleDto(VehicleEntity vehicle) {
    List<CleanerDto> cleaners = cleanerRepository.findByVehicle_Id(vehicle.getId()).stream()
        .map(this::toCleanerDto)
        .toList();

    return new VehicleDto(
        vehicle.getId(),
        vehicle.getCode(),
        vehicle.getLicensePlate(),
        cleaners
    );
  }

  private CleanerDto toCleanerDto(CleanerEntity cleaner) {
    return new CleanerDto(
        cleaner.getId(),
        cleaner.getFullName(),
        cleaner.getVehicle().getId()
    );
  }
}
