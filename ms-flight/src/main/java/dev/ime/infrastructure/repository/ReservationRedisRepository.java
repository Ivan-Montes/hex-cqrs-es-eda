package dev.ime.infrastructure.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import dev.ime.infrastructure.entity.ReservationRedisEntity;

public interface ReservationRedisRepository extends CrudRepository<ReservationRedisEntity, UUID>{

	List<ReservationRedisEntity> findByFlightId(UUID flightId);
	
}
