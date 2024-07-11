package dev.ime.application.handler;


import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.event.ReservationDeletedEvent;
import dev.ime.application.exception.ResourceNotFoundException;
import dev.ime.application.usecase.DeleteReservationCommand;
import dev.ime.application.usecase.UpdateReservationCommand;
import dev.ime.domain.event.Event;
import dev.ime.domain.model.Reservation;
import dev.ime.domain.port.outbound.DatabaseSequencePort;
import dev.ime.domain.port.outbound.EventNoSqlWriteRepositoryPort;
import dev.ime.domain.port.outbound.GenericReadRepositoryPort;

@ExtendWith(MockitoExtension.class)
class DeleteReservationCommandHandlerTest {

	@Mock
	private EventNoSqlWriteRepositoryPort eventNoSqlWriteRepositoryPort;
	
	@Mock
	private DatabaseSequencePort databaseSequencePort;
	
	@Mock
	private GenericReadRepositoryPort<Reservation> genericNoSqlReadRepositoryPort;

	@InjectMocks
	private DeleteReservationCommandHandler deleteReservationCommandHandler;

	private final UUID reservationId = UUID.randomUUID();
	private final UUID clientId = UUID.randomUUID();
	private final UUID flightId = UUID.randomUUID();
	private Set<UUID> seatIdsSet;
	private final Long databaseSequence = 37L;
	private DeleteReservationCommand deleteReservationCommand;
	
	@BeforeEach
	private void createObjects() {
		
		
		 deleteReservationCommand = new DeleteReservationCommand(reservationId);
		
				
	}
	
	@Test
	void DeleteReservationCommandHandlerTest_handle_ReturnOptionalEvent() {

		ReservationDeletedEvent event = new ReservationDeletedEvent(
				databaseSequence,
				reservationId
				);
		Mockito.when(genericNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(new Reservation()));
		Mockito.when(databaseSequencePort.generateSequence(Mockito.anyString())).thenReturn(databaseSequence);
		Mockito.when(eventNoSqlWriteRepositoryPort.save(Mockito.any(Event.class))).thenReturn(Optional.ofNullable(event));

		Optional<Event> optEvent = deleteReservationCommandHandler.handle(deleteReservationCommand);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(ReservationDeletedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence)
				);
	}

	@Test
	void DeleteReservationCommandHandler_handle_ReturnIllegalArgumentException() {
		
		UpdateReservationCommand updateReservationCommand = new UpdateReservationCommand(
				reservationId,
				clientId,
				flightId,
				seatIdsSet
				);
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> deleteReservationCommandHandler.handle(updateReservationCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);		
	}

	@Test
	void DeleteReservationCommandHandler_handle_ReturnResourceNotFoundException() {
		
		Mockito.when(genericNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> deleteReservationCommandHandler.handle(deleteReservationCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);
	}
	
}
