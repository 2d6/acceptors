package de.qudosoft.acceptors;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.testng.annotations.Test;

import de.qudosoft.acceptors.interfaces.IConsumer;


public class AcceptorTest {
	
	private IConsumer<Object> consumeAlways = new IConsumer<Object>(){
		@Override
		public Optional<List<Object>> consume(List<Object> input) {
			return Optional.of(input);
		}
	};
	
	private IConsumer<Object> consumeNever = new IConsumer<Object>() {
		@Override
		public Optional<List<Object>> consume(List<Object> input) {
			return Optional.empty();
		}
	};

	@Test
	public void acceptsTrueIfConsumerConsumesAll() {
		final Acceptor<Object> acceptor = new Acceptor<>(consumeAlways);
		
		final boolean accepts = acceptor.accepts(Arrays.asList(new Object()));
		
		assertTrue(accepts);
	}
	
	@Test
	public void acceptsFalseIfConsumerConsumesNothing() {
		final Acceptor<Object> acceptor = new Acceptor<>(consumeNever);
		
		final boolean accepts = acceptor.accepts(Arrays.asList(new Object()));
		
		assertFalse(accepts);
	}
	
	@Test
	public void acceptsTrueIfInputEmptyAndConsumerConsumesEmpty() {
		final Acceptor<Object> acceptor = new Acceptor<>(consumeAlways);
		
		final boolean accepts = acceptor.accepts(Collections.emptyList());
		
		assertTrue(accepts);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void acceptsThrowsIllArgExceptionIfInputNull() {
		final Acceptor<Object> acceptor = new Acceptor<>(consumeNever);
		
		acceptor.accepts((List<Object>) null);
	}
		
}
