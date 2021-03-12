package com.tascigorkem.restaurantservice.infrastructure.menufood;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface MenuFoodRepository extends ReactiveCrudRepository<MenuFoodEntity, UUID> {

    Mono<MenuFoodEntity> findAllByMenuIdAndFoodId(UUID menuId, UUID foodId);

}
