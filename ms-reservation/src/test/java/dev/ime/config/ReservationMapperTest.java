package dev.ime.config;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.dto.ReservationDto;
import dev.ime.domain.model.Reservation;
import dev.ime.infrastructure.entity.ReservationJpaEntity;

@ExtendWith(MockitoExtension.class)
class ReservationMapperTest {

	@InjectMocks
	private ReservationMapper reservationMapper;
	
	private final UUID reservationId = UUID.randomUUID();
	private final UUID clientId = UUID.randomUUID();
	private final UUID flightId = UUID.randomUUID();
	private Set<UUID> seatIdsSet;
	private Reservation reservationTest;
	private List<Reservation> reservationList;
	private ReservationJpaEntity reservationJpaEntityTest;
	private List<ReservationJpaEntity> reservationJpaEntityList;
	
	@BeforeEach
	private void createObjects() {

		reservationList = new ArrayList<>();
		reservationTest = new Reservation();
		reservationTest.setReservationId(reservationId);
		reservationTest.setClientId(clientId);
		reservationTest.setFlightId(flightId);
		reservationTest.setSeatIdsSet(seatIdsSet);
		
		reservationJpaEntityList = new ArrayList<>();		
		reservationJpaEntityTest = new ReservationJpaEntity();
		reservationJpaEntityTest.setReservationId(reservationId);
		reservationJpaEntityTest.setClientId(clientId);
		reservationJpaEntityTest.setFlightId(flightId);
		reservationJpaEntityTest.setSeatIdsSet(seatIdsSet);
		
	}
	
	@Test
	void ReservationMapper_fromJpaToDomain_ReturnReservation() {
		
		Reservation reservation = reservationMapper.fromJpaToDomain(reservationJpaEntityTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(reservation).isNotNull(),
				()-> Assertions.assertThat(reservation.getReservationId()).isEqualTo(reservationId),
				()-> Assertions.assertThat(reservation.getClientId()).isEqualTo(clientId),
				()-> Assertions.assertThat(reservation.getFlightId()).isEqualTo(flightId),
				()-> Assertions.assertThat(reservation.getSeatIdsSet()).isEqualTo(seatIdsSet)
				);
	}

	@Test
	void ReservationMapper_fromListJpaToListDomain_ReturnListReservation() {
		
		reservationJpaEntityList.add(reservationJpaEntityTest);
		
		List<Reservation> list = reservationMapper.fromListJpaToListDomain(reservationJpaEntityList);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).getReservationId()).isEqualTo(reservationId),
				()-> Assertions.assertThat(list.get(0).getClientId()).isEqualTo(clientId),
				()-> Assertions.assertThat(list.get(0).getFlightId()).isEqualTo(flightId),
				()-> Assertions.assertThat(list.get(0).getSeatIdsSet()).isEqualTo(seatIdsSet)
				);	
	}

	@Test
	void ReservationMapper_fromListJpaToListDomain_ReturnListReservationEmpty() {
				
		List<Reservation> list = reservationMapper.fromListJpaToListDomain(null);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isEmpty()
				);	
	}
	
	@Test
	void ReservationMapper_fromDomainToDto_ReturnDto() {
		
		ReservationDto dto = reservationMapper.fromDomainToDto(reservationTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(dto).isNotNull(),
				()-> Assertions.assertThat(dto.reservationId()).isEqualTo(reservationId),
				()-> Assertions.assertThat(dto.clientId()).isEqualTo(clientId),
				()-> Assertions.assertThat(dto.flightId()).isEqualTo(flightId),
				()-> Assertions.assertThat(dto.seatIdsSet()).isEqualTo(seatIdsSet)
				);
	}

	@Test
	void ReservationMapper_fromListDomainToListDto_ReturnListDto() {
		
		reservationList.add(reservationTest);
		
		List<ReservationDto> list =reservationMapper.fromListDomainToListDto(reservationList);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).reservationId()).isEqualTo(reservationId),
				()-> Assertions.assertThat(list.get(0).clientId()).isEqualTo(clientId),
				()-> Assertions.assertThat(list.get(0).flightId()).isEqualTo(flightId),
				()-> Assertions.assertThat(list.get(0).seatIdsSet()).isEqualTo(seatIdsSet)
				);	
	}

	@Test
	void ReservationMapper_fromListDomainToListDto_ReturnListReservationEmpty() {
				
		List<ReservationDto> list = reservationMapper.fromListDomainToListDto(null);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isEmpty()
				);	
	}	
	
}
