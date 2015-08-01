package de.qudosoft.acceptors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.qudosoft.acceptors.interfaces.IConsumer;

/**
 * Concatenates {@link IConsumer consumers}
 */
public class ConsumerConcatenation {

	/**
	 * Returns a new {@link IConsumer consumer} resulting from the concatenation of the
	 * input {@link List} of {@link IConsumer consumers}. The returned consumer
	 * acts as if the given input consumers consumed input in sequentially. Its consume() method
	 * returns an empty {@link Optional} if any of the input consumers returns an empty {@link Optional}
	 * during processing.
	 * @param consumers The input consumers
	 * @param <T> The item type of the input consumers
	 * @return The concatenated consumer
	 */
	public static <T> IConsumer<T> concatenate(List<IConsumer<T>> consumers) {
		
		throwExceptionOnInvalidInput(consumers);
		
		return new IConsumer<T>() {
			
			List<IConsumer<T>> consumerList = new ArrayList<>(consumers);
			
			@Override
			public Optional<List<T>> consume(List<T> items) {
				
				final List<T> consumed = new ArrayList<>();
				List<T> remainder = new ArrayList<>(items);
				
				for (IConsumer<T> consumer : consumerList) {
					Optional<List<T>> singleConsumed = consumer.consume(remainder);
					
					if (!singleConsumed.isPresent()) {
						return Optional.empty();
					}
					
					consumed.addAll(singleConsumed.get());
					remainder = remainder.subList(singleConsumed.get().size(), remainder.size());
				}
				return Optional.of(consumed);
			}
			
		};
	}
	
	private static <T> void throwExceptionOnInvalidInput(List<IConsumer<T>> consumers) {
		if (consumers == null || consumers.isEmpty() || containsNull(consumers) ) {
			throw new IllegalArgumentException("Invalid Input");
		}
	}
	
	private static boolean containsNull(List<?> list) {
		for (Object element : list) {
			if (element == null) {
				return true;
			}
		}
		return false;
	}
}
