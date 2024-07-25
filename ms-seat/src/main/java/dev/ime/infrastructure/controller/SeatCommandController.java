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

import dev.ime.application.dto.SeatDto;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.inbound.GenericCommandControllerPort;
import dev.ime.domain.port.inbound.GenericCommandServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/seats")
@Tag(name = "Seat", description="Seat Operations")
public class SeatCommandController implements GenericCommandControllerPort<SeatDto>{

	private final GenericCommandServicePort<SeatDto> genericCommandServicePort;
	
	public SeatCommandController(GenericCommandServicePort<SeatDto> genericCommandServicePort) {
		super();
		this.genericCommandServicePort = genericCommandServicePort;
	}

	@PostMapping
	@Override
	@Operation(summary="Create a new Seat", description="Create a new Seat, @return an object Response")
	public ResponseEntity<Event> create(@Valid @RequestBody SeatDto dto) {

		return createResponse( genericCommandServicePort.create(dto) );
	}

	@PutMapping("/{id}")
	@Override
	@Operation(summary="Update fields in a Seat", description="Update fields in a Seat, @return an object Response")
	public ResponseEntity<Event> update(@PathVariable UUID id, @Valid @RequestBody SeatDto dto) {

		return createResponse( genericCommandServicePort.update(id, dto) );
	}

	@DeleteMapping("/{id}")
	@Override
	@Operation(summary="Delete a Seat by its Id", description="Delete a Seat by its Id, @return an object Response")
	public ResponseEntity<Event> deleteById(@PathVariable UUID id) {

		return createResponse( genericCommandServicePort.deleteById(id) );
	}

	private ResponseEntity<Event> createResponse(Optional<Event> optEvent) {
		
		return optEvent.isPresent()? new ResponseEntity<>(optEvent.get(), HttpStatus.CREATED)
				:ResponseEntity.ok(null);
		
	}
	
}
