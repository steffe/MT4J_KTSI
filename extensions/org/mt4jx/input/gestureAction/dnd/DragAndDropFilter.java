package org.mt4jx.input.gestureAction.dnd;

import org.mt4j.components.MTComponent;

/**
 * <p>
 * Interface for filters which may be applied to 
 * components involved in drag and drop operations.
 * </p>
 * 
 * @author R. Scarberry
 *
 */
public interface DragAndDropFilter {

	/**
	 * Returns true if the specified component is acceptable 
	 * for the drag and drop operation.  
	 * 
	 * @param component
	 * 
	 * @return true or false.
	 */
	boolean dndAccept(MTComponent component);
	
}
