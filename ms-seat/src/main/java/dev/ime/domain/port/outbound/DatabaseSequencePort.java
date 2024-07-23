package dev.ime.domain.port.outbound;

public interface DatabaseSequencePort {

	public Long generateSequence(String seqName);
	
}
