package com.mobiautobackend.domain.repositories;

import com.mobiautobackend.domain.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {
}