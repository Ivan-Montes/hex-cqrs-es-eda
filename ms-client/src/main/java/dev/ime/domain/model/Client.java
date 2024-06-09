package dev.ime.domain.model;

import java.util.Objects;
import java.util.UUID;

public class Client {

	private UUID clientId;
	private String name;
	private String lastname;
	
	public Client() {
		super();
	}

	public Client(UUID clientId, String name, String lastname) {
		super();
		this.clientId = clientId;
		this.name = name;
		this.lastname = lastname;
	}

	public UUID getClientId() {
		return clientId;
	}

	public void setClientId(UUID clientId) {
		this.clientId = clientId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	@Override
	public int hashCode() {
		return Objects.hash(clientId, lastname, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		return Objects.equals(clientId, other.clientId) && Objects.equals(lastname, other.lastname)
				&& Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "Client [id=" + clientId + ", name=" + name + ", lastname=" + lastname + "]";
	}	
	
}
