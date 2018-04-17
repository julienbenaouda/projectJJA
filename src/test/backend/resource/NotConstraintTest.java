package test.backend.resource;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import taskman.backend.constraint.AmountComparator;
import taskman.backend.constraint.Constraint;
import taskman.backend.constraint.NotConstraint;
import taskman.backend.resource.ResourceType;

public class NotConstraintTest {
	private Constraint constraint;
	private NotConstraint nConstraint;
	private ResourceType type;

	@Before
	public void setUp() {
		type = new ResourceType("test");
		constraint = new Constraint(type, AmountComparator.GREATER_THAN, 3);
		nConstraint = new NotConstraint(constraint);
	}

	@Test
	public void testSolution_true() {
		HashMap<ResourceType, Integer> requirements = new HashMap<>();
		requirements.put(type, 2);
		assertTrue(nConstraint.solution(requirements));
	}
	
	@Test
	public void testSolution_false() {
		HashMap<ResourceType, Integer> requirements = new HashMap<>();
		requirements.put(type, 7);
		assertFalse(nConstraint.solution(requirements));
	}

}
