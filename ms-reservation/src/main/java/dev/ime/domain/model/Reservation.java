package dev.ime.domain.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Reservation {

	private UUID reservationId;
	private UUID clientId;
	private UUID flightId;
	private Set<UUID> seatIdsSet = new HashSet<>();
	
	public Reservation() {
		super();
	}

	public Reservation(UUID reservationId, UUID clientId, UUID flightId, Set<UUID> seatIdsSet) {
		super();
		this.reservationId = reservationId;
		this.clientId = clientId;
		this.flightId = flightId;
		this.seatIdsSet = seatIdsSet;
	}

	public UUID getReservationId() {
		return reservationId;
	}

	public void setReservationId(UUID reservationId) {
		this.reservationId = reservationId;
	}

	public UUID getClientId() {
		return clientId;
	}

	public void setClientId(UUID clientId) {
		this.clientId = clientId;
	}

	public UUID getFlightId() {
		return flightId;
	}

	public void setFlightId(UUID flightId) {
		this.flightId = flightId;
	}

	public Set<UUID> getSeatIdsSet() {
		return seatIdsSet;
	}

	public void setSeatIdsSet(Set<UUID> seatIdsSet) {
		this.seatIdsSet = seatIdsSet;
	}

	@Override
	public int hashCode() {
		return Objects.hash(clientId, flightId, reservationId, seatIdsSet);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reservation other = (Reservation) obj;
		return Objects.equals(clientId, other.clientId) && Objects.equals(flightId, other.flightId)
				&& Objects.equals(reservationId, other.reservationId) && Objects.equals(seatIdsSet, other.seatIdsSet);
	}

	@Override
	public String toString() {
		return "Reservation [reservationId=" + reservationId + ", clientId=" + clientId + ", flightId=" + flightId
				+ ", seatIdsSet=" + seatIdsSet + "]";
	}
	
}
