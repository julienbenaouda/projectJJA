package test.backend.resource;

import org.junit.Assert;
import org.junit.Test;
import taskman.backend.resource.Plan;
import taskman.backend.resource.ResourceManager;
import taskman.backend.resource.ResourceType;
import taskman.backend.task.Task;

public class PlanTest {

    private static ResourceManager resourceManager;

    @Test
    public void setUp(){
        resourceManager = new ResourceManager();
    }

    @Test
    public void planTest(){
        Task task = new Task("task", "descr", 23, 0.01);
        Plan plan = new Plan(task);
        Assert.assertEquals("The task is not assigned to the plan.", task, plan.getTask());
    }

    @Test
    public void addRequirementTest(){
        Task task = new Task("task", "descr", 23, 0.01);
        Plan plan = new Plan(task);
        ResourceType resourceType = new ResourceType("car");
        plan.addRequirement(resourceType, 3);
        Assert.assertEquals("The resource type is not correct set.", (Integer) 3, plan.getRequirements().get(resourceType));
    }

}
