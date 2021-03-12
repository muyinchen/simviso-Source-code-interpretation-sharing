package com.tascigorkem.restaurantservice.infrastructure.menu;

import com.tascigorkem.restaurantservice.domain.exception.MenuNotFoundException;
import com.tascigorkem.restaurantservice.domain.menu.MenuDto;
import com.tascigorkem.restaurantservice.domain.menu.MenuPersistencePort;
import com.tascigorkem.restaurantservice.infrastructure.base.Status;
import com.tascigorkem.restaurantservice.util.DateUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
public class MenuPersistenceAdapter implements MenuPersistencePort {

    private final MenuRepository menuRepository;

    public MenuPersistenceAdapter(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public Flux<MenuDto> getAllMenus() {
        return menuRepository.findAll().filter(menuEntity -> !menuEntity.isDeleted())
                .map(this::mapToMenuDto);
    }

    @Override
    public Mono<MenuDto> getMenuById(UUID id) {
        return menuRepository.findById(id)
                .switchIfEmpty(
                        Mono.error(new MenuNotFoundException("id", id.toString())))
                .map(this::mapToMenuDto);
    }

    @Override
    public Mono<MenuDto> addMenu(MenuDto menuDto) {
        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());
        return menuRepository.save(MenuEntity.builder()
                .id(UUID.randomUUID())
                .creationTime(now)
                .updateTime(now)
                .status(Status.CREATED)
                .deleted(false)
                .name(menuDto.getName())
                .menuType(menuDto.getMenuType())
                .restaurantId(menuDto.getRestaurantId())
                .build())
                .map(this::mapToMenuDto);
    }

    @Override
    public Mono<MenuDto> updateMenu(MenuDto menuDto) {
        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());

        return menuRepository.findById(menuDto.getId()).flatMap(menuEntity -> {
            menuEntity.setUpdateTime(now);
            menuEntity.setStatus(Status.UPDATED);
            menuEntity.setName(menuDto.getName());
            menuEntity.setMenuType(menuDto.getMenuType());
            menuEntity.setRestaurantId(menuDto.getRestaurantId());
            return menuRepository.save(menuEntity);
        })
                .switchIfEmpty(
                        Mono.error(new MenuNotFoundException("id", menuDto.getId().toString())))
                .map(this::mapToMenuDto);
    }

    @Override
    public Mono<MenuDto> removeMenu(UUID id) {
        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());

        return menuRepository.findById(id).flatMap(menuEntity -> {
            menuEntity.setUpdateTime(now);
            menuEntity.setStatus(Status.UPDATED);
            menuEntity.setDeleted(true);
            menuEntity.setDeletionTime(now);
            return menuRepository.save(menuEntity);
        })
                .switchIfEmpty(
                        Mono.error(new MenuNotFoundException("id", id.toString())))
                .map(this::mapToMenuDto);

    }

    protected MenuDto mapToMenuDto(MenuEntity menuEntity) {
        return MenuDto.builder()
                .id(menuEntity.getId())
                .name(menuEntity.getName())
                .menuType(menuEntity.getMenuType())
                .restaurantId(menuEntity.getRestaurantId())
                .build();
    }

    protected MenuEntity mapToMenuEntity(MenuDto menuDto) {
        return MenuEntity.builder()
                .id(menuDto.getId())
                .name(menuDto.getName())
                .menuType(menuDto.getMenuType())
                .restaurantId(menuDto.getRestaurantId())
                .build();
    }
}
