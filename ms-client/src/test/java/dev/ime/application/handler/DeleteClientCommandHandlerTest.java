package dev.ime.application.handler;


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

import dev.ime.application.event.ClientDeletedEvent;
import dev.ime.application.exception.ReservationAssociatedException;
import dev.ime.application.usecase.DeleteClientCommand;
import dev.ime.application.usecase.UpdateClientCommand;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.ClientNoSqlWriteRepositoryPort;
import dev.ime.domain.port.outbound.DatabaseSequencePort;
import dev.ime.domain.port.outbound.ReservationRedisProjectorPort;

@ExtendWith(MockitoExtension.class)
class DeleteClientCommandHandlerTest {

	@Mock
	private ClientNoSqlWriteRepositoryPort clientNoSqlWriteRepositoryPort;

	@Mock
	private DatabaseSequencePort databaseSequencePort;	

	@Mock
	private ReservationRedisProjectorPort reservationRedisProjectorPort;	
	
	@InjectMocks
	private DeleteClientCommandHandler deleteClientCommandHandler;
	
	private UpdateClientCommand updateClientCommand;
	private DeleteClientCommand deleteClientCommand;
	private ClientDeletedEvent event;
	private final UUID id = UUID.randomUUID();
	private final String name = "Triss";
	private final String lastname = "Merigold";
	private final Long databaseSequence = 9L;
	
	@BeforeEach
	private void createObjects() {

		updateClientCommand = new UpdateClientCommand(id, name, lastname);
		event = new ClientDeletedEvent(databaseSequence, id);
		deleteClientCommand = new DeleteClientCommand(id);
		
	}
	
	@Test
	void DeleteClientCommandHandler_handle_ReturnOptionalEvent() {
		
		Mockito.when(reservationRedisProjectorPort.existsReservationRedisEntityByClientId(Mockito.any(UUID.class))).thenReturn(false);
		Mockito.when(databaseSequencePort.generateSequence(Mockito.anyString())).thenReturn(databaseSequence);
		Mockito.when(clientNoSqlWriteRepositoryPort.save(Mockito.any(Event.class))).thenReturn(Optional.ofNullable(event));
		
		Optional<Event> optEvent = deleteClientCommandHandler.handle(deleteClientCommand);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(ClientDeletedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence)
				);
	}	

	@Test
	void DeleteClientCommandHandler_handle_ReturnIllegalArgumentException() {
	
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> deleteClientCommandHandler.handle(updateClientCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);		
	}

	@Test
	void DeleteClientCommandHandler_handle_ReturnReservationAssociatedException() {
		
		Mockito.when(reservationRedisProjectorPort.existsReservationRedisEntityByClientId(Mockito.any(UUID.class))).thenReturn(true);
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ReservationAssociatedException.class, ()-> deleteClientCommandHandler.handle(deleteClientCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ReservationAssociatedException.class)
				);		
	}

}
