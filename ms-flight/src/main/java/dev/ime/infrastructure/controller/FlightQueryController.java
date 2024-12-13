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

import dev.ime.application.dto.FlightDto;
import dev.ime.config.FlightMapper;
import dev.ime.domain.model.Flight;
import dev.ime.domain.port.inbound.FlightQueryControllerPort;
import dev.ime.domain.port.inbound.FlightQueryServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/flights")
@Tag(name = "Flight", description="Flight Operations")
public class FlightQueryController implements FlightQueryControllerPort<FlightDto>{

	private final FlightQueryServicePort flightQueryServicePort;
	private final FlightMapper flightMapper;
	
	public FlightQueryController(FlightQueryServicePort flightQueryServicePort, FlightMapper flightMapper) {
		super();
		this.flightQueryServicePort = flightQueryServicePort;
		this.flightMapper = flightMapper;
	}

	@GetMapping
	@Override
	@Operation(summary="Get a List of all Flight, optionally paged", description="Get a List of all Flight, @return an object Response with a List of DTO's")
	public ResponseEntity<List<FlightDto>> getAll(
	        @RequestParam(required = false)  Integer page,
	        @RequestParam(required = false)  Integer size) {

		Integer pageValue = page != null && page >= 0 ? page : 0;
		Integer sizeValue = size != null && size >= 1 ? size : 20;
		
		List<Flight> list = flightQueryServicePort.getAll(pageValue, sizeValue);
		
		return ResponseEntity.ok( list.isEmpty()? Collections.emptyList():flightMapper.fromListDomainToListDto(list));
	}

	@GetMapping("/{id}")
	@Override
	@Operation(summary="Get a Flight according to an Id", description="Get a Flight according to an Id, @return an object Response with the entity required in a DTO")
	public ResponseEntity<FlightDto> getById(@PathVariable UUID id) {
		
		return ResponseEntity.ok(flightQueryServicePort.getById(id).map(flightMapper::fromDomainToDto).orElse(new FlightDto()));
	}
	
	
}
