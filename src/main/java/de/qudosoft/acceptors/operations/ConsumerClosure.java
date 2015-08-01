package de.qudosoft.acceptors.operations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import de.qudosoft.acceptors.EmptyListConsumer;
import de.qudosoft.acceptors.interfaces.IConsumer;

/**
 * Creates star (Regular Expression character '*') and 
 * plus closures (Regular Expression character '+') on {@link IConsumer consumers}.
 */
public class ConsumerClosure {

	/**
	 * Creates a new {@link IConsumer consumer} consuming the plus-closure of the given
	 * {@link List} of {@link IConsumer consumers}
	 * @param consumer The {@link List} of {@link IConsumer consumers}
	 * @return The plus closure of the {@link IConsumer consumers}.
	 */
	public static <T> IConsumer<T> plus(IConsumer<T> consumer) {
		return new IConsumer<T>() {
			
			IConsumer<T> singleConsumer = consumer;

			@Override
			public Optional<List<T>> consume(List<T> items) {
				
				List<T> consumed = new ArrayList<>();
				List<T> remainder = new ArrayList<>(items);
				
				if (!singleConsumer.consume(remainder).isPresent()) {
					return Optional.empty();
				}
				
				while (singleConsumer.consume(remainder).isPresent() && !remainder.isEmpty()) {
					Optional<List<T>> singleConsumed = singleConsumer.consume(remainder);
					
					consumed.addAll(singleConsumed.get());
					remainder = remainder.subList(singleConsumed.get().size(), remainder.size());
				}
				return Optional.of(consumed);
			}
		};
	}

	/**
	 * Creates a new {@link IConsumer consumer} consuming the star-closure of the given
	 * {@link List} of {@link IConsumer consumers}
	 * @param consumer The {@link List} of {@link IConsumer consumers}
	 * @return The star closure of the {@link IConsumer consumers}.
	 */
	public static <T> IConsumer<T> star(IConsumer<T> consumer) {
		IConsumer<T> emptyListConsumer = new EmptyListConsumer<>();
		return ConsumerUnion.apply(Arrays.asList(plus(consumer), emptyListConsumer));
	}

	
}
