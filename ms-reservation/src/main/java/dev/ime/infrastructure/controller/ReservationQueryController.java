package dev.ime.infrastructure.controller;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.ime.application.dto.ReservationDto;
import dev.ime.config.ReservationMapper;
import dev.ime.domain.model.Reservation;
import dev.ime.domain.port.inbound.GenericQueryControllerPort;
import dev.ime.domain.port.inbound.GenericQueryServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/reservations")
@Tag(name = "Reservation", description="Reservation Operations")
public class ReservationQueryController implements GenericQueryControllerPort<ReservationDto>{

	private final GenericQueryServicePort<Reservation> genericQueryServicePort;
	private final ReservationMapper reservationMapper;
	
	public ReservationQueryController(GenericQueryServicePort<Reservation> genericQueryServicePort,
			ReservationMapper reservationMapper) {
		super();
		this.genericQueryServicePort = genericQueryServicePort;
		this.reservationMapper = reservationMapper;
	}


	@GetMapping
	@Override
	@Operation(summary="Get a List of all Reservation, optionally paged", description="Get a List of all Reservation, @return an object Response with a List of DTO's")
	public ResponseEntity<List<ReservationDto>> getAll(
	        @RequestParam(value = "page", required = false)  Integer page,
	        @RequestParam(value = "size", required = false)  Integer size) {

		Integer pageValue = page != null && page >= 0 ? page : 0;
		Integer sizeValue = size != null && size >= 1 ? size : 20;
	
		List<Reservation> list = genericQueryServicePort.getAll(pageValue, sizeValue);
		
		return ResponseEntity.ok( list.isEmpty()? Collections.emptyList():reservationMapper.fromListDomainToListDto(list));
		
	}

	@GetMapping("/{id}")
	@Override
	@Operation(summary="Get a Reservation according to an Id", description="Get a Reservation according to an Id, @return an object Response with the entity required in a DTO")
	public ResponseEntity<ReservationDto> getById(@PathVariable UUID id) {
		
		return ResponseEntity.ok(genericQueryServicePort.getById(id).map(reservationMapper::fromDomainToDto).orElse(new ReservationDto()));

	}
	
}
