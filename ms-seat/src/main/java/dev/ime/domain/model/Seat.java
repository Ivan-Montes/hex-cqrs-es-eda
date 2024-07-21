package dev.ime.domain.model;

import java.util.Objects;
import java.util.UUID;

public class Seat {

	private UUID seatId;
	private String seatNumber;
	private Plane plane;
	
	public Seat() {
		super();
	}
	
	public Seat(UUID seatId, String seatNumber, Plane plane) {
		super();
		this.seatId = seatId;
		this.seatNumber = seatNumber;
		this.plane = plane;
	}
	
	public UUID getSeatId() {
		return seatId;
	}
	public void setSeatId(UUID seatId) {
		this.seatId = seatId;
	}
	public String getSeatNumber() {
		return seatNumber;
	}
	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}
	public Plane getPlane() {
		return plane;
	}
	public void setPlane(Plane plane) {
		this.plane = plane;
	}
	@Override
	public int hashCode() {
		return Objects.hash(plane, seatId, seatNumber);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Seat other = (Seat) obj;
		return Objects.equals(plane, other.plane) && Objects.equals(seatId, other.seatId)
				&& Objects.equals(seatNumber, other.seatNumber);
	}
	@Override
	public String toString() {
		return "Seat [seatId=" + seatId + ", seatNumber=" + seatNumber + ", plane=" + plane + "]";
	}	
	
}
