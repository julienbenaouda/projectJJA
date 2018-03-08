package test;

import org.junit.BeforeClass;
import taskman.Task;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import static java.time.temporal.ChronoUnit.MINUTES;

/**
 * This is a test class for the Task class.
 * @author Jeroen Van Der Donckt
 *
 */
public class TaskTest {

    private static Task task;
    private static Duration duration;
    private static String start;
    private static String end;

    @BeforeClass
    public static void setUp(){
        duration = Duration.of(22, MINUTES);
        start = "04/03/2018 15:31";
        end = "04/03/2018 15:53";
        task = new Task("Very interesting description.", duration, 0.15, start, end);
    }

    @Test
    public void testTask(){
        Assert.assertEquals("The task ID is not correct", 1, (int) task.getID());
        Assert.assertEquals("The descriptions are not equal", "Very interesting description.", task.getDescription());
        Assert.assertEquals("The estimated durations are not equal", duration, task.getEstimatedDuration());
        Assert.assertEquals("The acceptable deviations are not equal", 0.15, task.getAcceptableDeviation(), 0);
        //Assert.assertEquals("The start dates are not equal", start, task.getStartTime());
        Assert.assertEquals("The years of the start time are not equal", 2018, task.getStartTime().getYear());
        Assert.assertEquals("The months of the start time are not equal", 3, task.getStartTime().getMonthValue());
        Assert.assertEquals("The days of the start time are not equal", 4, task.getStartTime().getDayOfMonth());
        Assert.assertEquals("The hours of the start time are not equal", 15, task.getStartTime().getHour());
        Assert.assertEquals("The minutes of the start time are not equal", 31, task.getStartTime().getMinute());
        Assert.assertEquals("The seconds of the start time are not equal", 0, task.getStartTime().getSecond());
        //Assert.assertEquals("The end dates are not equal", end, task.getEndTime().toString());
        Assert.assertEquals("The years of the end time are not equal", 2018, task.getEndTime().getYear());
        Assert.assertEquals("The months of the end time are not equal", 3, task.getEndTime().getMonthValue());
        Assert.assertEquals("The days of the end time are not equal", 4, task.getEndTime().getDayOfMonth());
        Assert.assertEquals("The hours of the end time are not equal", 15, task.getEndTime().getHour());
        Assert.assertEquals("The minutes of the end time are not equal", 53, task.getEndTime().getMinute());
        Assert.assertEquals("The seconds of the end time are not equal", 0, task.getEndTime().getSecond());
    }


    @Test
    public void testLastTaskID(){
        int prevLastTaskId = Task.getLastTaskID();
        Task newTask = new Task("Less interesting description.", duration, 0.21, start, end);
        Assert.assertEquals("Last task ID does not equal the ID of the latest task.", Integer.valueOf(prevLastTaskId + 1), Task.getLastTaskID());
        Assert.assertEquals("ID of the new task does not equal the latest task ID", Task.getLastTaskID(), newTask.getID());
    }

    @Test
    public void testSetAlternative(){
        Assert.assertEquals("There is already an alternative", null, task.getAlternative());

        Task alternative = new Task("Alternative interesting description.", duration, 1.21, start, end);
        task.setAlternative(alternative);
        Assert.assertEquals("The alternative does not equal the newly added alternative", alternative, task.getAlternative());
    }

    @Test
    public void testAddDepedency_RemoveDepedency(){
        ArrayList<Task> dependecies = task.getDependencies();
        Assert.assertEquals("The depedencies list is not empty", 0, dependecies.size());

        Task dependency = new Task("Another interesting description.", duration, 0.1, start, end);
        task.addDependency(dependency);
        dependecies = task.getDependencies();
        Assert.assertEquals("The depedencies list does not contains 1 depedency", 1, dependecies.size());
        Assert.assertEquals("The depedency in the list is not the newly added depedency", dependency, dependecies.get(0));

        //Assert.task.addDependency(task);
    }

    @Test
    public void testGetTask() {
        Assert.fail("Not yet implemented");
    }


}
