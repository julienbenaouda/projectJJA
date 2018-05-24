package test.backend.constraint.constraint;

import org.junit.Before;
import org.junit.Test;
import taskman.backend.constraint.amount.Constant;
import taskman.backend.constraint.amount.Type;
import taskman.backend.constraint.constraint.Equals;
import taskman.backend.resource.ResourceType;

import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EqualsTest {
	private Equals equals;
	private ResourceType type;

	@Before
	public void setUp() {
		type = new ResourceType("test");
		equals = new Equals(new Type(type), new Constant(2));
	}

	@Test
	public void testEvaluate_true() {
		HashMap<ResourceType, Integer> requirements = new HashMap<>();
		requirements.put(type, 2);
		assertTrue(equals.evaluate(requirements));
	}

	@Test
	public void testEvaluate_false() {
		HashMap<ResourceType, Integer> requirements = new HashMap<>();
		requirements.put(type, 5);
		assertFalse(equals.evaluate(requirements));
	}
}
