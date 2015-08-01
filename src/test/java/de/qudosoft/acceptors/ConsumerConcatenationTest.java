package de.qudosoft.acceptors;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.qudosoft.acceptors.interfaces.IConsumer;


public class ConsumerConcatenationTest {
	
	private IConsumer<Object> emptyListConsumer = new IConsumer<Object>() {
		@Override
		public Optional<List<Object>> consume(List<Object> input) {
			return Optional.of(Collections.emptyList());
		}
	};
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void concatenateThrowsIllArgExceptionIfInputEmpty() {
		ConsumerConcatenation.concatenate(Collections.emptyList());
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void concatenateThrowsIllArgExceptionIfInputNull() {
		ConsumerConcatenation.concatenate(null);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void concatenateThrowsIllArgExceptionIfAnyInputElementNull() {
		ConsumerConcatenation.concatenate(Arrays.asList((IConsumer<Object>) null));
	}
	
	@Test(dataProvider = "getItemCount")
	public void concatenatedConsumerConsumesMultipleItems(int itemCount) {
		final List<Object> items = createObjectList(itemCount);
		final List<IConsumer<Object>> consumers = createConsumerList(items);
		
		final IConsumer<Object> concatenated = ConsumerConcatenation.concatenate(consumers);
		
		Optional<List<Object>> consumed = concatenated.consume(items);
		assertEquals(consumed.get(), items);
	}
	
	@Test(dataProvider = "getItemCount")
	public void concatenatedConsumerConsumesExpectedAmount(int itemCount) {
		final List<Object> items = createObjectList(itemCount);
		final List<IConsumer<Object>> consumers = createConsumerList(items);
		items.add(new Object());
		
		final IConsumer<Object> concatenated = ConsumerConcatenation.concatenate(consumers);
		
		Optional<List<Object>> consumed = concatenated.consume(items);
		assertNotEquals(consumed.get(), items);
	}
	
	@Test(dataProvider = "getItemCount")
	public void concatenatedConsumerReturnsEmptyOptionalIfTooFewItems(int itemCount) {
		final List<Object> items = createObjectList(itemCount);
		final List<IConsumer<Object>> consumers = createConsumerList(items);
		items.remove(items.size()-1);
		
		final IConsumer<Object> concatenated = ConsumerConcatenation.concatenate(consumers);
		
		Optional<List<Object>> consumed = concatenated.consume(items);
		assertFalse(consumed.isPresent());
	}
	
	@Test(dataProvider = "getItemCount")
	public void concatenatedConsumerReturnsEmptyListIfAcceptorsAcceptEmptyList(int itemCount) {
		final List<Object> items = Collections.emptyList();
		final List<IConsumer<Object>> consumers = Collections.nCopies(itemCount, emptyListConsumer);
		
		final IConsumer<Object> concatenated = ConsumerConcatenation.concatenate(consumers);
		
		Optional<List<Object>> consumed = concatenated.consume(items);
		assertEquals(consumed.get(), items);
	}
	
	@DataProvider
	private Object[][] getItemCount() {
		return new Object[][] {
				{ 1 },
				{ 2 },
				{ 10 }
		};
	}
	
	private List<Object> createObjectList(int objectCount) {
		List<Object> objects = new ArrayList<>();
		for (int i = 0; i < objectCount; i++) {
			objects.add(new Object());
		}
		return objects;
	}
	
	private List<IConsumer<Object>> createConsumerList(List<Object> objects) {
		List<IConsumer<Object>> consumers = new ArrayList<>();
		for (Object object : objects) {
			consumers.add(new SingleItemConsumer<>(object));
		}
		return consumers;
	}
}
