package de.qudosoft.acceptors.interfaces;

import java.util.List;
import java.util.Optional;

/**
 * Consumes a {@link List} of items
 * @param <T> The type of the items
 */
public interface IConsumer<T> {

	/** Consumes a {@link List} of items
	 * @param input The {@link List} of items
	 * @return An {@link Optional} containing the consumed portion of the input, i.e. the items processed by this
	 * method. Returns an empty {@link Optional} if no items were consumed.
	 */
	public Optional<List<T>> consume(List<T> input);
}
