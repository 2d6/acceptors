package de.qudosoft.acceptors.operations;

import java.util.List;

import de.qudosoft.acceptors.interfaces.IConsumer;

/**
 * Provides operations such as concatenation or union on {@link IConsumer consumers}. All provided
 * methods return a new {@link IConsumer}.
 */
public class ConsumerOperations {
	
	/**
	 * Creates a new {@link IConsumer consumer} consuming the concatenation of the input {@link List}
	 * of {@link IConsumer consumers}.
	 * @param consumers The input consumers
	 * @return The concatenated {@link IConsumer};
	 */
	public static <T> IConsumer<T> concatenate(List<IConsumer<T>> consumers) {
		throwExceptionOnInvalidInput(consumers);
		
		return ConsumerConcatenation.apply(consumers);
	}
	
	/**
	 * Creates a new {@link IConsumer consumer} consuming the union of the input {@link List}
	 * of {@link IConsumer consumers}.
	 * @param consumers The input consumers
	 * @return The union {@link IConsumer};
	 */
	public static IConsumer<Object> union(List<IConsumer<Object>> consumers) {
		throwExceptionOnInvalidInput(consumers);
		
		return ConsumerUnion.apply(consumers);
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