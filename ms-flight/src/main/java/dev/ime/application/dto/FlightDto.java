package dev.ime.application.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import dev.ime.application.config.ApplicationConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record FlightDto(
		UUID flightId,
		@NotBlank @Pattern( regexp = ApplicationConstant.PATTERN_NAME_FULL ) String origin,
		@NotBlank @Pattern( regexp = ApplicationConstant.PATTERN_NAME_FULL ) String destiny,
		@NotBlank @Pattern( regexp = ApplicationConstant.PATTERN_LOCALDATE ) String departureDate,
		@NotBlank @Pattern( regexp = ApplicationConstant.PATTERN_LOCALTIME ) String departureTime,
		@NotNull UUID planeId
		) {
	
	public FlightDto() {
		this(UUID.fromString(ApplicationConstant.ZEROUUID), 
				ApplicationConstant.NODATA, 
				"", 
				"0000-00-00",
				"", 
				UUID.fromString(ApplicationConstant.ZEROUUID));
	}	
	
	public Boolean validateParseDepartureDate() {
		
		try {
			
			LocalDate.parse(this.departureDate);
			
		}catch(Exception ex){
			
			return false;
			
		}
		
		return true;
	
	}

	public Boolean validateParseDepartureTime() {
		
		try {
			
			LocalTime.parse(this.departureTime);
			
		}catch(Exception ex){
			
			return false;
			
		}
		
		return true;
	
	}	

}
