package test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import org.junit.Before;
import org.junit.Test;

import taskman.backend.resource.DeveloperResource;
import taskman.backend.resource.Reservation;
import taskman.backend.resource.ResourceType;
import taskman.backend.task.Task;
import taskman.backend.time.TimeSpan;

public class DeveloperResourceTest {
	private DeveloperResource dr;
	private LocalTime breakTime;

	@Before
	public void setUp() {
		breakTime = LocalTime.of(12, 0);
		dr = new DeveloperResource(new ResourceType("test"), breakTime);
	}

	@Test
	public void testIsAvailable_false() {
		LocalDateTime start = LocalDateTime.of(2018, Month.JULY, 26, 12, 30);
		LocalDateTime end = LocalDateTime.of(2018, Month.SEPTEMBER, 26, 14, 0);
		TimeSpan t = new TimeSpan(start, end);
		assertFalse(dr.isAvailable(t));
	}

	@Test
	public void testIsAvailable_true() {
		LocalDateTime start = LocalDateTime.of(2018, Month.JULY, 26, 9, 0);
		LocalDateTime end = LocalDateTime.of(2018, Month.JULY, 26, 10, 0);
		TimeSpan t = new TimeSpan(start, end);
		assertTrue(dr.isAvailable(t));
	}
	
	@Test
	public void testDeveloperResource_legal() {
		assertEquals(breakTime, dr.getBreakTime().getStartTime());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testDeveloperResource_illegal() {
		dr = new DeveloperResource(new ResourceType("test"), null);
	}

}
