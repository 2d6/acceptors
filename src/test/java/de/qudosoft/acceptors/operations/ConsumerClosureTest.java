package de.qudosoft.acceptors.operations;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.testng.annotations.Test;

import de.qudosoft.acceptors.EmptyListConsumer;
import de.qudosoft.acceptors.SingleItemConsumer;
import de.qudosoft.acceptors.interfaces.IConsumer;


public class ConsumerClosureTest {

	private static final Object OBJECT_A = new Object();
	private static final Object OBJECT_B = new Object();
	
	private IConsumer<Object> emptyListConsumer = new EmptyListConsumer<>();
	
	/*
	 * PLUS closure
	 */
	
	@Test
	public void plusClosureConsumesSingleItem() {
		final IConsumer<Object> consumer = new SingleItemConsumer<Object>(OBJECT_A);
		final List<Object> items = Arrays.asList(OBJECT_A);
		
		IConsumer<Object> plusClosure = ConsumerClosure.plus(consumer);
		
		Optional<List<Object>> consumed = plusClosure.consume(items);
		assertEquals(consumed.get(), items);
	}
	
	@Test(dataProviderClass = OperationsTestUtils.class, dataProvider = "getItemCount")
	public void plusClosureConsumesMultipleItems(int itemCount) {
		final IConsumer<Object> consumer = new SingleItemConsumer<Object>(OBJECT_A);
		final List<Object> items = Collections.nCopies(itemCount, OBJECT_A);
		
		IConsumer<Object> plusClosure = ConsumerClosure.plus(consumer);
		
		Optional<List<Object>> consumed = plusClosure.consume(items);
		assertEquals(consumed.get(), items);
	}
	
	@Test(dataProviderClass = OperationsTestUtils.class, dataProvider = "getItemCount")
	public void plusClosureOnlyConsumesMatching(int itemCount) {
		final IConsumer<Object> consumer = new SingleItemConsumer<Object>(OBJECT_A);
		final List<Object> expectedConsumed = Collections.nCopies(itemCount, OBJECT_A);
		final List<Object> items = new ArrayList<>(expectedConsumed);
		items.add(OBJECT_B);
		
		IConsumer<Object> plusClosure = ConsumerClosure.plus(consumer);
		
		Optional<List<Object>> consumed = plusClosure.consume(items);
		assertEquals(consumed.get(), expectedConsumed);
	}
	
	@Test
	public void plusClosureDoesNotConsumeEmptyList() {
		final IConsumer<Object> consumer = new SingleItemConsumer<Object>(OBJECT_A);
		final List<Object> items = Collections.emptyList();
		
		IConsumer<Object> plusClosure = ConsumerClosure.plus(consumer);
		
		Optional<List<Object>> consumed = plusClosure.consume(items);
		assertFalse(consumed.isPresent());
	}
	
	@Test
	public void plusClosureConsumesEmptyListForEmptyListConsumer() {
		final List<Object> items = Collections.emptyList();
		
		IConsumer<Object> plusClosure = ConsumerClosure.plus(emptyListConsumer);
		
		Optional<List<Object>> consumed = plusClosure.consume(items);
		assertTrue(consumed.isPresent());
	}
	
	/*
	 * STAR closure
	 */
	
	@Test
	public void starClosureConsumesSingleItem() {
		final IConsumer<Object> consumer = new SingleItemConsumer<Object>(OBJECT_A);
		final List<Object> items = Arrays.asList(OBJECT_A);
		
		IConsumer<Object> starClosure = ConsumerClosure.star(consumer);
		
		Optional<List<Object>> consumed = starClosure.consume(items);
		assertEquals(consumed.get(), items);
	}
	
	@Test(dataProviderClass = OperationsTestUtils.class, dataProvider = "getItemCount")
	public void starClosureConsumesMultipleItems(int itemCount) {
		final IConsumer<Object> consumer = new SingleItemConsumer<Object>(OBJECT_A);
		final List<Object> items = Collections.nCopies(itemCount, OBJECT_A);
		
		IConsumer<Object> starClosure = ConsumerClosure.star(consumer);
		
		Optional<List<Object>> consumed = starClosure.consume(items);
		assertEquals(consumed.get(), items);
	}
	
	@Test(dataProviderClass = OperationsTestUtils.class, dataProvider = "getItemCount")
	public void starClosureOnlyConsumesMatching(int itemCount) {
		final IConsumer<Object> consumer = new SingleItemConsumer<Object>(OBJECT_A);
		final List<Object> expectedConsumed = Collections.nCopies(itemCount, OBJECT_A);
		final List<Object> items = new ArrayList<>(expectedConsumed);
		items.add(OBJECT_B);
		
		IConsumer<Object> starClosure = ConsumerClosure.star(consumer);
		
		Optional<List<Object>> consumed = starClosure.consume(items);
		assertEquals(consumed.get(), expectedConsumed);
	}
	
	@Test
	public void starClosureConsumesEmptyList() {
		final IConsumer<Object> consumer = new SingleItemConsumer<Object>(OBJECT_A);
		final List<Object> items = Collections.emptyList();
		
		IConsumer<Object> starClosure = ConsumerClosure.star(consumer);
		
		Optional<List<Object>> consumed = starClosure.consume(items);
		assertTrue(consumed.isPresent());
	}

}
