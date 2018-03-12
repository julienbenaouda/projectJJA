package test;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import taskman.Status;
import taskman.Task;
import taskman.XmlObject;

import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.HashMap;

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
    private final static DateTimeFormatter dateFormatter =DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm").withResolverStyle(ResolverStyle.STRICT);

    private static Task root;
    private static Task alternative1_3;
    private static Task alternative1_2_1;
    private static Task dependency1_2;
    private static Task dependency1_1_3;
    private static Task alternative1_3d;

    @BeforeClass
    public static void setUp(){
        duration = "22";
        deviation = "0.15";
        task = new Task("Very interesting description.", duration, deviation);

        String estimatedDuration = "5";
        String acceptableDeviation = "0.2356";
        String startTime = "11/03/2018 01:36";
        String endTime = "11/03/2018 01:45";

        root = new Task("root description", estimatedDuration, acceptableDeviation) {
            private Status status;

            @Override
            public void updateStatus(HashMap<String, String> form) {
                this.status = Status.fromString(form.get("status"));
            }

            @Override
            public Status getStatus(){
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
    }

    @Test
    public void testTask(){
        Assert.assertEquals("The descriptions are not equal", "Very interesting description.", task.getDescription());
        Assert.assertEquals("The estimated durations are not equal", duration, task.getEstimatedDuration());
        Assert.assertEquals("The acceptable deviations are not equal", 0.15, task.getAcceptableDeviation(), 0);
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
        Assert.assertEquals("The estimated durations are not equal", "1076", taskHM.getEstimatedDuration());
        Assert.assertEquals("The acceptable deviations are not equal", 1.34 , taskHM.getAcceptableDeviation(), 0);
    }


    @Test
    public void testLastTaskID(){
        int prevLastTaskId = Task.getLastTaskID();
        Task newTask = new Task("Less interesting description.", duration, deviation);
        Assert.assertEquals("Last task ID does not equal the ID of the latest task.", Integer.valueOf(prevLastTaskId + 1), Task.getLastTaskID());
        Assert.assertEquals("ID of the new task does not equal the latest task ID", Task.getLastTaskID(), newTask.getID());
    }

    // TODO

    @Test (expected = IllegalArgumentException.class)
    public void testInvalidEndTime(){
        Task invalidUpdateStatusTask = new Task("description 1234", duration, deviation);
        HashMap<String, String> form = new HashMap<>();
        form.put("startTime", "10/03/2018 10:49");
        form.put("endTime", "10/03/2018 10:48");
        form.put("status", "FINISHED");

        invalidUpdateStatusTask.updateStatus(form);
    }


    @Test
    public void testUpdateStatus(){
        Task updateStatusTask = new Task("description 1234", duration, deviation);
        HashMap<String, String> form = new HashMap<>();
        form.put("startTime", "10/03/2018 23:34");
        form.put("endTime", "11/03/2018 21:34");
        form.put("status", "FINISHED");

        Assert.assertEquals("The status is not correct.", Status.AVAILABLE, updateStatusTask.getStatus());
        updateStatusTask.updateStatus(form);
        Assert.assertEquals("The start time is not correctly updated.", "10/03/2018 23:34", updateStatusTask.getStartTime());
        Assert.assertEquals("The end time is not correctly updated.", "11/03/2018 21:34", updateStatusTask.getEndTime());
        Assert.assertEquals("The status is not correctly updated.", Status.FINISHED, updateStatusTask.getStatus());

        // TODO: advance time implementeren en shit zodat status op available komt
        // TODO deze test ook nog eens nagaan
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInvalidTimeUpdateStatus(){
        HashMap<String, String> form = new HashMap<>();
        form.put("startTime", "10/03/2018 10:49");
        form.put("endTime", "10/03/2018 10:48");
        form.put("status", "FINISHED");
        task.updateStatus(form);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInvalidStatusUpdateStatus(){
        HashMap<String, String> form = new HashMap<>();
        form.put("startTime", "10/03/2018 23:34");
        form.put("endTime", "11/03/2018 21:34");
        form.put("status", "AVAILABLE");
        task.updateStatus(form);
    }


    @Test
    public void testSetAlternative(){
        Assert.assertEquals("There is already an alternative", null, task.getAlternative());

        Task setAlternative = new Task("description of this task", duration, deviation){
            private Status status;

            @Override
            public void updateStatus(HashMap<String, String> form) {
                this.status = Status.fromString(form.get("status"));
            }

            @Override
            public Status getStatus(){
                return status;
            }
        };

        Task alternative = new Task("Alternative interesting description.", duration, deviation);

        HashMap<String, String> form = new HashMap<>();
        form.put("status", "FAILED");
        setAlternative.updateStatus(form);

        setAlternative.setAlternative(alternative);
        Assert.assertEquals("The alternative does not equal the newly added alternative", alternative, setAlternative.getAlternative());
    }

    @Test (expected = IllegalStateException.class)
    public void testInvaladSetAlternative(){
        Task alternative = new Task("alternative task", duration, deviation);
        task.setAlternative(alternative);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInvalidSetAlternativeRoot(){
        Assert.assertEquals("There is already an alternative", null, task.getAlternative());

        Task setAlternative = new Task("description of this task", duration, deviation){
            private Status status;

            @Override
            public void updateStatus(HashMap<String, String> form) {
                this.status = Status.fromString(form.get("status"));
            }

            @Override
            public Status getStatus(){
                return status;
            }
        };

        Task alternative = new Task("Alternative interesting description.", duration, deviation);

        HashMap<String, String> form = new HashMap<>();
        form.put("status", "FAILED");
        setAlternative.updateStatus(form);
        setAlternative.setAlternative(setAlternative);
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


    @Test (expected = IllegalArgumentException.class)
    public void testIllegalSetAlternativeRecursive1(){
        HashMap<String, String> form = new HashMap<>();
        form.put("status", "FAILED");
        root.updateStatus(form);
        root.setAlternative(alternative1_3);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testIllegalSetAlternativeRecursive2(){
        HashMap<String, String> form = new HashMap<>();
        form.put("status", "FAILED");
        root.updateStatus(form);
        root.setAlternative(alternative1_2_1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testIllegalSetAlternativeRecursive3(){
        HashMap<String, String> form = new HashMap<>();
        form.put("status", "FAILED");
        root.updateStatus(form);
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

    @Test
    public void testGetTaskDetails(){
        HashMap<String, String> taskDetails = task.getTaskDetails();

        Assert.assertEquals("The task ID is not correct", task.getID().toString(), taskDetails.get("id"));
        Assert.assertEquals("The descriptions are not equal", "Very interesting description.", taskDetails.get("description"));
        Assert.assertEquals("The estimated durations are not equal", duration, taskDetails.get("estimatedDuration"));
        Assert.assertEquals("The acceptable deviations are not equal", deviation, taskDetails.get("acceptableDeviation"));
    }

    @Test
    public void testCompareTo(){
        Assert.assertEquals("The task is not equal to itself.", 0, task.compareTo(task));
        Assert.assertEquals("The task is not equal to itself ID.", 0, task.compareTo(task.getID()));

        Assert.assertEquals("The task it's ID is not smaller than root.", -1, task.compareTo(root));
        Assert.assertEquals("The task it's ID is not smaller than root ID.", -1, task.compareTo(root.getID()));

        Assert.assertEquals("The dependency it's ID is not larger than dependent.", 1, dependency1_1_3.compareTo(dependency1_2));
        Assert.assertEquals("The dependency it's ID is not smaller than dependent ID.", 1, dependency1_1_3.compareTo(dependency1_2.getID()));
    }


    @Test
    public void testExportXML()
    {
        try {
            Task taskXml = new Task("task description", "103", "0.32");
            XmlObject object = new XmlObject();
            taskXml.addToXml(object.addXmlObject("task"));
            Task tNew = Task.getFromXml(object.getXmlObjects("task").get(0));
            Assert.assertEquals("ID is not equal.", taskXml.getID(), tNew.getID());
            Assert.assertEquals("Description is not equal.",taskXml.getDescription(), tNew.getDescription());
            Assert.assertEquals("Estimated duration is not equal.", taskXml.getEstimatedDuration(), tNew.getEstimatedDuration());
            Assert.assertEquals("Acceptable deviation is not equal.", taskXml.getAcceptableDeviation(), tNew.getAcceptableDeviation());
            Assert.assertEquals("Status is not equal.", taskXml.getStatus(), tNew.getStatus());
            Assert.assertEquals("Start time is not equal.", taskXml.getStartTime(), tNew.getStartTime());
            Assert.assertEquals("End time is not equal", taskXml.getEndTime(), tNew.getEndTime());
        } catch (Exception e) {
            // Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void getDelayTest() {
        HashMap<String,String> taskForm = Task.getCreationForm();
        taskForm.put("description", "test task description");
        taskForm.put("estimatedDuration", "60");
        taskForm.put("acceptableDeviation", "0.1");
        Task delayTask = new Task(taskForm);

        HashMap<String, String> updateForm = Task.getUpdateStatusForm();
        updateForm.put("status", "FINISHED");
        updateForm.put("startTime", "01/01/2000 00:00");
        updateForm.put("endTime", "01/01/2000 02:00");
        delayTask.updateStatus(updateForm);

        Assert.assertEquals( "Wrong delay!","60", delayTask.getDelay());
    }

}
