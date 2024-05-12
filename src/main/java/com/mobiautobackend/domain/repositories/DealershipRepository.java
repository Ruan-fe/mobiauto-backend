package com.mobiautobackend.domain.repositories;

import com.mobiautobackend.domain.entities.Dealership;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DealershipRepository extends CrudRepository<Dealership, String> {
    Optional<Dealership> findByCnpj(String cnpj);

    Optional<Dealership> findByCnpjOrMembersId(String cnpj, String memberId);
}
