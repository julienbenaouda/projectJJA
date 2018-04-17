package test.backend.time;

import org.junit.Before;
import org.junit.Test;
import taskman.backend.time.AvailabilityPeriod;
import taskman.backend.time.TimeSpan;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import static org.junit.Assert.*;

public class AvailabilityPeriodTest {
	private AvailabilityPeriod availabilityPeriod;
	private LocalTime start;
	private LocalTime end;

	@Before
	public void setUp() throws Exception {
		start = LocalTime.of(12, 0);
		end = LocalTime.of(17, 0);
		availabilityPeriod = new AvailabilityPeriod(start, end);
	}

	@Test
	public void testAvailabilityPeriod_legal() {
		assertEquals(start, availabilityPeriod.getStartTime());
		assertEquals(end, availabilityPeriod.getEndTime());
	}


	@Test(expected=IllegalArgumentException.class)
	public void testAvailabilityPeriod_illegal() {
		availabilityPeriod = new AvailabilityPeriod(null, null);
	}

	@Test
	public void testCanHaveAsEndTime_true() {
		assertTrue(availabilityPeriod.canHaveAsEndTime(end));
	}

	@Test
	public void testCanHaveAsEndTime_false() {
		end = start.minusHours(2);
		assertFalse(availabilityPeriod.canHaveAsEndTime(end));
	}

	@Test
	public void testOverlaps_true() {
		LocalDateTime startTime = LocalDateTime.of(2018, Month.JULY, 26, 10, 0); 
		LocalDateTime endTime = LocalDateTime.of(2018, Month.JULY, 26, 18, 0);
		TimeSpan t = new TimeSpan(startTime, endTime);
		assertTrue(availabilityPeriod.overlaps(t));
	}

	@Test
	public void testOverlaps_false() {
		LocalDateTime startTime = LocalDateTime.of(2018, Month.JULY, 26, 17, 1); 
		LocalDateTime endTime = LocalDateTime.of(2018, Month.JULY, 26, 18, 0);
		TimeSpan t = new TimeSpan(startTime, endTime);
		assertFalse(availabilityPeriod.overlaps(t));
	}

}
