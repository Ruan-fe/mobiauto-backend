package com.mobiautobackend.domain.services;

import com.mobiautobackend.domain.entities.Vehicle;
import com.mobiautobackend.domain.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle create(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public Optional<Vehicle> findById(String vehicleId) {
        return vehicleRepository.findById(vehicleId);
    }

    public Optional<Vehicle> findByIdAndDealershipId(String vehicleId, String dealershipId) {
        return vehicleRepository.findByIdAndDealershipId(vehicleId, dealershipId);
    }
}
