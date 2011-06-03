package org.mt4jx.input.gestureAction.dnd;

import org.mt4j.components.MTComponent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
/**
 * @author Uwe Laufs
 * @see DragAndDropAction
 */
public interface DropActionListener {
	public void objectDroppedOnTarget(MTComponent droppedObject, DropTarget dt, DragEvent de);
	public void objectDroppedNotOnTarget(MTComponent droppedObject, DragEvent de);
}
