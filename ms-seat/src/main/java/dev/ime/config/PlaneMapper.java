package dev.ime.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import dev.ime.application.dto.PlaneDto;
import dev.ime.domain.model.Plane;
import dev.ime.infrastructure.entity.PlaneMongoEntity;

@Component
public class PlaneMapper {

	public Plane fromMongoToDomain(PlaneMongoEntity mongoEntity) {
		
		Plane plane = new Plane();
		plane.setPlaneId(mongoEntity.getPlaneId());
		plane.setName(mongoEntity.getName());
		plane.setCapacity(mongoEntity.getCapacity());	
		
		return plane;
	}
	
	public List<Plane> fromListMongoToListDomain(List<PlaneMongoEntity> listMongo){
		
		if ( listMongo == null ) {
			return new ArrayList<>();
		}
		
		return listMongo.stream()
			.map(this::fromMongoToDomain)
			.toList();	
		
	}
	
	public PlaneDto fromDomainToDto(Plane domainEntity) {
		
		return new PlaneDto(
				domainEntity.getPlaneId(),
				domainEntity.getName(),
				domainEntity.getCapacity()
				);				
		
	}

	public List<PlaneDto> fromListDomainToListDto(List<Plane> listDomain) {
		
		if ( listDomain == null ) {
			return new ArrayList<>();
		}
		
		return listDomain.stream()
				.map(this::fromDomainToDto)
				.toList();
	}
	
}
