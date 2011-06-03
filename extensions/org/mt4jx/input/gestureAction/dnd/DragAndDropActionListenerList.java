package org.mt4jx.input.gestureAction.dnd;

import java.util.ArrayList;

import org.mt4j.components.MTComponent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;

/**
 * <p>Convenience class specifically for containing instances of 
 * DragAndDropActionListener.  Itself a DragAndDropActionListener, it forwards calls to
 * its listener methods to all of its members.
 * </p>
 * 
 * @author R.Scarberry
 *
 */
public class DragAndDropActionListenerList extends ArrayList<DragAndDropActionListener> 
implements DragAndDropActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4853052488927421483L;

	@Override
	public void objectDroppedOnTarget(MTComponent droppedObject, DropTarget dt,
			DragEvent de) {
		if (this.size() > 0) {
			// Don't use an iterator, since a listener may remove itself in response to the
			// event and cause a ConcurrentModificationException.
			DragAndDropActionListener[] listeners = this.toArray(new DragAndDropActionListener[this.size()]);
			for (DragAndDropActionListener l: listeners) {
				l.objectDroppedOnTarget(droppedObject, dt, de);
			}
		}
	}

	@Override
	public void objectDroppedNotOnTarget(MTComponent droppedObject, DragEvent de) {
		if (this.size() > 0) {
			// Don't use an iterator, since a listener may remove itself in response to the
			// event and cause a ConcurrentModificationException.
			DragAndDropActionListener[] listeners = this.toArray(new DragAndDropActionListener[this.size()]);
			for (DragAndDropActionListener l: listeners) {
				l.objectDroppedNotOnTarget(droppedObject, de);
			}
		}
	}

	@Override
	public void objectEnteredTarget(MTComponent object, DropTarget dt,
			DragEvent de) {
		if (this.size() > 0) {
			// Don't use an iterator, since a listener may remove itself in response to the
			// event and cause a ConcurrentModificationException.
			DragAndDropActionListener[] listeners = this.toArray(new DragAndDropActionListener[this.size()]);
			for (DragAndDropActionListener l: listeners) {
				l.objectEnteredTarget(object, dt, de);
			}
		}
	}

	@Override
	public void objectExitedTarget(MTComponent object, DropTarget dt,
			DragEvent de) {
		if (this.size() > 0) {
			// Don't use an iterator, since a listener may remove itself in response to the
			// event and cause a ConcurrentModificationException.
			DragAndDropActionListener[] listeners = this.toArray(new DragAndDropActionListener[this.size()]);
			for (DragAndDropActionListener l: listeners) {
				l.objectExitedTarget(object, dt, de);
			}
		}
	}
}
