package dev.ime.infrastructure.entity;

import java.time.LocalDate;
import java.time.LocalTime;
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

@Document("flights")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class FlightMongoEntity {

	@Id
	@Field( name = "flight_mongo_id")
	private ObjectId mongoId;
	
	@Indexed(unique = true)
	@Field( name = "flight_id")
	private UUID flightId;
	
	@Field( name = "flight_origin")
	private String origin;
	
	@Field( name = "flight_destiny")
	private String destiny;	
	
	@Field( name = "flight_departure_date")
	private LocalDate departureDate;
	
	@Field( name = "flight_departure_time")
	private LocalTime departureTime;

	@Field( name = "plane_id")
	private UUID planeId;
	
}
