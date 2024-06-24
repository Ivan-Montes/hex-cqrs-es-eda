package dev.ime.infrastructure.repository.read;


import java.time.LocalDate;
import java.time.LocalTime;
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

import dev.ime.infrastructure.entity.FlightMongoEntity;

@Testcontainers
@DataMongoTest
class FlightNoSqlReadRepositoryTest {

	@Autowired
	private FlightNoSqlReadRepository flightNoSqlReadRepository;

	@ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo"));
	
	static {
        mongoDBContainer.start();
    }

	private FlightMongoEntity flightMongoEntityTest;
	private final ObjectId objectId = ObjectId.get();
	private final UUID flightId = UUID.randomUUID();
	private final String origin = "Madrid";
	private final String destiny = "Tokyo";
	private final LocalDate departureDate = LocalDate.parse("2024-04-01");
	private final LocalTime departureTime = LocalTime.parse("15:00");
	private final UUID planeId = UUID.randomUUID();
	
	@BeforeEach
	private void createObjects() {

		flightMongoEntityTest = new FlightMongoEntity();
		flightMongoEntityTest.setMongoId(objectId);
		flightMongoEntityTest.setFlightId(flightId);
		flightMongoEntityTest.setOrigin(origin);
		flightMongoEntityTest.setDestiny(destiny);
		flightMongoEntityTest.setDepartureDate(departureDate);
		flightMongoEntityTest.setDepartureTime(departureTime);
		flightMongoEntityTest.setPlaneId(planeId);
		
		flightNoSqlReadRepository.save(flightMongoEntityTest);
	}


    @AfterEach
    void cleanUp() {
    	flightNoSqlReadRepository.deleteAll();
    }
    
	@Test
    void connectionEstablished() {
        Assertions.assertThat(mongoDBContainer.isCreated()).isTrue();
        Assertions.assertThat(mongoDBContainer.isRunning()).isTrue();
    }

	@Test
	void FlightNoSqlReadRepository_findFirstByFlightId_ReturnOptionalEmpty() {
		
		Optional<FlightMongoEntity> optClient = flightNoSqlReadRepository.findFirstByFlightId(UUID.randomUUID());
		
		org.junit.jupiter.api.Assertions.assertAll(
				() -> Assertions.assertThat(optClient).isNotNull(),
				() -> Assertions.assertThat(optClient).isEmpty()
				);	
		
	}

	@Test
	void FlightNoSqlReadRepository_findFirstByFlightId_ReturnOptional() {
		
		Optional<FlightMongoEntity> optFlight = flightNoSqlReadRepository.findFirstByFlightId(flightId);
						
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optFlight).isNotNull(),
				() -> Assertions.assertThat(optFlight.get().getMongoId()).isEqualTo(objectId),
				()-> Assertions.assertThat(optFlight.get().getFlightId()).isEqualTo(flightId),
				()-> Assertions.assertThat(optFlight.get().getOrigin()).isEqualTo(origin),
				()-> Assertions.assertThat(optFlight.get().getDestiny()).isEqualTo(destiny),
				()-> Assertions.assertThat(optFlight.get().getDepartureDate()).isEqualTo(departureDate),
				()-> Assertions.assertThat(optFlight.get().getDepartureTime()).isEqualTo(departureTime),
				()-> Assertions.assertThat(optFlight.get().getPlaneId()).isEqualTo(planeId)
				);	
	}
	
}
