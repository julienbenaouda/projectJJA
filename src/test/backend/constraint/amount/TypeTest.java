package test.backend.constraint.amount;
import org.junit.Before;
import org.junit.Test;
import taskman.backend.constraint.amount.Type;
import taskman.backend.resource.ResourceType;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class TypeTest {
	private Type type;
	private ResourceType resourceType;

	@Before
	public void setUp() {
		resourceType = new ResourceType("test");
		type = new Type(resourceType);
	}

	@Test
	public void testEvaluate() {
		for (int i = 0; i <= 10; i++) {
			HashMap<ResourceType, Integer> requirements = new HashMap<>();
			requirements.put(resourceType, i);
			assertEquals(i, type.evaluate(requirements));
		}
	}
	
	@Test
	public void testEvaluateDefaultValue() {
		HashMap<ResourceType, Integer> requirements = new HashMap<>();
		assertEquals(0, type.evaluate(requirements));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEvaluateNegativeValue() {
		HashMap<ResourceType, Integer> requirements = new HashMap<>();
		requirements.put(resourceType, -1);
		type.evaluate(requirements);
	}

}
