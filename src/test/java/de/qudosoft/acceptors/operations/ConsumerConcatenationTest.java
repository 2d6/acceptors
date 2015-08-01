package de.qudosoft.acceptors.operations;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.testng.annotations.Test;

import de.qudosoft.acceptors.EmptyListConsumer;
import de.qudosoft.acceptors.interfaces.IConsumer;


public class ConsumerConcatenationTest {
	
	private IConsumer<Object> emptyListConsumer = new EmptyListConsumer<>();
	
	@Test(dataProviderClass = OperationsTestUtils.class, dataProvider = "getItemCount")
	public void concatenatedConsumerConsumesMultipleItems(int itemCount) {
		final List<Object> items = OperationsTestUtils.createObjectList(itemCount);
		final List<IConsumer<Object>> consumers = OperationsTestUtils.createConsumerList(items);
		
		final IConsumer<Object> concatenated = ConsumerConcatenation.apply(consumers);
		
		Optional<List<Object>> consumed = concatenated.consume(items);
		assertEquals(consumed.get(), items);
	}
	
	@Test(dataProviderClass = OperationsTestUtils.class, dataProvider = "getItemCount")
	public void concatenatedConsumerConsumesExpectedAmount(int itemCount) {
		final List<Object> items = OperationsTestUtils.createObjectList(itemCount);
		final List<IConsumer<Object>> consumers = OperationsTestUtils.createConsumerList(items);
		items.add(new Object());
		
		final IConsumer<Object> concatenated = ConsumerConcatenation.apply(consumers);
		
		Optional<List<Object>> consumed = concatenated.consume(items);
		assertNotEquals(consumed.get(), items);
	}
	
	@Test(dataProviderClass = OperationsTestUtils.class, dataProvider = "getItemCount")
	public void concatenatedConsumerReturnsEmptyOptionalIfTooFewItems(int itemCount) {
		final List<Object> items = OperationsTestUtils.createObjectList(itemCount);
		final List<IConsumer<Object>> consumers = OperationsTestUtils.createConsumerList(items);
		items.remove(items.size()-1);
		
		final IConsumer<Object> concatenated = ConsumerConcatenation.apply(consumers);
		
		Optional<List<Object>> consumed = concatenated.consume(items);
		assertFalse(consumed.isPresent());
	}
	
	@Test(dataProviderClass = OperationsTestUtils.class, dataProvider = "getItemCount")
	public void concatenatedConsumerReturnsEmptyListIfAcceptorsAcceptEmptyList(int itemCount) {
		final List<Object> items = Collections.emptyList();
		final List<IConsumer<Object>> consumers = Collections.nCopies(itemCount, emptyListConsumer);
		
		final IConsumer<Object> concatenated = ConsumerConcatenation.apply(consumers);
		
		Optional<List<Object>> consumed = concatenated.consume(items);
		assertEquals(consumed.get(), items);
	}
}
