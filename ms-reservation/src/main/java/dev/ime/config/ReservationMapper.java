package dev.ime.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import dev.ime.application.dto.ReservationDto;
import dev.ime.domain.model.Reservation;
import dev.ime.infrastructure.entity.ReservationJpaEntity;

@Component
public class ReservationMapper {

	public Reservation fromJpaToDomain(ReservationJpaEntity jpaEntity) {
		
		return new Reservation(
				jpaEntity.getReservationId(),
				jpaEntity.getClientId(),
				jpaEntity.getFlightId(),
				jpaEntity.getSeatIdsSet()
				);
	}

	public List<Reservation> fromListJpaToListDomain(List<ReservationJpaEntity> listJpa) {
		
		if ( listJpa == null ) {
			return new ArrayList<>();
		}
		
		return listJpa.stream()
				.map(this::fromJpaToDomain)
				.toList();
	}
	
	public ReservationDto fromDomainToDto(Reservation domainEntity) {
		
		return new ReservationDto(
				domainEntity.getReservationId(),
				domainEntity.getClientId(),
				domainEntity.getFlightId(),
				domainEntity.getSeatIdsSet()
				);
		
	}

	public List<ReservationDto> fromListDomainToListDto(List<Reservation> listDomain) {
		
		if ( listDomain == null ) {
			return new ArrayList<>();
		}
		
		return listDomain.stream()
				.map(this::fromDomainToDto)
				.toList();
	}
	
}
