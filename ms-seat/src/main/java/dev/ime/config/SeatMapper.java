package dev.ime.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import dev.ime.application.dto.SeatDto;
import dev.ime.domain.model.Plane;
import dev.ime.domain.model.Seat;
import dev.ime.infrastructure.entity.PlaneMongoEntity;
import dev.ime.infrastructure.entity.SeatMongoEntity;

@Component
public class SeatMapper {

	public Seat fromMongoToDomain(SeatMongoEntity mongoSeat, PlaneMongoEntity mongoPlane) {
		
		Seat seat = new Seat();
		seat.setSeatId(mongoSeat.getSeatId());
		seat.setSeatNumber(mongoSeat.getSeatNumber());
		seat.setPlane(new Plane(
				mongoPlane.getPlaneId(),
				mongoPlane.getName(),
				mongoPlane.getCapacity())
				);
		
		return seat;
	}
	
	public SeatDto fromDomainToDto(Seat domainEntity) {
		
		return new SeatDto(
				domainEntity.getSeatId(),
				domainEntity.getSeatNumber(),
				domainEntity.getPlane().getPlaneId());
	}

	public List<SeatDto> fromListDomainToListDto(List<Seat> listDomain) {
		
		if ( listDomain == null ) {
			return new ArrayList<>();
		}
		
		return listDomain.stream()
				.map(this::fromDomainToDto)
				.toList();
	}

}
