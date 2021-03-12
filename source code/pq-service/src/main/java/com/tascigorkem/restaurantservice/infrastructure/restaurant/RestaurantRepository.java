package com.tascigorkem.restaurantservice.infrastructure.restaurant;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RestaurantRepository extends ReactiveCrudRepository<RestaurantEntity, UUID> {

}
