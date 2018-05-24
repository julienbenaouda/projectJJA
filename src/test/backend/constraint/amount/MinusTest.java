package test.backend.constraint.amount;

import org.junit.Before;
import org.junit.Test;
import taskman.backend.constraint.amount.Constant;
import taskman.backend.constraint.amount.Minus;
import taskman.backend.resource.ResourceType;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class MinusTest {
	private Minus minus;

	@Before
	public void setUp() {
		minus = new Minus(new Constant(5), new Constant(2));
	}

	@Test
	public void testEvaluate() {
		HashMap<ResourceType, Integer> requirements = new HashMap<>();
		assertEquals(3, minus.evaluate(requirements));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testEvaluate_illegal() {
		minus = new Minus(new Constant(2), new Constant(25));
		HashMap<ResourceType, Integer> requirements = new HashMap<>();
		assertEquals(-23, minus.evaluate(requirements));
	}


}
