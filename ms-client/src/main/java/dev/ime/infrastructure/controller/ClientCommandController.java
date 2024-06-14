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

import dev.ime.application.dto.ClientDto;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.inbound.ClientCommandControllerPort;
import dev.ime.domain.port.inbound.ClientCommandServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clients")
@Tag( name="Client", description="Client Operations")
public class ClientCommandController implements ClientCommandControllerPort<ClientDto> {

	private final ClientCommandServicePort<ClientDto> clientCommandServicePort;
	
	public ClientCommandController(ClientCommandServicePort<ClientDto> clientCommandServicePort) {
		super();
		this.clientCommandServicePort = clientCommandServicePort;
	}

	@PostMapping
	@Override
	@Operation(summary="Create a new Client", description="Create a new Client, @return an object Response")
	public ResponseEntity<Event> create(@Valid @RequestBody ClientDto dto) {
		
		return createResponse( clientCommandServicePort.create(dto) );
	}

	@PutMapping("/{id}")
	@Override
	@Operation(summary="Update fields in a Client", description="Update fields in a Client, @return an object Response")
	public ResponseEntity<Event> update(@PathVariable UUID id, @Valid @RequestBody ClientDto dto) {
		
		return createResponse( clientCommandServicePort.update(id, dto) );
	}

	@DeleteMapping("/{id}")
	@Override
	@Operation(summary="Delete a Client by its Id", description="Delete a Client by its Id, @return an object Response")
	public ResponseEntity<Event> deleteById(@PathVariable UUID id) {		
		
		return createResponse( clientCommandServicePort.deleteById(id) );
	}

	private ResponseEntity<Event> createResponse(Optional<Event> optEvent) {
		
		return optEvent.isPresent()? new ResponseEntity<>(optEvent.get(), HttpStatus.CREATED)
				:ResponseEntity.ok(null);
		
	}

}
