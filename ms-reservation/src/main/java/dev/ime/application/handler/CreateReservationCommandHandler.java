package dev.ime.application.handler;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.event.ReservationCreatedEvent;
import dev.ime.application.exception.EntityAssociatedException;
import dev.ime.application.exception.ResourceNotFoundException;
import dev.ime.application.usecase.CreateReservationCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.BaseProjectorPort;
import dev.ime.domain.port.outbound.DatabaseSequencePort;
import dev.ime.domain.port.outbound.EventNoSqlWriteRepositoryPort;
import dev.ime.domain.port.outbound.FlightSpecificProjectorPort;
import dev.ime.domain.port.outbound.ReservationSpecificReadRepositoryPort;
import dev.ime.domain.port.outbound.SeatSpecificProjectorPort;

@Component
public class CreateReservationCommandHandler implements CommandHandler<Optional<Event>>{

	private final EventNoSqlWriteRepositoryPort eventNoSqlWriteRepositoryPort;
	private final DatabaseSequencePort databaseSequencePort;	
	private final BaseProjectorPort clientBaseProjectorPort;		
	private final BaseProjectorPort flightBaseProjectorPort;
	private final BaseProjectorPort seatBaseProjectorPort;
	private final ReservationSpecificReadRepositoryPort reservationSpecificReadRepositoryPort;
	private final FlightSpecificProjectorPort flightSpecificProjectorPort;
	private final SeatSpecificProjectorPort seatSpecificProjectorPort;
	
	public CreateReservationCommandHandler(CreateReservationCommandHandlerBuilder builder) {
		super();
		this.eventNoSqlWriteRepositoryPort = builder.eventNoSqlWriteRepositoryPort;
		this.databaseSequencePort = builder.databaseSequencePort;
		this.clientBaseProjectorPort = builder.clientBaseProjectorPort;
		this.flightBaseProjectorPort = builder.flightBaseProjectorPort;
		this.seatBaseProjectorPort = builder.seatBaseProjectorPort;
		this.reservationSpecificReadRepositoryPort = builder.reservationSpecificReadRepositoryPort;
		this.flightSpecificProjectorPort = builder.flightSpecificProjectorPort;
		this.seatSpecificProjectorPort = builder.seatSpecificProjectorPort;
	}

	@Override
	public Optional<Event> handle(Command command) {
		
		if ( command instanceof CreateReservationCommand createReservationCommand ) {
			
			validateClientExists(createReservationCommand.clientId());
			validateFlightExists(createReservationCommand.flightId());
			validateSeatExists(createReservationCommand.seatIdsSet());
			validateFlightHasTheseSeats(createReservationCommand);
			validateSeatAlreadyBooked(createReservationCommand);
			
			ReservationCreatedEvent event = createReservationCreatedEvent(createReservationCommand);
			
			return eventNoSqlWriteRepositoryPort.save(event);
			
		}else {
			
			throw new IllegalArgumentException(ApplicationConstant.MSG_ILLEGAL_COMMAND);
		
		}		
	}

	private ReservationCreatedEvent createReservationCreatedEvent(CreateReservationCommand createReservationCommand) {
		
		return new ReservationCreatedEvent(
				databaseSequencePort.generateSequence(ApplicationConstant.SEQ_GEN),
				createReservationCommand.reservationId(),
				createReservationCommand.clientId(),
				createReservationCommand.flightId(),
				createReservationCommand.seatIdsSet()
				);
		
	}

	private void validateClientExists(UUID clientId) {

		if ( !clientBaseProjectorPort.existsById(clientId) ) {
			 
			throw new ResourceNotFoundException(Map.of(ApplicationConstant.CLIENTID, clientId.toString() ));
		
		}		
	}

	private void validateFlightExists(UUID flightId) {

		if ( !flightBaseProjectorPort.existsById(flightId) ) {
			 
			throw new ResourceNotFoundException(Map.of(ApplicationConstant.FLIGHTID, flightId.toString() ));
		
		}		
	}

	private void validateSeatExists(Set<UUID> seatIdsSet) {
		
		if ( seatIdsSet.stream()
				.map( seatBaseProjectorPort::existsById )
				.anyMatch( bool -> bool.equals(false) ) ) {
			 
			throw new ResourceNotFoundException(Map.of(ApplicationConstant.SEATID, seatIdsSet.toString() ));
		
		}
	}

	private void validateFlightHasTheseSeats(CreateReservationCommand command) {
		
		UUID planeIdFound = findPlaneRegardingFlight(command.flightId());
		List<UUID> seatRedisEntityList = findSeatByPlaneId(planeIdFound);
		compareSeats(command.seatIdsSet(), seatRedisEntityList);
		
	}

	private void compareSeats(Set<UUID> seatIdsSet, List<UUID> seatRedisEntityList) {
		
		if ( !seatIdsSet.stream()
				.allMatch( seatRedisEntityList::contains) ) {
			 
			throw new ResourceNotFoundException(Map.of(ApplicationConstant.SEATID, seatIdsSet.toString() ));
		
		}
	}

	private List<UUID> findSeatByPlaneId(UUID planeIdFound) {
		
		List<UUID> seatRedisEntityUUIDList = seatSpecificProjectorPort.findSeatByPlaneId(planeIdFound);
		
		if ( seatRedisEntityUUIDList.isEmpty() ) {
			
			throw new ResourceNotFoundException(Map.of(ApplicationConstant.PLANEID, planeIdFound.toString() ));

		}
		
		return seatRedisEntityUUIDList;
		
	}

	private UUID findPlaneRegardingFlight(UUID flightId) {
		
		UUID planeIdFound = flightSpecificProjectorPort.findPlaneRegardingFlight(flightId);
		
		if ( planeIdFound == null ) {
			 
			throw new ResourceNotFoundException(Map.of(ApplicationConstant.FLIGHTID, flightId.toString() ));
		
		}
		
		return planeIdFound;
		
	}

	private void validateSeatAlreadyBooked(CreateReservationCommand command) {
		
		if ( reservationSpecificReadRepositoryPort.countReservationJpaEntityByFlightIdAndSeatIdSet( command.flightId(), command.seatIdsSet() ) > 0 ) {
			 
			throw new EntityAssociatedException(Map.of(ApplicationConstant.SEATID, command.seatIdsSet().toString() ));
		
		}		
	}
	
	public static class CreateReservationCommandHandlerBuilder{
		
		private EventNoSqlWriteRepositoryPort eventNoSqlWriteRepositoryPort;
		private DatabaseSequencePort databaseSequencePort;
		private BaseProjectorPort clientBaseProjectorPort;		
		private BaseProjectorPort flightBaseProjectorPort;
		private BaseProjectorPort seatBaseProjectorPort;
		private ReservationSpecificReadRepositoryPort reservationSpecificReadRepositoryPort;
		private FlightSpecificProjectorPort flightSpecificProjectorPort;
		private SeatSpecificProjectorPort seatSpecificProjectorPort;		

		public CreateReservationCommandHandlerBuilder() {
			super();
		}

		public CreateReservationCommandHandlerBuilder setEventNoSqlWriteRepositoryPort(EventNoSqlWriteRepositoryPort eventNoSqlWriteRepositoryPort) {
			this.eventNoSqlWriteRepositoryPort = eventNoSqlWriteRepositoryPort;
			return this;
		}

		public CreateReservationCommandHandlerBuilder setDatabaseSequencePort(DatabaseSequencePort databaseSequencePort) {
			this.databaseSequencePort = databaseSequencePort;
			return this;
		}
		
		public CreateReservationCommandHandlerBuilder setClientBaseProjectorPort(@Qualifier("clientRedisProjectorAdapter")BaseProjectorPort clientBaseProjectorPort) {
			this.clientBaseProjectorPort = clientBaseProjectorPort;
			return this;
		}
		
		public CreateReservationCommandHandlerBuilder setFlightBaseProjectorPort(@Qualifier("flightRedisProjectorAdapter")BaseProjectorPort flightBaseProjectorPort) {
			this.flightBaseProjectorPort = flightBaseProjectorPort;
			return this;
		}
		
		public CreateReservationCommandHandlerBuilder setSeatBaseProjectorPort(@Qualifier("seatRedisProjectorAdapter")BaseProjectorPort seatBaseProjectorPort) {
			this.seatBaseProjectorPort = seatBaseProjectorPort;
			return this;
		}

		public CreateReservationCommandHandlerBuilder setReservationSpecificReadRepositoryPort(ReservationSpecificReadRepositoryPort reservationSpecificReadRepositoryPort) {
			this.reservationSpecificReadRepositoryPort = reservationSpecificReadRepositoryPort;
			return this;
		}

		public CreateReservationCommandHandlerBuilder setFlightSpecificProjectorPort(FlightSpecificProjectorPort flightSpecificProjectorPort) {
			this.flightSpecificProjectorPort = flightSpecificProjectorPort;
			return this;
		}

		public CreateReservationCommandHandlerBuilder setSeatSpecificProjectorPort(SeatSpecificProjectorPort seatSpecificProjectorPort) {
			this.seatSpecificProjectorPort = seatSpecificProjectorPort;
			return this;
		}		

		public CreateReservationCommandHandler build() {
			return new CreateReservationCommandHandler(this);
		}
		
	}
	
}
