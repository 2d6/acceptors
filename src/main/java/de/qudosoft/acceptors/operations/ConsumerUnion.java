package de.qudosoft.acceptors.operations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.qudosoft.acceptors.interfaces.IConsumer;

/**
 * Creates a union of {@link IConsumer consumers}
 */
class ConsumerUnion {

	/**
	 * Creates a new {@link IConsumer consumer} consuming the union of the given
	 * {@link List} of {@link IConsumer consumers}
	 * @param consumers The {@link List} of {@link IConsumer consumers}
	 * @return The union {@link IConsumer consumer}
	 */
	public static <T> IConsumer<T> apply(List<IConsumer<T>> consumers) {
		return new IConsumer<T>() {
			
			List<IConsumer<T>> consumerList = new ArrayList<>(consumers);

			@Override
			public Optional<List<T>> consume(List<T> items) {
				
				List<T> remainder = new ArrayList<>(items);
				
				for (IConsumer<T> consumer : consumerList) {
					Optional<List<T>> singleConsumed = consumer.consume(remainder);
					
					if (singleConsumed.isPresent()) {
						return singleConsumed;
					}
				}
				return Optional.empty();
			}
		};
		
	}

}
