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

import dev.ime.application.exception.ResourceNotFoundException;
import dev.ime.application.usecase.GetAllFlightQuery;
import dev.ime.application.usecase.GetByIdFlightQuery;
import dev.ime.domain.model.Flight;
import dev.ime.domain.port.outbound.FlightNoSqlReadRepositoryPort;

@ExtendWith(MockitoExtension.class)
class GetByIdFlightQueryHandlerTest {

	@Mock
	private FlightNoSqlReadRepositoryPort flightNoSqlReadRepositoryPort;

	@InjectMocks
	private GetByIdFlightQueryHandler getByIdFlightQueryHandler;
	
	private GetAllFlightQuery getAllFlightQuery;
	private GetByIdFlightQuery getByIdFlightQuery;
	private Flight flightTest;
	private final UUID flightId = UUID.randomUUID();
	private final String origin = "Madrid";
	private final String destiny = "Tokyo";
	private final LocalDate departureDate = LocalDate.parse("2024-04-01");
	private final LocalTime departureTime = LocalTime.parse("15:00");
	private final UUID planeId = UUID.randomUUID();

	@BeforeEach
	private void createObjects() {
		
		getAllFlightQuery = new GetAllFlightQuery(0,1);
		getByIdFlightQuery = new GetByIdFlightQuery( flightId );		
		
		flightTest = new Flight();
		flightTest.setFlightId(flightId);
		flightTest.setOrigin(origin);
		flightTest.setDestiny(destiny);
		flightTest.setDepartureDate(departureDate);
		flightTest.setDepartureTime(departureTime);
		flightTest.setPlaneId(planeId);
		
	}	

	@Test
	void GetByIdFlighttQueryHandler_handle_ReturnOptFlight() {
		
		Mockito.when(flightNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(flightTest));
		
		Optional<Flight> optFlight = getByIdFlightQueryHandler.handle(getByIdFlightQuery);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optFlight).isNotNull(),
				()-> Assertions.assertThat(optFlight.get().getFlightId()).isEqualTo(flightId),
				()-> Assertions.assertThat(optFlight.get().getOrigin()).isEqualTo(origin),
				()-> Assertions.assertThat(optFlight.get().getDestiny()).isEqualTo(destiny),
				()-> Assertions.assertThat(optFlight.get().getDepartureDate()).isEqualTo(departureDate),
				()-> Assertions.assertThat(optFlight.get().getDepartureTime()).isEqualTo(departureTime),
				()-> Assertions.assertThat(optFlight.get().getPlaneId()).isEqualTo(planeId)
				);	
	}
	
	@Test
	void GetByIdFlightQueryHandler_handle_ReturnIllegalArgumentException() {		
				
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> getByIdFlightQueryHandler.handle(getAllFlightQuery));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);		
	}	

	@Test
	void GetByIdFlightQueryHandler_handle_ReturnResourceNotFoundException() {
		
		Mockito.when(flightNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));
	
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> getByIdFlightQueryHandler.handle(getByIdFlightQuery));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);		
	}
	
}
