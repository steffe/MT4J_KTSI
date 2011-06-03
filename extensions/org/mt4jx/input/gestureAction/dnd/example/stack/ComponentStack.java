package org.mt4jx.input.gestureAction.dnd.example.stack;

import java.util.ArrayList;
import java.util.List;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.bounds.IBoundingShape;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Matrix;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;
import org.mt4jx.input.gestureAction.dnd.DragAndDropAction;
import org.mt4jx.input.gestureAction.dnd.DragAndDropActionListener;
import org.mt4jx.input.gestureAction.dnd.DragAndDropTarget;
import org.mt4jx.input.gestureAction.dnd.DropAction;
import org.mt4jx.input.gestureAction.dnd.DragAndDropFilter;
import org.mt4jx.input.gestureAction.dnd.DropTarget;

import processing.core.PApplet;

/**
 * <p>A rectangular component in which other components are
 * stacked via drag-and-drop.  
 * </p>
 * 
 * @author R.Scarberry
 *
 */
public class ComponentStack extends MTRectangle 
	implements DragAndDropTarget, DragAndDropActionListener {

	// Components that have been dropped on the stack.
	private List<MTComponent> mStackedComponents = new ArrayList<MTComponent>();
	
	// Border component shown only when a component is dragged over the stack.
	private MTPolygon mActivationBorder;
	
	// Characteristics of mActivationBorder.
	private MTColor mActivationColor = MTColor.RED;
	private float mActivationStrokeWeight = 3.0f;
	// Whether or not to show mActivationBorder.
	private boolean mStrokeOnActivation = true;
	
	private float mMinStackedComponentDimensionRatio = 0.4f;
	private float mStackedComponentOffset = 5f;
	
	// If true, the stack will self-destruct when the last component
	// stacked upon it has been removed.
	private boolean mDestructWhenEmpty;
	
	// To limit what components may be dropped on this stack.
	private DragAndDropFilter mDropFilter;
	
	public ComponentStack(PApplet pApplet, float width, float height) {
		this(pApplet, new Vertex(0, 0, 0, 0, 0), width, height);
	}
	
	public ComponentStack(PApplet pApplet, float x, float y, float width, float height) {
		this(pApplet, new Vertex(x, y, 0, 0, 0), width, height);
	}
	
	public ComponentStack(PApplet pApplet, float x, float y, float z, float width, float height) {
		this(pApplet, new Vertex(x, y, z, 0, 0), width, height);
	}
	
	public ComponentStack(PApplet pApplet, Vertex upperLeft, float width, float height) {
		super(pApplet, upperLeft, width, height);
		this.setName("unnamed component stack");
		
		if (pApplet instanceof MTApplication) {
			
			MTApplication mtApp = (MTApplication) pApplet;
			
			this.registerInputProcessor(new TapProcessor(mtApp, 25, true, 350));
			this.addGestureListener(TapProcessor.class, new IGestureEventListener() {

				@Override
				public boolean processGestureEvent(MTGestureEvent ge) {
					if (ge instanceof TapEvent) {
						TapEvent te = (TapEvent) ge;
						if (mStackedComponents.size() > 0) {
							boolean dt = te.isDoubleTap();
							if (dt) {
								showActivationBorder();
								MTComponent comp = mStackedComponents.get(mStackedComponents.size() - 1);
								comp.setPickable(true);
							}
						}
					}
					return false;
				}
				
			});
		}
	}
	
	public boolean getDestructWhenEmpty() {
		return mDestructWhenEmpty;
	}
	
	public void setDestructWhenEmpty(boolean b) {
		mDestructWhenEmpty = b;
	}
	
	public void addToTopOfStack(MTComponent comp) {
		// Add component as child.
		this.addChild(comp);
		comp.setPickable(false);		
		if (!this.mStackedComponents.contains(comp)) {			
			this.mStackedComponents.add(comp);
			IGestureEventListener[] gestureListeners = comp.getGestureListeners();
			for (IGestureEventListener l: gestureListeners) {
				if (l instanceof DragAndDropAction) {
					((DragAndDropAction) l).addDragAndDropActionListener(this);
				} else if (l instanceof DropAction) {
					((DropAction) l).addListener(this);
				}
			}
			sizeStackedComponents();
		} else {
			// Dropped back on component without being removed.
			sizeStackedComponents();
		}
	}
	
	public MTComponent getComponentOnTopOfStack() {
		if (mStackedComponents.size() > 0) {
			return mStackedComponents.get(mStackedComponents.size() - 1);
		}
		return null;
	}
	
	public MTComponent[] getStackedComponents() {
		return mStackedComponents.toArray(new MTComponent[mStackedComponents.size()]);
	}
	
	@Override
	public void componentDropped(MTComponent droppedComponent, DragEvent de) {
		// Turn off the activation border
		hideActivationBorder();
		if (droppedComponent != this.getComponentOnTopOfStack()) {
			addToTopOfStack(droppedComponent);
		} else {
			droppedComponent.setPickable(false);
			sizeStackedComponents();
		}
	}

	@Override
	public void componentEntered(MTComponent enteredComponent) {
		showActivationBorder();
	}

	@Override
	public void componentExited(MTComponent comp) {
		hideActivationBorder();
	}
	
	public void removeChild(MTComponent comp) {
		super.removeChild(comp);
		IGestureEventListener[] gestureListeners = comp.getGestureListeners();
		for (IGestureEventListener l: gestureListeners) {
			if (l instanceof DragAndDropAction) {
				((DragAndDropAction) l).removeDragAndDropActionListener(this);
			} else if (l instanceof DropAction) {
				((DropAction) l).removeListener(this);
			}
		}
		int n = mStackedComponents.indexOf(comp);
		if (n >= 0) {
			mStackedComponents.remove(n);
			sizeStackedComponents();
		}
		if (mStackedComponents.size() == 0 && mDestructWhenEmpty) {
			this.destroy();
		}
	}
	
	private void sizeStackedComponents() {
		
		final int sz = this.mStackedComponents.size();
		
		if (sz == 0) {
			return;
		}
		
		float w  = this.getWidthXY(TransformSpace.LOCAL);
		float h = this.getHeightXY(TransformSpace.LOCAL);
		
		if (w == 0f || h == 0f) {
			return;
		}
		
		float offset = mStackedComponentOffset;
		float componentWidth = w - offset*(sz - 1);
	
		if (componentWidth/w < mMinStackedComponentDimensionRatio) {
			componentWidth = w * mMinStackedComponentDimensionRatio;
			offset = (w - componentWidth)/(sz - 1);
		}
		
		float componentHeight = h - offset*(sz - 1);
		if (componentHeight/h < mMinStackedComponentDimensionRatio) {
			componentHeight = h * mMinStackedComponentDimensionRatio;
			offset = (h - componentHeight)/(sz - 1);
			componentWidth = w - offset*(sz - 1);
		}
		
		Vector3D ctr = this.getCenterPointLocal();
		ctr.x += (offset * (sz - 1))/2f;
		ctr.y += (offset * (sz - 1))/2f;
		
		for (int i=0; i<sz; i++) {
			
			MTComponent comp = this.mStackedComponents.get(i);
			comp.setLocalMatrix(new Matrix());
		
			IBoundingShape bounds = comp.getBounds();
			Vector3D componentCtr = this.globalToLocal(bounds.getCenterPointGlobal());
			
			float cw = bounds.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
			float ch = bounds.getHeightXY(TransformSpace.RELATIVE_TO_PARENT);
			
			comp.translate(ctr.getSubtracted(componentCtr), TransformSpace.RELATIVE_TO_PARENT);
			if (cw > 0f && ch > 0f) {
				comp.scale(componentWidth/cw, componentHeight/ch, 1f, ctr, TransformSpace.RELATIVE_TO_PARENT);
			}			
			
			ctr.x -= offset;
			ctr.y -= offset;
		}
	}
	
	private void showActivationBorder() {	
		
		if (mStrokeOnActivation && mActivationBorder == null) {

			mActivationBorder = new MTPolygon(this.getRenderer(), this.getVerticesGlobal());
			mActivationBorder.setNoFill(true);
			mActivationBorder.setStrokeColor(mActivationColor);
			mActivationBorder.setStrokeWeight(mActivationStrokeWeight);
			mActivationBorder.setPickable(false);
			
			Matrix gmat = mActivationBorder.getGlobalMatrix();
			Matrix inv = this.getGlobalInverseMatrix();
			this.addChild(mActivationBorder);

			mActivationBorder.setLocalMatrix(inv.mult(gmat));
		}
	}
	
	private void hideActivationBorder() {
		if (mActivationBorder != null) {			
			this.removeChild(mActivationBorder);
			mActivationBorder.destroy();
			mActivationBorder = null;
		}
	}

	@Override
	public void objectDroppedOnTarget(MTComponent droppedObject, DropTarget dt,
			DragEvent de) {
	}

	@Override
	public void objectDroppedNotOnTarget(MTComponent droppedObject, DragEvent de) {
	}

	@Override
	public void objectEnteredTarget(MTComponent object, DropTarget dt,
			DragEvent de) {
		// Stub
	}

	@Override
	public void objectExitedTarget(MTComponent object, DropTarget dt,
			DragEvent de) {
		// Stub
	}

	public DragAndDropFilter getDropFilter() {
		return mDropFilter;
	}
	
	public void setDropFilter(DragAndDropFilter filter) {
		mDropFilter = filter;
	}

	@Override
	public boolean dndAccept(MTComponent component) {
		return mDropFilter == null || mDropFilter.dndAccept(component);
	}
}
