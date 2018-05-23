package test.backend.constraint;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import taskman.backend.new_constraint.amount.Constant;
import taskman.backend.resource.ResourceType;

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
