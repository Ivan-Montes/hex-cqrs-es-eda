package dev.ime.infrastructure.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservations")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReservationJpaEntity {
	
	@Id
	@Column( name = "reservation_id" )
	private UUID reservationId;

	@Column( name = "client_id", nullable = false)
	private UUID clientId;

	@Column( name = "flight_id", nullable = false)
	private UUID flightId;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
	        name="seats",
	        joinColumns=@JoinColumn(name="reservation_id")
	  )
	@Column( name = "seat_id")
	private Set<UUID> seatIdsSet = new HashSet<>();
	
}
