package dev.ime.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.ime.application.handler.CreateReservationCommandHandler.CreateReservationCommandHandlerBuilder;
import dev.ime.application.handler.UpdateReservationCommandHandler.UpdateReservationCommandHandlerBuilder;
import dev.ime.domain.port.outbound.DatabaseSequencePort;
import dev.ime.domain.port.outbound.EventNoSqlWriteRepositoryPort;
import dev.ime.domain.port.outbound.FlightSpecificProjectorPort;
import dev.ime.domain.port.outbound.ReservationSpecificReadRepositoryPort;
import dev.ime.domain.port.outbound.SeatSpecificProjectorPort;
import dev.ime.infrastructure.adapter.ClientRedisProjectorAdapter;
import dev.ime.infrastructure.adapter.FlightRedisProjectorAdapter;
import dev.ime.infrastructure.adapter.ReservationReadRepositoryAdapter;
import dev.ime.infrastructure.adapter.SeatRedisProjectorAdapter;

@Configuration
public class ReservationCommandHandlerConfig {

    private final ApplicationContext applicationContext; 

	public ReservationCommandHandlerConfig(ApplicationContext applicationContext) {
		super();
		this.applicationContext = applicationContext;
	}

	@Bean
	CreateReservationCommandHandlerBuilder createCreateReservationCommandHandlerBuilder() {
		
		return new CreateReservationCommandHandlerBuilder()
				.setEventNoSqlWriteRepositoryPort(applicationContext.getBean(EventNoSqlWriteRepositoryPort.class))
				.setDatabaseSequencePort(applicationContext.getBean(DatabaseSequencePort.class))
				.setClientBaseProjectorPort(applicationContext.getBean(ClientRedisProjectorAdapter.class))
				.setFlightBaseProjectorPort(applicationContext.getBean(FlightRedisProjectorAdapter.class))
				.setSeatBaseProjectorPort(applicationContext.getBean(SeatRedisProjectorAdapter.class))
				.setReservationSpecificReadRepositoryPort(applicationContext.getBean(ReservationSpecificReadRepositoryPort.class))
				.setFlightSpecificProjectorPort(applicationContext.getBean(FlightSpecificProjectorPort.class))
				.setSeatSpecificProjectorPort(applicationContext.getBean(SeatSpecificProjectorPort.class))
				;
	
	}

	@Bean
	UpdateReservationCommandHandlerBuilder createUpdateReservationCommandHandlerBuilder() {
		
		return new UpdateReservationCommandHandlerBuilder()
				.setEventNoSqlWriteRepositoryPort(applicationContext.getBean(EventNoSqlWriteRepositoryPort.class))
				.setDatabaseSequencePort(applicationContext.getBean(DatabaseSequencePort.class))
				.setClientBaseProjectorPort(applicationContext.getBean(ClientRedisProjectorAdapter.class))
				.setFlightBaseProjectorPort(applicationContext.getBean(FlightRedisProjectorAdapter.class))
				.setSeatBaseProjectorPort(applicationContext.getBean(SeatRedisProjectorAdapter.class))
				.setReservationSpecificReadRepositoryPort(applicationContext.getBean(ReservationSpecificReadRepositoryPort.class))
				.setFlightSpecificProjectorPort(applicationContext.getBean(FlightSpecificProjectorPort.class))
				.setSeatSpecificProjectorPort(applicationContext.getBean(SeatSpecificProjectorPort.class))
				.setGenericReadRepositoryPort(applicationContext.getBean(ReservationReadRepositoryAdapter.class))
				;
	
	}
	
}
