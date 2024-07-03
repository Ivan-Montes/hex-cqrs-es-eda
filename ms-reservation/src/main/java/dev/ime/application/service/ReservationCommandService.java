package dev.ime.application.service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.dispatch.ReservationCommandDispatcher;
import dev.ime.application.dto.ReservationDto;
import dev.ime.application.exception.EventUnexpectedException;
import dev.ime.application.usecase.CreateReservationCommand;
import dev.ime.application.usecase.DeleteReservationCommand;
import dev.ime.application.usecase.UpdateReservationCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.inbound.GenericCommandServicePort;
import dev.ime.domain.port.outbound.PublisherPort;

@Service
public class ReservationCommandService implements GenericCommandServicePort<ReservationDto>{

	private final ReservationCommandDispatcher reservationCommandDispatcher;
	private final PublisherPort publisherPort;
	
	public ReservationCommandService(ReservationCommandDispatcher reservationCommandDispatcher,
			PublisherPort publisherPort) {
		super();
		this.reservationCommandDispatcher = reservationCommandDispatcher;
		this.publisherPort = publisherPort;
	}

	@Override
	public Optional<Event> create(ReservationDto dto) {
		
		CreateReservationCommand command = new CreateReservationCommand(
				UUID.randomUUID(),
				dto.clientId(),
				dto.flightId(),
				dto.seatIdsSet()
				);
		return handleCommand(command);
	}

	@Override
	public Optional<Event> update(UUID id, ReservationDto dto) {
		
		UpdateReservationCommand command = new UpdateReservationCommand(
				id,
				dto.clientId(),
				dto.flightId(),
				dto.seatIdsSet()
				);
		return handleCommand(command);
	}

	@Override
	public Optional<Event> deleteById(UUID id) {
		
		DeleteReservationCommand command = new DeleteReservationCommand(id);
		
		return handleCommand(command);
	}


	private Optional<Event> handleCommand(Command command) {
		
        CommandHandler<Optional<Event>> handler = reservationCommandDispatcher.getCommandHandler(command);
        Optional<Event> optEvent =  handler.handle(command);
        checkEventEmpty(optEvent);
        publishEventIfPresent(optEvent);
        
		return optEvent;
		
    }
	
	private void checkEventEmpty(Optional<Event> optEvent) {
		
		if ( optEvent.isEmpty() ) {
			
			throw new EventUnexpectedException(Map.of(ApplicationConstant.CAT_EVE, ApplicationConstant.NODATA));
			
		}		
	}
	
	private void publishEventIfPresent(Optional<Event> optEvent) {
		
		if ( optEvent.isPresent() ) {
			
			publisherPort.publishEvent(optEvent.get());	
			
		}		
	}

}
