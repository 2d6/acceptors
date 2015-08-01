package de.qudosoft.acceptors;

import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class EmptyListConsumerTest {

	@Test(dataProvider = "getItems")
	public void consumeReturnsEmptyList(List<Object> items) {
		final EmptyListConsumer<Object> consumer = new EmptyListConsumer<>();
		
		Optional<List<Object>> consumed = consumer.consume(items);
		
		assertTrue(consumed.get().isEmpty());
	}
	
	@DataProvider
	private Object[][] getItems() {
		return new Object[][] {
				{ Collections.emptyList() },
				{ Arrays.asList(new Object()) }
		};
	}
}
