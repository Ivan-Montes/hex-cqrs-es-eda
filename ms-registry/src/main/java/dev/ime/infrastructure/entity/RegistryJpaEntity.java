package dev.ime.infrastructure.entity;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "registries")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegistryJpaEntity {

	@Id
	@Column( name = "event_id", nullable = false)
	private UUID eventId;
	
	@Column( name = "event_category", nullable = false)
	private String eventCategory;

	@Column( name = "event_type", nullable = false)
	private String eventType;

	@Column( name = "time_instant", nullable = false)
	private Instant timeInstant;

	@Column( nullable = false )
	private Long sequence;  
	
	@Column( name = "event_data", columnDefinition = "JSONB", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String eventData;
	
}
