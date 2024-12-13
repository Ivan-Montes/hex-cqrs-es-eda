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

import dev.ime.application.dto.ClientDto;
import dev.ime.config.ClientMapper;
import dev.ime.domain.model.Client;
import dev.ime.domain.port.inbound.ClientQueryControllerPort;
import dev.ime.domain.port.inbound.ClientQueryServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/clients")
@Tag(name = "Client", description="Client Operations")
public class ClientQueryController implements ClientQueryControllerPort<ClientDto>{

	private final ClientQueryServicePort clientQueryServicePort;
	private final ClientMapper clientMapper;
	
	public ClientQueryController(ClientQueryServicePort clientQueryServicePort, ClientMapper clientMapper) {
		super();
		this.clientQueryServicePort = clientQueryServicePort;
		this.clientMapper = clientMapper;
	}
	
	@GetMapping
	@Override
	@Operation(summary="Get a List of all Client, optionally paged", description="Get a List of all Client, @return an object Response with a List of DTO's")
	public ResponseEntity<List<ClientDto>> getAll(
	        @RequestParam(required = false)  Integer page,
	        @RequestParam(required = false)  Integer size) {

		Integer pageValue = page != null && page >= 0 ? page : 0;
		Integer sizeValue = size != null && size >= 1 ? size : 20;
	    
	    List<Client> list = clientQueryServicePort.getAll(pageValue, sizeValue);

		return ResponseEntity.ok( list.isEmpty()? Collections.emptyList():clientMapper.fromListDomainToListDto(list));
	}
	
	@GetMapping("/{id}")
	@Override
	@Operation(summary="Get a Client according to an Id", description="Get a Client according to an Id, @return an object Response with the entity required in a DTO")
	public ResponseEntity<ClientDto> getById(@PathVariable UUID id) {
		
		return ResponseEntity.ok(clientQueryServicePort.getById(id).map(clientMapper::fromDomainToDto).orElse(new ClientDto()));
	}

}
