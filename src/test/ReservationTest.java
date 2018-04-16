package test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.Month;

import org.junit.Before;
import org.junit.Test;

import taskman.backend.resource.Reservation;
import taskman.backend.resource.Resource;
import taskman.backend.resource.ResourceType;
import taskman.backend.task.Task;
import taskman.backend.time.TimeSpan;

public class ReservationTest {
	private Reservation reservation;
	private Resource resource;
	private Task task;
	private TimeSpan timeSpan;

	@Before
	public void setUp() {
		task = new Task("test", 25l, 5.5);
		resource = new Resource(new ResourceType("test"));
		LocalDateTime start = LocalDateTime.of(2018, Month.JULY, 26, 12, 0);
		LocalDateTime end = LocalDateTime.of(2018, Month.JULY, 26, 12, 25);
		timeSpan = new TimeSpan(start, end);
		reservation = new Reservation(task, resource, timeSpan);
	}

	@Test
	public void testReservation_legal() {
		assertEquals(task, reservation.getTask());
		assertEquals(resource, reservation.getResource());
		assertEquals(timeSpan, reservation.getTimeSpan());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testReservation_illegal() {
		reservation = new Reservation(null, null, null);
	}

	@Test
	public void testOverlaps_true() {
		LocalDateTime start = LocalDateTime.of(2018, Month.JULY, 26, 12, 10);
		LocalDateTime end = LocalDateTime.of(2018, Month.JULY, 29, 12, 25);
		TimeSpan overlappingTimeSpan = new TimeSpan(start, end);
		assertTrue(reservation.overlaps(overlappingTimeSpan));
	}
	
	@Test
	public void testOverlaps_false() {
		LocalDateTime start = LocalDateTime.of(2018, Month.JULY, 20, 14, 0);
		LocalDateTime end = LocalDateTime.of(2018, Month.JULY, 23, 12, 25);
		TimeSpan overlappingTimeSpan = new TimeSpan(start, end);
		assertFalse(reservation.overlaps(overlappingTimeSpan));
	}

}
