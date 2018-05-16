package taskman.backend.simulation;

import taskman.backend.branchOffice.BranchOfficeManager;
import taskman.backend.importexport.ImportExportException;
import taskman.backend.importexport.XmlObject;
import taskman.backend.time.Clock;
import taskman.backend.user.User;

/**
 * Class representing a simulation.
 * If the user is running a simulation, this class holds a copy of the old system. It also contains methods for starting, cancelling and keeping a simulation. 
 *
 * @author Jeroen Van Der Donckt, Alexander Braekevelt, Julien Benaouda
 */
public class SimulationManager {

    /**
     * Creates a new simulation.
     */
    public SimulationManager(){};

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
     * @throws IllegalArgumentException if the user is not a project manager
     */
    public void startSimulation(BranchOfficeManager branchOfficeManager, Clock clock, User user) throws ImportExportException {
    	if(!user.getUserType().equals("project manager")) {
    		throw new IllegalArgumentException("The user is not allowed to perform this action");
    	}
        XmlObject xmlObject = new XmlObject(branchOfficeManager, clock);
        setPreviousState(xmlObject.toXMLString());
    }

    /**
     * Cancels the simulation.
     * @return the xml object used to reconstruct the controller
     * @post the system its state is set back to the previous state of the simulation
     * @throws IllegalStateException if there is no previous state in the simulation
     * @throws ImportExportException if there occurs an error while resetting the system to the xml strong
     */
    public XmlObject cancelSimulation() throws IllegalStateException, ImportExportException {
        if (getPreviousState() != null){
            return XmlObject.fromXMLString(getPreviousState());
        }
        else {
            throw new IllegalStateException("There is no previous state of the system saved.");
        }
    }

    /**
     * Executes the simulation.
     * The simulation is not revoked.
     *
     * @post the previous state of the simulation is set to null
     */
    public void executeSimulation() {
        setPreviousState(null);
    }
}
