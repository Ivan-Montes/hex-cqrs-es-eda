package dev.ime.application.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import dev.ime.application.dispatch.SeatQueryDispatcher;
import dev.ime.application.usecase.query.GetAllSeatQuery;
import dev.ime.application.usecase.query.GetByIdSeatQuery;
import dev.ime.domain.model.Seat;
import dev.ime.domain.port.inbound.GenericQueryServicePort;
import dev.ime.domain.query.QueryHandler;

@Service
public class SeatQueryService  implements GenericQueryServicePort<Seat>{

	private final SeatQueryDispatcher seatQueryDispatcher;
	
	public SeatQueryService(SeatQueryDispatcher seatQueryDispatcher) {
		super();
		this.seatQueryDispatcher = seatQueryDispatcher;
	}

	@Override
	public List<Seat> getAll(Integer page, Integer size) {
		
		GetAllSeatQuery getAllQuery = new GetAllSeatQuery(page, size);
		QueryHandler<List<Seat>> queryHandler = seatQueryDispatcher.getQueryHandler(getAllQuery);
		
		return queryHandler.handle(getAllQuery);
	}

	@Override
	public Optional<Seat> getById(UUID id) {
		
		GetByIdSeatQuery getByIdQuery = new GetByIdSeatQuery(id);
		QueryHandler<Optional<Seat>> queryHandler = seatQueryDispatcher.getQueryHandler(getByIdQuery);
		
		return queryHandler.handle(getByIdQuery);
	}

}
