package com.tascigorkem.restaurantservice.infrastructure.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public abstract class BaseEntity implements Persistable<UUID> {

    @Column("creation_time")
    protected LocalDateTime creationTime;
    @Column("update_time")
    protected LocalDateTime updateTime;
    @Column("status_type")
    protected Status status;
    @Column("deletion_time")
    protected LocalDateTime deletionTime;
    @Column("deleted")
    private boolean deleted;
    @Id
    @Column("id")
    private UUID id;

    @Override
    public boolean isNew() {
        return getStatus() == Status.CREATED;
    }

}
