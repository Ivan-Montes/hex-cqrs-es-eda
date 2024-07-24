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

@Document("planes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PlaneMongoEntity {

	@Id
	@Field( name = "plane_mongo_id")
	private ObjectId mongoId;
	
	@Indexed(unique = true)
	@Field( name = "plane_id")
	private UUID planeId;

	@Field( name = "plane_name")
	private String name;	

	@Field( name = "plane_capacity")
	private Integer capacity;	
	
}
