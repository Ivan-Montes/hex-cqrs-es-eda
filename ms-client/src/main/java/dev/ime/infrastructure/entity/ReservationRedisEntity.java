package dev.ime.infrastructure.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RedisHash
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReservationRedisEntity {

	@Id
	private UUID reservationId;
	@Indexed
	private UUID clientId;
	private UUID flightId;
	private Set<UUID> seatIdsSet = new HashSet<>();
	
}
