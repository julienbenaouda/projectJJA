/*
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

public class ResourceTest {
	private Resource resource;
	private ResourceType type;

	@Before
	public void setUp() throws Exception {
		type = new ResourceType("test");
		resource = new Resource(type);
	}

	@Test
	public void testResource_legal() {
		assertEquals(type, resource.getType());
		assertEquals(0, resource.getReservations().size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testResource_illegal() {
		resource = new Resource(null);
	}

	@Test
	public void testIsAvailable_false() {
		LocalDateTime start = LocalDateTime.of(2018, Month.JULY, 26, 0, 0);
		LocalDateTime end = LocalDateTime.of(2018, Month.AUGUST, 26, 0, 0);
		TimeSpan t = new TimeSpan(start, end);
		Task task = new Task("test", 25l, 5.5);
		Reservation r = new Reservation(task, resource, t);
		resource.addReservation(r);
		start = LocalDateTime.of(2018, Month.JUNE, 26, 0, 0);
		end = LocalDateTime.of(2018, Month.AUGUST, 26, 0, 0);
		t = new TimeSpan(start, end);
		assertFalse(resource.isAvailable(t));
	}

	@Test
	public void testIsAvailable_true() {
		LocalDateTime start = LocalDateTime.of(2018, Month.JULY, 26, 0, 0);
		LocalDateTime end = LocalDateTime.of(2018, Month.AUGUST, 26, 0, 0);
		TimeSpan t = new TimeSpan(start, end);
		Task task = new Task("test", 25l, 5.5);
		Reservation r = new Reservation(task, resource, t);
		resource.addReservation(r);
		start = LocalDateTime.of(2018, Month.JUNE, 26, 0, 0);
		end = LocalDateTime.of(2018, Month.JULY, 20, 0, 0);
		t = new TimeSpan(start, end);
		assertTrue(resource.isAvailable(t));
	}
	
	@Test
	public void testCreateReservation() {
		LocalDateTime start = LocalDateTime.of(2018, Month.JULY, 26, 0, 0);
		LocalDateTime end = LocalDateTime.of(2018, Month.AUGUST, 26, 0, 0);
		TimeSpan timeSpan = new TimeSpan(start, end);
		Task task = new Task("test", 25l, 5.5);
		resource.createReservation(task, timeSpan);
		assertTrue(resource.getReservations().size() == 1);
	}

}
*/
