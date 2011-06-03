package org.mt4jx.input.gestureAction.dnd;

import org.mt4j.components.MTComponent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;

/**
 * Extends DropActionListener so listeners can be notified when object 
 * enter or exit a DropTarget.
 * 
 * @author R. Scarberry
 *
 */
public interface DragAndDropActionListener extends DropActionListener {
	
	void objectEnteredTarget(MTComponent object, DropTarget dt, DragEvent de);
	void objectExitedTarget(MTComponent object, DropTarget dt, DragEvent de);

}
