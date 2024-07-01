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

import dev.ime.infrastructure.entity.ClientMongoEntity;

@Testcontainers
@DataMongoTest
class ClientNoSqlReadRepositoryTest {

	@Autowired
	private ClientNoSqlReadRepository clientNoSqlReadRepository;

	@ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo"));
	
	static {
        mongoDBContainer.start();
    }
	
	private ClientMongoEntity mongoEntity;
	private final ObjectId objectId = ObjectId.get();
	private final UUID clientId = UUID.randomUUID();
	private final String name = "Triss";
	private final String lastname = "Merigold";	
	
	@BeforeEach
	void initializeDataContainer() {

		mongoEntity = new ClientMongoEntity();
		mongoEntity.setMongoId(objectId);
		mongoEntity.setClientId(clientId);
		mongoEntity.setName(name);
		mongoEntity.setLastname(lastname);
		
		clientNoSqlReadRepository.save(mongoEntity);
	}

    @AfterEach
    void cleanUp() {
    	clientNoSqlReadRepository.deleteAll();
    }
    
	@Test
    void connectionEstablished() {
        Assertions.assertThat(mongoDBContainer.isCreated()).isTrue();
        Assertions.assertThat(mongoDBContainer.isRunning()).isTrue();
    }
	
	@Test
	void ClientNoSqlReadRepository_findFirstByClientId_ReturnOptionalEmpty() {
		
		Optional<ClientMongoEntity> optClient = clientNoSqlReadRepository.findFirstByClientId(UUID.randomUUID());
		
		org.junit.jupiter.api.Assertions.assertAll(
				() -> Assertions.assertThat(optClient).isNotNull(),
				() -> Assertions.assertThat(optClient).isEmpty()
				);	
		
	}

	@Test
	void ClientNoSqlReadRepository_findFirstByClientId_ReturnOptional() {
		
		Optional<ClientMongoEntity> optClient = clientNoSqlReadRepository.findFirstByClientId(clientId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				() -> Assertions.assertThat(optClient).isNotNull(),
				() -> Assertions.assertThat(optClient).isNotEmpty(),
				() -> Assertions.assertThat(optClient.get().getMongoId()).isEqualTo(objectId),
				() -> Assertions.assertThat(optClient.get().getClientId()).isEqualTo(clientId),
				() -> Assertions.assertThat(optClient.get().getName()).isEqualTo(name),
				() -> Assertions.assertThat(optClient.get().getLastname()).isEqualTo(lastname)
				);	
		
	}
	
}
