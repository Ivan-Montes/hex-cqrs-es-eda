package dev.ime.domain.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

public class Flight {

	private UUID flightId;
	private String origin;
	private String destiny;
	private LocalDate departureDate;
	private LocalTime departureTime;
	private UUID planeId;
	
	public Flight() {
		super();
	}
	public Flight(UUID flightId, String origin, String destiny, LocalDate departureDate, LocalTime departureTime,
			UUID planeId) {
		super();
		this.flightId = flightId;
		this.origin = origin;
		this.destiny = destiny;
		this.departureDate = departureDate;
		this.departureTime = departureTime;
		this.planeId = planeId;
	}
	public UUID getFlightId() {
		return flightId;
	}
	public void setFlightId(UUID flightId) {
		this.flightId = flightId;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getDestiny() {
		return destiny;
	}
	public void setDestiny(String destiny) {
		this.destiny = destiny;
	}
	public LocalDate getDepartureDate() {
		return departureDate;
	}
	public void setDepartureDate(LocalDate departureDate) {
		this.departureDate = departureDate;
	}
	public LocalTime getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(LocalTime departureTime) {
		this.departureTime = departureTime;
	}
	public UUID getPlaneId() {
		return planeId;
	}
	public void setPlaneId(UUID planeId) {
		this.planeId = planeId;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(departureDate, departureTime, destiny, flightId, origin, planeId);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Flight other = (Flight) obj;
		return Objects.equals(departureDate, other.departureDate) && Objects.equals(departureTime, other.departureTime)
				&& Objects.equals(destiny, other.destiny) && Objects.equals(flightId, other.flightId)
				&& Objects.equals(origin, other.origin) && Objects.equals(planeId, other.planeId);
	}
	
	@Override
	public String toString() {
		return "Flight [id=" + flightId + ", origin=" + origin + ", destiny=" + destiny + ", departureDate=" + departureDate
				+ ", departureTime=" + departureTime + ", planeId=" + planeId + "]";
	}
	
}
