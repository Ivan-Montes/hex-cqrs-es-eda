package dev.ime.infrastructure.repository.read;


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

import dev.ime.infrastructure.entity.PlaneMongoEntity;

@Testcontainers
@DataMongoTest
class PlaneNoSqlReadRepositoryTest {

	@Autowired
	private PlaneNoSqlReadRepository planeNoSqlReadRepository;

	@ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo"));
	
	static {
        mongoDBContainer.start();
    }

	private PlaneMongoEntity planeMongoEntity;
	private final ObjectId objectId = ObjectId.get();
	private final UUID planeId = UUID.randomUUID();
	private final String name = "Pursuit Special";
	private final Integer capacity = 2;	

	@BeforeEach
	void initializeDataContainer() {
		
		planeMongoEntity = new PlaneMongoEntity(objectId, planeId, name, capacity);
		
		planeNoSqlReadRepository.save(planeMongoEntity);
	}

    @AfterEach
    void cleanUp() {
    	planeNoSqlReadRepository.deleteAll();
    }
    
	@Test
    void connectionEstablished() {
        Assertions.assertThat(mongoDBContainer.isCreated()).isTrue();
        Assertions.assertThat(mongoDBContainer.isRunning()).isTrue();
    }
	
	@Test
	void PlaneNoSqlReadRepository_findFirstByPlaneId_ReturnOptionalEmpty() {
		
		Optional<PlaneMongoEntity> optPlane = planeNoSqlReadRepository.findFirstByPlaneId(UUID.randomUUID());
		
		org.junit.jupiter.api.Assertions.assertAll(
				() -> Assertions.assertThat(optPlane).isNotNull(),
				() -> Assertions.assertThat(optPlane).isEmpty()
				);	
	}

	@Test
	void PlaneNoSqlReadRepository_findFirstByPlaneId_ReturnOptional() {
		
		Optional<PlaneMongoEntity> optPlane = planeNoSqlReadRepository.findFirstByPlaneId(planeId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optPlane).isNotNull(),
				() -> Assertions.assertThat(optPlane.get().getMongoId()).isEqualTo(objectId),
				()-> Assertions.assertThat(optPlane.get().getPlaneId()).isEqualTo(planeId),
				()-> Assertions.assertThat(optPlane.get().getName()).isEqualTo(name),
				()-> Assertions.assertThat(optPlane.get().getCapacity()).isEqualTo(capacity)
				);
	}	
	
}
