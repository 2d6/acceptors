package de.qudosoft.acceptors;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import de.qudosoft.acceptors.interfaces.IConsumer;

/**
 * A {@link IConsumer consumer} which accepts an empty {@link List} of
 * items. This means that it always returns an {@link Optional} containing an
 * empty {@link List} for arbitrary input items (even for an empty {@link List}).
 * @param <T> The type of the items
 */
public class EmptyListConsumer<T> implements IConsumer<T> {

	@Override
	public Optional<List<T>> consume(List<T> items) {
		return Optional.of(Collections.emptyList());
	}

}
