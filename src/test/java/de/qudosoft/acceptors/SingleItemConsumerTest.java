package de.qudosoft.acceptors;

import static org.testng.Assert.assertFalse;
import static org.testng.AssertJUnit.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.testng.annotations.Test;


public class SingleItemConsumerTest {

	@Test
	public void consumerConsumesSingleItem() {
        Object A = new Object();
        SingleItemConsumer<Object> consumer = new SingleItemConsumer<Object>(A);

        Optional<List<Object>> consumed = consumer.consume(Arrays.asList(A));

        assertEquals(consumed.get(), Arrays.asList(A));
	}

	@Test
	public void consumerOnlyConsumesDesignatedItem() {
        Object A = new Object();
        Object B = new Object();
        SingleItemConsumer<Object> consumer = new SingleItemConsumer<Object>(A);

        Optional<List<Object>> consumed = consumer.consume(Arrays.asList(B));

        assertFalse(consumed.isPresent());
	}

	@Test
	public void consumerDoesNotConsumeMultipleItems() {
		Object A = new Object();
        SingleItemConsumer<Object> consumer = new SingleItemConsumer<Object>(A);

        Optional<List<Object>> consumed = consumer.consume(Arrays.asList(A, A));

        assertEquals(consumed.get(), Arrays.asList(A));
	}
	
	@Test
	public void consumerReturnsEmptyOptionalOnEmptyInput() {
		Object A = new Object();
        SingleItemConsumer<Object> consumer = new SingleItemConsumer<Object>(A);

        Optional<List<Object>> consumed = consumer.consume(Collections.emptyList());

        assertFalse(consumed.isPresent());
	}

}
