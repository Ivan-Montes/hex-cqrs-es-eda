package dev.ime.infrastructure.repository;


import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.redis.testcontainers.RedisContainer;

import dev.ime.infrastructure.entity.ReservationRedisEntity;


@Testcontainers
@DataRedisTest
class ReservationRedisRepositoryTest {

	@Autowired
	private ReservationRedisRepository reservationRedisRepository;

    @ServiceConnection
	static RedisContainer redisContainer = new RedisContainer(DockerImageName.parse("redis:latest"));
	
	static {
		redisContainer.start();
    }

	private final UUID reservationId = UUID.randomUUID();
	private final UUID clientId = UUID.randomUUID();
	private final UUID flightId = UUID.randomUUID();
	private Set<UUID> seatIdsSet = new HashSet<>();
	private ReservationRedisEntity reservationRedisEntityTest;

	@BeforeEach
	private void initializeDataContainer() {
		
		reservationRedisEntityTest = new ReservationRedisEntity(reservationId, clientId, flightId, seatIdsSet);
	
		reservationRedisRepository.save(reservationRedisEntityTest);
		
	}

	@Test
    void connectionEstablished() {
        Assertions.assertThat(redisContainer.isCreated()).isTrue();
        Assertions.assertThat(redisContainer.isRunning()).isTrue();
    }

}

