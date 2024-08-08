package dev.ime.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import dev.ime.domain.event.Event;


public interface EventNoSqlWriteRepository extends MongoRepository<Event, UUID>{

}
