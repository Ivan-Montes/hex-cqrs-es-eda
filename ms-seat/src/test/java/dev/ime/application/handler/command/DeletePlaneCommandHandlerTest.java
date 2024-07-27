package dev.ime.application.handler.command;


import java.util.ArrayList;
import java.util.List;
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

import dev.ime.application.event.PlaneDeletedEvent;
import dev.ime.application.exception.EntityAssociatedException;
import dev.ime.application.exception.ResourceNotFoundException;
import dev.ime.application.usecase.command.DeletePlaneCommand;
import dev.ime.application.usecase.command.UpdatePlaneCommand;
import dev.ime.domain.event.Event;
import dev.ime.domain.model.Plane;
import dev.ime.domain.model.Seat;
import dev.ime.domain.port.outbound.DatabaseSequencePort;
import dev.ime.domain.port.outbound.EventNoSqlWriteRepositoryPort;
import dev.ime.domain.port.outbound.FlightRedisProjectorPort;
import dev.ime.domain.port.outbound.GenericNoSqlReadRepositoryPort;
import dev.ime.domain.port.outbound.SeatSpecificNoSqlReadRepositoryPort;

@ExtendWith(MockitoExtension.class)
class DeletePlaneCommandHandlerTest {

	@Mock
	private EventNoSqlWriteRepositoryPort eventNoSqlWriteRepositoryPort;
	
	@Mock
	private DatabaseSequencePort databaseSequencePort;
	
	@Mock
	private GenericNoSqlReadRepositoryPort<Plane> genericNoSqlReadRepositoryPort;
	
	@Mock
	private SeatSpecificNoSqlReadRepositoryPort<Seat> seatSpecificNoSqlReadRepositoryPort;	

	@Mock
	private FlightRedisProjectorPort flightRedisProjectorPort;
	
	@InjectMocks
	private DeletePlaneCommandHandler deletePlaneCommandHandler;	

	private UpdatePlaneCommand updatePlaneCommand;
	private DeletePlaneCommand deletePlaneCommand;
	private PlaneDeletedEvent event;
	private final Long databaseSequence = 27L;
	private final UUID planeId = UUID.randomUUID();
	private final String name = "Pursuit Special";
	private final Integer capacity = 2;
	
	@BeforeEach
	private void createObjects() {	
		
		updatePlaneCommand = new UpdatePlaneCommand(
				planeId,
				name,
				capacity
				);
		
		event = new PlaneDeletedEvent(
				databaseSequence,
				planeId
				);
		
		deletePlaneCommand = new DeletePlaneCommand(planeId);
	}

	@Test
	void DeletePlaneCommandHandler_handle_ReturnOptionalEvent() {
		
		Mockito.when(genericNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(new Plane()));
		Mockito.when(seatSpecificNoSqlReadRepositoryPort.findByPlaneId(Mockito.any(UUID.class))).thenReturn(new ArrayList<>());
		Mockito.when(flightRedisProjectorPort.existFlightRedisEntityByPlaneId(Mockito.any(UUID.class))).thenReturn(false);
		Mockito.when(databaseSequencePort.generateSequence(Mockito.anyString())).thenReturn(databaseSequence);
		Mockito.when(eventNoSqlWriteRepositoryPort.save(Mockito.any(Event.class))).thenReturn(Optional.ofNullable(event));
		
		Optional<Event> optEvent = deletePlaneCommandHandler.handle(deletePlaneCommand);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(PlaneDeletedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence)
				);
	}
	
	@Test
	void DeletePlaneCommandHandler_handle_ReturnIllegalArgumentException() {
	
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> deletePlaneCommandHandler.handle(updatePlaneCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);		
	}

	@Test
	void DeletePlaneCommandHandler_handle_ReturnResourceNotFoundException() {

		Mockito.when(genericNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> deletePlaneCommandHandler.handle(deletePlaneCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);		
	}
	
	@Test
	void DeletePlaneCommandHandler_handle_ReturnEntityAssociatedException() {
		
		List<Seat> seatList = new ArrayList<>();
		seatList.add(new Seat());
		Mockito.when(genericNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(new Plane()));
		Mockito.when(seatSpecificNoSqlReadRepositoryPort.findByPlaneId(Mockito.any(UUID.class))).thenReturn(seatList);
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(EntityAssociatedException.class, ()-> deletePlaneCommandHandler.handle(deletePlaneCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(EntityAssociatedException.class)
				);		
	}
	
	@Test
	void DeletePlaneCommandHandler_handle_ReturnEntityAssociatedExceptionByFlight() {		

		Mockito.when(genericNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(new Plane()));
		Mockito.when(seatSpecificNoSqlReadRepositoryPort.findByPlaneId(Mockito.any(UUID.class))).thenReturn(new ArrayList<>());
		Mockito.when(flightRedisProjectorPort.existFlightRedisEntityByPlaneId(Mockito.any(UUID.class))).thenReturn(true);
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(EntityAssociatedException.class, ()-> deletePlaneCommandHandler.handle(deletePlaneCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(EntityAssociatedException.class)
				);		
	}
}
