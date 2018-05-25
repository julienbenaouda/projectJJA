package test.backend.time;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import org.junit.Before;
import org.junit.Test;

import taskman.backend.time.TimeParser;

public class TimeParserTest {
	private TimeParser timeParser;


	@Test
	public void testConvertLocalDateTimeToString() {
		LocalDateTime time = LocalDateTime.of(2018, Month.DECEMBER, 2, 15, 0);
		String converted = TimeParser.convertLocalDateTimeToString(time);
		assertEquals("02/12/2018 15:00", converted);
	}

	@Test
	public void testConvertStringToLocalDateTime() {
		String time = "02/12/2018 15:00";
		LocalDateTime converted = TimeParser.convertStringToLocalDateTime(time);
		assertEquals(2018, converted.getYear());
		assertEquals(Month.DECEMBER, converted.getMonth());
		assertEquals(2, converted.getDayOfMonth());
		assertEquals(15, converted.getHour());
		assertEquals(0, converted.getMinute());
	}

	@Test
	public void testConvertLocalTimeToString() {
		LocalTime time = LocalTime.of(18, 0);
		String converted = TimeParser.convertLocalTimeToString(time);
		assertEquals("18:00", converted);
	}

	@Test
	public void testConvertStringToLocalTime() {
		String time = "16:20";
		LocalTime converted = TimeParser.convertStringToLocalTime(time);
		assertEquals(16, converted.getHour());
		assertEquals(20, converted.getMinute());
	}

	@Test
	public void testRoundUpLocalDateTime() {
		LocalDateTime time = LocalDateTime.of(2018, Month.JUNE, 1, 17, 56);
		LocalDateTime rounded = TimeParser.roundUpLocalDateTime(time);
		assertEquals(18, rounded.getHour());
		assertEquals(0, rounded.getMinute());
	}

	@Test
	public void testRoundUpLocalTime() {
		LocalTime time = LocalTime.of(17, 40);
		LocalTime rounded = TimeParser.roundUpLocalTime(time);
		assertEquals(18, rounded.getHour());
		assertEquals(0, rounded.getMinute());
	}

}
