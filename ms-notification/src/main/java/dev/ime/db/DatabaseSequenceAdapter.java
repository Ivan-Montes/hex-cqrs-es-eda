package dev.ime.db;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;


@Component
public class DatabaseSequenceAdapter implements DatabaseSequencePort{

    private final SecureRandom randomGen;
    
    
	public DatabaseSequenceAdapter() {
		super();
		this.randomGen = new SecureRandom();
	}

	@Override
	public Long generateSequence() {
		
		return randomGen.nextLong(99999);
		
	}

}
