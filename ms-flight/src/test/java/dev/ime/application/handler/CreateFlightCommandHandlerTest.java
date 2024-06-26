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

import dev.ime.application.event.FlightCreatedEvent;
import dev.ime.application.exception.ResourceNotFoundException;
import dev.ime.application.usecase.CreateFlightCommand;
import dev.ime.application.usecase.DeleteFlightCommand;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.DatabaseSequencePort;
import dev.ime.domain.port.outbound.FlightNoSqlWriteRepositoryPort;
import dev.ime.domain.port.outbound.PlaneRedisProjectorPort;

@ExtendWith(MockitoExtension.class)
class CreateFlightCommandHandlerTest {

	@Mock
	private FlightNoSqlWriteRepositoryPort flightNoSqlWriteRepositoryPort;

	@Mock
	private DatabaseSequencePort databaseSequencePort;	

	@Mock
	private PlaneRedisProjectorPort planeRedisProjectorPort;	

	@InjectMocks
	private CreateFlightCommandHandler createFlightCommandHandler;
	
	private CreateFlightCommand createFlightCommand;
	private DeleteFlightCommand deleteFlightCommand;
	private FlightCreatedEvent event;
	private final Long databaseSequence = 11L;
	private final UUID flightId = UUID.randomUUID();
	private final String origin = "Madrid";
	private final String destiny = "Tokyo";
	private final LocalDate departureDate = LocalDate.parse("2024-04-01");
	private final LocalTime departureTime = LocalTime.parse("15:00");
	private final UUID planeId = UUID.randomUUID();
	
	@BeforeEach
	private void createObjects() {		
		
		createFlightCommand = new CreateFlightCommand(
				flightId,
				origin,
				destiny,
				departureDate,
				departureTime,
				planeId
				);
		
		event = new FlightCreatedEvent(
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
	void CreateFlightCommandHandler_handle_ReturnOptionalEvent() {

		Mockito.when(planeRedisProjectorPort.existById(Mockito.any(UUID.class))).thenReturn(true);
		Mockito.when(databaseSequencePort.generateSequence(Mockito.anyString())).thenReturn(databaseSequence);
		Mockito.when(flightNoSqlWriteRepositoryPort.save(Mockito.any(Event.class))).thenReturn(Optional.ofNullable(event));
		
		Optional<Event> optEvent = createFlightCommandHandler.handle(createFlightCommand);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(FlightCreatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence)
				);
	}
	
	@Test
	void CreateFlightCommandHandler_handle_ReturnIllegalArgumentException() {
	
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> createFlightCommandHandler.handle(deleteFlightCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);		
	}

	@Test
	void CreateFlightCommandHandler_handle_ReturnResourceNotFoundException() {

		Mockito.when(planeRedisProjectorPort.existById(Mockito.any(UUID.class))).thenReturn(false);
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> createFlightCommandHandler.handle(createFlightCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);	
	}
	
}
