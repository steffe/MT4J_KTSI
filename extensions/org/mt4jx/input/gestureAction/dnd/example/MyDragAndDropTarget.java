package org.mt4jx.input.gestureAction.dnd.example;

import java.util.ArrayList;

import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4jx.input.gestureAction.dnd.DragAndDropTarget;

import processing.core.PApplet;

public class MyDragAndDropTarget extends MTRectangle implements DragAndDropTarget {
	
	private ArrayList<MTComponent> droppedComponents = new ArrayList<MTComponent>();
	private MTTextArea text;
	
	public MyDragAndDropTarget(PApplet pApplet, IFont font) {
		super(100, 100, 400, 200, pApplet);
		
		IFont arial = FontManager.getInstance().createFont(pApplet, "arial.ttf", 
				30, 				  //Font size
				new MTColor(0, 0, 0, 255),   //Font fill color
				new MTColor(0, 0, 0, 255));
		
		this.text = new MTTextArea(pApplet, arial);
		this.text.setNoFill(true);
		this.text.setNoStroke(true);
		this.setStrokeWeight(3f);
		this.addChild(text);
		this.text.setPositionGlobal(this.getCenterPointGlobal());
		this.text.removeAllGestureEventListeners();
		this.setPickable(false);
	}
	
	public void setText(){
		String text = this.getName() + "\n";
		for (int i = 0; i < this.droppedComponents.size(); i++) {
			text+= "- " + droppedComponents.get(i).getName() + "\n";
		}
		this.text.setText(text);
		this.text.setPositionGlobal(this.getCenterPointGlobal());
	}

	@Override
	public void componentDropped(MTComponent droppedComponent, DragEvent de) {
		if(!droppedComponents.contains(droppedComponent)){
			this.droppedComponents.add(droppedComponent);
		}

		this.setStrokeColor(this.getFillColor());
		System.out.println(this.getName() +": "+ droppedComponent.getName() + " dropped.");
		this.setText();
	}
	@Override
	public void componentEntered(MTComponent droppedComponent) {
		System.out.println(this.getName() +": "+ droppedComponent.getName() + " entered.");
		this.setStrokeColor(new MTColor(255,0,0));
		this.setText();
	}
	@Override
	public void componentExited(MTComponent droppedComponent) {
		this.droppedComponents.remove(droppedComponent);
		this.setStrokeColor(this.getFillColor());
		System.out.println(this.getName() +": "+ droppedComponent.getName() + " exited.");
		this.setText();
	}

	@Override
	public boolean dndAccept(MTComponent component) {
		return true;
	}
}
