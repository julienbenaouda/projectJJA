package test.backend.constraint;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import taskman.backend.constraint.ConstraintParser;
import taskman.backend.constraint.constraint.Constraint;
import taskman.backend.resource.ResourceManager;
import taskman.backend.resource.ResourceType;

import java.util.HashMap;

public class ConstraintParserTest {
	private ResourceManager resourceManager;

	@Before
	public void setUp() {
		resourceManager = new ResourceManager();
		resourceManager.createResourceType("type1");
		resourceManager.createResourceType("type2");
	}

	@Test
	public void testParse_and() {
		Constraint c1 = ConstraintParser.parse("and == 0 1 == 0 1", resourceManager);
		Assert.assertFalse(c1.evaluate(new HashMap<>()));

		Constraint c2 = ConstraintParser.parse("and == 0 0 == 0 1", resourceManager);
		Assert.assertFalse(c2.evaluate(new HashMap<>()));

		Constraint c3 = ConstraintParser.parse("and == 0 1 == 0 0", resourceManager);
		Assert.assertFalse(c3.evaluate(new HashMap<>()));

		Constraint c4 = ConstraintParser.parse("and == 0 0 == 0 0", resourceManager);
		Assert.assertTrue(c4.evaluate(new HashMap<>()));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testParse_illegalAnd1() {
		ConstraintParser.parse("and", resourceManager);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testParse_illegalAnd2() {
		ConstraintParser.parse("and == 0 0", resourceManager);
	}

	@Test
	public void testParse_or() {
		Constraint c1 = ConstraintParser.parse("or == 0 1 == 0 1", resourceManager);
		Assert.assertFalse(c1.evaluate(new HashMap<>()));

		Constraint c2 = ConstraintParser.parse("or == 0 0 == 0 1", resourceManager);
		Assert.assertTrue(c2.evaluate(new HashMap<>()));

		Constraint c3 = ConstraintParser.parse("or == 0 1 == 0 0", resourceManager);
		Assert.assertTrue(c3.evaluate(new HashMap<>()));

		Constraint c4 = ConstraintParser.parse("or == 0 0 == 0 0", resourceManager);
		Assert.assertTrue(c4.evaluate(new HashMap<>()));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testParse_illegalOr1() {
		ConstraintParser.parse("or", resourceManager);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testParse_illegalOr2() {
		ConstraintParser.parse("or == 0 0", resourceManager);
	}

	@Test
	public void testParse_not() {
		Constraint c1 = ConstraintParser.parse("not == 0 0", resourceManager);
		Assert.assertFalse(c1.evaluate(new HashMap<>()));

		Constraint c2 = ConstraintParser.parse("not == 0 1", resourceManager);
		Assert.assertTrue(c2.evaluate(new HashMap<>()));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testParse_illegalNot() {
		ConstraintParser.parse("not", resourceManager);
	}
	
	@Test
	public void testParse_equals() {
		Constraint c1 = ConstraintParser.parse("== 0 0", resourceManager);
		Assert.assertTrue(c1.evaluate(new HashMap<>()));

		Constraint c2 = ConstraintParser.parse("== 1 1", resourceManager);
		Assert.assertTrue(c2.evaluate(new HashMap<>()));

		Constraint c3 = ConstraintParser.parse("== 0 1", resourceManager);
		Assert.assertFalse(c3.evaluate(new HashMap<>()));

		Constraint c4 = ConstraintParser.parse("== 1 0", resourceManager);
		Assert.assertFalse(c4.evaluate(new HashMap<>()));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testParse_illegalEquals1() {
		ConstraintParser.parse("==", resourceManager);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testParse_illegalEquals2() {
		ConstraintParser.parse("== 0", resourceManager);
	}

	@Test
	public void testParse_notEquals() {
		Constraint c1 = ConstraintParser.parse("!= 0 0", resourceManager);
		Assert.assertFalse(c1.evaluate(new HashMap<>()));

		Constraint c2 = ConstraintParser.parse("!= 1 1", resourceManager);
		Assert.assertFalse(c2.evaluate(new HashMap<>()));

		Constraint c3 = ConstraintParser.parse("!= 0 1", resourceManager);
		Assert.assertTrue(c3.evaluate(new HashMap<>()));

		Constraint c4 = ConstraintParser.parse("!= 1 0", resourceManager);
		Assert.assertTrue(c4.evaluate(new HashMap<>()));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testParse_illegalNotEquals1() {
		ConstraintParser.parse("!=", resourceManager);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testParse_illegalNotEquals2() {
		ConstraintParser.parse("!= 0", resourceManager);
	}
	
	@Test
	public void testParse_smaller() {
		Constraint c1 = ConstraintParser.parse("< 0 1", resourceManager);
		Assert.assertTrue(c1.evaluate(new HashMap<>()));

		Constraint c2 = ConstraintParser.parse("< 1 0", resourceManager);
		Assert.assertFalse(c2.evaluate(new HashMap<>()));

		Constraint c3 = ConstraintParser.parse("< 1 1", resourceManager);
		Assert.assertFalse(c3.evaluate(new HashMap<>()));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testParse_illegalSmaller1() {
		ConstraintParser.parse("<", resourceManager);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testParse_illegalSmaller2() {
		ConstraintParser.parse("< 0", resourceManager);
	}

	@Test
	public void testParse_smallerEquals() {
		Constraint c1 = ConstraintParser.parse("<= 0 1", resourceManager);
		Assert.assertTrue(c1.evaluate(new HashMap<>()));

		Constraint c2 = ConstraintParser.parse("<= 1 0", resourceManager);
		Assert.assertFalse(c2.evaluate(new HashMap<>()));

		Constraint c3 = ConstraintParser.parse("<= 1 1", resourceManager);
		Assert.assertTrue(c3.evaluate(new HashMap<>()));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testParse_illegalSmallerEquals1() {
		ConstraintParser.parse("<=", resourceManager);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testParse_illegalSmallerEquals2() {
		ConstraintParser.parse("<= 0", resourceManager);
	}
	
	@Test
	public void testParse_greater() {
		Constraint c1 = ConstraintParser.parse("> 0 1", resourceManager);
		Assert.assertFalse(c1.evaluate(new HashMap<>()));

		Constraint c2 = ConstraintParser.parse("> 1 0", resourceManager);
		Assert.assertTrue(c2.evaluate(new HashMap<>()));

		Constraint c3 = ConstraintParser.parse("> 1 1", resourceManager);
		Assert.assertFalse(c3.evaluate(new HashMap<>()));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testParse_illegalGreater1() {
		ConstraintParser.parse(">", resourceManager);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testParse_illegalGreater2() {
		ConstraintParser.parse("> 0", resourceManager);
	}

	@Test
	public void testParse_greaterEqual() {
		Constraint c1 = ConstraintParser.parse(">= 0 1", resourceManager);
		Assert.assertFalse(c1.evaluate(new HashMap<>()));

		Constraint c2 = ConstraintParser.parse(">= 1 0", resourceManager);
		Assert.assertTrue(c2.evaluate(new HashMap<>()));

		Constraint c3 = ConstraintParser.parse(">= 1 1", resourceManager);
		Assert.assertTrue(c3.evaluate(new HashMap<>()));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testParse_illegalGreaterEquals1() {
		ConstraintParser.parse(">=", resourceManager);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testParse_illegalGreaterEquals2() {
		ConstraintParser.parse(">= 0", resourceManager);
	}

	@Test
	public void testParse_plus() {
		Constraint c1 = ConstraintParser.parse("== 1 + 0 0", resourceManager);
		Assert.assertFalse(c1.evaluate(new HashMap<>()));

		Constraint c2 = ConstraintParser.parse("== 1 + 0 1", resourceManager);
		Assert.assertTrue(c2.evaluate(new HashMap<>()));

		Constraint c3 = ConstraintParser.parse("== 1 + 1 0", resourceManager);
		Assert.assertTrue(c3.evaluate(new HashMap<>()));

		Constraint c4 = ConstraintParser.parse("== 1 + 1 1", resourceManager);
		Assert.assertFalse(c4.evaluate(new HashMap<>()));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testParse_illegalPlus1() {
		ConstraintParser.parse("+", resourceManager);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testParse_illegalPlus2() {
		ConstraintParser.parse("+ 0", resourceManager);
	}

	@Test
	public void testParse_minus() {
		Constraint c1 = ConstraintParser.parse("== 0 - 0 0", resourceManager);
		Assert.assertTrue(c1.evaluate(new HashMap<>()));

		Constraint c2 = ConstraintParser.parse("== 0 - 0 1", resourceManager);
		Assert.assertFalse(c2.evaluate(new HashMap<>()));

		Constraint c3 = ConstraintParser.parse("== 0 - 1 0", resourceManager);
		Assert.assertFalse(c3.evaluate(new HashMap<>()));

		Constraint c4 = ConstraintParser.parse("== 0 - 1 1", resourceManager);
		Assert.assertTrue(c4.evaluate(new HashMap<>()));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testParse_illegalMinus1() {
		ConstraintParser.parse("-", resourceManager);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testParse_illegalMinus2() {
		ConstraintParser.parse("- 0", resourceManager);
	}

	@Test
	public void testParse_type() {
		Constraint c = ConstraintParser.parse("== type1 12", resourceManager);
		Assert.assertFalse(c.evaluate(new HashMap<>()));
		for (int i = 0; i < 100 ; i++) {
			if (i == 12) continue;
			HashMap<ResourceType, Integer> requirements = new HashMap<>();
			requirements.put(resourceManager.getResourceType("type1"), i);
			Assert.assertFalse(c.evaluate(requirements));
		}
		HashMap<ResourceType, Integer> requirements = new HashMap<>();
		requirements.put(resourceManager.getResourceType("type1"), 12);
		Assert.assertTrue(c.evaluate(requirements));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testParse_illegalType() {
		ConstraintParser.parse("== type3 9", resourceManager);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testParse_null() {
		ConstraintParser.parse(null, resourceManager);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testParse_empty() {
		ConstraintParser.parse("", resourceManager);
	}

}
