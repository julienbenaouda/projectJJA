
package test.backend.resource;

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
	private LocalDateTime start;
	private LocalDateTime end;

	
	@Before
	public void setUp() {
		task = new Task("test", "test", 25l, 5.5);
		resource = new Resource("test", new ResourceType("test"));
		start = LocalDateTime.of(2018, Month.JULY, 26, 11, 0);
		end = LocalDateTime.of(2018, Month.JULY, 26, 12, 25);
		reservation = new Reservation(resource, start, end);
	}

	@Test
	public void testReservation_legal() {
		assertEquals(resource, reservation.getResource());
		assertEquals(start, reservation.getTimeSpan().getStartTime());
		assertEquals(end, reservation.getTimeSpan().getEndTime());
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
	
	public void testFinishEarlier() {
		end = end.minusHours(1);
		reservation.finishEarlier(end);
		assertEquals(end, reservation.getTimeSpan().getEndTime());
	}

}

