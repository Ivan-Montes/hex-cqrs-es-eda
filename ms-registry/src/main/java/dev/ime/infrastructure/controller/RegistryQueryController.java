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

import dev.ime.application.dto.RegistryDto;
import dev.ime.config.RegistryMapper;
import dev.ime.domain.model.Registry;
import dev.ime.domain.port.inbound.GenericQueryControllerPort;
import dev.ime.domain.port.inbound.GenericQueryServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/registries")
@Tag(name = "Registry", description="Registry Operations")
public class RegistryQueryController implements GenericQueryControllerPort<RegistryDto>{

	private final GenericQueryServicePort<Registry> genericQueryServicePort;
	private final RegistryMapper registryMapper;
	
	public RegistryQueryController(GenericQueryServicePort<Registry> genericQueryServicePort,
			RegistryMapper registryMapper) {
		super();
		this.genericQueryServicePort = genericQueryServicePort;
		this.registryMapper = registryMapper;
	}

	@GetMapping
	@Override
	@Operation(summary="Get a List of all Registry, optionally paged", description="Get a List of all Registry, @return an object Response with a List of DTO's")
	public ResponseEntity<List<RegistryDto>> getAll(
	        @RequestParam(required = false)  Integer page,
	        @RequestParam(required = false)  Integer size) {
		
		Integer pageValue = page != null && page >= 0 ? page : 0;
		Integer sizeValue = size != null && size >= 1 ? size : 20;
	
		List<Registry> list = genericQueryServicePort.getAll(pageValue, sizeValue);		
		
		return ResponseEntity.ok( list.isEmpty()? Collections.emptyList():registryMapper.fromListDomainToListDto(list));

	}

	@GetMapping("/{id}")
	@Override
	@Operation(summary="Get a Registry according to an Id", description="Get a Registry according to an Id, @return an object Response with the entity required in a DTO")
	public ResponseEntity<RegistryDto> getById(@PathVariable UUID id) {
		
		return ResponseEntity.ok(genericQueryServicePort.getById(id).map(registryMapper::fromDomainToDto).orElse(new RegistryDto()));

	}

}
