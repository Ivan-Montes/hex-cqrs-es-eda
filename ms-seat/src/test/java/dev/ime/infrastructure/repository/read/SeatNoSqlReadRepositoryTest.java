package dev.ime.infrastructure.repository.read;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import dev.ime.infrastructure.entity.SeatMongoEntity;

@Testcontainers
@DataMongoTest
class SeatNoSqlReadRepositoryTest {

	@Autowired
	private SeatNoSqlReadRepository seatNoSqlReadRepository;

	@ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo"));
	
	static {
        mongoDBContainer.start();
    }

	private SeatMongoEntity seatMongoEntityTest;
	private final ObjectId objectId = ObjectId.get();
	private final UUID seatId = UUID.randomUUID();
	private final String seatNumber = "PS1973";
	private final UUID planeId = UUID.randomUUID();

	@BeforeEach
	private void initializeDataContainer() {
		
		seatMongoEntityTest = new SeatMongoEntity();
		seatMongoEntityTest.setMongoId(objectId);
		seatMongoEntityTest.setSeatId(seatId);
		seatMongoEntityTest.setSeatNumber(seatNumber);
		seatMongoEntityTest.setPlaneId(planeId);
		
		seatNoSqlReadRepository.save(seatMongoEntityTest);
		
	}	

    @AfterEach
    void cleanUp() {
    	seatNoSqlReadRepository.deleteAll();
    }
    
	@Test
    void connectionEstablished() {
        Assertions.assertThat(mongoDBContainer.isCreated()).isTrue();
        Assertions.assertThat(mongoDBContainer.isRunning()).isTrue();
    }
	
	@Test
	void SeatNoSqlReadRepository_findFirstBySeatId_ReturnOptionalEmpty() {
		
		Optional<SeatMongoEntity> optSeat = seatNoSqlReadRepository.findFirstBySeatId(UUID.randomUUID());
		
		org.junit.jupiter.api.Assertions.assertAll(
				() -> Assertions.assertThat(optSeat).isNotNull(),
				() -> Assertions.assertThat(optSeat).isEmpty()
				);	
	}

	@Test
	void SeatNoSqlReadRepository_findFirstBySeatId_ReturnOptional() {

		Optional<SeatMongoEntity> optSeat = seatNoSqlReadRepository.findFirstBySeatId(seatId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optSeat).isNotNull(),
				() -> Assertions.assertThat(optSeat.get().getMongoId()).isEqualTo(objectId),
				()-> Assertions.assertThat(optSeat.get().getSeatId()).isEqualTo(seatId),
				()-> Assertions.assertThat(optSeat.get().getSeatNumber()).isEqualTo(seatNumber),
				()-> Assertions.assertThat(optSeat.get().getPlaneId()).isEqualTo(planeId)
				);	
	}

	@Test
	void SeatNoSqlReadRepository_findByPlaneId_ReturnListEmpty() {
		
		List<SeatMongoEntity> list = seatNoSqlReadRepository.findByPlaneId(UUID.randomUUID());
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isEmpty()
				);
	}

	@Test
	void SeatNoSqlReadRepository_findByPlaneId_ReturnList() {
		
		List<SeatMongoEntity> list = seatNoSqlReadRepository.findByPlaneId(planeId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1),
				() -> Assertions.assertThat(list.get(0).getMongoId()).isEqualTo(objectId),
				()-> Assertions.assertThat(list.get(0).getSeatId()).isEqualTo(seatId),
				()-> Assertions.assertThat(list.get(0).getSeatNumber()).isEqualTo(seatNumber),
				()-> Assertions.assertThat(list.get(0).getPlaneId()).isEqualTo(planeId)
				);
	}	
	
}
