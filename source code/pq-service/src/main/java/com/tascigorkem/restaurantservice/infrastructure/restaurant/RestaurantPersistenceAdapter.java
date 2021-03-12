package com.tascigorkem.restaurantservice.infrastructure.restaurant;

import com.tascigorkem.restaurantservice.domain.exception.RestaurantNotFoundException;
import com.tascigorkem.restaurantservice.domain.restaurant.RestaurantDto;
import com.tascigorkem.restaurantservice.domain.restaurant.RestaurantPersistencePort;
import com.tascigorkem.restaurantservice.infrastructure.base.Status;
import com.tascigorkem.restaurantservice.util.DateUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
public class RestaurantPersistenceAdapter implements RestaurantPersistencePort {

    private final RestaurantRepository restaurantRepository;

    public RestaurantPersistenceAdapter(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Flux<RestaurantDto> getAllRestaurants() {
        return restaurantRepository.findAll().filter(restaurantEntity -> !restaurantEntity.isDeleted())
                .map(this::mapToRestaurantDto);
    }

    @Override
    public Mono<RestaurantDto> getRestaurantById(UUID id) {
        return restaurantRepository.findById(id)
                .switchIfEmpty(
                        Mono.error(new RestaurantNotFoundException("id", id.toString())))
                .map(this::mapToRestaurantDto);
    }

    @Override
    public Mono<RestaurantDto> addRestaurant(RestaurantDto restaurantDto) {
        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());
        return restaurantRepository.save(RestaurantEntity.builder()
                .id(UUID.randomUUID())
                .creationTime(now)
                .updateTime(now)
                .status(Status.CREATED)
                .deleted(false)
                .name(restaurantDto.getName())
                .address(restaurantDto.getAddress())
                .phone(restaurantDto.getPhone())
                .employeeCount(restaurantDto.getEmployeeCount())
                .companyId(restaurantDto.getCompanyId())
                .build())
                .map(this::mapToRestaurantDto);
    }

    @Override
    public Mono<RestaurantDto> updateRestaurant(RestaurantDto restaurantDto) {
        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());

        return restaurantRepository.findById(restaurantDto.getId()).flatMap(restaurantEntity -> {
            restaurantEntity.setUpdateTime(now);
            restaurantEntity.setStatus(Status.UPDATED);
            restaurantEntity.setName(restaurantDto.getName());
            restaurantEntity.setAddress(restaurantDto.getAddress());
            restaurantEntity.setPhone(restaurantDto.getPhone());
            restaurantEntity.setEmployeeCount(restaurantDto.getEmployeeCount());
            restaurantEntity.setCompanyId(restaurantDto.getCompanyId());
            return restaurantRepository.save(restaurantEntity);
        })
                .switchIfEmpty(
                        Mono.error(new RestaurantNotFoundException("id", restaurantDto.getId().toString())))
                .map(this::mapToRestaurantDto);
    }

    @Override
    public Mono<RestaurantDto> removeRestaurant(UUID id) {
        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());

        return restaurantRepository.findById(id).flatMap(restaurantEntity -> {
            restaurantEntity.setUpdateTime(now);
            restaurantEntity.setStatus(Status.UPDATED);
            restaurantEntity.setDeleted(true);
            restaurantEntity.setDeletionTime(now);
            return restaurantRepository.save(restaurantEntity);
        })
                .switchIfEmpty(
                        Mono.error(new RestaurantNotFoundException("id", id.toString())))
                .map(this::mapToRestaurantDto);

    }

    protected RestaurantDto mapToRestaurantDto(RestaurantEntity restaurantEntity) {
        return RestaurantDto.builder()
                .id(restaurantEntity.getId())
                .name(restaurantEntity.getName())
                .address(restaurantEntity.getAddress())
                .phone(restaurantEntity.getPhone())
                .employeeCount(restaurantEntity.getEmployeeCount())
                .companyId(restaurantEntity.getCompanyId())
                .build();
    }

    protected RestaurantEntity mapToRestaurantEntity(RestaurantDto restaurantDto) {
        return RestaurantEntity.builder()
                .id(restaurantDto.getId())
                .name(restaurantDto.getName())
                .address(restaurantDto.getAddress())
                .phone(restaurantDto.getPhone())
                .employeeCount(restaurantDto.getEmployeeCount())
                .companyId(restaurantDto.getCompanyId())
                .build();
    }
}
