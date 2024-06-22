package dev.ime.infrastructure.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.ime.application.dto.FlightDto;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.inbound.FlightCommandControllerPort;
import dev.ime.domain.port.inbound.FlightCommandServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/flights")
@Tag(name = "Flight", description="Flight Operations")
public class FlightCommandController implements FlightCommandControllerPort<FlightDto>{

	private final FlightCommandServicePort<FlightDto> flightCommandServicePort;	
	
	public FlightCommandController(FlightCommandServicePort<FlightDto> flightCommandSerivcePort) {
		super();
		this.flightCommandServicePort = flightCommandSerivcePort;
	}

	@PostMapping
	@Override
	@Operation(summary="Create a new Flight", description="Create a new Flight, @return an object Response")
	public ResponseEntity<Event> create(@Valid @RequestBody FlightDto dto) {
		
		return createResponse( flightCommandServicePort.create(dto) );
	}

	@PutMapping("/{id}")
	@Override
	@Operation(summary="Update fields in a Flight", description="Update fields in a Flight, @return an object Response")
	public ResponseEntity<Event> update(@PathVariable UUID id, @Valid @RequestBody FlightDto dto) {
		
		return createResponse( flightCommandServicePort.update(id, dto) );
	}

	@DeleteMapping("/{id}")
	@Override
	@Operation(summary="Delete a Flight by its Id", description="Delete a Flight by its Id, @return an object Response")
	public ResponseEntity<Event> deleteById(@PathVariable UUID id) {
		
		return createResponse( flightCommandServicePort.deleteById(id) );
	}

	private ResponseEntity<Event> createResponse(Optional<Event> optEvent) {
		
		return optEvent.isPresent()? new ResponseEntity<>(optEvent.get(), HttpStatus.CREATED)
				:ResponseEntity.ok(null);
		
	}
	
}
