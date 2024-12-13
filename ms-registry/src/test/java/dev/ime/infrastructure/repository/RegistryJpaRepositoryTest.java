package dev.ime.infrastructure.repository;


import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.infrastructure.entity.RegistryJpaEntity;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RegistryJpaRepositoryTest {

	@Autowired
	private RegistryJpaRepository registryJpaRepository;
	
	@SuppressWarnings("resource")
	@Container
	@ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
    .withInitScript("init.sql");
	
	private final UUID registryId = UUID.randomUUID();
	private final Long sequence = 73L;
	private Instant timeInstant;
	private String eventDataStr = "{}";
	private RegistryJpaEntity registryJpaEntityTest;
	
	@BeforeEach
	void initializeDataContainer() {
		
		timeInstant = Instant.now();
		
		registryJpaEntityTest = new RegistryJpaEntity(
				registryId,
				ApplicationConstant.CAT_REGISTRY,
				ApplicationConstant.REGISTRY_CREATED,
				timeInstant,
				sequence,
				eventDataStr
				);
		
		List<RegistryJpaEntity> registryJpaEntityList = List.of(
				registryJpaEntityTest,
				new RegistryJpaEntity(UUID.randomUUID(),ApplicationConstant.CAT_REGISTRY,ApplicationConstant.REGISTRY_CREATED,Instant.now(),sequence,eventDataStr),
				new RegistryJpaEntity(UUID.randomUUID(),ApplicationConstant.CAT_REGISTRY,ApplicationConstant.REGISTRY_CREATED,Instant.now(),sequence,eventDataStr),
				new RegistryJpaEntity(UUID.randomUUID(),ApplicationConstant.CAT_REGISTRY,ApplicationConstant.REGISTRY_CREATED,Instant.now(),sequence,eventDataStr),
				new RegistryJpaEntity(UUID.randomUUID(),ApplicationConstant.CAT_REGISTRY,ApplicationConstant.REGISTRY_CREATED,Instant.now(),sequence,eventDataStr)
				);
		
		registryJpaRepository.saveAll(registryJpaEntityList);
		
	}

	@Test
    void connectionEstablished() {
        Assertions.assertThat(postgres.isCreated()).isTrue();
        Assertions.assertThat(postgres.isRunning()).isTrue();
    }
	 
	@Test
	void RegistryJpaRepository_findAll_ReturnOptReservationJpa() {
		 
		 List<RegistryJpaEntity> list = registryJpaRepository.findAll();
		 
			org.junit.jupiter.api.Assertions.assertAll(
					() -> Assertions.assertThat(list).isNotNull(),
					() -> Assertions.assertThat(list).isNotEmpty(),
					() -> Assertions.assertThat(list).hasSize(5)
					);	 
	 }

	@Test
	void RegistryJpaRepository_findById_ReturnOptReservationJpa() {
		
		Optional<RegistryJpaEntity> optRegistry = registryJpaRepository.findById(registryId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optRegistry).isNotNull(),	
				()-> Assertions.assertThat(optRegistry.get().getEventId()).isEqualTo(registryId),
				()-> Assertions.assertThat(optRegistry.get().getEventCategory()).isEqualTo(ApplicationConstant.CAT_REGISTRY),
				()-> Assertions.assertThat(optRegistry.get().getEventType()).isEqualTo(ApplicationConstant.REGISTRY_CREATED),
				()-> Assertions.assertThat(optRegistry.get().getTimeInstant()).isEqualTo(timeInstant),
				()-> Assertions.assertThat(optRegistry.get().getSequence()).isEqualTo(sequence),
				()-> Assertions.assertThat(optRegistry.get().toString()).hasToString(registryJpaEntityTest.toString()),
				()-> Assertions.assertThat(optRegistry.get().hashCode()).hasSameHashCodeAs(registryJpaEntityTest.hashCode())
				);
	}		

}
