package de.qudosoft.acceptors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import de.qudosoft.acceptors.interfaces.IConsumer;

/**
 * Consumes a single item
 * @param <T> The type of the consumed item
 */
public class SingleItemConsumer<T> implements IConsumer<T> {

	private T expected;
	
    public SingleItemConsumer(T expected) {
        this.expected = expected;
    }

    @Override
	public Optional<List<T>> consume(List<T> items) {
    	List<T> consumed = new ArrayList<>();
    	
    	if (!items.isEmpty() && items.get(0).equals(expected)) {
    		consumed = Arrays.asList(expected);
    		return Optional.of(consumed);
    	}
    	return Optional.empty();
	}

}
