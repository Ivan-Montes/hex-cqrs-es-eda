package dev.ime.application.handler;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.exception.ResourceNotFoundException;
import dev.ime.application.usecase.GetByIdReservationQuery;
import dev.ime.domain.model.Reservation;
import dev.ime.domain.port.outbound.GenericReadRepositoryPort;
import dev.ime.domain.query.Query;
import dev.ime.domain.query.QueryHandler;

@Component
public class GetByIdReservationQueryHandler implements QueryHandler<Optional<Reservation>>{

	private final GenericReadRepositoryPort<Reservation> genericNoSqlReadRepositoryPort;
	
	public GetByIdReservationQueryHandler(GenericReadRepositoryPort<Reservation> genericNoSqlReadRepositoryPort) {
		super();
		this.genericNoSqlReadRepositoryPort = genericNoSqlReadRepositoryPort;
	}

	@Override
	public Optional<Reservation> handle(Query query) {
		
		if ( query instanceof GetByIdReservationQuery getByIdReservationQuery) {
	
			Reservation reservation = validateReservationExists(getByIdReservationQuery.reservationId());
			
			return Optional.ofNullable(reservation);
			
		}else {
			
			throw new IllegalArgumentException(ApplicationConstant.MSG_ILLEGAL_QUERY);
			
		}
	}

	private Reservation validateReservationExists(UUID reservationId) {

		Optional<Reservation> optReservation = genericNoSqlReadRepositoryPort.findById(reservationId);
		
		if ( optReservation.isEmpty() ) {
			 
			throw new ResourceNotFoundException(Map.of(ApplicationConstant.RESERVID, reservationId.toString()));
		}
		
		return optReservation.get();
		
	}
	
}
