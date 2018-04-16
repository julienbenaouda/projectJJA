package test;

import static org.junit.Assert.*;

import org.junit.Test;

import taskman.backend.resource.AmountComparator;

public class AmountComparatorTest {

	@Test
	public void testEvaluate_EQUAL() {
		AmountComparator a = AmountComparator.EQUALS;
		assertTrue(a.evaluate(5, 5));
		assertFalse(a.evaluate(5, 10));
	}
	
	@Test
	public void testEvaluate_NOT_EQUAL() {
		AmountComparator a = AmountComparator.NOT_EQUALS;
		assertFalse(a.evaluate(5, 5));
		assertTrue(a.evaluate(5, 10));
	}

	@Test
	public void testEvaluate_GREATER_THAN() {
		AmountComparator a = AmountComparator.GREATER_THAN;
		assertTrue(a.evaluate(7, 5));
		assertFalse(a.evaluate(5, 5));
	}

	@Test
	public void testEvaluate_GREATER_THAN_EQUAL() {
		AmountComparator a = AmountComparator.GREATER_THAN_OR_EQUALS;
		assertTrue(a.evaluate(5, 5));
		assertFalse(a.evaluate(5, 10));
	}

	@Test
	public void testEvaluate_SMALLER_THAN() {
		AmountComparator a = AmountComparator.SMALLER_THAN;
		assertTrue(a.evaluate(3, 5));
		assertFalse(a.evaluate(20, 10));
	}

	@Test
	public void testEvaluate_SMALLER_THAN_EQUAL() {
		AmountComparator a = AmountComparator.SMALLER_THAN_OR_EQUALS;
		assertTrue(a.evaluate(5, 5));
		assertFalse(a.evaluate(20, 10));
	}

}
