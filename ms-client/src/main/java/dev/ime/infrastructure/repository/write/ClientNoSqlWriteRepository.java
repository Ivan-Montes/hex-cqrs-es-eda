package dev.ime.infrastructure.repository.write;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import dev.ime.domain.event.Event;

@Qualifier("writeMongoTemplate")
public interface ClientNoSqlWriteRepository extends MongoRepository<Event, UUID>{

}
