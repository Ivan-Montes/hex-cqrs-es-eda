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

@Document("clients")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ClientMongoEntity {
	
	@Id
	private ObjectId mongoId;
	
	@Indexed(unique = true)
	@Field( name = "client_id")
	private UUID clientId;	

	@Field( name = "client_name")
	private String name;	

	@Field( name = "client_lastname")
	private String lastname;
	
}
