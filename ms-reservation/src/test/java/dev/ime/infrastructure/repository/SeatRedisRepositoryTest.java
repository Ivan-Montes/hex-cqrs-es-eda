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

import dev.ime.infrastructure.entity.SeatRedisEntity;

@Testcontainers
@DataRedisTest
class SeatRedisRepositoryTest {

	@Autowired
	private SeatRedisRepository seatRedisRepository;

    @ServiceConnection
	static RedisContainer redisContainer = new RedisContainer(DockerImageName.parse("redis:latest"));
	
	static {
		redisContainer.start();
    }

	private final UUID seatId = UUID.randomUUID();
	private final UUID planeId = UUID.randomUUID();
	private SeatRedisEntity seatRedisEntityTest;

	@BeforeEach
	private void initializeDataContainer() {
		
		seatRedisEntityTest = new SeatRedisEntity(seatId, planeId);
		
		seatRedisRepository.save(seatRedisEntityTest);
		
	}

	@Test
    void connectionEstablished() {
        Assertions.assertThat(redisContainer.isCreated()).isTrue();
        Assertions.assertThat(redisContainer.isRunning()).isTrue();
    }
	
	@Test
	void SeatRedisRepository_findByPlaneId_ReturnListEmpty() {
		
		List<SeatRedisEntity> list = seatRedisRepository.findByPlaneId(UUID.randomUUID());

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isEmpty()
				);
	}

	@Test
	void SeatRedisRepository_findByPlaneId_ReturnList() {
		
		List<SeatRedisEntity> list = seatRedisRepository.findByPlaneId(planeId);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).getSeatId()).isEqualTo(seatId),
				()-> Assertions.assertThat(list.get(0).getPlaneId()).isEqualTo(planeId)
				);
	}

}
