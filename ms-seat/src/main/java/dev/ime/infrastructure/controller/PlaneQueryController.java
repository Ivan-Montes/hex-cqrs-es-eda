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

import dev.ime.application.dto.PlaneDto;
import dev.ime.config.PlaneMapper;
import dev.ime.domain.model.Plane;
import dev.ime.domain.port.inbound.GenericQueryControllerPort;
import dev.ime.domain.port.inbound.GenericQueryServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/planes")
@Tag(name = "Plane", description="Plane Operations")
public class PlaneQueryController implements GenericQueryControllerPort<PlaneDto>{

	private final GenericQueryServicePort<Plane> genericQueryServicePort;
	private final PlaneMapper planeMapper;	
	
	public PlaneQueryController(GenericQueryServicePort<Plane> genericQueryServicePort, PlaneMapper planeMapper) {
		super();
		this.genericQueryServicePort = genericQueryServicePort;
		this.planeMapper = planeMapper;
	}
	
	@GetMapping
	@Override
	@Operation(summary="Get a List of all Plane, optionally paged", description="Get a List of all Plane, @return an object Response with a List of DTO's")
	public ResponseEntity<List<PlaneDto>> getAll(
	        @RequestParam(value = "page", required = false)  Integer page,
	        @RequestParam(value = "size", required = false)  Integer size) {

		Integer pageValue = page != null && page >= 0 ? page : 0;
		Integer sizeValue = size != null && size >= 1 ? size : 20;
	    
		List<Plane> list = genericQueryServicePort.getAll(pageValue, sizeValue);
		
		return ResponseEntity.ok( list.isEmpty()? Collections.emptyList():planeMapper.fromListDomainToListDto(list));
	}
	
	@GetMapping("/{id}")
	@Override
	@Operation(summary="Get a Plane according to an Id", description="Get a Plane according to an Id, @return an object Response with the entity required in a DTO")
	public ResponseEntity<PlaneDto> getById(@PathVariable UUID id) {
		
		return ResponseEntity.ok(genericQueryServicePort.getById(id).map(planeMapper::fromDomainToDto).orElse(new PlaneDto()));

	}
	
}
