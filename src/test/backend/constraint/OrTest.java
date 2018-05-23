package test.backend.constraint;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import taskman.backend.new_constraint.amount.Constant;
import taskman.backend.new_constraint.amount.Type;
import taskman.backend.new_constraint.constraint.Equals;
import taskman.backend.new_constraint.constraint.Or;
import taskman.backend.resource.ResourceType;

public class OrTest {
	private Or or;
	private ResourceType type;

	@Before
	public void setUp() {
		type = new ResourceType("test");
		or = new Or(new Equals(new Type(type), new Constant(2)), new Equals(new Type(type), new Constant(3)));
	}

	@Test
	public void testEvaluate_true() {
		HashMap<ResourceType, Integer> requirements = new HashMap<>();
		requirements.put(type, 2);
		assertTrue(or.evaluate(requirements));
	}

	@Test
	public void testEvaluate_false() {
		HashMap<ResourceType, Integer> requirements = new HashMap<>();
		requirements.put(type, 5);
		assertFalse(or.evaluate(requirements));
	}

}
