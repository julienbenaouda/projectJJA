package test.backend.constraint;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import taskman.backend.new_constraint.amount.Constant;
import taskman.backend.new_constraint.amount.Minus;
import taskman.backend.new_constraint.amount.Plus;
import taskman.backend.resource.ResourceType;

public class PlusTest {
	private Plus plus;

	@Before
	public void setUp() {
		plus = new Plus(new Constant(5), new Constant(5));
	}

	@Test
	public void testEvaluate() {
		HashMap<ResourceType, Integer> requirements = new HashMap<>();
		assertEquals(10, plus.evaluate(requirements));
	}
	

}
