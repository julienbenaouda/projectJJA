package test.backend.constraint.amount;

import org.junit.Before;
import org.junit.Test;
import taskman.backend.constraint.amount.Constant;
import taskman.backend.constraint.amount.Plus;
import taskman.backend.resource.ResourceType;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

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
