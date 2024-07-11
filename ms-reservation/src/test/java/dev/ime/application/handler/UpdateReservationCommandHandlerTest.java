package dev.ime.application.handler;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.event.ReservationUpdatedEvent;
import dev.ime.application.exception.EntityAssociatedException;
import dev.ime.application.exception.ResourceNotFoundException;
import dev.ime.application.handler.UpdateReservationCommandHandler.UpdateReservationCommandHandlerBuilder;
import dev.ime.application.usecase.DeleteReservationCommand;
import dev.ime.application.usecase.UpdateReservationCommand;
import dev.ime.domain.event.Event;
import dev.ime.domain.model.Reservation;
import dev.ime.domain.port.outbound.BaseProjectorPort;
import dev.ime.domain.port.outbound.DatabaseSequencePort;
import dev.ime.domain.port.outbound.EventNoSqlWriteRepositoryPort;
import dev.ime.domain.port.outbound.FlightSpecificProjectorPort;
import dev.ime.domain.port.outbound.GenericReadRepositoryPort;
import dev.ime.domain.port.outbound.ReservationSpecificReadRepositoryPort;
import dev.ime.domain.port.outbound.SeatSpecificProjectorPort;

@ExtendWith(MockitoExtension.class)
class UpdateReservationCommandHandlerTest {

	private UpdateReservationCommandHandlerBuilder updateReservationCommandHandlerBuilder;
	private UpdateReservationCommandHandler updateReservationCommandHandler;
	
	@Mock
	private EventNoSqlWriteRepositoryPort eventNoSqlWriteRepositoryPort;
	@Mock
	private DatabaseSequencePort databaseSequencePort;
	@Mock
	private GenericReadRepositoryPort<Reservation> genericNoSqlReadRepositoryPort;
	@Mock	
	private BaseProjectorPort clientBaseProjectorPort;
	@Mock		
	private BaseProjectorPort flightBaseProjectorPort;
	@Mock
	private BaseProjectorPort seatBaseProjectorPort;
	@Mock
	private ReservationSpecificReadRepositoryPort reservationSpecificReadRepositoryPort;
	@Mock
	private FlightSpecificProjectorPort flightSpecificProjectorPort;
	@Mock
	private SeatSpecificProjectorPort seatSpecificProjectorPort;

	private final UUID reservationId = UUID.randomUUID();
	private final UUID clientId = UUID.randomUUID();
	private final UUID flightId = UUID.randomUUID();
	private Set<UUID> seatIdsSet;
	private final Long databaseSequence = 37L;
	private final UUID planeId = UUID.randomUUID();
	private final UUID seatId = UUID.randomUUID();
	private List<UUID> seatRedisEntityUUIDList;
	private ReservationUpdatedEvent event;
	private UpdateReservationCommand updateReservationCommand;
	
	@BeforeEach
	private void createObjects() {
		
		seatIdsSet = new HashSet<>();
		
		updateReservationCommandHandlerBuilder = new UpdateReservationCommandHandler.UpdateReservationCommandHandlerBuilder()
				.setEventNoSqlWriteRepositoryPort(eventNoSqlWriteRepositoryPort)
				.setDatabaseSequencePort(databaseSequencePort)
				.setGenericReadRepositoryPort(genericNoSqlReadRepositoryPort)
				.setClientBaseProjectorPort(clientBaseProjectorPort)
				.setFlightBaseProjectorPort(flightBaseProjectorPort)
				.setSeatBaseProjectorPort(seatBaseProjectorPort)
				.setReservationSpecificReadRepositoryPort(reservationSpecificReadRepositoryPort)
				.setFlightSpecificProjectorPort(flightSpecificProjectorPort)
				.setSeatSpecificProjectorPort(seatSpecificProjectorPort);
		
		seatRedisEntityUUIDList = new ArrayList<>();		
		
		updateReservationCommand = new UpdateReservationCommand(
				reservationId,
				clientId,
				flightId,
				seatIdsSet);

		updateReservationCommandHandler = new UpdateReservationCommandHandler(updateReservationCommandHandlerBuilder);
	}

	@Test
	void UpdateReservationCommandHandler_handle_ReturnOptionalEvent() {		
		
		Mockito.when(clientBaseProjectorPort.existsById(Mockito.any(UUID.class))).thenReturn(true);
		Mockito.when(flightBaseProjectorPort.existsById(Mockito.any(UUID.class))).thenReturn(true);
		Mockito.when(seatBaseProjectorPort.existsById(Mockito.any(UUID.class))).thenReturn(true);
		Mockito.when(genericNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(new Reservation()));
		Mockito.when(flightSpecificProjectorPort.findPlaneRegardingFlight(Mockito.any(UUID.class))).thenReturn(planeId);
		seatIdsSet.add(seatId);
		seatRedisEntityUUIDList.add(seatId);
		Mockito.when(seatSpecificProjectorPort.findSeatByPlaneId(Mockito.any(UUID.class))).thenReturn(seatRedisEntityUUIDList);
		Mockito.when(reservationSpecificReadRepositoryPort.countReservationJpaEntityByFlightIdSeatIdSetButDifferentClient(Mockito.any(UUID.class), Mockito.anySet(), Mockito.any(UUID.class))).thenReturn(0);
		Mockito.when(reservationSpecificReadRepositoryPort.countReservationJpaEntityByFlightIdSeatIdSetWithSameClientDifferentReservation(Mockito.any(UUID.class),Mockito.any(UUID.class),Mockito.anySet(),Mockito.any(UUID.class))).thenReturn(0); 
		Mockito.when(databaseSequencePort.generateSequence(Mockito.anyString())).thenReturn(databaseSequence);		
		event = new ReservationUpdatedEvent(databaseSequence, reservationId, clientId, flightId, seatIdsSet);
		Mockito.when(eventNoSqlWriteRepositoryPort.save(Mockito.any(Event.class))).thenReturn(Optional.ofNullable(event));
		
		Optional<Event> optEvent = updateReservationCommandHandler.handle(updateReservationCommand);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(ReservationUpdatedEvent.class)
				);
	}

	@Test
	void UpdateReservationCommandHandler_handle_ReturnIllegalArgumentException() {

		DeleteReservationCommand deleteReservationCommand = new DeleteReservationCommand(reservationId);
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> updateReservationCommandHandler.handle(deleteReservationCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);		
	}	
	
	@Test
	void UpdateReservationCommandHandler_handle_ReturnResourceNotFoundExceptionByNoClientId() {
		
		Mockito.when(clientBaseProjectorPort.existsById(Mockito.any(UUID.class))).thenReturn(false);	
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> updateReservationCommandHandler.handle(updateReservationCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);		
	}

	@Test
	void UpdateReservationCommandHandler_handle_ReturnResourceNotFoundExceptionByNoFlightId() {		
		
		Mockito.when(clientBaseProjectorPort.existsById(Mockito.any(UUID.class))).thenReturn(true);
		Mockito.when(flightBaseProjectorPort.existsById(Mockito.any(UUID.class))).thenReturn(false);	
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> updateReservationCommandHandler.handle(updateReservationCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);		
	}

	@Test
	void UpdateReservationCommandHandler_handle_ReturnResourceNotFoundExceptionByNoSeatId() {		

		Mockito.when(clientBaseProjectorPort.existsById(Mockito.any(UUID.class))).thenReturn(true);
		Mockito.when(flightBaseProjectorPort.existsById(Mockito.any(UUID.class))).thenReturn(true);
		seatIdsSet.add(seatId);
		Mockito.when(seatBaseProjectorPort.existsById(Mockito.any(UUID.class))).thenReturn(false);
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> updateReservationCommandHandler.handle(updateReservationCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);		
	}

	@Test
	void UpdateReservationCommandHandler_handle_ReturnResourceNotFoundExceptionByNoReservationId() {

		Mockito.when(clientBaseProjectorPort.existsById(Mockito.any(UUID.class))).thenReturn(true);
		Mockito.when(flightBaseProjectorPort.existsById(Mockito.any(UUID.class))).thenReturn(true);
		seatIdsSet.add(seatId);
		Mockito.when(seatBaseProjectorPort.existsById(Mockito.any(UUID.class))).thenReturn(true);
		Mockito.when(genericNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> updateReservationCommandHandler.handle(updateReservationCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);		
	}

	@Test
	void UpdateReservationCommandHandler_handle_ReturnResourceNotFoundExceptionByNoPlaneId() {

		Mockito.when(clientBaseProjectorPort.existsById(Mockito.any(UUID.class))).thenReturn(true);
		Mockito.when(flightBaseProjectorPort.existsById(Mockito.any(UUID.class))).thenReturn(true);
		seatIdsSet.add(seatId);
		Mockito.when(seatBaseProjectorPort.existsById(Mockito.any(UUID.class))).thenReturn(true);
		Mockito.when(genericNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(new Reservation()));
		Mockito.when(flightSpecificProjectorPort.findPlaneRegardingFlight(Mockito.any(UUID.class))).thenReturn(null);
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> updateReservationCommandHandler.handle(updateReservationCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);		
	}

	@Test
	void UpdateReservationCommandHandler_handle_ReturnResourceNotFoundExceptionByNoSeatInPlane() {

		Mockito.when(clientBaseProjectorPort.existsById(Mockito.any(UUID.class))).thenReturn(true);
		Mockito.when(flightBaseProjectorPort.existsById(Mockito.any(UUID.class))).thenReturn(true);
		seatIdsSet.add(seatId);
		Mockito.when(seatBaseProjectorPort.existsById(Mockito.any(UUID.class))).thenReturn(true);
		Mockito.when(genericNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(new Reservation()));
		Mockito.when(flightSpecificProjectorPort.findPlaneRegardingFlight(Mockito.any(UUID.class))).thenReturn(planeId);
		Mockito.when(seatSpecificProjectorPort.findSeatByPlaneId(Mockito.any(UUID.class))).thenReturn(seatRedisEntityUUIDList);

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> updateReservationCommandHandler.handle(updateReservationCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);		
	}

	@Test
	void UpdateReservationCommandHandler_handle_ReturnResourceNotFoundExceptionByFailCompareSeats() {

		Mockito.when(clientBaseProjectorPort.existsById(Mockito.any(UUID.class))).thenReturn(true);
		Mockito.when(flightBaseProjectorPort.existsById(Mockito.any(UUID.class))).thenReturn(true);
		seatIdsSet.add(seatId);
		Mockito.when(seatBaseProjectorPort.existsById(Mockito.any(UUID.class))).thenReturn(true);
		Mockito.when(genericNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(new Reservation()));
		Mockito.when(flightSpecificProjectorPort.findPlaneRegardingFlight(Mockito.any(UUID.class))).thenReturn(planeId);
		seatRedisEntityUUIDList.add(UUID.randomUUID());
		Mockito.when(seatSpecificProjectorPort.findSeatByPlaneId(Mockito.any(UUID.class))).thenReturn(seatRedisEntityUUIDList);

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> updateReservationCommandHandler.handle(updateReservationCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);		
	}

	@Test
	void UpdateReservationCommandHandler_handle_ReturnResourceNotFoundExceptionBySeatAlreadyBooked() {

		Mockito.when(clientBaseProjectorPort.existsById(Mockito.any(UUID.class))).thenReturn(true);
		Mockito.when(flightBaseProjectorPort.existsById(Mockito.any(UUID.class))).thenReturn(true);
		seatIdsSet.add(seatId);
		Mockito.when(seatBaseProjectorPort.existsById(Mockito.any(UUID.class))).thenReturn(true);
		Mockito.when(genericNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(new Reservation()));
		Mockito.when(flightSpecificProjectorPort.findPlaneRegardingFlight(Mockito.any(UUID.class))).thenReturn(planeId);
		seatRedisEntityUUIDList.add(seatId);
		Mockito.when(seatSpecificProjectorPort.findSeatByPlaneId(Mockito.any(UUID.class))).thenReturn(seatRedisEntityUUIDList);
		Mockito.when(reservationSpecificReadRepositoryPort.countReservationJpaEntityByFlightIdSeatIdSetButDifferentClient(Mockito.any(UUID.class), Mockito.anySet(), Mockito.any(UUID.class))).thenReturn(99);		

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(EntityAssociatedException.class, ()-> updateReservationCommandHandler.handle(updateReservationCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(EntityAssociatedException.class)
				);		
	}

	@Test
	void UpdateReservationCommandHandler_handle_ReturnResourceNotFoundExceptionBySeatAlreadyBookedThatSeat() {

		Mockito.when(clientBaseProjectorPort.existsById(Mockito.any(UUID.class))).thenReturn(true);
		Mockito.when(flightBaseProjectorPort.existsById(Mockito.any(UUID.class))).thenReturn(true);
		seatIdsSet.add(seatId);
		Mockito.when(seatBaseProjectorPort.existsById(Mockito.any(UUID.class))).thenReturn(true);
		Mockito.when(genericNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(new Reservation()));
		Mockito.when(flightSpecificProjectorPort.findPlaneRegardingFlight(Mockito.any(UUID.class))).thenReturn(planeId);
		seatRedisEntityUUIDList.add(seatId);
		Mockito.when(seatSpecificProjectorPort.findSeatByPlaneId(Mockito.any(UUID.class))).thenReturn(seatRedisEntityUUIDList);
		Mockito.when(reservationSpecificReadRepositoryPort.countReservationJpaEntityByFlightIdSeatIdSetButDifferentClient(Mockito.any(UUID.class), Mockito.anySet(), Mockito.any(UUID.class))).thenReturn(0);		
		Mockito.when(reservationSpecificReadRepositoryPort.countReservationJpaEntityByFlightIdSeatIdSetWithSameClientDifferentReservation(Mockito.any(UUID.class),Mockito.any(UUID.class),Mockito.anySet(),Mockito.any(UUID.class))).thenReturn(73); 
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(EntityAssociatedException.class, ()-> updateReservationCommandHandler.handle(updateReservationCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(EntityAssociatedException.class)
				);		
	}


}
