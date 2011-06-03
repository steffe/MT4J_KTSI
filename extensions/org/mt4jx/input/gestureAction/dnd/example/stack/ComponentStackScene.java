package org.mt4jx.input.gestureAction.dnd.example.stack;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Matrix;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.input.gestureAction.dnd.DragAndDropAction;
import org.mt4jx.input.gestureAction.dnd.DragAndDropActionListener;
import org.mt4jx.input.gestureAction.dnd.DragAndDropTarget;
import org.mt4jx.input.gestureAction.dnd.DropTarget;

import processing.core.PApplet;

public class ComponentStackScene extends AbstractScene 
	implements DragAndDropActionListener {

	public ComponentStackScene(MTApplication mtApplication, String name) {
		
		super(mtApplication, name);
		
		//Disable frustum culling for this scene - optional
		this.getCanvas().setFrustumCulling(false);
		
		//Set the background color
		this.setClearColor(new MTColor(146, 150, 100, 255));
				
		float tw = mtApplication.width/6f;
		float th = 0.6f * mtApplication.height;
		
		float tx1 = mtApplication.width/3f;
		float ty1 = (mtApplication.height - th)/2f;
		
		// Stack drop target on the left side.
		ComponentStack target1 = new ComponentStack(mtApplication, tx1, ty1, tw, th);
		
		target1.setFillColor(new MTColor(0,255,0,255));
		target1.setStrokeColor(new MTColor(0,255,0,255));
		
		// pickableObjectsOnly MUST be true, since target2 could be dropped onto target1.
		// If it were false, and you dropped target2 onto target1, dragging and dropping target1
		// could cause a crash.
		DragAndDropAction target1Action = new DragAndDropAction(true);
		target1Action.addDragAndDropActionListener(this);
		target1.addGestureListener(DragProcessor.class, target1Action);
		target1.setName("target1");
		
		this.getCanvas().addChild(target1);
		
		float tx2 = 2f*mtApplication.width/3f;
		float ty2 = ty1;
		
		// Simple rectangle drop target on the right.
		MyDnDTarget target2 = new MyDnDTarget(tx2, ty2, tw, th, mtApplication);
		target2.setFillColor(MTColor.GRAY);
		DragAndDropAction target2Action = new DragAndDropAction();
		target2Action.addDragAndDropActionListener(this);
		target2.addGestureListener(DragProcessor.class, target2Action);
		target2.setName("target2");
			
		this.getCanvas().addChild(target2);
		
		int numComponents = 4;
		
		float ch = mtApplication.height/(numComponents + 1);
		float cw = (ch/th) * tw;
		float cx = (tx1 - cw)/2f;
		
		float ygap = (mtApplication.height - (ch * numComponents))/(numComponents + 1);
		
		MTColor[] colors = { MTColor.AQUA, MTColor.BLUE, MTColor.YELLOW, MTColor.GREEN, MTColor.GRAY };
		
		DragAndDropAction dndAction = new DragAndDropAction();
		dndAction.addDragAndDropActionListener(this);
		
		// Small rectangles to drag and drop on the left.
		for (int i=0; i<numComponents; i++) {
			
			MTRectangle rect = new MTRectangle(cx, ygap * (i+1) + ch * i, cw, ch, mtApplication);
			rect.setFillColor(colors[i%colors.length]);
			rect.setStrokeColor(MTColor.BLACK);			
			rect.addGestureListener(DragProcessor.class, dndAction);
			rect.setName("rect " + (i+1));
			
			this.getCanvas().addChild(rect);
		}
		
		IFont font = FontManager.getInstance().createFont(mtApplication, "arial.ttf", 24, MTColor.WHITE,
				MTColor.WHITE);
		
		MTTextArea explanation = new MTTextArea(mtApplication, font);
		explanation.setNoFill(true);
		explanation.setNoStroke(true);
		explanation.setText(
				"The green rectangle is a ComponentStack, while the\n" +
				"gray rectangle is a simple drop target.  All rectangles\n" +
				"may be dragged and dropped.  To remove items from the stack\n" +
				"double-click the stack.");
		this.getCanvas().addChild(explanation);
		
		explanation.setPositionGlobal(new Vector3D((cx + cw) + (mtApplication.width - cx - cw)/2f,
				ty1/2, 0));
		
		explanation.setPickable(false);
		
	}
	
	@Override
	public void init() {
	}

	@Override
	public void shutDown() {
	}


	@Override
	public void objectDroppedOnTarget(MTComponent droppedObject, DropTarget dt,
			DragEvent de) {
		System.out.printf("%s dropped on target %s\n", droppedObject.toString(), 
				dt.toString());
	}

	@Override
	public void objectDroppedNotOnTarget(MTComponent droppedObject, DragEvent de) {		
		System.out.printf("%s dropped on no valid target\n", droppedObject.toString());
		addChildInPlace(droppedObject, this.getCanvas());
	}


	@Override
	public void objectEnteredTarget(MTComponent object, DropTarget dt,
			DragEvent de) {
		System.out.printf("%s has entered target %s\n", object.toString(), 
				dt.toString());
	}


	@Override
	public void objectExitedTarget(MTComponent object, DropTarget dt,
			DragEvent de) {
		System.out.printf("%s has exited target %s\n", object.toString(), 
				dt.toString());
		addChildInPlace(object, this.getCanvas());
	}

	/**
	 * Transfer a component to a new parent without changing the component's 
	 * global position or scaling.
	 * 
	 * @param child
	 * @param newParent
	 */
	public static void addChildInPlace(MTComponent child, MTComponent newParent) {
		if (child.getParent() != newParent) {
			// In order for the child not to jump or scale, need for the its new
			// global matrix to be the same as this.
			Matrix gmat = child.getGlobalMatrix();
			Matrix newParentGMatInverse = newParent.getGlobalInverseMatrix();
			newParent.addChild(child);
			// ParentGlobal X Local = OldGlobal
			// ==> ParentGlobalInv X ParentGlobal X Local = ParentGlobalInv X OldGlobal
			// ==> Local = ParentGlobalInv X OldGlobal.
			child.setLocalMatrix(newParentGMatInverse.mult(gmat));
		}
	}

	static class MyDnDTarget extends MTRectangle implements DragAndDropTarget {

		public MyDnDTarget(float x, float y, float width,
				float height, PApplet pApplet) {
			super(pApplet, x, y, width, height);
			this.setStrokeWeight(3f);
			this.setStrokeColor(MTColor.PURPLE);
			this.setNoStroke(true);
		}

		@Override
		public void componentDropped(MTComponent droppedComponent, DragEvent de) {
			this.setNoStroke(true);
			MTComponent parent = this.getParent();
			if (parent == null || !parent.equals(droppedComponent)) {
				addChildInPlace(droppedComponent, this);
			}
		}

		@Override
		public void componentEntered(MTComponent enteredComponent) {
			this.setNoStroke(false);
		}

		@Override
		public void componentExited(MTComponent exitedComponent) {
			this.setNoStroke(true);
		}

		@Override
		public boolean dndAccept(MTComponent component) {
			return true;
		}
		
	}
}
