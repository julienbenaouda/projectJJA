package test;

import org.junit.BeforeClass;
import taskman.Status;
import taskman.Task;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.HashMap;

import static java.time.temporal.ChronoUnit.MINUTES;

/**
 * This is a test class for the Task class.
 * @author Jeroen Van Der Donckt
 *
 */
public class TaskTest {

    private static Task task;
    private static String duration;
    private static String deviation;
    private static String start;
    private static String end;
    private final static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm").withResolverStyle(ResolverStyle.STRICT);


    @BeforeClass
    public static void setUp(){
        duration = "22";
        deviation = "0.15";
        task = new Task("Very interesting description.", duration, deviation);
    }

    @Test
    public void testTask(){
        Assert.assertEquals("The task ID is not correct", 1, (int) task.getID());
        Assert.assertEquals("The descriptions are not equal", "Very interesting description.", task.getDescription());
        Assert.assertEquals("The estimated durations are not equal", Long.parseLong(duration), task.getEstimatedDuration().toMinutes());
        Assert.assertEquals("The acceptable deviations are not equal", 0.15, task.getAcceptableDeviation(), 0);
        /*
        Assert.assertEquals("The start dates are not equal", start, task.getStartTime().format(dateFormatter));
        Assert.assertEquals("The years of the start time are not equal", 2018, task.getStartTime().getYear());
        Assert.assertEquals("The months of the start time are not equal", 3, task.getStartTime().getMonthValue());
        Assert.assertEquals("The days of the start time are not equal", 4, task.getStartTime().getDayOfMonth());
        Assert.assertEquals("The hours of the start time are not equal", 15, task.getStartTime().getHour());
        Assert.assertEquals("The minutes of the start time are not equal", 31, task.getStartTime().getMinute());
        Assert.assertEquals("The seconds of the start time are not equal", 0, task.getStartTime().getSecond());
        Assert.assertEquals("The end dates are not equal", end, task.getEndTime().format(dateFormatter));
        Assert.assertEquals("The years of the end time are not equal", 2018, task.getEndTime().getYear());
        Assert.assertEquals("The months of the end time are not equal", 3, task.getEndTime().getMonthValue());
        Assert.assertEquals("The days of the end time are not equal", 4, task.getEndTime().getDayOfMonth());
        Assert.assertEquals("The hours of the end time are not equal", 15, task.getEndTime().getHour());
        Assert.assertEquals("The minutes of the end time are not equal", 53, task.getEndTime().getMinute());
        Assert.assertEquals("The seconds of the end time are not equal", 0, task.getEndTime().getSecond());
        */
    }


    @Test
    public void testTaskHashMap(){
        HashMap<String, String> form = new HashMap<>();
        form.put("description", "another description");
        form.put("estimatedDuration", "1076");
        form.put("acceptableDeviation", "1.34");
        Task taskHM = new Task(form);

        Assert.assertEquals("The task ID is not the last task ID", (int) Task.getLastTaskID(), (int) taskHM.getID());
        Assert.assertEquals("The descriptions are not equal", "another description", taskHM.getDescription());
        Assert.assertEquals("The estimated durations are not equal", Long.parseLong("1076"), taskHM.getEstimatedDuration().toMinutes());
        /*
        Assert.assertEquals("The acceptable deviations are not equal", 1.34, taskHM.getAcceptableDeviation(), 0);
        Assert.assertEquals("The start dates are not equal", "10/03/2018 12:45", taskHM.getStartTime().format(dateFormatter));
        Assert.assertEquals("The years of the start time are not equal", 2018, taskHM.getStartTime().getYear());
        Assert.assertEquals("The months of the start time are not equal", 3, taskHM.getStartTime().getMonthValue());
        Assert.assertEquals("The days of the start time are not equal", 10, taskHM.getStartTime().getDayOfMonth());
        Assert.assertEquals("The hours of the start time are not equal", 12, taskHM.getStartTime().getHour());
        Assert.assertEquals("The minutes of the start time are not equal", 45, taskHM.getStartTime().getMinute());
        Assert.assertEquals("The seconds of the start time are not equal", 0, taskHM.getStartTime().getSecond());
        Assert.assertEquals("The end dates are not equal", "11/03/2018 05:30", taskHM.getEndTime().format(dateFormatter));
        Assert.assertEquals("The years of the end time are not equal", 2018, taskHM.getEndTime().getYear());
        Assert.assertEquals("The months of the end time are not equal", 3, taskHM.getEndTime().getMonthValue());
        Assert.assertEquals("The days of the end time are not equal", 11, taskHM.getEndTime().getDayOfMonth());
        Assert.assertEquals("The hours of the end time are not equal", 5, taskHM.getEndTime().getHour());
        Assert.assertEquals("The minutes of the end time are not equal", 30, taskHM.getEndTime().getMinute());
        Assert.assertEquals("The seconds of the end time are not equal", 0, taskHM.getEndTime().getSecond());
        */
    }


    @Test
    public void testLastTaskID(){
        int prevLastTaskId = Task.getLastTaskID();
        Task newTask = new Task("Less interesting description.", duration, deviation);
        Assert.assertEquals("Last task ID does not equal the ID of the latest task.", Integer.valueOf(prevLastTaskId + 1), Task.getLastTaskID());
        Assert.assertEquals("ID of the new task does not equal the latest task ID", Task.getLastTaskID(), newTask.getID());
    }

    // TODO
    /*
    @Test (expected = IllegalArgumentException.class)
    public void testInvalidEndTime(){
        String startTime = "10/03/2018 10:49";
        String invalidEndTime = "10/03/2018 10:48";
        Task invalidTask = new Task("description 123", duration, deviation, startTime, invalidEndTime);
    }
    */

    @Test
    public void testUpdateStatus(){
        Task updateStatusTask = new Task("description 1234", duration, deviation);
        HashMap<String, String> form = new HashMap<>();
        form.put("startTime", "10/03/2018 23:34");
        form.put("endTime", "11/03/2018 21:34");
        form.put("status", "FINISHED");

        Assert.assertEquals("The status is not correct.", Status.UNAVAILABLE, updateStatusTask.getStatus());
        updateStatusTask.updateStatus(form);
        Assert.assertEquals("The start time is not correctly updated.", "10/03/2018 23:34", updateStatusTask.getStartTime().format(dateFormatter));
        Assert.assertEquals("The end time is not correctly updated.", "11/03/2018 21:34", updateStatusTask.getEndTime().format(dateFormatter));
        Assert.assertEquals("The status is not correctly updated.", Status.FINISHED, updateStatusTask.getStatus());

        // TODO: advance time implementeren en shit zodat status op available komt
        // TODO deze test ook nog eens nagaan
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInvalidUpdateStatus(){
        HashMap<String, String> form = new HashMap<>();
        form.put("startTime", "10/03/2018 10:49");
        form.put("endTime", "10/03/2018 10:48");
        form.put("status", "AVAILABLE");
        task.updateStatus(form);
    }


    @Test
    public void testSetAlternative(){
        Assert.assertEquals("There is already an alternative", null, task.getAlternative());

        Task alternative = new Task("Alternative interesting description.", duration, deviation);
        task.setAlternative(alternative);
        Assert.assertEquals("The alternative does not equal the newly added alternative", alternative, task.getAlternative());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInvalidSetAlternativeRoot(){
        task.setAlternative(task);
    }

    @Test
    public void testAddDepedency_RemoveDepedency(){
        ArrayList<Task> dependecies = task.getDependencies();
        Assert.assertEquals("The depedencies list is not empty", 0, dependecies.size());

        Task dependency = new Task("Another interesting description.", duration, deviation);
        task.addDependency(dependency);
        dependecies = task.getDependencies();
        Assert.assertEquals("The depedencies list does not contains 1 depedency", 1, dependecies.size());
        Assert.assertEquals("The depedency in the list is not the newly added depedency", dependency, dependecies.get(0));

        task.removeDependency(dependency);
        dependecies = task.getDependencies();
        Assert.assertEquals("The dependencies list is not empty", 0, dependecies.size());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInvalidAddDependencyRoot(){
        task.addDependency(task);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInvalidRemoveDependency(){
        task.removeDependency(task);
    }

    private static Task root;
    private static Task alternative1_3;
    private static Task alternative1_2_1;
    private static Task dependency1_2;
    private static Task dependency1_1_3;

    @BeforeClass
    public static void setupContainsLoop(){
        String estimatedDuration = "5";
        String acceptableDeviation = "0.2356";
        String startTime = "11/03/2018 01:36";
        String endTime = "11/03/2018 01:45";

        root = new Task("root description", estimatedDuration, acceptableDeviation);

        Task dependency1_1 = new Task ("dependency 1_1 description", estimatedDuration, acceptableDeviation);
        dependency1_2 = new Task ("dependency 1_2 description", estimatedDuration, acceptableDeviation);
        Task dependency1_3 = new Task ("dependency 1_3 description", estimatedDuration, acceptableDeviation);

        root.addDependency(dependency1_1);
        root.addDependency(dependency1_2);
        root.addDependency(dependency1_3);

        Task alternative1_1 = new Task ("alternative 1_1 description", estimatedDuration, acceptableDeviation);
        Task alternative1_2 = null;
        alternative1_3 = new Task ("alternative 1_3 description", estimatedDuration, acceptableDeviation);

        dependency1_1.setAlternative(alternative1_1);
        dependency1_2.setAlternative(alternative1_2);
        dependency1_3.setAlternative(alternative1_3);

        Task dependency1_1_1 = new Task ("dependency 1_1_1 description", estimatedDuration, acceptableDeviation);
        Task dependency1_1_2 = new Task ("dependency 1_1_2 description", estimatedDuration, acceptableDeviation);
        dependency1_1_3 = new Task ("dependency 1_1_3 description", estimatedDuration, acceptableDeviation);

        dependency1_1.addDependency(dependency1_1_1);
        dependency1_1.addDependency(dependency1_1_2);
        dependency1_1.addDependency(dependency1_1_3);

        Task alternative1_1_1 = new Task ("alternative 1_1_1 description", estimatedDuration, acceptableDeviation);
        Task alternative1_1_2 = new Task ("alternative 1_1_1 description", estimatedDuration, acceptableDeviation);
        Task alternative1_1_3 = null;

        dependency1_1_1.setAlternative(alternative1_1_1);
        dependency1_1_2.setAlternative(alternative1_1_2);
        dependency1_1_3.setAlternative(alternative1_1_3);

        Task dependency1_2_1 = new Task ("dependency 1_2_1 description", estimatedDuration, acceptableDeviation);
        Task dependency1_2_2 = new Task ("dependency 1_2_2 description", estimatedDuration, acceptableDeviation);

        dependency1_2.addDependency(dependency1_2_1);
        dependency1_2.addDependency(dependency1_2_2);

        alternative1_2_1 = new Task ("alternative 1_2_1 description", estimatedDuration, acceptableDeviation);
        Task alternative1_2_2 = new Task ("alternative 1_2_2 description", estimatedDuration, acceptableDeviation);

        dependency1_2_1.setAlternative(alternative1_2_1);
        dependency1_2_2.setAlternative(alternative1_2_2);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testIllegalSetAlternativeRecursive1(){
        root.setAlternative(alternative1_3);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testIllegalSetAlternativeRecursive2(){
        root.setAlternative(alternative1_2_1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testIllegalAddDependencyRecursive1(){
        root.addDependency(dependency1_2);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testIllegalAddDependencyRecursive2(){
        root.addDependency(dependency1_1_3);
    }

    @Test
    public void testGetTaskDetails(){
        HashMap<String, String> taskDetails = task.getTaskDetails();

        //Assert.assertEquals();
    }

    @Test
    public void testCompareTo(){
        Assert.fail("Not yet implemented");
    }

    // TODO (xml testen)


}
