package dev.ime.infrastructure.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import dev.ime.infrastructure.entity.SeatRedisEntity;

public interface SeatRedisRepository extends CrudRepository<SeatRedisEntity, UUID>{

	List<SeatRedisEntity> findByPlaneId(UUID planeId);
	
}
