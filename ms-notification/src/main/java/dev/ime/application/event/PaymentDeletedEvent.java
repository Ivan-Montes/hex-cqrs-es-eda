package dev.ime.application.event;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import dev.ime.config.ApplicationConstant;

public class PaymentDeletedEvent extends Event{

	private final UUID paymentId;

	@JsonCreator
	public PaymentDeletedEvent(@JsonProperty("sequence")Long sequence, @JsonProperty("paymentId")UUID paymentId) {
		super(ApplicationConstant.CAT_PAY, ApplicationConstant.PAYMENT_DELETED, sequence);
		this.paymentId = paymentId;
	}

	public UUID getPaymentId() {
		return paymentId;
	}

	@Override
	public String toString() {
		return "PaymentDeletedEvent [paymentId=" + paymentId + ", eventId=" + eventId + ", eventCategory="
				+ eventCategory + ", eventType=" + eventType + ", timeInstant=" + timeInstant + ", sequence=" + sequence
				+ "]";
	}	
	
}
