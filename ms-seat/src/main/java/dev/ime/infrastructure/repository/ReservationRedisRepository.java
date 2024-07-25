package dev.ime.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import dev.ime.infrastructure.entity.ReservationRedisEntity;

public interface ReservationRedisRepository extends CrudRepository<ReservationRedisEntity, UUID>, QueryByExampleExecutor<ReservationRedisEntity> {

	
}
