package dev.ime.application.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import dev.ime.application.dispatch.ReservationQueryDispatcher;
import dev.ime.application.usecase.GetAllReservationQuery;
import dev.ime.application.usecase.GetByIdReservationQuery;
import dev.ime.domain.model.Reservation;
import dev.ime.domain.port.inbound.GenericQueryServicePort;
import dev.ime.domain.query.QueryHandler;

@Service
public class ReservationQueryService implements GenericQueryServicePort<Reservation>{

	private final ReservationQueryDispatcher reservationQueryDispatcher;
	
	public ReservationQueryService(ReservationQueryDispatcher reservationQueryDispatcher) {
		super();
		this.reservationQueryDispatcher = reservationQueryDispatcher;
	}

	@Override
	public List<Reservation> getAll(Integer page, Integer size) {
		
		GetAllReservationQuery getAllQuery = new GetAllReservationQuery(page, size);
		QueryHandler<List<Reservation>> queryHandler = reservationQueryDispatcher.getQueryHandler(getAllQuery);
		
		return queryHandler.handle(getAllQuery);
		
	}

	@Override
	public Optional<Reservation> getById(UUID id) {
		
		GetByIdReservationQuery getByIdQuery = new GetByIdReservationQuery(id);
		QueryHandler<Optional<Reservation>> queryHandler = reservationQueryDispatcher.getQueryHandler(getByIdQuery);
		
		return queryHandler.handle(getByIdQuery);
		
	}

}
