package org.mt4jx.input.gestureAction.dnd;

import java.util.Hashtable;

import org.mt4j.components.MTComponent;
import org.mt4j.components.interfaces.IMTComponent3D;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;

/**
 * When listening to DragProcessor, DefaultDragAndDropAction calls
 * componentDropped when a MTComponent is dropped on top of another MTComponent
 * that implements MTDropTarget.
 * 
 * @author Uwe Laufs
 * @see DropTarget
 */
public class DragAndDropAction extends AbstractDnDAction {
	private Hashtable<IMTComponent3D, DropTarget> componentAndCurrentTarget = new Hashtable<IMTComponent3D, DropTarget>();
	
	// For storing listeners.  Lazily instantiated.
	private DragAndDropActionListenerList listeners;
	
	private boolean pickableObjectsOnly = false;

	public DragAndDropAction(){

	}
	
	/**
	 * @param pickableObjectsOnly if set to 'true', objects with isPickable() = 'false' will not be checked. Set parameter to 'false' as far as possible to improve performance.
	 */
	public DragAndDropAction(boolean pickableObjectsOnly){
		this.pickableObjectsOnly = pickableObjectsOnly;
	}
	
	public void addDragAndDropActionListener(DragAndDropActionListener l) {
		if (listeners == null) listeners = new DragAndDropActionListenerList();
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}
	
	public void removeDragAndDropActionListener(DragAndDropActionListener l) {
		if (listeners != null) {
			listeners.remove(l);
			if (listeners.size() == 0) {
				listeners = null;
			}
		}
	}

	@Override
	public boolean gestureDetected(MTGestureEvent g) {
		return false;
	}

	@Override
	public boolean gestureUpdated(MTGestureEvent g) {
		IMTComponent3D sourceComponent = g.getTargetComponent();
		if (!(sourceComponent instanceof MTComponent)) {
			return false;
		}
		
		MTComponent draggedComponent = (MTComponent) sourceComponent;
		DragEvent de = (g instanceof DragEvent) ? (DragEvent) g : null;
		
		DragAndDropTarget dropTarget = (DragAndDropTarget)this.detectDropTarget(g, pickableObjectsOnly);
		
		// entered ?
		if (dropTarget != null && !targetRemembered(sourceComponent)) {			
			this.storeComponentTarget(sourceComponent, dropTarget);
			dropTarget.componentEntered((MTComponent) sourceComponent);
			if (listeners != null) {
				listeners.objectEnteredTarget(draggedComponent, dropTarget, de);
			}
			// dragged somewhere else ?
		} else if (dropTarget == null && !targetRemembered(sourceComponent)) {
			// ignore
		} else if (this.targetChanged(sourceComponent, dropTarget)) {
//			System.out.println("CHANGED");
			DropTarget target = this.getComponentTarget(sourceComponent);
			if(target instanceof DragAndDropTarget){
				((DragAndDropTarget)target).componentExited((MTComponent) sourceComponent);
				this.forgetComponentTarget(sourceComponent);
				if (listeners != null) {
					listeners.objectExitedTarget(draggedComponent, target, de);
				}
				if (dropTarget != null) {
					this.storeComponentTarget(sourceComponent, dropTarget);
					dropTarget.componentEntered((MTComponent) sourceComponent);
					if (listeners != null) {
						listeners.objectEnteredTarget(draggedComponent, dropTarget, de);
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean gestureEnded(MTGestureEvent g) {
		IMTComponent3D target = g.getTargetComponent();
		if (!(target instanceof MTComponent)) {
			return false;
		}
		
		MTComponent droppedComponent = (MTComponent) target;

		if(g instanceof DragEvent){
			
			DropTarget dropTarget = this.detectDropTarget(g, pickableObjectsOnly);
			
			DragEvent dragEvent = ((DragEvent)g);
			if (dropTarget != null) {
				dropTarget.componentDropped((MTComponent) g.getTargetComponent(), dragEvent);
				this.storeComponentTarget(g.getTargetComponent(), dropTarget);
				if (listeners != null) {
					listeners.objectDroppedOnTarget(droppedComponent, dropTarget, dragEvent);
				}
			} else {
				if (listeners != null) {
					listeners.objectDroppedNotOnTarget(droppedComponent, dragEvent);
				}
			}
		}
		return false;
	}

	private void storeComponentTarget(IMTComponent3D component,
			DropTarget dropTarget) {
		if (component != null && dropTarget != null) {
			this.componentAndCurrentTarget.put(component, dropTarget);
		}
	}

	private void forgetComponentTarget(IMTComponent3D component) {
		if (component != null) {
			this.componentAndCurrentTarget.remove(component);
		}
	}

	private DropTarget getComponentTarget(IMTComponent3D component) {
		if (component != null) {
			return this.componentAndCurrentTarget.get(component);
		} else {
			return null;
		}
	}

	private boolean targetRemembered(IMTComponent3D component) {
		if (component == null) {
			return false;
		} else {
			return this.componentAndCurrentTarget.get(component) != null;
		}
	}

	private boolean targetChanged(IMTComponent3D component,
			DropTarget dropTarget) {
		DropTarget rememberedTarget = this.componentAndCurrentTarget
				.get(component);
		// System.out.println(rememberedTarget + "|" + dropTarget);
		if (rememberedTarget == null && dropTarget == null) {
			return false;
		} else if (rememberedTarget != null && dropTarget != null) {
			return !rememberedTarget.equals(dropTarget);
		} else {
			// System.out.println( (rememberedTarget!=null && dropTarget==null)
			// );
			return true;
		}
	}
}
