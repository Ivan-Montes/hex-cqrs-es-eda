package dev.ime.infrastructure.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Document(collection = "database_sequences")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Generated
public class DatabaseSequence {
	
	@Id
    private String id;

    private Long seq;
    
}
