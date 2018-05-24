package taskman.backend.branchOffice;

import java.util.ArrayList;
import java.util.List;

/**
 * this class represents the branch office manager.
 * It keeps a list of all branch offices and a reference to the current branch office.
 * @author Julien Benaouda
 *
 */
public class BranchOfficeManager {

	/**
	 * creates a new branch office manager
	 */
	public BranchOfficeManager() {
		branchOffices = new ArrayList<>();
	}
	
	/**
	 * represents the list of branch offices
	 */
	private List<BranchOffice> branchOffices;
	
	/**
	 * adds a branchOffice to the list of branch offices
	 * @param branchOffice the new branch office to add
	 */
	private void addBranchOffice(BranchOffice branchOffice) {
		branchOffices.add(branchOffice);
	}
	
	/**
	 * create a new branch office with the given name
	 * @param name the name of the branch office
	 * @post adds the branch office to the list of branch offices
	 * @throws IllegalArgumentException when a branch office with the given name already exists
	 */
	public void createBranchOffice(String name) throws IllegalArgumentException {
		for(BranchOffice b: branchOffices) {
			if(b.getName().equals(name)) {
				throw new IllegalArgumentException("A branch office with the given name already exists. Please try another name!");
			}
		}
		BranchOffice b = new BranchOffice(name);
		addBranchOffice(b);
	}
	
	/**
	 * returns a list of all branch offices
	 */
	public List<BranchOffice> getBranchOffices() {
		return new ArrayList<>(branchOffices);
	}
	
	/**
	 * changes the current branch office to the given branch office
	 * @param branchOffice the branch office to be set as new branch office
	 * @throws IllegalArgumentException when the given branch office is nulll
	 */
	public void changeCurrentBranchOffice(BranchOffice branchOffice) throws IllegalArgumentException {
		if(branchOffice == null) {
			throw new IllegalArgumentException("The branch office can't be null");
		}
		setCurrentBranchOffice(branchOffice);
	}

	/**
	 * deactivates the current branch office
	 * @post no current branch office is present
	 */
	public void deactivateCurrentBranchOffice() {
		setCurrentBranchOffice(null);
	}
	
	/**
	 * represents the current branch office
	 */
	private BranchOffice currentBranchOffice = null;

	/**
	 * returns the current BranchOffice of the system
	 * @throws IllegalStateException if no current branch office is found.
	 */
	public BranchOffice getCurrentBranchOffice() throws IllegalStateException {
		if (currentBranchOffice == null) {
			throw new IllegalStateException("No current branch office is found!");
		} else {
			return currentBranchOffice;
		}
	}

	/**
	 * sets the current branch office to the given branch office
	 * @param currentBranchOffice the new BranchOffice to set
	 */
	private void setCurrentBranchOffice(BranchOffice currentBranchOffice) {
		this.currentBranchOffice = currentBranchOffice;
	}

}
