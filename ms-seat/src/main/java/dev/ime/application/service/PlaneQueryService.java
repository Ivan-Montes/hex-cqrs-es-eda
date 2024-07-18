package dev.ime.application.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import dev.ime.application.dispatch.PlaneQueryDispatcher;
import dev.ime.application.usecase.query.GetAllPlaneQuery;
import dev.ime.application.usecase.query.GetByIdPlaneQuery;
import dev.ime.domain.model.Plane;
import dev.ime.domain.port.inbound.GenericQueryServicePort;
import dev.ime.domain.query.QueryHandler;

@Service
public class PlaneQueryService implements GenericQueryServicePort<Plane>{

	private final PlaneQueryDispatcher planeQueryDispatcher;
	
	public PlaneQueryService(PlaneQueryDispatcher planeQueryDispatcher) {
		super();
		this.planeQueryDispatcher = planeQueryDispatcher;
	}

	@Override
	public List<Plane> getAll(Integer page, Integer size) {
		
		GetAllPlaneQuery getAllQuery = new GetAllPlaneQuery(page, size);
		QueryHandler<List<Plane>> queryHandler = planeQueryDispatcher.getQueryHandler(getAllQuery);
		
		return queryHandler.handle(getAllQuery);
	}

	@Override
	public Optional<Plane> getById(UUID id) {
		
		GetByIdPlaneQuery getByIdQuery = new GetByIdPlaneQuery(id);
		QueryHandler<Optional<Plane>> queryHandler = planeQueryDispatcher.getQueryHandler(getByIdQuery);
		
		return queryHandler.handle(getByIdQuery);
	}

}
