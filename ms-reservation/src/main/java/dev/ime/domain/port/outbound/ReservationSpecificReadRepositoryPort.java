package dev.ime.domain.port.outbound;

import java.util.Collection;
import java.util.UUID;


public interface ReservationSpecificReadRepositoryPort {

	int countReservationJpaEntityByFlightIdAndSeatIdSet(UUID flightId, Collection<UUID> seatIdsSet);
	int countReservationJpaEntityByFlightIdSeatIdSetButDifferentClient(UUID flightId, Collection<UUID> seatIdsSet, UUID clientId);
	int countReservationJpaEntityByFlightIdSeatIdSetWithSameClientDifferentReservation(UUID reservationId, UUID flightId, Collection<UUID> seatIdsSet, UUID clientId);
	
}
