package Test;

import Domain.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import static java.time.temporal.ChronoUnit.MINUTES;

public class TaskTest {

    private Task task;
    private Duration duration;
    private LocalDateTime start;
    private LocalDateTime end;
    private ArrayList<Task> dependencies;

    @Before
    public void setUp() {
        duration = Duration.of(22, MINUTES);
        start = LocalDateTime.of(2018, Month.FEBRUARY, 26, 20, 16);
        end = LocalDateTime.of(2018, Month.MAY, 12 , 19, 27);
        dependencies = new ArrayList<>();
        task = new Task("Very interesting description.", duration, 0.15, start, end, dependencies, null)
    }

    @Test
    public void testTesk() {
        Assert.assertEquals("The descriptions are not equal", "Very interesting description.", task.getDescription());
        Assert.assertEquals("The start dates are not equal", start, task.getStartTime());
        Assert.assertEquals("The end dates are not equal", end, task.getEndTime());
        Assert.assertEquals("The acceptable deviation are not equal", 0.15, task.getAcceptableDeviation());
        Assert.assertEquals("The dependencies are not equal", dependencies, task.getDependencies());
        Assert.assertEquals("The alternative task is nog equal", null, task.getAlternative());
    }

    @Test
    public void testAddTask() {
        Task dependency = new Task("Another interesting description.", duration, 0.1, start, end, dependencies, null);
        task.addDependency(dependency);

    }

    @Test
    public void testGetTask() {
        fail("Not yet implemented");
    }


}
