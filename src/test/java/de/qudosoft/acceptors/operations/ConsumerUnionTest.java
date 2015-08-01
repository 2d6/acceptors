package de.qudosoft.acceptors.operations;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.testng.annotations.Test;

import de.qudosoft.acceptors.EmptyListConsumer;
import de.qudosoft.acceptors.SingleItemConsumer;
import de.qudosoft.acceptors.interfaces.IConsumer;


public class ConsumerUnionTest {
	
	private static final Object OBJECT_A = new Object();
	private static final Object OBJECT_B = new Object();
	private static final List<Object> ITEMS = Arrays.asList(OBJECT_A, OBJECT_B);
	
	/*
	 * Mocking
	 */
	
	private IConsumer<Object> consumesNothing = new IConsumer<Object>() {
		@Override
		public Optional<List<Object>> consume(List<Object> input) {
			return Optional.empty();
		}
	};
	
	private IConsumer<Object> emptyListConsumer = new EmptyListConsumer<>();
	
	@Test
	public void unionConsumerConsumesUnion() {
		final List<IConsumer<Object>> consumers = OperationsTestUtils.createConsumerList(ITEMS);
		
		final IConsumer<Object> union = ConsumerUnion.apply(consumers);
		
		Optional<List<Object>> consumed = union.consume(Arrays.asList(OBJECT_A));
		assertEquals(consumed.get(), Arrays.asList(OBJECT_A));
		consumed = union.consume(Arrays.asList(OBJECT_B));
		assertEquals(consumed.get(), Arrays.asList(OBJECT_B));
	}
	
	@Test
	public void unionConsumerUsesFirstMatchingConsumer() {
		final List<IConsumer<Object>> consumers = 
				Arrays.asList(consumesNothing, new SingleItemConsumer<>(OBJECT_B));
		
		final IConsumer<Object> union = ConsumerUnion.apply(consumers);
		
		Optional<List<Object>> consumed = union.consume(Arrays.asList(OBJECT_B));
		assertEquals(consumed.get(), Arrays.asList(OBJECT_B));
	}
	
	@Test(dataProviderClass = OperationsTestUtils.class, dataProvider = "getItemCount")
	public void unionConsumerReturnsEmptyOptionalIfAllReturnEmptyOptional(int itemCount) {
		final List<IConsumer<Object>> consumers = Collections.nCopies(itemCount, consumesNothing);
		
		final IConsumer<Object> union = ConsumerUnion.apply(consumers);
		
		Optional<List<Object>> consumed = union.consume(Arrays.asList(OBJECT_A));
		assertFalse(consumed.isPresent());
	}
	
	@Test(dataProviderClass = OperationsTestUtils.class, dataProvider = "getItemCount")
	public void unionConsumerReturnsEmptyListIfAllAcceptEmptyList(int itemCount) {
		final List<IConsumer<Object>> consumers = Collections.nCopies(itemCount, emptyListConsumer);
		final List<Object> items = Collections.emptyList();
		
		final IConsumer<Object> union = ConsumerUnion.apply(consumers);
		
		Optional<List<Object>> consumed = union.consume(items);
		assertEquals(consumed.get(), items);
	}
}
