package dev.ime.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import dev.ime.infrastructure.entity.ClientRedisEntity;

public interface ClientRedisRepository extends CrudRepository<ClientRedisEntity, UUID>{

}
