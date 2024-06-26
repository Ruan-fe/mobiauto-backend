package com.mobiautobackend.domain.services;

import com.mobiautobackend.domain.entities.Vehicle;
import com.mobiautobackend.domain.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<Vehicle> findAllByFilters(String dealershipId, Pageable pageable) {
        if (dealershipId != null) {
            return vehicleRepository.findByDealershipId(dealershipId, pageable);
        }
        return vehicleRepository.findAll(pageable);
    }
}
