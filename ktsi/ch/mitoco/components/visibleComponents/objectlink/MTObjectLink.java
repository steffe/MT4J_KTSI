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
public class MTObjectLink extends MTLine {
	
	/** Start Object. */
	private MyMTObject startObject;
	
	/** End Object. */
	private MyMTObject endObject;

	/** MTEllipse. */
	private MTEllipse linkHead;
	
	/** PApplet.*/
	private PApplet pApplet;
	
	/** endPoint.*/
	private Vertex endPoint;
	
	
	/** MTObjectLink Constructor.
	 * 
	 * @param pApplet PApplet
	 * @param startPoint vertex
	 * @param endPoint vertex
	 */
	public MTObjectLink(PApplet pApplet, Vertex startPoint, Vertex endPoint) {
		super(pApplet, startPoint, endPoint);
		this.pApplet = pApplet;
		this.endPoint = endPoint;
		init();
	}

	/** Init Method.*/
	private void init() {
		System.out.println("Link generated ");
		linkHead = new MTEllipse(pApplet, endPoint, 20, 20);
		linkHead.setStrokeColor(MTColor.NAVY);
		linkHead.setStrokeWeight(4);
		linkHead.setVisible(true);
		linkHead.setFillColor(MTColor.GRAY);
		linkHead.setName("linkHead");
		this.addChild(linkHead);
		
		this.setStrokeColor(MTColor.NAVY);
		this.setStrokeWeight(4);
		this.setVisible(true);
		this.translateGlobal(new Vector3D(0,0,-20));
	
		setComposite(true);
		reset();

		unregisterAllInputProcessors();
		removeAllGestureEventListeners();

		registerInputProcessor(new DragProcessor(pApplet, true));		
		addGestureListener(DragProcessor.class, new IGestureEventListener() {

			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				DragEvent event = (DragEvent) ge;
				if (event.getId() == DragEvent.GESTURE_ENDED) {
					reset();
				} else {
					Vector3D to = getGlobalVecToParentRelativeSpace(linkHead, event.getTo());
					setNewEndPosition(new Vertex(to));
				}
				return false;
			}
		});

	 addGestureListener(DragProcessor.class, new DragAndDropAction());
	}

	/** 
	 * Set the Link Description. 
	 * 
	 * @param text String
	 * */
	public void setDescription(final String text) {
		
	}
	
	private void setNewEndPosition(Vertex newendPoint) {
		setVertices(new Vertex[]{getVerticesLocal()[0], newendPoint});
		linkHead.setPositionRelativeToParent(getVerticesLocal()[1]);
	
	}
	
	
	private void reset() {
		setVertices(new Vertex[]{getVerticesLocal()[0], endPoint});
		linkHead.setPositionRelativeToParent(getVerticesLocal()[1]);
		
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
