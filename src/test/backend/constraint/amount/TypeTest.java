package test.backend.constraint.amount;
import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import taskman.backend.constraint.amount.Type;
import taskman.backend.resource.ResourceType;

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
		HashMap<ResourceType, Integer> requirements = new HashMap<>();
		requirements.put(resourceType, 4);
		assertEquals(4, type.evaluate(requirements));
	}
	
	@Test
	public void testEvaluateDefaultValue() {
		HashMap<ResourceType, Integer> requirements = new HashMap<>();
		assertEquals(0, type.evaluate(requirements));
	}


}
