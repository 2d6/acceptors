package de.qudosoft.acceptors.operations;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.testng.annotations.Test;

import de.qudosoft.acceptors.EmptyListConsumer;
import de.qudosoft.acceptors.SingleItemConsumer;
import de.qudosoft.acceptors.interfaces.IConsumer;


public class ConsumerOperationsTest {
	
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
	
	/*
	 * Input handling
	 */

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void concatenateThrowsIllArgExceptionIfInputEmpty() {
		ConsumerOperations.concatenate(Collections.emptyList());
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void concatenateThrowsIllArgExceptionIfInputNull() {
		ConsumerOperations.concatenate(null);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void concatenateThrowsIllArgExceptionIfAnyInputElementNull() {
		ConsumerOperations.concatenate(Arrays.asList((IConsumer<Object>) null));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void unionThrowsIllArgExceptionIfInputEmpty() {
		ConsumerOperations.union(Collections.emptyList());
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void unionThrowsIllArgExceptionIfInputNull() {
		ConsumerOperations.union(null);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void unionhrowsIllArgExceptionIfAnyInputElementNull() {
		ConsumerOperations.union(Arrays.asList((IConsumer<Object>) null));
	}
	
	/*
	 * Concatenation
	 */
	
	@Test(dataProviderClass = OperationsTestUtils.class, dataProvider = "getItemCount")
	public void concatenatedConsumerConsumesMultipleItems(int itemCount) {
		final List<Object> items = OperationsTestUtils.createObjectList(itemCount);
		final List<IConsumer<Object>> consumers = OperationsTestUtils.createConsumerList(items);
		
		final IConsumer<Object> concatenated = ConsumerOperations.concatenate(consumers);
		
		Optional<List<Object>> consumed = concatenated.consume(items);
		assertEquals(consumed.get(), items);
	}
	
	@Test(dataProviderClass = OperationsTestUtils.class, dataProvider = "getItemCount")
	public void concatenatedConsumerConsumesExpectedAmount(int itemCount) {
		final List<Object> items = OperationsTestUtils.createObjectList(itemCount);
		final List<IConsumer<Object>> consumers = OperationsTestUtils.createConsumerList(items);
		items.add(new Object());
		
		final IConsumer<Object> concatenated = ConsumerOperations.concatenate(consumers);
		
		Optional<List<Object>> consumed = concatenated.consume(items);
		assertNotEquals(consumed.get(), items);
	}
	
	@Test(dataProviderClass = OperationsTestUtils.class, dataProvider = "getItemCount")
	public void concatenatedConsumerReturnsEmptyOptionalIfTooFewItems(int itemCount) {
		final List<Object> items = OperationsTestUtils.createObjectList(itemCount);
		final List<IConsumer<Object>> consumers = OperationsTestUtils.createConsumerList(items);
		items.remove(items.size()-1);
		
		final IConsumer<Object> concatenated = ConsumerOperations.concatenate(consumers);
		
		Optional<List<Object>> consumed = concatenated.consume(items);
		assertFalse(consumed.isPresent());
	}
	
	@Test(dataProviderClass = OperationsTestUtils.class, dataProvider = "getItemCount")
	public void concatenatedConsumerReturnsEmptyListIfAcceptorsAcceptEmptyList(int itemCount) {
		final List<Object> items = Collections.emptyList();
		final List<IConsumer<Object>> consumers = Collections.nCopies(itemCount, emptyListConsumer);
		
		final IConsumer<Object> concatenated = ConsumerOperations.concatenate(consumers);
		
		Optional<List<Object>> consumed = concatenated.consume(items);
		assertEquals(consumed.get(), items);
	}
	
	/*
	 * Union
	 */
	
	@Test
	public void unionConsumerConsumesUnion() {
		final List<IConsumer<Object>> consumers = OperationsTestUtils.createConsumerList(ITEMS);
		
		final IConsumer<Object> union = ConsumerOperations.union(consumers);
		
		Optional<List<Object>> consumed = union.consume(Arrays.asList(OBJECT_A));
		assertEquals(consumed.get(), Arrays.asList(OBJECT_A));
		consumed = union.consume(Arrays.asList(OBJECT_B));
		assertEquals(consumed.get(), Arrays.asList(OBJECT_B));
	}
	
	@Test
	public void unionConsumerUsesFirstMatchingConsumer() {
		final List<IConsumer<Object>> consumers = 
				Arrays.asList(consumesNothing, new SingleItemConsumer<>(OBJECT_B));
		
		final IConsumer<Object> union = ConsumerOperations.union(consumers);
		
		Optional<List<Object>> consumed = union.consume(Arrays.asList(OBJECT_B));
		assertEquals(consumed.get(), Arrays.asList(OBJECT_B));
	}
	
	@Test(dataProviderClass = OperationsTestUtils.class, dataProvider = "getItemCount")
	public void unionConsumerReturnsEmptyOptionalIfAllReturnEmptyOptional(int itemCount) {
		final List<IConsumer<Object>> consumers = Collections.nCopies(itemCount, consumesNothing);
		
		final IConsumer<Object> union = ConsumerOperations.union(consumers);
		
		Optional<List<Object>> consumed = union.consume(Arrays.asList(OBJECT_A));
		assertFalse(consumed.isPresent());
	}
	
	@Test(dataProviderClass = OperationsTestUtils.class, dataProvider = "getItemCount")
	public void unionConsumerReturnsEmptyListIfAllAcceptEmptyList(int itemCount) {
		final List<IConsumer<Object>> consumers = Collections.nCopies(itemCount, emptyListConsumer);
		final List<Object> items = Collections.emptyList();
		
		final IConsumer<Object> union = ConsumerOperations.union(consumers);
		
		Optional<List<Object>> consumed = union.consume(items);
		assertEquals(consumed.get(), items);
	}
}
