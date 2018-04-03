package test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.Month;

import org.junit.Before;
import org.junit.Test;

import taskman.TaskStatus;
import taskman.Timespan;

public class TimespanTest {
	Timespan timespan;
	LocalDateTime start;
	LocalDateTime end;

	@Before
	public void setUp() {
		start = LocalDateTime.of(2003, Month.FEBRUARY, 20, 15, 0);
		end = LocalDateTime.of(2005, Month.MARCH, 22, 10, 0);
		timespan = new Timespan(start, end, TaskStatus.FINISHED);
	}

	@Test
	public void testTimeSpan_legalCase() {
		assertEquals(start, timespan.getStartTime());
		assertEquals(end, timespan.getEndTime());
		assertEquals(TaskStatus.FINISHED, timespan.getStatus());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testTimeSpan_illegalCase() {
		end = LocalDateTime.of(1999, Month.DECEMBER, 1, 11, 0);
		timespan = new Timespan(start, end, TaskStatus.FINISHED);
	}
	
	@Test
	public void testCanHaveAsEndTime_legalCase()
	{
		assertTrue(timespan.canHaveAsEndTime(end));
	}
	
	@Test
	public void testCanHaveAsEndTime_illegalCase()
	{
		end = start;
		assertFalse(timespan.canHaveAsEndTime(end));
	}
}
