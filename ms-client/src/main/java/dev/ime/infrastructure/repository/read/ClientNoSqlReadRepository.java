package dev.ime.infrastructure.repository.read;

import java.util.Optional;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;

import dev.ime.infrastructure.entity.ClientMongoEntity;

@Qualifier("readMongoTemplate")
public interface ClientNoSqlReadRepository extends MongoRepository<ClientMongoEntity, ObjectId>{
	
	Optional<ClientMongoEntity> findFirstByClientId(UUID id);
}
