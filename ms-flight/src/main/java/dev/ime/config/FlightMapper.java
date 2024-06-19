package dev.ime.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import dev.ime.application.dto.FlightDto;
import dev.ime.domain.model.Flight;
import dev.ime.infrastructure.entity.FlightMongoEntity;

@Component
public class FlightMapper {

	public FlightMapper() {
		super();
	}

	public Flight fromMongoToDomain(FlightMongoEntity mongoEntity) {
		
		Flight flight = new Flight();
		flight.setFlightId(mongoEntity.getFlightId());
		flight.setOrigin(mongoEntity.getOrigin());
		flight.setDestiny(mongoEntity.getDestiny());
		flight.setDepartureDate(mongoEntity.getDepartureDate());
		flight.setDepartureTime(mongoEntity.getDepartureTime());
		flight.setPlaneId(mongoEntity.getPlaneId());
		
		return flight;
	}

	public List<Flight> fromListMongoToListDomain(List<FlightMongoEntity> listMongo) {
		
		if ( listMongo == null ) {
			return new ArrayList<>();
		}
		
		return listMongo.stream()
			.map(this::fromMongoToDomain)
			.toList();	
	}
	
	public FlightDto fromDomainToDto(Flight domainEntity) {
		
		return new FlightDto(domainEntity.getFlightId(), 
						domainEntity.getOrigin(),
						domainEntity.getDestiny(),
						domainEntity.getDepartureDate().toString(),
						domainEntity.getDepartureTime().toString(),
						domainEntity.getPlaneId()
				);
		
	}

	public List<FlightDto> fromListDomainToListDto(List<Flight> listDomain) {
		
		if ( listDomain == null ) {
			return new ArrayList<>();
		}
		
		return listDomain.stream()
				.map(this::fromDomainToDto)
				.toList();
	}
	
}
