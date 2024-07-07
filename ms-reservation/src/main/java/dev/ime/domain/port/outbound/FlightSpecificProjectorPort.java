package dev.ime.domain.port.outbound;

import java.util.UUID;

public interface FlightSpecificProjectorPort {

	UUID findPlaneRegardingFlight(UUID id);

}
