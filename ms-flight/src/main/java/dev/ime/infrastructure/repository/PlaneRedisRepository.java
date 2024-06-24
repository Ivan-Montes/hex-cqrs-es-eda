package dev.ime.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import dev.ime.infrastructure.entity.PlaneRedisEntity;

public interface PlaneRedisRepository extends CrudRepository<PlaneRedisEntity, UUID>{

}
