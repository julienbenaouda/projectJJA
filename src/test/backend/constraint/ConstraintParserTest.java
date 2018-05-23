package test.backend.constraint;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import taskman.backend.new_constraint.ConstraintParser;
import taskman.backend.new_constraint.constraint.Constraint;
import taskman.backend.resource.ResourceManager;

public class ConstraintParserTest {
	private ResourceManager resourceManager;

	@Before
	public void setUp() {
		resourceManager = new ResourceManager();
		resourceManager.createResourceType("type1");
		resourceManager.createResourceType("type2");
	}
	
	@Test
	public void testParse_equals() {
		Constraint c = ConstraintParser.parse("== type1 1", resourceManager);
		assertEquals("Equals", c.getClass().getSimpleName());
	}
	
	@Test
	public void testParse_not() {
		Constraint c = ConstraintParser.parse("not == type1 1", resourceManager);
		assertEquals("Not", c.getClass().getSimpleName());
	}
	
	@Test
	public void testParse_or() {
		Constraint c = ConstraintParser.parse("or == type1 1 == type2 2", resourceManager);
		assertEquals("Or", c.getClass().getSimpleName());
	}
	
	@Test
	public void testParseSmaller() {
		Constraint c = ConstraintParser.parse("< type1 1", resourceManager);
		assertEquals("Smaller", c.getClass().getSimpleName());
	}
	
	@Test
	public void testParse_greater() {
		Constraint c = ConstraintParser.parse("> type1 1", resourceManager);
		assertEquals("Not", c.getClass().getSimpleName());
	}
	
	@Test
	public void testParse_lessEqual() {
		Constraint c = ConstraintParser.parse("<= type1 1", resourceManager);
		assertEquals("Or", c.getClass().getSimpleName());
	}
	
	@Test
	public void testParse_greaterEqual() {
		Constraint c = ConstraintParser.parse(">= type1 1", resourceManager);
		assertEquals("Not", c.getClass().getSimpleName());
	}
	
	@Test
	public void testParse_and() {
		Constraint c = ConstraintParser.parse("and == type1 1 == type2 2", resourceManager);
		assertEquals("Not", c.getClass().getSimpleName());
	}
	
	@Test
	public void testParse_notEqual() {
		Constraint c = ConstraintParser.parse("!= type1 1", resourceManager);
		assertEquals("Not", c.getClass().getSimpleName());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testParse_illegalType() {
		Constraint c = ConstraintParser.parse("== type3 9", resourceManager);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testParse_illegal() {
		ConstraintParser.parse(null, resourceManager);
	}

}
