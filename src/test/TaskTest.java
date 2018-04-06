package test;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import taskman.Task;
import taskman.TaskStatus;
import taskman.TimeSpan;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * This is a test class for the Task class.
 * @author Jeroen Van Der Donckt
 *
 */
public class TaskTest {

    private static Task root;
    private static Task alternative1_3;
    private static Task alternative1_2_1;
    private static Task dependency1_2;
    private static Task dependency1_1_3;
    private static Task alternative1_3d;

    @BeforeClass
    public static void setUp(){
        long duration = 22;
        double deviation = 0.15;
        Task task = new Task("Very interesting description.", duration, deviation);

        long estimatedDuration = 5;
        double acceptableDeviation = 0.2356;

        root = new Task("root description", estimatedDuration, acceptableDeviation){
            private TaskStatus status;

            @Override
            public void updateStatus(TimeSpan timeSpan, TaskStatus status) {
                this.status = status;
            }

            @Override
            public TaskStatus getStatus(){
                return status;
            }
        };


        Task dependency1_1 = new Task ("dependency 1_1 description", estimatedDuration, acceptableDeviation){
            private Task alternative;

            @Override
            public void setAlternative(Task alternative){
                this.alternative = alternative;
            }

            @Override
            public Task getAlternative(){ return alternative; }
        };
        dependency1_2 = new Task ("dependency 1_2 description", estimatedDuration, acceptableDeviation){
            private Task alternative;

            @Override
            public void setAlternative(Task alternative){
                this.alternative = alternative;
            }

            @Override
            public Task getAlternative(){ return alternative; }
        };
        Task dependency1_3 = new Task ("dependency 1_3 description", estimatedDuration, acceptableDeviation){
            private Task alternative;

            @Override
            public void setAlternative(Task alternative){
                this.alternative = alternative;
            }

            @Override
            public Task getAlternative(){ return alternative; }
        };

        root.addDependency(dependency1_1);
        root.addDependency(dependency1_2);
        root.addDependency(dependency1_3);

        Task alternative1_1 = new Task ("alternative 1_1 description", estimatedDuration, acceptableDeviation);
        Task alternative1_2 = null;
        alternative1_3 = new Task ("alternative 1_3 description", estimatedDuration, acceptableDeviation){
            private Task alternative;
            @Override
            public void setAlternative(Task alternative){
                this.alternative = alternative;
            }

            @Override
            public Task getAlternative(){ return alternative; }
        };

        Task alternative1_3a = new Task ("alternative 1_3a description", estimatedDuration, acceptableDeviation) {
            private Task alternative;
            @Override
            public void setAlternative(Task alternative){
                this.alternative = alternative;
            }

            @Override
            public Task getAlternative(){ return alternative; }
        };
        Task alternative1_3b = new Task ("alternative 1_3b description", estimatedDuration, acceptableDeviation){
            private Task alternative;
            @Override
            public void setAlternative(Task alternative){
                this.alternative = alternative;
            }

            @Override
            public Task getAlternative(){ return alternative; }
        };
        Task alternative1_3c = new Task ("alternative 1_3c description", estimatedDuration, acceptableDeviation){
            private Task alternative;
            @Override
            public void setAlternative(Task alternative){
                this.alternative = alternative;
            }

            @Override
            public Task getAlternative(){ return alternative; }
        };
        alternative1_3d = new Task ("alternative 1_3d description", estimatedDuration, acceptableDeviation);
        alternative1_3.setAlternative(alternative1_3a);
        alternative1_3a.setAlternative(alternative1_3b);
        alternative1_3b.setAlternative(alternative1_3c);
        alternative1_3c.setAlternative(alternative1_3d);

        dependency1_1.setAlternative(alternative1_1);
        dependency1_2.setAlternative(alternative1_2);
        dependency1_3.setAlternative(alternative1_3);

        Task dependency1_1_1 = new Task ("dependency 1_1_1 description", estimatedDuration, acceptableDeviation){
            private Task alternative;
            @Override
            public void setAlternative(Task alternative){
                this.alternative = alternative;
            }

            @Override
            public Task getAlternative(){ return alternative; }
        };
        Task dependency1_1_2 = new Task ("dependency 1_1_2 description", estimatedDuration, acceptableDeviation){
            private Task alternative;
            @Override
            public void setAlternative(Task alternative){
                this.alternative = alternative;
            }

            @Override
            public Task getAlternative(){ return alternative; }
        };
        dependency1_1_3 = new Task ("dependency 1_1_3 description", estimatedDuration, acceptableDeviation){
            private Task alternative;
            @Override
            public void setAlternative(Task alternative){
                this.alternative = alternative;
            }

            @Override
            public Task getAlternative(){ return alternative; }
        };

        dependency1_1.addDependency(dependency1_1_1);
        dependency1_1.addDependency(dependency1_1_2);
        dependency1_1.addDependency(dependency1_1_3);

        Task alternative1_1_1 = new Task ("alternative 1_1_1 description", estimatedDuration, acceptableDeviation);
        Task alternative1_1_2 = new Task ("alternative 1_1_1 description", estimatedDuration, acceptableDeviation);
        Task alternative1_1_3 = null;

        dependency1_1_1.setAlternative(alternative1_1_1);
        dependency1_1_2.setAlternative(alternative1_1_2);
        dependency1_1_3.setAlternative(alternative1_1_3);

        Task dependency1_2_1 = new Task ("dependency 1_2_1 description", estimatedDuration, acceptableDeviation){
            private Task alternative;
            @Override
            public void setAlternative(Task alternative){
                this.alternative = alternative;
            }

            @Override
            public Task getAlternative(){ return alternative; }
        };
        Task dependency1_2_2 = new Task ("dependency 1_2_2 description", estimatedDuration, acceptableDeviation){
            private Task alternative;
            @Override
            public void setAlternative(Task alternative){
                this.alternative = alternative;
            }

            @Override
            public Task getAlternative(){ return alternative; }
        };

        dependency1_2.addDependency(dependency1_2_1);
        dependency1_2.addDependency(dependency1_2_2);

        alternative1_2_1 = new Task ("alternative 1_2_1 description", estimatedDuration, acceptableDeviation);
        Task alternative1_2_2 = new Task ("alternative 1_2_2 description", estimatedDuration, acceptableDeviation);

        dependency1_2_1.setAlternative(alternative1_2_1);
        dependency1_2_2.setAlternative(alternative1_2_2);

        TimeSpan timeSpanNull = new TimeSpan(null, null);
        root.updateStatus(timeSpanNull,TaskStatus.FAILED);
    }


    @Test
    public void testTask(){
        Task task = new Task("Very interesting description.", 22, 0.15);
        Assert.assertEquals("The descriptions are not equal", "Very interesting description.", task.getDescription());
        Assert.assertEquals("The estimated durations are not equal", 22, task.getEstimatedDuration());
        Assert.assertEquals("The acceptable deviations are not equal", 0.15, task.getAcceptableDeviation(), 0);
        Assert.assertEquals("There is no time span added", null, task.getTimeSpan());
        Assert.assertEquals("The status is not available", true, task.isAvailable());
    }

    @Test
    public void testIsFinished(){
        Task taskNotFinished = new Task("blabla", 13, 0.23);
        Assert.assertEquals("The status is finished", false, taskNotFinished.isFinished());

        Task taskFinished = new Task("blablabla", 33, 0.08){
            private TaskStatus status;

            @Override
            public void updateStatus(TimeSpan timeSpan ,TaskStatus status) {
                this.status = status;
            }

            @Override
            public TaskStatus getStatus(){
                return status;
            }
        };
        TimeSpan timeSpanNull = new TimeSpan(null, null);
        taskFinished.updateStatus(timeSpanNull, TaskStatus.FINISHED);
        Assert.assertEquals("The status is no finished", true, taskFinished.isFinished());
    }

    @Test
    public void testIsAvailable(){
        Task availableTask = new Task("available task", 23, 2);
        Assert.assertEquals("The task is not available", true, availableTask.isAvailable());
        Task dependencyFinished = new Task("dependency finished", 23, 1.45){
            private TaskStatus status;

            @Override
            public void updateStatus(TimeSpan timeSpan ,TaskStatus status) {
                this.status = status;
            }

            @Override
            public TaskStatus getStatus(){
                return status;
            }
        };
        availableTask.addDependency(dependencyFinished);
        TimeSpan timeSpanNull = new TimeSpan(null, null);

        dependencyFinished.updateStatus(timeSpanNull, TaskStatus.FINISHED);
        Assert.assertEquals("The task is not available", true, availableTask.isAvailable());
        Task dependencyFailed =  new Task("dependency failed", 23, 1.45){
            private TaskStatus status;

            @Override
            public void updateStatus(TimeSpan timeSpan ,TaskStatus status) {
                this.status = status;
            }

            @Override
            public TaskStatus getStatus(){
                return status;
            }
        };
        dependencyFinished.updateStatus(timeSpanNull, TaskStatus.INACTIVE);
        dependencyFinished.addDependency(dependencyFailed);
        dependencyFinished.updateStatus(timeSpanNull, TaskStatus.FINISHED);
        Task alternativeFinished = new Task("alternative finished", 25, 5.6){
            private TaskStatus status;

            @Override
            public void updateStatus(TimeSpan timeSpan ,TaskStatus status) {
                this.status = status;
            }

            @Override
            public TaskStatus getStatus(){
                return status;
            }
        };
        dependencyFailed.updateStatus(timeSpanNull, TaskStatus.FAILED);
        dependencyFailed.setAlternative(alternativeFinished);
        alternativeFinished.updateStatus(timeSpanNull, TaskStatus.FINISHED);
        Assert.assertEquals("The task is not available", true, availableTask.isAvailable());

        Task unavailableTask = new Task("unavailable task", 789, 1.25);
        Task unavialableTask2 = new Task("unavailable task 2", 4, 0.003);
        unavailableTask.addDependency(unavialableTask2);
        Assert.assertEquals("The task is available", false, unavailableTask.isAvailable());
        unavailableTask.removeDependency(unavialableTask2);
        dependencyFinished.updateStatus(timeSpanNull, TaskStatus.INACTIVE);
        unavailableTask.addDependency(dependencyFinished);
        //dependencyFinished.removeDependency(dependencyFailed);
        dependencyFailed.updateStatus(timeSpanNull, TaskStatus.INACTIVE);
        dependencyFailed.addDependency(unavialableTask2);
        dependencyFinished.updateStatus(timeSpanNull, TaskStatus.FINISHED);
        dependencyFailed.updateStatus(timeSpanNull, TaskStatus.FAILED);
        Assert.assertEquals("The task is available", false, unavailableTask.isAvailable());
        dependencyFailed.updateStatus(timeSpanNull, TaskStatus.INACTIVE);
        dependencyFailed.removeDependency(unavialableTask2);
        dependencyFailed.updateStatus(timeSpanNull, TaskStatus.FAILED);
        dependencyFailed.setAlternative(unavialableTask2);
        Assert.assertEquals("The task is available", false, unavailableTask.isAvailable());

    }

    @Test (expected = IllegalStateException.class)
    public void testIllegalStateIsAvailable(){
        Task failedTask = new Task("failed task", 1, 0.03){
            private TaskStatus status;

            @Override
            public void updateStatus(TimeSpan timeSpan ,TaskStatus status) {
                this.status = status;
            }

            @Override
            public TaskStatus getStatus(){
                return status;
            }
        };
        TimeSpan timeSpanNull = new TimeSpan(null, null);
        failedTask.updateStatus(timeSpanNull, TaskStatus.FAILED);
        failedTask.isAvailable();
    }

    @Test
    public void testDelay(){
        Task task = new Task("Description1", 20, 0.5);
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plus(35, ChronoUnit.MINUTES);
        TimeSpan timeSpan = new TimeSpan(startTime, endTime);
        task.updateStatus(timeSpan, TaskStatus.FINISHED);
        Assert.assertEquals("The delay is not correctly calculated", 5, task.getDelay(), 0);
    }

    @Test (expected = IllegalStateException.class)
    public void testIlegalStateDelay(){
        Task task = new Task("Descr", 14, 0.2315);
        task.getDelay();
    }

    @Test
    public void testUpdateStatus(){
        Task updateStatusTask = new Task("Very inspiring description.", 33, 1.0589);
        LocalDateTime starTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plus(456, ChronoUnit.SECONDS);
        TimeSpan timeSpan = new TimeSpan(starTime, endTime);

        Assert.assertEquals("The status is not available", true, updateStatusTask.isAvailable());
        Assert.assertEquals("The time span is not null", null, updateStatusTask.getTimeSpan());
        updateStatusTask.updateStatus(timeSpan, TaskStatus.FINISHED);
        Assert.assertEquals("The status is not finished", TaskStatus.FINISHED, updateStatusTask.getStatus());
        Assert.assertEquals("The start time is not correctly set", starTime, updateStatusTask.getTimeSpan().getStartTime());
        Assert.assertEquals("The end time is not correctly set", endTime, updateStatusTask.getTimeSpan().getEndTime());
        // First 3 tests above this comment are not necessary but I will leave them here
        Assert.assertEquals("The time span is not correctly set", timeSpan, updateStatusTask.getTimeSpan());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInvalidEndTimeUpdateStatus(){
        Task invalidUpdateStatusTask = new Task("description 1234", 15, 0.13);
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = LocalDateTime.now().plus(35, ChronoUnit.MINUTES);
        TimeSpan timeSpan = new TimeSpan(startTime, endTime);

        invalidUpdateStatusTask.updateStatus(timeSpan,  TaskStatus.FAILED);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInvalidStatusUpdateStatus(){
        Task invalidUpdateStatusTask = new Task("description 1234", 15, 0.13);
        LocalDateTime starTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plus(456, ChronoUnit.SECONDS);
        TimeSpan timeSpan = new TimeSpan(starTime, endTime);

        invalidUpdateStatusTask.updateStatus(timeSpan, TaskStatus.INACTIVE);
    }

    @Test
    public void testAlternative(){
        Task setAlternative = new Task("description of this task", 24, 1){
            private TaskStatus status;

            @Override
            public void updateStatus(TimeSpan timeSpan ,TaskStatus status) {
                this.status = status;
            }

            @Override
            public TaskStatus getStatus(){
                return status;
            }
        };
        TimeSpan timeSpanNull = new TimeSpan(null, null);
        setAlternative.updateStatus(timeSpanNull, TaskStatus.FAILED);

        Assert.assertEquals("There is already an alternative", null, setAlternative.getAlternative());
        Task alternative = new Task("DescRiPTioNNNNN", 245, 0.015);
        setAlternative.setAlternative(alternative);
        Assert.assertEquals("The alternative is not correctly set", alternative, setAlternative.getAlternative());
    }

    @Test (expected = IllegalStateException.class)
    public void testInvaladSetAlternative(){
        Task task = new Task("DescRiPTioNNNNN", 245, 0.015);
        Task alternative = new Task("alternative task", 2, 0.25);
        task.setAlternative(alternative);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInvalidSetAlternativeToItself(){
        Task setAlternative = new Task("description of this task", 24, 1){
            private TaskStatus status;

            @Override
            public void updateStatus(TimeSpan timeSpan ,TaskStatus status) {
                this.status = status;
            }

            @Override
            public TaskStatus getStatus(){
                return status;
            }
        };
        TimeSpan timeSpanNull = new TimeSpan(null, null);
        setAlternative.updateStatus(timeSpanNull, TaskStatus.FAILED);
        setAlternative.setAlternative(setAlternative);
    }

    // TODO: loop nog keer testen

    @Test
    public void testAddDepedency_RemoveDepedency(){
        Task task = new Task("DescRiPTioNNNNN", 245, 0.015);
        Assert.assertEquals("The depedencies list is not empty", 0, task.getDependencies().size());

        Task dependency = new Task("Another interesting description.", 2, 0);
        task.addDependency(dependency);
        Assert.assertEquals("The depedencies list does not contains 1 depedency", 1, task.getDependencies().size());
        Assert.assertEquals("The depedency in the list is not the newly added depedency", dependency, task.getDependencies().get(0));

        task.removeDependency(dependency);
        Assert.assertEquals("The dependencies list is not empty", 0, task.getDependencies().size());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testIllegalAddItself(){
        Task task = new Task("DescRiPTioNNNNN", 245, 0.015);
        task.addDependency(task);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testIllegalRemoveDependency(){
        Task task = new Task("DescRiPTioNNNNN", 245, 0.015);
        task.removeDependency(task);
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
    public void testIllegalSetAlternativeRecursive3(){ ;
        root.setAlternative(alternative1_3d);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testIllegalAddDependencyRecursive1(){
        root.addDependency(dependency1_2);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testIllegalAddDependencyRecursive2(){
        root.addDependency(dependency1_1_3);
    }

}
