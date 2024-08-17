package dev.ime.application.event;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import dev.ime.config.ApplicationConstant;

public class NotificationDeletedEvent extends Event {

	private final UUID notificationId;

	@JsonCreator
	public NotificationDeletedEvent(@JsonProperty("sequence")Long sequence, @JsonProperty("notificationId")UUID notificationId
			) {
		super(ApplicationConstant.CAT_NOT, ApplicationConstant.NOTIFICATION_DELETED, sequence);
		this.notificationId = notificationId;
	}

	public UUID getNotificationId() {
		return notificationId;
	}

	@Override
	public String toString() {
		return "NotificationDeletedEvent [notificationId=" + notificationId + ", eventId=" + eventId
				+ ", eventCategory=" + eventCategory + ", eventType=" + eventType + ", timeInstant=" + timeInstant
				+ ", sequence=" + sequence + "]";
	}
	
	
}
