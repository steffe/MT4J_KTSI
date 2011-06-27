package ch.mitoco.templateMode;

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
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Matrix;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;
import org.mt4jx.input.gestureAction.dnd.DragAndDropAction;
import org.mt4jx.input.gestureAction.dnd.DragAndDropActionListener;
import org.mt4jx.input.gestureAction.dnd.DragAndDropFilter;
import org.mt4jx.input.gestureAction.dnd.DragAndDropTarget;
import org.mt4jx.input.gestureAction.dnd.DropAction;
import org.mt4jx.input.gestureAction.dnd.DropTarget;

import processing.core.PApplet;

/** Das neue Objekt wird zusammengestellt.
 * 
 *  @author rfeigenwinter
 *  
 *  Befindet sich noch im Aufbau, deswegen sind keine weiterführenden JavaDoc
 *  Kommentare in diesem Objekt.
 * 
 */

public class ObjectRectangle extends MTRectangle implements DragAndDropTarget, DragAndDropActionListener
	{
	
	private List<MTComponent> mStackedComponents = new ArrayList<MTComponent>();
	
	// Border component shown only when a component is dragged over the stack.
	private MTPolygon mActivationBorder;
	
	// Characteristics of mActivationBorder.
	private MTColor mActivationColor = MTColor.RED;
	private float mActivationStrokeWeight = 3.0f;
	private float mMinStackedComponentDimensionRatio = 0.4f;
	private float mStackedComponentOffset = 5f;
	// Whether or not to show mActivationBorder.
	private boolean mStrokeOnActivation = true;

	// To limit what components may be dropped on this stack.
	private DragAndDropFilter mDropFilter;
	private PApplet pApplet;
	
	//For Snap Grid

	private int horizontalTiles = 2;
	private int verticalTiles = 2;
	private ArrayList<MTRectangle> mtGrids = new ArrayList<MTRectangle>();

	public ObjectRectangle(PApplet pApplet, final float x, final float y, final float width, final float height) {
		this(pApplet, new Vertex(x, y, 0, 0, 0), width, height);
	}
	public ObjectRectangle(PApplet pApplet, float x, float y, float z, float width, float height) {
		this(pApplet, new Vertex(x, y, z, 0, 0), width, height);
	}

	public ObjectRectangle(final PApplet pApplet, final Vertex upperLeft, final float width, final float height) {
		super(pApplet, upperLeft, width, height);
		this.setName("unnamed component stack");
		removeAllGestureEventListeners(DragProcessor.class);
		this.pApplet = pApplet;
		
		
		
		
		
		
		
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
	
	public MTComponent getComponentOnTopOfStack() {
		if (mStackedComponents.size() > 0) {
			return mStackedComponents.get(mStackedComponents.size() - 1);
		}
		return null;
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
			//sizeStackedComponents();
		} else {
			// Dropped back on component without being removed.
			//sizeStackedComponents();
		}
	}

	@Override
	public void componentDropped(MTComponent droppedComponent, DragEvent de) {
		// Turn off the activation border
		hideActivationBorder();
		if (droppedComponent != this.getComponentOnTopOfStack()) {
			addToTopOfStack(droppedComponent);
		} else {
			droppedComponent.setPickable(false);
			//sizeStackedComponents();
		}
		
	}
	
	

	@Override
	public boolean dndAccept(MTComponent component) {
		return mDropFilter == null || mDropFilter.dndAccept(component);
		//return false;
	}

	@Override
	public void objectDroppedOnTarget(MTComponent droppedObject, DropTarget dt,
			DragEvent de) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void objectDroppedNotOnTarget(MTComponent droppedObject, DragEvent de) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void objectEnteredTarget(MTComponent object, DropTarget dt,
			DragEvent de) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void objectExitedTarget(MTComponent object, DropTarget dt,
			DragEvent de) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentEntered(MTComponent enteredComponent) {
		showActivationBorder();
		
	}

	@Override
	public void componentExited(MTComponent exitedComponent) {
		hideActivationBorder();
		
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
	
	public DragAndDropFilter getDropFilter() {
		return mDropFilter;
	}
	
	public void setDropFilter(DragAndDropFilter filter) {
		mDropFilter = filter;
	}
	

	
	public void onEnter() {
		
	}
	
	public void onLeave() {	}



	
	
	
	
	
	
	}
