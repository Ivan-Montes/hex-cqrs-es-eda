package dev.ime.infrastructure.adapter;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import dev.ime.config.ReservationMapper;
import dev.ime.domain.model.Reservation;
import dev.ime.domain.port.outbound.GenericReadRepositoryPort;
import dev.ime.domain.port.outbound.ReservationSpecificReadRepositoryPort;
import dev.ime.infrastructure.repository.ReservationJpaRepository;

@Repository
public class ReservationReadRepositoryAdapter implements GenericReadRepositoryPort<Reservation>, ReservationSpecificReadRepositoryPort{

	private final ReservationJpaRepository reservationJpaRepository;
	private final ReservationMapper reservationMapper;
	
	public ReservationReadRepositoryAdapter(ReservationJpaRepository reservationJpaRepository,
			ReservationMapper reservationMapper) {
		super();
		this.reservationJpaRepository = reservationJpaRepository;
		this.reservationMapper = reservationMapper;
	}

	@Override
	public List<Reservation> findAll(Integer page, Integer size) {
		
		return reservationMapper.fromListJpaToListDomain(reservationJpaRepository.findAll(PageRequest.of(page, size)).toList() );

	}

	@Override
	public Optional<Reservation> findById(UUID id) {
		
		return reservationJpaRepository.findById(id).map(reservationMapper::fromJpaToDomain);
	
	}

	@Override
	public int countReservationJpaEntityByFlightIdAndSeatIdSet(UUID flightId, Collection<UUID> seatIdsSet) {
		
		return reservationJpaRepository.countReservationJpaEntityByFlightIdAndSeatIdSet(flightId, seatIdsSet);
		
	}

	@Override
	public int countReservationJpaEntityByFlightIdSeatIdSetButDifferentClient(UUID flightId,
			Collection<UUID> seatIdsSet, UUID clientId) {
		
		return reservationJpaRepository.countReservationJpaEntityByFlightIdSeatIdSetButDifferentClient(flightId, seatIdsSet, clientId);
		
	}

	@Override
	public int countReservationJpaEntityByFlightIdSeatIdSetWithSameClientDifferentReservation(UUID reservationId,
			UUID flightId, Collection<UUID> seatIdsSet, UUID clientId) {
	
		return reservationJpaRepository.countReservationJpaEntityByFlightIdSeatIdSetWithSameClientDifferentReservation(reservationId, flightId, seatIdsSet, clientId);

	}	
	
}
