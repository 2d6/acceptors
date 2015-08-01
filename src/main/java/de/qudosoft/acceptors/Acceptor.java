package de.qudosoft.acceptors;

import java.util.List;
import java.util.Optional;

import de.qudosoft.acceptors.interfaces.IAcceptor;
import de.qudosoft.acceptors.interfaces.IConsumer;

/**
 * Implements the {@link IAcceptor} interface using an {@link IConsumer} to process a {@link List}
 * of items of type <T>
 * @param <T> The type T of the items
 */
public class Acceptor<T> implements IAcceptor<T> {
	
	IConsumer<T> consumer;

	public Acceptor(IConsumer<T> consumer) {
		this.consumer = consumer;
	}

	@Override
	public boolean accepts(List<T> items) {
		if (items == null) {
			throw new IllegalArgumentException("Input may not be null");
		}
		
		Optional<List<T>> consumed = consumer.consume(items);
		
		return consumed.isPresent() && items.equals(consumed.get());
	}

}
