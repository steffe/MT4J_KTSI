package org.mt4jx.input.gestureAction.dnd;

import org.mt4j.components.MTComponent;

public interface DragAndDropTarget extends DropTarget {
	/**
	 * Is called by DragAndDropAction, when a component is dragged above the MTDropTarget. 
	 * @param enteredComponent The MTComponent that has entered the DropTarget
	 */
	public void componentEntered(MTComponent enteredComponent);
	/**
	 * Is called by DragAndDropAction, when a component is dragged away from above the MTDropTarget. 
	 * @param exitedComponent The component that exited from the DropTarget
	 */
	public void componentExited(MTComponent exitedComponent);
}
