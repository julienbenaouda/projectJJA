package taskman.backend.simulation;

import taskman.backend.importexport.ImportExportException;
import taskman.backend.importexport.XmlObject;
import taskman.backend.project.ProjectOrganizer;
import taskman.backend.resource.ResourceManager;
import taskman.backend.time.Clock;
import taskman.backend.user.UserManager;

/**
 * Class representing a simulation.
 *
 * @author Jeroen Van Der Donckt, Alexander Braekevelt, Julien Benaouda
 */
public class Simulation {

    /**
     * Creates a new simulation.
     */
    public Simulation(){};

    /**
     * Represents the previous state of the simulation (an xml string).
     */
    private String previousState;

    /**
     * Returns the previous state of the simulation.
     *
     * @return the previous state of the simulation
     */
    private String getPreviousState(){
        return previousState;
    }

    /**
     * Sets the previous state of the simulation to the given state.
     *
     * @param previousState the previous state of the simulation
     * @post the previous state of the system is set to the given state
     */
    private void setPreviousState(String previousState){
        this.previousState = previousState;
    }

    /**
     * Starts the simulation.
     *
     * @param projectOrganizer the project organizer of the system
     * @param userManager the user manager of the system
     * @param resourceManager the resource manager of the system
     * @param clock the clock of the system
     * @post the previous state of the simulation is set to an xml string representing the system its state
     * @throws ImportExportException if there occurs an error while creating the xml string
     */
    public void start(ProjectOrganizer projectOrganizer, UserManager userManager, ResourceManager resourceManager, Clock clock) throws ImportExportException {
        XmlObject xmlObject = new XmlObject(projectOrganizer, userManager, resourceManager, clock);
        setPreviousState(xmlObject.toXMLString());
    }

    /**
     * Cancels the simulation.
     *
     * @post the system its state is set back to the previous state of the simulation
     * @throws IllegalStateException if there is no previous state in the simulation
     * @throws ImportExportException if there occurs an error while resetting the system to the xml strong
     */
    public void cancel() throws IllegalStateException, ImportExportException {
        if (getPreviousState() != null){
            XmlObject.fromXMLString(getPreviousState());
        }
        else {
            throw new IllegalStateException("There is no previous state of the system saved.");
        }
    }

    /**
     * Executes the simulation.
     * The simulation is not revoked.
     *
     * @pos the previous state of the simulation is set to null
     */
    public void execute() {
        setPreviousState(null);
    }
}
