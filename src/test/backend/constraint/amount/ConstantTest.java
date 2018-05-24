package test.backend.constraint.amount;

import org.junit.Test;
import taskman.backend.constraint.amount.Constant;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class ConstantTest {

	@Test
	public void testEvaluate() {
		for (int i = -10; i <= 10; i++) {
			Constant constant = new Constant(i);
			assertEquals(i, constant.evaluate(new HashMap<>()));
		}
	}

}
