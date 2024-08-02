package dev.ime.application.event;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.domain.event.Event;

public class NotificationUpdatedEvent extends Event {

	private final UUID notificationId;
	private final UUID paymentId;
	
	@JsonCreator
	public NotificationUpdatedEvent(@JsonProperty("sequence")Long sequence, @JsonProperty("notificationId")UUID notificationId,
			@JsonProperty("paymentId")UUID paymentId) {
		super(ApplicationConstant.CAT_NOT, ApplicationConstant.NOTIFICATION_UPDATED, sequence);
		this.notificationId = notificationId;
		this.paymentId = paymentId;
	}

	public UUID getNotificationId() {
		return notificationId;
	}

	public UUID getPaymentId() {
		return paymentId;
	}

	@Override
	public String toString() {
		return "NotificationUpdatedEvent [notificationId=" + notificationId + ", paymentId=" + paymentId + ", eventId="
				+ eventId + ", eventCategory=" + eventCategory + ", eventType=" + eventType + ", timeInstant="
				+ timeInstant + ", sequence=" + sequence + "]";
	}
	
}
