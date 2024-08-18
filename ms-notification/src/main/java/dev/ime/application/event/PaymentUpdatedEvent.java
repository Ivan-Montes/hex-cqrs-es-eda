package dev.ime.application.event;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import dev.ime.config.ApplicationConstant;

public class PaymentUpdatedEvent extends Event{

	private final UUID paymentId;
	private final UUID reservationId;
	private final UUID clientId;
	private final UUID flightId;

	@JsonCreator
	public PaymentUpdatedEvent(@JsonProperty("sequence")Long sequence, @JsonProperty("paymentId")UUID paymentId,
			@JsonProperty("reservationId")UUID reservationId, @JsonProperty("clientId")UUID clientId, @JsonProperty("flightId")UUID flightId) {
		super(ApplicationConstant.CAT_PAY, ApplicationConstant.PAYMENT_UPDATED, sequence);
		this.paymentId = paymentId;
		this.reservationId = reservationId;
		this.clientId = clientId;
		this.flightId = flightId;
	}

	public UUID getPaymentId() {
		return paymentId;
	}

	public UUID getReservationId() {
		return reservationId;
	}

	public UUID getClientId() {
		return clientId;
	}

	public UUID getFlightId() {
		return flightId;
	}

	@Override
	public String toString() {
		return "PaymentUpdatedEvent [paymentId=" + paymentId + ", reservationId=" + reservationId + ", clientId="
				+ clientId + ", flightId=" + flightId + ", eventId=" + eventId + ", eventCategory=" + eventCategory
				+ ", eventType=" + eventType + ", timeInstant=" + timeInstant + ", sequence=" + sequence + "]";
	}
	
}
