package com.mobiautobackend.domain.repositories;

import com.mobiautobackend.domain.entities.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    Page<Vehicle> findByDealershipId(String dealershipId, Pageable pageable);

    Page<Vehicle> findAll(Pageable pageable);

    Optional<Vehicle> findByIdAndDealershipId(String id, String dealershipId);
}
