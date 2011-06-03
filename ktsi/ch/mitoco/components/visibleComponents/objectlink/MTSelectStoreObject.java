package ch.mitoco.components.visibleComponents.objectlink;

/**
 * This Class store selected Pair for links and other functions.
 * 
 * @author tandrich
 *
 */
public class MTSelectStoreObject implements Cloneable {

	/**  startObjectID.	 */
	private int startObjectID;
	
	/**  endObjectID.	 */
	private int endObjectID;
	
	/**  endObjectID.	 */
	private boolean valid = false;
	
	/** 
	 * Standard Constructor.
	 * 
	 */
	public MTSelectStoreObject() {
		
	}
	
	/**
	 * 
	 * Constructor. 
	 * 
	 * @param startObjectID
	 * @param endObjectID
	 * @param valid
	 */
	public MTSelectStoreObject(int startObjectID, int endObjectID, boolean valid) {
		this.startObjectID = startObjectID;
		this.endObjectID = endObjectID;
		this.valid = valid;
	}

	/**
	 * 
	 * 
	 * @return startObjectID int
	 */
	public final int getStartObjectID() {
		return startObjectID;
	}

	public void setStartObjectID(int startObjectID) {
		this.startObjectID = startObjectID;
	}

	public int getEndObjectID() {
		return endObjectID;
	}

	public void setEndObjectID(int endObjectID) {
		this.endObjectID = endObjectID;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
	

	
}
