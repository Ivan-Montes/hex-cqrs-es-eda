package dev.ime.infrastructure.entity;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RedisHash
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlaneRedisEntity {

	@Id
	private UUID planeId;
	
}
