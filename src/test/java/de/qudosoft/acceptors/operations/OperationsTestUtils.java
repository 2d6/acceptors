package de.qudosoft.acceptors.operations;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.DataProvider;

import de.qudosoft.acceptors.SingleItemConsumer;
import de.qudosoft.acceptors.interfaces.IConsumer;

public class OperationsTestUtils {

	@DataProvider
	public static Object[][] getItemCount() {
		return new Object[][] {
				{ 1 },
				{ 2 },
				{ 10 }
		};
	}
	
	public static List<Object> createObjectList(int objectCount) {
		List<Object> objects = new ArrayList<>();
		for (int i = 0; i < objectCount; i++) {
			objects.add(new Object());
		}
		return objects;
	}
	
	public static List<IConsumer<Object>> createConsumerList(List<Object> objects) {
		List<IConsumer<Object>> consumers = new ArrayList<>();
		for (Object object : objects) {
			consumers.add(new SingleItemConsumer<>(object));
		}
		return consumers;
	}
}
