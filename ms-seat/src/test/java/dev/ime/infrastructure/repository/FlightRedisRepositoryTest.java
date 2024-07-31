package dev.ime.infrastructure.repository;


import java.util.List;
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

import dev.ime.infrastructure.entity.FlightRedisEntity;

@Testcontainers
@DataRedisTest
class FlightRedisRepositoryTest {

	@Autowired
	private FlightRedisRepository flightRedisRepository;
	
    @ServiceConnection
	static RedisContainer redisContainer = new RedisContainer(DockerImageName.parse("redis:latest"));
	
	static {
		redisContainer.start();
    }

	private final UUID flightId = UUID.randomUUID();
	private final UUID planeId = UUID.randomUUID();
	private FlightRedisEntity flightRedisEntityTest;
	
	@BeforeEach
	private void initializeDataContainer() {
		
		flightRedisEntityTest = new FlightRedisEntity();
		flightRedisEntityTest.setFlightId(flightId);
		flightRedisEntityTest.setPlaneId(planeId);
		
		flightRedisRepository.save(flightRedisEntityTest);
		
	}

	@Test
    void connectionEstablished() {
        Assertions.assertThat(redisContainer.isCreated()).isTrue();
        Assertions.assertThat(redisContainer.isRunning()).isTrue();
    }
	
	@Test
	void FlightRedisRepository_findByPlaneId_ReturnListEmpty() {
		
		List<FlightRedisEntity> list = flightRedisRepository.findByPlaneId(UUID.randomUUID());
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isEmpty()
				);
		
	}

	@Test
	void FlightRedisRepository_findByPlaneId_ReturnList() {
		
		List<FlightRedisEntity> list = flightRedisRepository.findByPlaneId(planeId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).getFlightId()).isEqualTo(flightId),
				()-> Assertions.assertThat(list.get(0).getPlaneId()).isEqualTo(planeId)
				);
		
	}	

}
