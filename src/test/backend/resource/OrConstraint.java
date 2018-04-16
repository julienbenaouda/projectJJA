package test.backend.resource;

import org.junit.Before;
import org.junit.Test;
import taskman.backend.resource.*;

import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OrConstraint {
	private Constraint constraint1;
	private Constraint constraint2;
	private ConstraintComponent constraint;
	private ResourceType type1;
	private ResourceType type2;

	@Before
	public void setup() {
		type1 = new ResourceType("type1");
		type2 = new ResourceType("type2");
		constraint1 = new Constraint(type1, AmountComparator.EQUALS, 1);
		constraint2 = new Constraint(type2, AmountComparator.EQUALS, 2);
		constraint = new AndConstraint(constraint1, constraint2);
	}

	@Test
	public void testSolution_true() {
			HashMap<ResourceType, Integer> requirements = new HashMap<>();
			requirements.put(type1, 1);
			requirements.put(type2, 2);
			assertTrue(constraint.solution(requirements));
	}

	@Test
	public void testSolution_false() {
			HashMap<ResourceType, Integer> requirements = new HashMap<>();
			assertFalse(constraint.solution(requirements));
	}
}
