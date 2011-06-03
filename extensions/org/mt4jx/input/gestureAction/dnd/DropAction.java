package org.mt4jx.input.gestureAction.dnd;

import java.util.ArrayList;

import org.mt4j.components.MTComponent;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;

/**
 * Diffrent to DragAndDropAction, the DropAction implementation only fires DropTarget.componentDropped(MTComponent droppedComponent) events. This means that DropAction causes much less computing efforts because it does not look for DropTargets every time the object is moved. DropAction should be used instead of DragAndDropAction, if performance is too slow with DradAndDropAction.
 * @author Uwe Laufs
 */
public class DropAction extends AbstractDnDAction {
	private boolean pickableObjectsOnly = false;
	private ArrayList<DropActionListener> listeners = new ArrayList<DropActionListener>();
	
	public DropAction(){
	}
	/**
	 * @param pickableObjectsOnly if set to 'true', objects with isPickable() = 'false' will not be checked. Set parameter to 'false' as far as possible to improve performance.
	 */
	public DropAction(boolean pickableObjectsOnly){
		this.pickableObjectsOnly = pickableObjectsOnly;
	}
	@Override
	public boolean gestureDetected(MTGestureEvent g) {
		return false;
	}
	@Override
	public boolean gestureUpdated(MTGestureEvent g) {
		return false;
	}
	@Override
	public boolean gestureEnded(MTGestureEvent g) {
		if (!(g.getTargetComponent() instanceof MTComponent)) {
			return false;
		}
		DropTarget dropTarget = this.detectDropTarget(g, this.pickableObjectsOnly);
		if(g instanceof DragEvent){
			DragEvent dragEvent = (DragEvent)g;

			//		System.out.println("DropAction: dropTarget != null:  " + (dropTarget != null));
//		System.out.println("this.listeners.size():" + this.listeners.size());
//		for (int i = 0; i < this.listeners.size(); i++) {
//			System.out.println("this.listeners.get(i).getClass(): "+this.listeners.get(i).getClass());	
//		}
		
		if (dropTarget != null) {
			dropTarget.componentDropped((MTComponent) g.getTargetComponent(), dragEvent);
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).objectDroppedOnTarget(
						(MTComponent) g.getTargetComponent(), dropTarget, dragEvent);
			}
		} else {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).objectDroppedNotOnTarget(
						(MTComponent) g.getTargetComponent(), dragEvent);
			}
		}
		}

		return false;
	}
	public void addListener(DropActionListener dal){
		if(!this.listeners.contains(dal)){
			this.listeners.add(dal);
		}
		System.out.println("#listeners:" + this.listeners.size());
	}
	
	public void removeListener(DropActionListener dal) {
		this.listeners.remove(dal);
	}
}
