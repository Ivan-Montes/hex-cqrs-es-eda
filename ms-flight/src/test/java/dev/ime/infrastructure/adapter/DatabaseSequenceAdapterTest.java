package dev.ime.infrastructure.adapter;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.infrastructure.entity.DatabaseSequence;

@ExtendWith(MockitoExtension.class)
class DatabaseSequenceAdapterTest {

	@Mock
	private MongoOperations mongoOperations;

	@InjectMocks
	private DatabaseSequenceAdapter databaseSequenceAdapter;
	
	@Test
	void DatabaseSequenceAdapter_generateSequence_ReturnLong() {
		
		DatabaseSequence dbSequenceEntity = new DatabaseSequence();
		dbSequenceEntity.setId(ApplicationConstant.SEQ_GEN);
		dbSequenceEntity.setSeq(1L);
		
		Mockito.when(mongoOperations.findAndModify(Mockito.any(Query.class), Mockito.any(Update.class), Mockito.any(FindAndModifyOptions.class), Mockito.any())).thenReturn(dbSequenceEntity);
	
		Long resultValue = databaseSequenceAdapter.generateSequence(ApplicationConstant.SEQ_GEN);
	
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isEqualTo(1L)
				);		
	}
	
	@Test
	void DatabaseSequenceAdapter_generateSequence_ReturnDefault() {
		
		DatabaseSequence dbSequenceEntity = new DatabaseSequence();
		dbSequenceEntity.setId(ApplicationConstant.SEQ_GEN);
		dbSequenceEntity.setSeq(1L);
		
		Mockito.when(mongoOperations.findAndModify(Mockito.any(Query.class), Mockito.any(Update.class), Mockito.any(FindAndModifyOptions.class), Mockito.any())).thenReturn(null);
	
		Long resultValue = databaseSequenceAdapter.generateSequence(ApplicationConstant.SEQ_GEN);
	
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue).isEqualTo(1L)
				);		
	}

}
