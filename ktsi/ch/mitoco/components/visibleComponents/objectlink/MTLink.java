package ch.mitoco.components.visibleComponents.objectlink;

import java.util.ArrayList;

import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTLine;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;
import org.mt4jx.input.gestureAction.dnd.DragAndDropAction;
import org.mt4jx.input.gestureAction.dnd.DragAndDropTarget;


import ch.mitoco.components.visibleComponents.MyMTObject;

import processing.core.PApplet;

/** MTObjectLink for connect two myMTObjects with a MTLine. */
public class MTLink extends MTLine {
	
	/** Start Object. */
	private MyMTObject startObject;
	
	/** End Object. */
	private MyMTObject endObject;
	
	/** MTLink Constructor.
	 * 
	 * @param pApplet PApplet
	 * @param startPoint vertex
	 * @param endPoint vertex
	 */
	public MTLink(PApplet pApplet, Vertex startPoint, Vertex endPoint) {
		super(pApplet, startPoint, endPoint);
		init();
	}

	/** Init Method.*/
	private void init() {
		System.out.println("Link generated ");
		
		this.setStrokeColor(MTColor.GREEN);
		this.setStrokeWeight(4);
		this.setVisible(true);
		this.translateGlobal(new Vector3D(0,0,-20));

	}

	/** 
	 * Set the Link Description. 
	 * 
	 * @param text String
	 * */
	public void setDescription(final String text) {
		
	}
	
	/**
	 * 
	 * @return
	 */
	public MyMTObject getStartObject() {
		return startObject;
	}

	/**
	 * 
	 * @param startObject
	 */
	public void setStartObject(MyMTObject startObject) {
		this.startObject = startObject;
	}

	/**
	 * 
	 * @return
	 */
	public MyMTObject getEndObject() {
		return endObject;
	}

	/**
	 * 
	 * @param endObject
	 */
	public void setEndObject(MyMTObject endObject) {
		this.endObject = endObject;
	}
	
}
