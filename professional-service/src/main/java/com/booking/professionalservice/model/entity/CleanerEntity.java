package com.booking.professionalservice.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cleaner")
public class CleanerEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "ID")
  private String id;

  @Column(name = "full_name", nullable = false)
  private String fullName;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "vehicle_id", nullable = false)
  private VehicleEntity vehicle;

  protected CleanerEntity() {}

  public CleanerEntity(String fullName, VehicleEntity vehicle) {
    this.fullName = fullName;
    this.vehicle = vehicle;
  }

  public String getId() { return id; }
  public String getFullName() { return fullName; }
  public VehicleEntity getVehicle() { return vehicle; }
}
