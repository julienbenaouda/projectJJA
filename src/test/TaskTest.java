package test;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import taskman.Task;
import taskman.TaskStatus;
import taskman.TimeSpan;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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

        root = new Task("root description", estimatedDuration, acceptableDeviation) {
            private TimeSpan timeSpan;

            @Override
            public void updateStatus(TimeSpan timeSpan){
                this.timeSpan = timeSpan;
            }

            @Override
            public TimeSpan getLastTimeSpan(){
                return timeSpan;
            }

        };

        TimeSpan timeSpan = new TimeSpan(TaskStatus.FAILED);
        root.updateStatus(timeSpan);

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
    }



    @Test
    public void testTask(){
        Task task = new Task("Very interesting description.", 22, 0.15);
        Assert.assertEquals("The descriptions are not equal", "Very interesting description.", task.getDescription());
        Assert.assertEquals("The estimated durations are not equal", 22, task.getEstimatedDuration());
        Assert.assertEquals("The acceptable deviations are not equal", 0.15, task.getAcceptableDeviation(), 0);
        Assert.assertEquals("The status is not available", TaskStatus.AVAILABLE, task.getLastTimeSpan().getStatus());
    }

    @Test
    public void testUpdateStatus(){
        Task updateStatusTask = new Task("Very inspiring description.", 33, 1.0589);
        LocalDateTime starTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plus(456, ChronoUnit.SECONDS);
        TimeSpan timeSpan = new TimeSpan(starTime, endTime, TaskStatus.FINISHED);

        Assert.assertEquals("The status is not available", TaskStatus.AVAILABLE, updateStatusTask.getLastTimeSpan().getStatus());
        Assert.assertEquals("The start time is not null", null, updateStatusTask.getLastTimeSpan().getStartTime());
        Assert.assertEquals("The end time is not null", null, updateStatusTask.getLastTimeSpan().getEndTime());
        updateStatusTask.updateStatus(timeSpan);
        Assert.assertEquals("The status is not finished", TaskStatus.FINISHED, updateStatusTask.getLastTimeSpan().getStatus());
        Assert.assertEquals("The start time is not correctly set", starTime, updateStatusTask.getLastTimeSpan().getStartTime());
        Assert.assertEquals("The end time is not correctly set", endTime, updateStatusTask.getLastTimeSpan().getEndTime());
        // Firt 3 tests above this comment are not necessary but I will leave them here
        Assert.assertEquals("The time span is not correctly set", timeSpan, updateStatusTask.getLastTimeSpan());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInvalidEndTimeUpdateStatus(){
        Task invalidUpdateStatusTask = new Task("description 1234", 15, 0.13);
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = LocalDateTime.now().plus(35, ChronoUnit.MINUTES);
        TimeSpan timeSpan = new TimeSpan(startTime, endTime, TaskStatus.FAILED);

        invalidUpdateStatusTask.updateStatus(timeSpan);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInvalidStatusUpdateStatus(){
        Task invalidUpdateStatusTask = new Task("description 1234", 15, 0.13);
        LocalDateTime starTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plus(456, ChronoUnit.SECONDS);
        TimeSpan timeSpan = new TimeSpan(starTime, endTime, TaskStatus.UNAVAILABLE);

        invalidUpdateStatusTask.updateStatus(timeSpan);
    }

    @Test
    public void testAlternative(){
        Task setAlternative = new Task("description of this task", 24, 1){
            private TimeSpan timeSpan;

            @Override
            public void updateStatus(TimeSpan timeSpan){
                this.timeSpan = timeSpan;
            }

            @Override
            public TimeSpan getLastTimeSpan(){
                return timeSpan;
            }

        };
        TimeSpan timeSpan = new TimeSpan(TaskStatus.FAILED);
        setAlternative.updateStatus(timeSpan);

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

    @Test (expected = IllegalStateException.class)
    public void testInvalidSetAlternativeToItself(){
        Task setAlternative = new Task("description of this task", 24, 1){
            private TimeSpan timeSpan;

            @Override
            public void updateStatus(TimeSpan timeSpan){
                this.timeSpan = timeSpan;
            }

            @Override
            public TimeSpan getLastTimeSpan(){
                return timeSpan;
            }

        };
        TimeSpan timeSpan = new TimeSpan(TaskStatus.FAILED);
        setAlternative.updateStatus(timeSpan);
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
