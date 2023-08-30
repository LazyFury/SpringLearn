package io.lazyfury.mall.code.entity;

import io.lazyfury.mall.code.convert.UuidConverter;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@MappedSuperclass
public abstract class Base {

    @CreationTimestamp
    LocalDateTime created;

    @UpdateTimestamp
    LocalDateTime updated;

    @GeneratedValue(strategy = GenerationType.UUID)
    @Convert(converter = UuidConverter.class)
    UUID uuid;

    @PrePersist
    void prePresist(){
        uuid = UUID.randomUUID();
    }
}

