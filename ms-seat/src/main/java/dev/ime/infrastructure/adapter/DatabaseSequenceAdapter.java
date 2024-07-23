package dev.ime.infrastructure.adapter;


import java.util.Objects;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import dev.ime.domain.port.outbound.DatabaseSequencePort;
import dev.ime.infrastructure.entity.DatabaseSequence;

@Repository
public class DatabaseSequenceAdapter implements DatabaseSequencePort{
	
    private final MongoOperations mongoOperations;
    
	public DatabaseSequenceAdapter(MongoOperations mongoOperations) {
		super();
		this.mongoOperations = mongoOperations;
	}

	@Override
	public Long generateSequence(String seqName) {
		
		DatabaseSequence counter = mongoOperations.findAndModify(
				Query.query(Criteria.where("_id").is(seqName)),
				new Update().inc("seq", 1L),
				FindAndModifyOptions.options().returnNew(true).upsert(true),
				DatabaseSequence.class);
		
		return !Objects.isNull(counter) ? counter.getSeq() : 1;
	}

}
