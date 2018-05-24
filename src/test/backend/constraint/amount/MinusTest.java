package test.backend.constraint.amount;

import org.junit.Test;
import taskman.backend.constraint.amount.Constant;
import taskman.backend.constraint.amount.Minus;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class MinusTest {

	@Test
	public void testEvaluate() {
		for (int i = -10; i <= 10; i++) {
			for (int j = -10; j <= 10; j++) {
				Minus minus = new Minus(new Constant(i), new Constant(j));
				assertEquals(i - j, minus.evaluate(new HashMap<>()));
			}
		}
	}

}
