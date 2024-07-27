package dev.ime.application.handler.command;


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

import dev.ime.application.event.SeatUpdatedEvent;
import dev.ime.application.exception.ResourceNotFoundException;
import dev.ime.application.usecase.command.DeleteSeatCommand;
import dev.ime.application.usecase.command.UpdateSeatCommand;
import dev.ime.domain.event.Event;
import dev.ime.domain.model.Plane;
import dev.ime.domain.model.Seat;
import dev.ime.domain.port.outbound.DatabaseSequencePort;
import dev.ime.domain.port.outbound.EventNoSqlWriteRepositoryPort;
import dev.ime.domain.port.outbound.GenericNoSqlReadRepositoryPort;

@ExtendWith(MockitoExtension.class)
class UpdateSeatCommandHandlerTest {

	@Mock
	private EventNoSqlWriteRepositoryPort eventNoSqlWriteRepositoryPort;

	@Mock
	private DatabaseSequencePort databaseSequencePort;

	@SuppressWarnings("rawtypes")
	@Mock
	private GenericNoSqlReadRepositoryPort genericNoSqlReadRepositoryPort;

	@InjectMocks
	private UpdateSeatCommandHandler updateSeatCommandHandler;

	private UpdateSeatCommand updateSeatCommand;
	private DeleteSeatCommand deleteSeatCommand;
	private SeatUpdatedEvent event;
	private final Long databaseSequence = 27L;
	private final UUID seatId = UUID.randomUUID();
	private final String seatNumber = "PS1973";
	private final UUID planeId = UUID.randomUUID();

	@BeforeEach
	private void createObjects() {	
		
		updateSeatCommand = new UpdateSeatCommand(
				seatId,
				seatNumber,
				planeId
				);
		
		deleteSeatCommand = new DeleteSeatCommand(seatId);
		
		event = new SeatUpdatedEvent(
				databaseSequence,
				seatId,
				seatNumber,
				planeId
				);		
	}		
	
	@Test
	void UpdateSeatCommandHandler_handle_ReturnOptionalEvent() {
		
		Mockito.when(genericNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(
				Optional.ofNullable(new Seat()),
				Optional.ofNullable(new Plane())
				);
		Mockito.when(databaseSequencePort.generateSequence(Mockito.anyString())).thenReturn(databaseSequence);
		Mockito.when(eventNoSqlWriteRepositoryPort.save(Mockito.any(Event.class))).thenReturn(Optional.ofNullable(event));		
		
		Optional<Event> optEvent = updateSeatCommandHandler.handle(updateSeatCommand);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(SeatUpdatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence)
				);
	}

	@Test
	void UpdateSeatCommandHandler_handle_ReturnIllegalArgumentException() {		
				
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> updateSeatCommandHandler.handle(deleteSeatCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);		
	}

	@Test
	void UpdateSeatCommandHandler_handle_ReturnResourceNotFoundExceptionBySeatNotExists() {
		
		Mockito.when(genericNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> updateSeatCommandHandler.handle(updateSeatCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);		
	}	

	@Test
	void UpdateSeatCommandHandler_handle_ReturnResourceNotFoundExceptionByPlaneNotExists() {
		
		Mockito.when(genericNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class)))
		.thenReturn(Optional.ofNullable(new Seat()))
		.thenReturn(Optional.ofNullable(null));

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> updateSeatCommandHandler.handle(updateSeatCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);		
	}
}
