package test.backend.constraint.amount;

import org.junit.Test;
import taskman.backend.constraint.amount.Constant;
import taskman.backend.constraint.amount.Plus;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class PlusTest {

	@Test
	public void testEvaluate() {
		for (int i = -10; i <= 10; i++) {
			for (int j = -10; i <= 10; i++) {
				Plus plus = new Plus(new Constant(i), new Constant(j));
				assertEquals(i + j, plus.evaluate(new HashMap<>()));
			}
		}
	}
	

}
