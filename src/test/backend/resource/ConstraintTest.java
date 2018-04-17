/**
 * 
 */
package test.backend.resource;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import taskman.backend.constraint.AmountComparator;
import taskman.backend.constraint.Constraint;
import taskman.backend.resource.ResourceType;

/**
 * @author Julien
 *
 */
public class ConstraintTest {
	private Constraint constraint;
	private ResourceType type;
	private AmountComparator comparator;
	private int amount;

	@Before
	public void setUp() throws Exception {
		type = new ResourceType("test");
		comparator = AmountComparator.EQUALS;
		amount = 2;
		constraint = new Constraint(type, comparator, amount);
	}

	@Test
	public void testConstraint() {
		assertEquals(type, constraint.getResourceType());
		assertEquals(comparator, constraint.getAmountComparator());
		assertEquals(amount, constraint.getAmount());
	}

	@Test
	public void testSolution_true() {
		HashMap<ResourceType, Integer> requirements = new HashMap<>();
		requirements.put(type, 2);
		assertTrue(constraint.solution(requirements));
	}

	@Test
	public void testSolution_false() {
		HashMap<ResourceType, Integer> requirements = new HashMap<>();
		requirements.put(type, 87);
		assertFalse(constraint.solution(requirements));
	}

}
