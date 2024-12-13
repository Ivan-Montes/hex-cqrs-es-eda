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

import dev.ime.application.dto.SeatDto;
import dev.ime.config.SeatMapper;
import dev.ime.domain.model.Seat;
import dev.ime.domain.port.inbound.GenericQueryControllerPort;
import dev.ime.domain.port.inbound.GenericQueryServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/seats")
@Tag(name = "Seat", description="Seat Operations")
public class SeatQueryController implements GenericQueryControllerPort<SeatDto>{

	private final GenericQueryServicePort<Seat> genericQueryServicePort;
	private final SeatMapper seatMapper;
	
	public SeatQueryController(GenericQueryServicePort<Seat> genericQueryServicePort, SeatMapper seatMapper) {
		super();
		this.genericQueryServicePort = genericQueryServicePort;
		this.seatMapper = seatMapper;
	}

	@GetMapping
	@Override
	@Operation(summary="Get a List of all Seat, optionally paged", description="Get a List of all Seat, @return an object Response with a List of DTO's")
	public ResponseEntity<List<SeatDto>> getAll(
	        @RequestParam(required = false)  Integer page,
	        @RequestParam(required = false)  Integer size) {

		Integer pageValue = page != null && page >= 0 ? page : 0;
		Integer sizeValue = size != null && size >= 1 ? size : 20;
	    
		List<Seat> list = genericQueryServicePort.getAll(pageValue, sizeValue);
		
		return ResponseEntity.ok( list.isEmpty()? Collections.emptyList():seatMapper.fromListDomainToListDto(list));
	}

	@GetMapping("/{id}")
	@Override
	@Operation(summary="Get a Seat according to an Id", description="Get a Seat according to an Id, @return an object Response with the entity required in a DTO")
	public ResponseEntity<SeatDto> getById(@PathVariable UUID id) {
		
		return ResponseEntity.ok(genericQueryServicePort.getById(id).map(seatMapper::fromDomainToDto).orElse(new SeatDto()));

	}

}
