package test.backend.constraint;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import taskman.backend.new_constraint.amount.Constant;
import taskman.backend.new_constraint.amount.Type;
import taskman.backend.new_constraint.constraint.Constraint;
import taskman.backend.new_constraint.constraint.Equals;
import taskman.backend.new_constraint.constraint.Not;
import taskman.backend.resource.ResourceType;

public class NotTest {
	private Not not;
	private ResourceType type;

	@Before
	public void setUp() {
		type = new ResourceType("test");
		not = new Not((new Equals(new Type(type), new Constant(2))));
	}

	@Test
	public void testEvaluate_true() {
		HashMap<ResourceType, Integer> requirements = new HashMap<>();
		requirements.put(type, 3);
		assertTrue(not.evaluate(requirements));
	}

	@Test
	public void testEvaluate_false() {
		HashMap<ResourceType, Integer> requirements = new HashMap<>();
		requirements.put(type, 2);
		assertFalse(not.evaluate(requirements));
	}

}
