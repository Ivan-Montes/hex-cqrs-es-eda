package dev.ime.infrastructure.adapter;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import dev.ime.config.ReservationMapper;
import dev.ime.domain.model.Reservation;
import dev.ime.infrastructure.entity.ReservationJpaEntity;
import dev.ime.infrastructure.repository.ReservationJpaRepository;

@ExtendWith(MockitoExtension.class)
class ReservationReadRepositoryAdapterTest {

	@Mock
	private ReservationJpaRepository reservationJpaRepository;
	
	@Mock
	private ReservationMapper reservationMapper;
	
	@InjectMocks
	private ReservationReadRepositoryAdapter reservationReadRepositoryAdapter;

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

		seatIdsSet = new HashSet<>();
		
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
	void ReservationReadRepositoryAdapter_findAll_ReturnListReservation() {

		reservationJpaEntityList.add(reservationJpaEntityTest);
		reservationList.add(reservationTest);
		@SuppressWarnings("unchecked")
		Page<ReservationJpaEntity> pageMock = Mockito.mock(Page.class);
		Mockito.when(reservationJpaRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(pageMock);
		Mockito.when(pageMock.toList()).thenReturn(reservationJpaEntityList);
		Mockito.when(reservationMapper.fromListJpaToListDomain(Mockito.anyList())).thenReturn(reservationList);
		
		List<Reservation> list = reservationReadRepositoryAdapter.findAll(0,1);

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
	void ReservationReadRepositoryAdapter_findById_ReturnOptionalReservation() {
		
		Mockito.when(reservationJpaRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(reservationJpaEntityTest));
		Mockito.when(reservationMapper.fromJpaToDomain(Mockito.any(ReservationJpaEntity.class))).thenReturn(reservationTest);
		
		Optional<Reservation> optReservation = reservationReadRepositoryAdapter.findById(reservationId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optReservation).isNotNull(),
				()-> Assertions.assertThat(optReservation.get().getReservationId()).isEqualTo(reservationId),
				()-> Assertions.assertThat(optReservation.get().getClientId()).isEqualTo(clientId),
				()-> Assertions.assertThat(optReservation.get().getFlightId()).isEqualTo(flightId),
				()-> Assertions.assertThat(optReservation.get().getSeatIdsSet()).isEqualTo(seatIdsSet)
				);	
	}

	@Test
	void ReservationReadRepositoryAdapter_countReservationJpaEntityByFlightIdAndSeatIdSet_ReturnInt() {

		Mockito.when(reservationJpaRepository.countReservationJpaEntityByFlightIdAndSeatIdSet(Mockito.any(UUID.class), Mockito.anyCollection())).thenReturn(3);
		
		int resultValue = reservationReadRepositoryAdapter.countReservationJpaEntityByFlightIdAndSeatIdSet(flightId, seatIdsSet);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isEqualTo(3)
				);
	}

	@Test
	void ReservationReadRepositoryAdapter_countReservationJpaEntityByFlightIdSeatIdSetButDifferentClient_ReturnInt() {

		Mockito.when(reservationJpaRepository.countReservationJpaEntityByFlightIdSeatIdSetButDifferentClient(Mockito.any(UUID.class), Mockito.anyCollection(), Mockito.any(UUID.class))).thenReturn(2);
		
		int resultValue = reservationReadRepositoryAdapter.countReservationJpaEntityByFlightIdSeatIdSetButDifferentClient(flightId, seatIdsSet, clientId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isEqualTo(2)
				);
	}

	@Test
	void ReservationReadRepositoryAdapter_countReservationJpaEntityByFlightIdSeatIdSetWithSameClientDifferentReservation_ReturnInt() {

		Mockito.when(reservationJpaRepository.countReservationJpaEntityByFlightIdSeatIdSetWithSameClientDifferentReservation(Mockito.any(UUID.class),Mockito.any(UUID.class), Mockito.anyCollection(), Mockito.any(UUID.class))).thenReturn(1);
		
		int resultValue = reservationReadRepositoryAdapter.countReservationJpaEntityByFlightIdSeatIdSetWithSameClientDifferentReservation(reservationId, flightId, seatIdsSet, clientId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isEqualTo(1)
				);
	}
	
}
