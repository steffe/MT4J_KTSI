package org.mt4jx.input.gestureAction.dnd;

import java.util.List;

import org.mt4j.components.MTCanvas;
import org.mt4j.components.MTComponent;
import org.mt4j.components.PickResult;
import org.mt4j.components.PickResult.PickEntry;
import org.mt4j.components.interfaces.IMTComponent3D;
import org.mt4j.components.visibleComponents.AbstractVisibleComponent;
import org.mt4j.input.inputData.AbstractCursorInputEvt;
import org.mt4j.input.inputData.InputCursor;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleEvent;
import org.mt4j.util.math.Vector3D;
/**
 * Office Layout Enginering, Project Semantic Touch, (C) 2009 Fraunhofer IAO, all rights reserved.
 * @author Uwe Laufs
 */
public abstract class AbstractDnDAction implements IGestureEventListener {
	/**
	 * Abstract class that provides some functionality required for gesture actions.
     * @author Uwe Laufs
	 */
	@Override
	public boolean processGestureEvent(MTGestureEvent g) {
		if(g instanceof DragEvent){
		switch (g.getId()) {
			case DragEvent.GESTURE_STARTED:
				this.gestureDetected(g);
				break;
			case DragEvent.GESTURE_UPDATED:
				this.gestureUpdated(g);
				break;
			case DragEvent.GESTURE_ENDED:
				this.gestureEnded(g);
				break;
		}
		}
		return false;
	}
	protected MTCanvas getParentCanvas(MTComponent as) {
		MTComponent tmp = as.getRoot();
		if(tmp instanceof MTCanvas){
			return (MTCanvas)tmp;
		}else{
			MTComponent mtc = as;
			while ((!(mtc == null)) && (!((mtc = mtc.getParent()) instanceof MTCanvas))) {
			}
			return (MTCanvas) mtc;
		}
	}
	/**
	 * @param g
	 * @return returns involved cursor position or null, if none available
	 */
	protected Vector3D getCursorPosition(MTGestureEvent g) {
		if(g instanceof DragEvent){
			DragEvent dragEvent = (DragEvent)g;
			InputCursor cursor = dragEvent.getDragCursor();
			AbstractCursorInputEvt lastCursorEvent = cursor.getEvents().get(cursor.getEventCount()-1);
			return new Vector3D(lastCursorEvent.getX(), lastCursorEvent.getY());
		}else if(g instanceof ScaleEvent){
			//TODO implement
			return null;
		}else{
			return null;
		}
	}
	
	
	protected MTComponent getVisibileParentComponent(MTGestureEvent g){
		IMTComponent3D inputComponent = g.getTarget();
		if((inputComponent instanceof MTComponent)){
			return getVisibileParentComponent((MTComponent)inputComponent);
		}else{
			return null;
		}
	}
	/**
	 * @param an MTComponent
	 * @return the highest level parent AbstractVisibleComponent in the component hierarchy
	 */
	protected MTComponent getVisibileParentComponent(MTComponent sourceComponent){
		MTComponent candidate = null;
		{
			MTComponent tmp = sourceComponent;
			while( !((tmp=tmp.getParent()) instanceof MTCanvas) ){
				if(tmp instanceof AbstractVisibleComponent){
					candidate = (MTComponent)tmp;
				}
			}
		}
		if(candidate==null){
			return sourceComponent;
		}else{
			return candidate;
		}
	}
		
	/**
	 * This method is called in case GESTURE_DETECTED.
	 * @param g The current MTGestureEvent
	 * @return boolean consumed
	 */
	public abstract boolean gestureDetected(MTGestureEvent g);
	/**
	 * This method is called in case GESTURE_UPDATED.
	 * @param g The current MTGestureEvent
	 * @return boolean consumed
	 */
	public abstract boolean gestureUpdated(MTGestureEvent g);
	/**
	 * This method is called in case GESTURE_ENDED.
	 * @param g The current MTGestureEvent
	 * @return boolean consumed
	 */
	public abstract boolean gestureEnded(MTGestureEvent g);
	
	/**
	 * @param g
	 * @return The MTDropTarget that is selected by the current gesture event or
	 *         null if there is no such MTDropTarget.
	 */
	protected DropTarget detectDropTarget(MTGestureEvent g, boolean pickableObjectsOnly) {
		DropTarget returnValue = null;
		MTComponent sourceComponent = (MTComponent) g.getTarget();
		Vector3D componentDropPositions = this.getCursorPosition(g);
		{
				MTCanvas parentCanvas = this.getParentCanvas(sourceComponent);
				PickResult prCanvas = parentCanvas.pick(componentDropPositions
						.getX(), componentDropPositions.getY(), pickableObjectsOnly);
				List<PickEntry> pickList = prCanvas.getPickList();
				
				for(int i=0; i<pickList.size(); i++){
//				for (int i = pickList.size() - 1; i >= 0; i--) {
					
					System.out.println("PickList#" + pickList.size());
					PickEntry currentPickEntry = pickList.get(i);
					MTComponent currentComponent = currentPickEntry.hitObj;
					// get "parent" of composed components for listener invocation
//					currentComponent = this
//							.getVisibileParentComponent(currentComponent);
					// skip the currently dragged source component
					if (!(currentComponent.equals(sourceComponent))) {
						if (currentComponent instanceof DropTarget) {
							DropTarget target = (DropTarget) currentComponent;
							// Only pick a target that will accept the 
							// source component.
							if (target.dndAccept(sourceComponent)) {
								System.out.println("DROP TARGET DETECTED:" +
										target);
								return target;
							}
						} else {
						 System.out.println("NOT A DROP TARGET:" + currentComponent);
						}
					}
				}
		}
//		System.out.println("return null");
		return null;
	}
}
