package dev.ime.application.handler;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.event.ReservationUpdatedEvent;
import dev.ime.application.exception.EntityAssociatedException;
import dev.ime.application.exception.ResourceNotFoundException;
import dev.ime.application.usecase.UpdateReservationCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;
import dev.ime.domain.event.Event;
import dev.ime.domain.model.Reservation;
import dev.ime.domain.port.outbound.BaseProjectorPort;
import dev.ime.domain.port.outbound.DatabaseSequencePort;
import dev.ime.domain.port.outbound.EventNoSqlWriteRepositoryPort;
import dev.ime.domain.port.outbound.FlightSpecificProjectorPort;
import dev.ime.domain.port.outbound.GenericReadRepositoryPort;
import dev.ime.domain.port.outbound.ReservationSpecificReadRepositoryPort;
import dev.ime.domain.port.outbound.SeatSpecificProjectorPort;

@Component
public class UpdateReservationCommandHandler implements CommandHandler<Optional<Event>>{

	private final EventNoSqlWriteRepositoryPort eventNoSqlWriteRepositoryPort;
	private final DatabaseSequencePort databaseSequencePort;
	private final GenericReadRepositoryPort<Reservation> genericNoSqlReadRepositoryPort;	
	private final BaseProjectorPort clientBaseProjectorPort;		
	private final BaseProjectorPort flightBaseProjectorPort;
	private final BaseProjectorPort seatBaseProjectorPort;
	private final FlightSpecificProjectorPort flightSpecificProjectorPort;
	private final SeatSpecificProjectorPort seatSpecificProjectorPort;
	private final ReservationSpecificReadRepositoryPort reservationSpecificReadRepositoryPort;

	public UpdateReservationCommandHandler(UpdateReservationCommandHandlerBuilder builder) {
		super();
		this.eventNoSqlWriteRepositoryPort = builder.eventNoSqlWriteRepositoryPort;
		this.databaseSequencePort = builder.databaseSequencePort;
		this.clientBaseProjectorPort = builder.clientBaseProjectorPort;
		this.flightBaseProjectorPort = builder.flightBaseProjectorPort;
		this.seatBaseProjectorPort = builder.seatBaseProjectorPort;
		this.reservationSpecificReadRepositoryPort = builder.reservationSpecificReadRepositoryPort;
		this.flightSpecificProjectorPort = builder.flightSpecificProjectorPort;
		this.seatSpecificProjectorPort = builder.seatSpecificProjectorPort;
		this.genericNoSqlReadRepositoryPort = builder.genericReadRepositoryPort;
	}
	
	@Override
	public Optional<Event> handle(Command command) {
		
		if ( command instanceof UpdateReservationCommand updateReservationCommand ) {

			validateClientExists(updateReservationCommand.clientId());
			validateFlightExists(updateReservationCommand.flightId());
			validateSeatExists(updateReservationCommand.seatIdsSet());			
			validateReservationExists(updateReservationCommand.reservationId());
			validateFlightHasTheseSeats(updateReservationCommand);
			validateSeatAlreadyBooked(updateReservationCommand);
			validateClientAlreadyBookedThatSeat(updateReservationCommand);
			
			ReservationUpdatedEvent event = createReservationUpdatedEvent(updateReservationCommand);
			
			return eventNoSqlWriteRepositoryPort.save(event);
			
		}else {
			
			throw new IllegalArgumentException(ApplicationConstant.MSG_ILLEGAL_COMMAND);
		
		}
	}
	
	private void validateReservationExists(UUID reservationId) {

		if ( genericNoSqlReadRepositoryPort.findById(reservationId).isEmpty() ) {
			 
			throw new ResourceNotFoundException(Map.of(ApplicationConstant.RESERVID, reservationId.toString()));
		
		}		
	}
	
	private ReservationUpdatedEvent createReservationUpdatedEvent(UpdateReservationCommand updateReservationCommand) {
		
		return new ReservationUpdatedEvent(
				databaseSequencePort.generateSequence(ApplicationConstant.SEQ_GEN),
				updateReservationCommand.reservationId(),
				updateReservationCommand.clientId(),
				updateReservationCommand.flightId(),
				updateReservationCommand.seatIdsSet()
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

	private void validateFlightHasTheseSeats(UpdateReservationCommand command) {
		
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

	private void validateSeatAlreadyBooked(UpdateReservationCommand command) {
		
		if ( reservationSpecificReadRepositoryPort.countReservationJpaEntityByFlightIdSeatIdSetButDifferentClient( command.flightId(), command.seatIdsSet(), command.clientId() ) > 0 ) {
			 
			throw new EntityAssociatedException(Map.of(ApplicationConstant.SEATID, command.seatIdsSet().toString() ));
		
		}		
	}

	private void validateClientAlreadyBookedThatSeat(UpdateReservationCommand command) {
		
		if ( reservationSpecificReadRepositoryPort.countReservationJpaEntityByFlightIdSeatIdSetWithSameClientDifferentReservation( command.reservationId(), command.flightId(), command.seatIdsSet(), command.clientId() ) > 0 ) {
			 
			throw new EntityAssociatedException(Map.of(ApplicationConstant.SEATID, command.seatIdsSet().toString() ));
		
		}
	}

	public static class UpdateReservationCommandHandlerBuilder{
		
		private GenericReadRepositoryPort<Reservation> genericReadRepositoryPort;
		private EventNoSqlWriteRepositoryPort eventNoSqlWriteRepositoryPort;
		private DatabaseSequencePort databaseSequencePort;
		private BaseProjectorPort clientBaseProjectorPort;		
		private BaseProjectorPort flightBaseProjectorPort;
		private BaseProjectorPort seatBaseProjectorPort;
		private ReservationSpecificReadRepositoryPort reservationSpecificReadRepositoryPort;
		private FlightSpecificProjectorPort flightSpecificProjectorPort;
		private SeatSpecificProjectorPort seatSpecificProjectorPort;		

		public UpdateReservationCommandHandlerBuilder setGenericReadRepositoryPort(GenericReadRepositoryPort<Reservation> genericReadRepositoryPort) {
			this.genericReadRepositoryPort = genericReadRepositoryPort;
			return this;
		}

		public UpdateReservationCommandHandlerBuilder setEventNoSqlWriteRepositoryPort(EventNoSqlWriteRepositoryPort eventNoSqlWriteRepositoryPort) {
			this.eventNoSqlWriteRepositoryPort = eventNoSqlWriteRepositoryPort;
			return this;
		}

		public UpdateReservationCommandHandlerBuilder setDatabaseSequencePort(DatabaseSequencePort databaseSequencePort) {
			this.databaseSequencePort = databaseSequencePort;
			return this;
		}
		
		public UpdateReservationCommandHandlerBuilder setClientBaseProjectorPort(@Qualifier("clientRedisProjectorAdapter")BaseProjectorPort clientBaseProjectorPort) {
			this.clientBaseProjectorPort = clientBaseProjectorPort;
			return this;
		}
		
		public UpdateReservationCommandHandlerBuilder setFlightBaseProjectorPort(@Qualifier("flightRedisProjectorAdapter")BaseProjectorPort flightBaseProjectorPort) {
			this.flightBaseProjectorPort = flightBaseProjectorPort;
			return this;
		}
		
		public UpdateReservationCommandHandlerBuilder setSeatBaseProjectorPort(@Qualifier("seatRedisProjectorAdapter")BaseProjectorPort seatBaseProjectorPort) {
			this.seatBaseProjectorPort = seatBaseProjectorPort;
			return this;
		}

		public UpdateReservationCommandHandlerBuilder setReservationSpecificReadRepositoryPort(
				ReservationSpecificReadRepositoryPort reservationSpecificReadRepositoryPort) {
			this.reservationSpecificReadRepositoryPort = reservationSpecificReadRepositoryPort;
			return this;
		}

		public UpdateReservationCommandHandlerBuilder setFlightSpecificProjectorPort(FlightSpecificProjectorPort flightSpecificProjectorPort) {
			this.flightSpecificProjectorPort = flightSpecificProjectorPort;
			return this;
		}

		public UpdateReservationCommandHandlerBuilder setSeatSpecificProjectorPort(SeatSpecificProjectorPort seatSpecificProjectorPort) {
			this.seatSpecificProjectorPort = seatSpecificProjectorPort;
			return this;
		}		

		public UpdateReservationCommandHandler build() {
			return new UpdateReservationCommandHandler(this);
		}		
	}
	
}
