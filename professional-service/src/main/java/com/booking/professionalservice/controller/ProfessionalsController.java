package com.booking.professionalservice.controller;

import com.booking.professionalservice.model.dto.request.CleanerDto;
import com.booking.professionalservice.model.dto.request.VehicleDto;
import com.booking.professionalservice.service.ProfessionalsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Professionals")
@RestController
@RequestMapping({"/api", "/api/v1"})
public class ProfessionalsController {

  private final ProfessionalsService professionalsService;

  public ProfessionalsController(ProfessionalsService professionalsService) {
    this.professionalsService = professionalsService;
  }

  @GetMapping("/vehicles")
  public List<VehicleDto> listVehicles() {
    return professionalsService.listVehicles();
  }

  @GetMapping("/vehicles/{vehicleId}/cleaners")
  public List<CleanerDto> listCleanersByVehicle(@PathVariable String vehicleId) {
    return professionalsService.listCleanersByVehicle(vehicleId);
  }

  @GetMapping("/cleaners")
  public List<CleanerDto> listAllCleaners() {
    return professionalsService.listAllCleaners();
  }
}
