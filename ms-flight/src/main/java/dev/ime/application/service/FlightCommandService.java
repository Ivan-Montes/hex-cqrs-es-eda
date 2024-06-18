package dev.ime.application.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.dispatch.FlightCommandDispatcher;
import dev.ime.application.dto.FlightDto;
import dev.ime.application.exception.DateTimeBasicException;
import dev.ime.application.exception.EventUnexpectedException;
import dev.ime.application.usecase.DeleteFlightCommand;
import dev.ime.application.usecase.UpdateFlightCommand;
import dev.ime.application.usecase.CreateFlightCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.inbound.FlightCommandServicePort;
import dev.ime.domain.port.outbound.PublisherPort;

@Service
public class FlightCommandService implements FlightCommandServicePort<FlightDto>{

	private final FlightCommandDispatcher flightCommandDispatcher;
	private final PublisherPort publisherPort;
	
	public FlightCommandService(FlightCommandDispatcher flightCommandDispatcher, PublisherPort publisherPort) {
		super();
		this.flightCommandDispatcher = flightCommandDispatcher;
		this.publisherPort = publisherPort;
	}

	@Override
	public Optional<Event> create(FlightDto dto) {
		
		checkTemporalData(dto);
		
		CreateFlightCommand createCommand = new CreateFlightCommand(
				UUID.randomUUID(),
				dto.origin(),
				dto.destiny(),
				LocalDate.parse(dto.departureDate()),
				LocalTime.parse(dto.departureTime()),
				dto.planeId()
				);
		
		return handleCommand(createCommand);
	}

	@Override
	public Optional<Event> update(UUID id, FlightDto dto) {

		checkTemporalData(dto);
		
		UpdateFlightCommand updateCommand = new UpdateFlightCommand(
				id,
				dto.origin(),
				dto.destiny(),
				LocalDate.parse(dto.departureDate()),
				LocalTime.parse(dto.departureTime()),
				dto.planeId()
				);
		
		return handleCommand(updateCommand);
	}

	@Override
	public Optional<Event> deleteById(UUID id) {
		
		DeleteFlightCommand deleteCommand = new DeleteFlightCommand(
				id
				);

		return handleCommand(deleteCommand);	
	}

	private Optional<Event> handleCommand(Command command) {
		
        CommandHandler<Optional<Event>> handler = flightCommandDispatcher.getCommandHandler(command);
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
	
	private void checkTemporalData(FlightDto dto) {
		
		throwExeptionFailedValidation( !dto.validateParseDepartureDate(), dto.departureDate() );
		throwExeptionFailedValidation( !dto.validateParseDepartureTime(), dto.departureTime() );
		
	}
	
	private void throwExeptionFailedValidation(boolean validationResult, String validatedValue) {
		
		if ( validationResult ) {
			
			throw new DateTimeBasicException(Map.of(ApplicationConstant.EX_DATETIME, validatedValue));
			
		}
		
	}
	
}
