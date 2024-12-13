package dev.ime.infrastructure.repository;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

import dev.ime.infrastructure.entity.ReservationJpaEntity;
import jakarta.persistence.Tuple;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReservationJpaRepositoryTest {

	@Autowired
	private ReservationJpaRepository reservationJpaRepository;

	@SuppressWarnings("resource")
	@Container
	@ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
    .withInitScript("init.sql");
	
	private final UUID reservationId = UUID.randomUUID();
	private final UUID clientId = UUID.randomUUID();
	private final UUID flightId = UUID.randomUUID();
	private Set<UUID> seatIdsSet = new HashSet<>();
	private final UUID seatId = UUID.randomUUID();
	
	@BeforeEach
	void initializeDataContainer() {
		 
		 seatIdsSet = new HashSet<>();
		 seatIdsSet.add(seatId);
		 List<ReservationJpaEntity> reservationJpaEntityList = List.of(
					new ReservationJpaEntity(reservationId, clientId, flightId, seatIdsSet),
					new ReservationJpaEntity(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), new HashSet<>()),
					new ReservationJpaEntity(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), new HashSet<>()),
					new ReservationJpaEntity(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), new HashSet<>()),
					new ReservationJpaEntity(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), new HashSet<>()),
					new ReservationJpaEntity(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), new HashSet<>()),
					new ReservationJpaEntity(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), new HashSet<>())
					);
		 reservationJpaRepository.saveAll(reservationJpaEntityList);
			
	 }

	@Test
    void connectionEstablished() {
        Assertions.assertThat(postgres.isCreated()).isTrue();
        Assertions.assertThat(postgres.isRunning()).isTrue();
    }
		 
	@Test
	void ReservationJpaRepository_findAll_ReturnOptReservationJpa() {
		 
		 List<ReservationJpaEntity> list = reservationJpaRepository.findAll();
		 
			org.junit.jupiter.api.Assertions.assertAll(
					() -> Assertions.assertThat(list).isNotNull(),
					() -> Assertions.assertThat(list).isNotEmpty(),
					() -> Assertions.assertThat(list).hasSize(7)
					);	 
	 }
	 
	@Test
	void ReservationJpaRepository_findById_ReturnOptReservationJpa() {
		
		Optional<ReservationJpaEntity> optReservation = reservationJpaRepository.findById(reservationId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optReservation).isNotNull(),
				()-> Assertions.assertThat(optReservation.get().getReservationId()).isEqualTo(reservationId),
				()-> Assertions.assertThat(optReservation.get().getClientId()).isEqualTo(clientId),
				()-> Assertions.assertThat(optReservation.get().getFlightId()).isEqualTo(flightId),
				()-> Assertions.assertThat(optReservation.get().getSeatIdsSet()).isEqualTo(seatIdsSet)
				);
	}	
	
	@Test
	void ReservationJpaRepository_countReservationJpaEntityByFlightIdAndSeatIdSet_ReturnInt() {			
		
		int resultValue = reservationJpaRepository.countReservationJpaEntityByFlightIdAndSeatIdSet(flightId, seatIdsSet);
		
		org.junit.jupiter.api.Assertions.assertAll(
				() -> Assertions.assertThat(resultValue).isEqualTo(1)
				);		
	}

	@Test
	void ReservationJpaRepository_countReservationJpaEntityByFlightIdSeatIdSetButDifferentClient_ReturnInt() {			
		
		int resultValue = reservationJpaRepository.countReservationJpaEntityByFlightIdSeatIdSetButDifferentClient(flightId, seatIdsSet, clientId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				() -> Assertions.assertThat(resultValue).isZero()
				);		
	}

	@Test
	void ReservationJpaRepository_countReservationJpaEntityByFlightIdSeatIdSetWithSameClientDifferentReservation_ReturnInt() {			
		
		int resultValue = reservationJpaRepository.countReservationJpaEntityByFlightIdSeatIdSetWithSameClientDifferentReservation(UUID.randomUUID(), flightId, seatIdsSet, clientId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				() -> Assertions.assertThat(resultValue).isEqualTo(1)
				);		
	}
	
	@Test
	void ReservationJpaRepository_findReservationJpaEntityByFlightIdAndSeatIdSetQueryNative_ReturnListTuple() {		
		
		List<Tuple> list = reservationJpaRepository.findReservationJpaEntityByFlightIdAndSeatIdSetQueryNative(flightId, seatIdsSet);
		
		org.junit.jupiter.api.Assertions.assertAll(
				() -> Assertions.assertThat(list).isNotNull(),
				() -> Assertions.assertThat(list).isNotEmpty()
				);		
	}	

}
