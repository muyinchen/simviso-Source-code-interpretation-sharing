package com.tascigorkem.restaurantservice.infrastructure.menu;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MenuRepository extends ReactiveCrudRepository<MenuEntity, UUID> {

}
