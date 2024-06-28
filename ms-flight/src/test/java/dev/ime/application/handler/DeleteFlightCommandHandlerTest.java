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

import dev.ime.application.event.FlightDeletedEvent;
import dev.ime.application.exception.ReservationAssociatedException;
import dev.ime.application.usecase.DeleteFlightCommand;
import dev.ime.application.usecase.UpdateFlightCommand;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.DatabaseSequencePort;
import dev.ime.domain.port.outbound.FlightNoSqlWriteRepositoryPort;
import dev.ime.domain.port.outbound.ReservationRedisProjectorPort;

@ExtendWith(MockitoExtension.class)
class DeleteFlightCommandHandlerTest {

	@Mock
	private FlightNoSqlWriteRepositoryPort flightNoSqlWriteRepositoryPort;

	@Mock
	private DatabaseSequencePort databaseSequencePort;

	@Mock
	private ReservationRedisProjectorPort reservationRedisProjectorPort;
	
	@InjectMocks
	private DeleteFlightCommandHandler deleteFlightCommandHandler;
	
	private UpdateFlightCommand updateFlightCommand;
	private DeleteFlightCommand deleteFlightCommand;
	private FlightDeletedEvent event;
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
		
		event = new FlightDeletedEvent(
				databaseSequence,
				flightId
				);
		
		deleteFlightCommand = new DeleteFlightCommand(flightId);
		
	}
	

	@Test
	void DeleteFlightCommandHandler_handle_ReturnOptionalEvent() {
		
		Mockito.when(reservationRedisProjectorPort.existsReservationRedisEntityByFlighId(Mockito.any(UUID.class))).thenReturn(false);
		Mockito.when(databaseSequencePort.generateSequence(Mockito.anyString())).thenReturn(databaseSequence);
		Mockito.when(flightNoSqlWriteRepositoryPort.save(Mockito.any(Event.class))).thenReturn(Optional.ofNullable(event));
		
		Optional<Event> optEvent = deleteFlightCommandHandler.handle(deleteFlightCommand);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(FlightDeletedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence)
				);
	}	

	@Test
	void DeleteFlightCommandHandler_handle_ReturnIllegalArgumentException() {
	
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> deleteFlightCommandHandler.handle(updateFlightCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);		
	}

	@Test
	void DeleteFlightCommandHandler_handle_ReturnReservationAssociatedException() {
		
		Mockito.when(reservationRedisProjectorPort.existsReservationRedisEntityByFlighId(Mockito.any(UUID.class))).thenReturn(true);
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ReservationAssociatedException.class, ()-> deleteFlightCommandHandler.handle(deleteFlightCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ReservationAssociatedException.class)
				);		
	}


}
