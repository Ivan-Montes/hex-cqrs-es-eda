package dev.ime.application.handler;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.event.FlightUpdatedEvent;
import dev.ime.application.exception.ResourceNotFoundException;
import dev.ime.application.usecase.DeleteFlightCommand;
import dev.ime.application.usecase.UpdateFlightCommand;
import dev.ime.domain.event.Event;
import dev.ime.domain.model.Flight;
import dev.ime.domain.port.outbound.DatabaseSequencePort;
import dev.ime.domain.port.outbound.FlightNoSqlReadRepositoryPort;
import dev.ime.domain.port.outbound.FlightNoSqlWriteRepositoryPort;
import dev.ime.domain.port.outbound.PlaneRedisProjectorPort;

@ExtendWith(MockitoExtension.class)
class UpdateFlightCommandHandlerTest {

	@Mock
	private FlightNoSqlWriteRepositoryPort flightNoSqlWriteRepositoryPort;

	@Mock
	private DatabaseSequencePort databaseSequencePort;

	@Mock
	private FlightNoSqlReadRepositoryPort flightNoSqlReadRepositoryPort;

	@Mock
	private PlaneRedisProjectorPort planeRedisProjectorPort;
	
	@InjectMocks
	private UpdateFlightCommandHandler updateFlightCommandHandler;

	private UpdateFlightCommand updateFlightCommand;
	private DeleteFlightCommand deleteFlightCommand;
	private FlightUpdatedEvent event;
	private final Long databaseSequence = 11L;
	private final UUID flightId = UUID.randomUUID();
	private final String origin = "Madrid";
	private final String destiny = "Tokyo";
	private final LocalDate departureDate = LocalDate.parse("2024-04-01");
	private final LocalTime departureTime = LocalTime.parse("15:00");
	private final UUID planeId = UUID.randomUUID();

	@BeforeEach
	private void createObjects() {		
		
		updateFlightCommand = new UpdateFlightCommand(
				flightId,
				origin,
				destiny,
				departureDate,
				departureTime,
				planeId
				);
		
		event = new FlightUpdatedEvent(
				databaseSequence,
				flightId,
				origin,
				destiny,
				departureDate,
				departureTime,
				planeId
				);
		
		deleteFlightCommand = new DeleteFlightCommand(flightId);
		
	}
	
	@Test
	void UpdateFlightCommandHandler_handle_ReturnOptionalEvent() {
		
		Mockito.when(flightNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(new Flight()));
		Mockito.when(planeRedisProjectorPort.existById(Mockito.any(UUID.class))).thenReturn(true);
		Mockito.when(databaseSequencePort.generateSequence(Mockito.anyString())).thenReturn(databaseSequence);
		Mockito.when(flightNoSqlWriteRepositoryPort.save(Mockito.any(Event.class))).thenReturn(Optional.ofNullable(event));
		
		Optional<Event> optEvent = updateFlightCommandHandler.handle(updateFlightCommand);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(FlightUpdatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence)
				);
	}
	
	

	@Test
	void UpdateFlightCommandHandler_handle_ReturnIllegalArgumentException() {
	
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> updateFlightCommandHandler.handle(deleteFlightCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);		
	}

	@Test
	void UpdateFlightCommandHandler_handle_ReturnResourceNotFoundException() {
		
		Mockito.when(flightNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> updateFlightCommandHandler.handle(updateFlightCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);		
	}

	@Test
	void UpdateFlightCommandHandler_handle_ReturnResourceNotFoundExceptionByPlane() {		

		Mockito.when(flightNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(new Flight()));
		Mockito.when(planeRedisProjectorPort.existById(Mockito.any(UUID.class))).thenReturn(false);
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> updateFlightCommandHandler.handle(updateFlightCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);		
	}

}
