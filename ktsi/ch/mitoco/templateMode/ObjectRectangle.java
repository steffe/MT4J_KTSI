package ch.mitoco.templateMode;

import java.util.ArrayList;
import java.util.List;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Matrix;
import org.mt4j.util.math.Vertex;
import org.mt4jx.input.gestureAction.dnd.DragAndDropActionListener;
import org.mt4jx.input.gestureAction.dnd.DragAndDropTarget;
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
	// Whether or not to show mActivationBorder.
	private boolean mStrokeOnActivation = true;

	public ObjectRectangle(final PApplet pApplet, final float x, final float y, final float width, final float height) {
		this(pApplet, new Vertex(x, y, 0, 0, 0), width, height);
	}

	public ObjectRectangle(final PApplet pApplet, final Vertex upperLeft, final float width, final float height) {
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

	@Override
	public void componentDropped(MTComponent droppedComponent, DragEvent de) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean dndAccept(MTComponent component) {
		// TODO Auto-generated method stub
		return false;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentExited(MTComponent exitedComponent) {
		// TODO Auto-generated method stub
		
	}
}
