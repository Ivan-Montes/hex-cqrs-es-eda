package dev.ime.db;



import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;



@ExtendWith(MockitoExtension.class)
class DatabaseSequenceAdapterTest {
	
	@InjectMocks
	private DatabaseSequenceAdapter databaseSequenceAdapter;	

	@Test
	void DatabaseSequenceAdapter_generateSequence_ReturnLong() {		
		
		Long resultValue = databaseSequenceAdapter.generateSequence();
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isNotNull(),
				()-> Assertions.assertThat(resultValue.getClass()).isEqualTo(Long.class)
				);	
	}

}
