package de.qudosoft.acceptors.operations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.qudosoft.acceptors.interfaces.IConsumer;

/**
 * Concatenates {@link IConsumer consumers}
 */
class ConsumerConcatenation extends ConsumerOperations {

	/**
	 * Creates a new {@link IConsumer consumer} consuming the concatenation of the given
	 * {@link List} of {@link IConsumer consumers}
	 * @param consumers The {@link List} of {@link IConsumer consumers}
	 * @return The concatenated {@link IConsumer consumers}
	 */
	public static <T> IConsumer<T> apply(List<IConsumer<T>> consumers) {
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
					remainder = remainder.subList(singleConsumed.get().size(),
							remainder.size());
				}
				return Optional.of(consumed);
			}
		};
	}
}
