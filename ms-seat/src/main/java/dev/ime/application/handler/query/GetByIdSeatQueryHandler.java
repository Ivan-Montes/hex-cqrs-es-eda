package dev.ime.application.handler.query;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.exception.ResourceNotFoundException;
import dev.ime.application.usecase.query.GetByIdSeatQuery;
import dev.ime.domain.model.Seat;
import dev.ime.domain.port.outbound.GenericNoSqlReadRepositoryPort;
import dev.ime.domain.query.Query;
import dev.ime.domain.query.QueryHandler;

@Component
public class GetByIdSeatQueryHandler implements QueryHandler<Optional<Seat>>{

	private final GenericNoSqlReadRepositoryPort<Seat> genericNoSqlReadRepositoryPort;

	public GetByIdSeatQueryHandler(GenericNoSqlReadRepositoryPort<Seat> genericNoSqlReadRepositoryPort) {
		super();
		this.genericNoSqlReadRepositoryPort = genericNoSqlReadRepositoryPort;
	}

	@Override
	public Optional<Seat> handle(Query query) {
		
		if ( query instanceof GetByIdSeatQuery getByIdSeatQuery) {
			
			Seat seat = validateSeatExists(getByIdSeatQuery.seatId());
			
			return Optional.ofNullable(seat);
			
		}else {
			
			throw new IllegalArgumentException(ApplicationConstant.MSG_ILLEGAL_QUERY);
			
		}
		
	}

	private Seat validateSeatExists(UUID seatId) {

		Optional<Seat> optSeat = genericNoSqlReadRepositoryPort.findById(seatId);
		
		if ( optSeat.isEmpty() ) {
			 
			throw new ResourceNotFoundException(Map.of(ApplicationConstant.SEATID,String.valueOf(seatId)));
		}
		
		return optSeat.get();
	}	

}
