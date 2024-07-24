package dev.ime.infrastructure.entity;

import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document("seats")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SeatMongoEntity {

	@Id
	@Field( name = "seat_mongo_id")
	private ObjectId mongoId;
	
	@Indexed(unique = true)
	@Field( name = "seat_id")
	private UUID seatId;
	
	@Field( name = "seat_number")
	private String seatNumber;
	
	@Field( name = "plane_id")
	@Indexed
	private UUID planeId;
	
}
