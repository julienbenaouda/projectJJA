package test.backend.constraint.constraint;

import org.junit.Before;
import org.junit.Test;
import taskman.backend.constraint.amount.Constant;
import taskman.backend.constraint.amount.Type;
import taskman.backend.constraint.constraint.Smaller;
import taskman.backend.resource.ResourceType;

import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SmallerTest {
	private Smaller smaller;
	private ResourceType type;

	@Before
	public void setUp() {
		type = new ResourceType("test");
		smaller = new Smaller(new Type(type), new Constant(5));
	}

	@Test
	public void testEvaluate_true() {
		HashMap<ResourceType, Integer> requirements = new HashMap<>();
		requirements.put(type, 2);
		assertTrue(smaller.evaluate(requirements));
	}

	@Test
	public void testEvaluate_false() {
		HashMap<ResourceType, Integer> requirements = new HashMap<>();
		requirements.put(type, 7);
		assertFalse(smaller.evaluate(requirements));
	}

}
