package test;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import taskman.backend.resource.AmountComparator;
import taskman.backend.resource.AndConstraint;
import taskman.backend.resource.Constraint;
import taskman.backend.resource.ConstraintComponent;
import taskman.backend.resource.ResourceType;

public class AndConstraintTest {
	private Constraint constraint1;
	private Constraint constraint2;
	private ConstraintComponent constraint;
	private ResourceType type1;
	private ResourceType type2;

	@Before
	public void setUp() throws Exception {
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
			requirements.put(type1, 1);
			requirements.put(type2, 5);
			assertFalse(constraint.solution(requirements));
	}
}
