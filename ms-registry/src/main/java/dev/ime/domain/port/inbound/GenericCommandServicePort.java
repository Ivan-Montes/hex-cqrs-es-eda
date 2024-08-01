package dev.ime.domain.port.inbound;



public interface GenericCommandServicePort<T> {

	void create(T entity);
	
}
