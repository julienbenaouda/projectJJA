package test.backend.constraint.amount;

import org.junit.Before;
import org.junit.Test;
import taskman.backend.constraint.amount.Constant;
import taskman.backend.resource.ResourceType;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class ConstantTest {
	private Constant constant;

	@Before
	public void setUp() {
		constant = new Constant(5);
	}

	@Test
	public void testEvaluate() {
		HashMap<ResourceType, Integer> requirements = new HashMap<>();
		assertEquals(5, constant.evaluate(requirements));
	}

}
