package com.tascigorkem.restaurantservice.infrastructure.company;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CompanyRepository extends ReactiveCrudRepository<CompanyEntity, UUID> {

}
