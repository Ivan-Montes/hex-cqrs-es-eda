package dev.ime.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import dev.ime.infrastructure.entity.FlightRedisEntity;

public interface FlightRedisRepository extends CrudRepository<FlightRedisEntity, UUID>{

}
