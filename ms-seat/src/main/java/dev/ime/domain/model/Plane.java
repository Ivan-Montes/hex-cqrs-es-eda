package dev.ime.domain.model;


import java.util.Objects;
import java.util.UUID;

public class Plane {

	private UUID planeId;
	private String name;
	private Integer capacity;
	
	public Plane() {
		super();
	}

	public Plane(UUID planeId, String name, Integer capacity) {
		super();
		this.planeId = planeId;
		this.name = name;
		this.capacity = capacity;
	}

	public UUID getPlaneId() {
		return planeId;
	}

	public void setPlaneId(UUID planeId) {
		this.planeId = planeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}	

	@Override
	public int hashCode() {
		return Objects.hash(capacity, name, planeId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Plane other = (Plane) obj;
		return Objects.equals(capacity, other.capacity) && Objects.equals(name, other.name)
				&& Objects.equals(planeId, other.planeId);
	}

	@Override
	public String toString() {
		return "Plane [planeId=" + planeId + ", name=" + name + ", capacity=" + capacity + "]";
	}	
	
}
