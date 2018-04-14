package taskman.backend.wrappers;

import java.time.LocalDateTime;

public interface ClockWrapper {

	/**
	 * Returns the time of the clock.
	 * @return a LocalDateTime object.
	 */
	LocalDateTime getTime();

}