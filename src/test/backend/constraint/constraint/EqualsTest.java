package test.backend.constraint.constraint;

import org.junit.Assert;
import org.junit.Test;
import taskman.backend.constraint.amount.Constant;
import taskman.backend.constraint.constraint.Equals;

import java.util.HashMap;

public class EqualsTest {

	@Test
	public void testEvaluate() {
		for (int i = -10; i <= 10; i++) {
			for (int j = -10; j <= 10; j++) {
				Equals equals = new Equals(new Constant(i), new Constant(j));
				Assert.assertEquals(i == j, equals.evaluate(new HashMap<>()));
			}
		}
	}

}
