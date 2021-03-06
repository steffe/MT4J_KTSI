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
	
//	/** Start Object. */
//	private MyMTObject startObject;
	
//	/** End Object. */
//	private MyMTObject endObject;

	/** MTEllipse. */
	private MTEllipse linkHead;
	
	/** PApplet.*/
	private PApplet pApplet;
	
	/** endPoint.*/
	private Vertex endPoint;
	
	/** Object ID. */
	private int id;
	
	/** StrokeWeight. */
	private int strokeWeight;
	
	/** Stroke Color.*/
	private MTColor strokeColor;
	
	/** MTObjectLink Constructor.
	 * 
	 * @param pApplet PApplet
	 * @param startPoint vertex
	 * @param endPoint vertex
	 */
	public MTObjectLink(PApplet pApplet, Vertex startPoint, Vertex endPoint, int ID) {
		super(pApplet, startPoint, endPoint);
		this.pApplet = pApplet;
		this.endPoint = endPoint;
		this.id = ID;
		this.strokeWeight = 3;
		this.strokeColor = MTColor.SILVER;
		init();
	}

	/** Init Method.*/
	private void init() {
		System.out.println("MTObjectLink: LinkerHandler Object generated ");
		linkHead = new MTEllipse(pApplet, endPoint, 15, 15);
		linkHead.setStrokeColor(strokeColor);
		linkHead.setStrokeWeight(strokeWeight);
		linkHead.setVisible(true);
		linkHead.setFillColor(MTColor.GREY);
		linkHead.setName("linkHead");
		this.addChild(linkHead);
		
		this.setStrokeColor(strokeColor);
		this.setStrokeWeight(strokeWeight);
		this.setVisible(true);
		this.translateGlobal(new Vector3D(0, 0, -1));
		this.setName(String.valueOf(id));
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

//	/** 
//	 * Set the Link Description. 
//	 * 
//	 * @param text String
//	 * */
//	public void setDescription(final String text) {
//		
//	}
	
	/**
	 * Setzt den neuen Punkt des Target Finder (LinkHead) und des Linienende.
	 * @param newendPoint 
	 */
	private void setNewEndPosition(Vertex newendPoint) {
		setVertices(new Vertex[]{getVerticesLocal()[0], newendPoint});
		linkHead.setPositionRelativeToParent(getVerticesLocal()[1]);
	
	}
	
	/**
	 * Setzt den Target Finder (LinkHead) und die Linie wieder auf den Ursprungspunkt zur�ck.
	 */
	private void reset() {
		setVertices(new Vertex[]{getVerticesLocal()[0], endPoint});
		linkHead.setPositionRelativeToParent(getVerticesLocal()[1]);
		
	}

	/** 
	 * Gibt die Linien dicke des Linkhead und der Linie zur�ck. 
	 * 
	 * @return strokeWeight int
	 */
	public final int getStrokeWeightLinkObject() {
		return strokeWeight;
	}

	/**
	 * Setzt die Dicke der Linie des Linkheads und der Linie.
	 * 
	 * @param strokeWeight int
	 */
	public void setStrokeWeightLinkObject(final int strokeWeight) {
		this.strokeWeight = strokeWeight;
	}

	/**
	 * Gibt die Farbe der Linie und des Linkheads zur�ck.
	 * 
	 * @return strokeColor MTColor
	 */
	public MTColor getStrokeColor() {
		return strokeColor;
	}

	/**
	 * Setzt die Farbe der Linien und des Linkheads
	 */
	public void setStrokeColor(MTColor strokeColor) {
		this.strokeColor = strokeColor;
	}
	
	
//	/**
//	 * 
//	 * @return
//	 */
//	public MyMTObject getStartObject() {
//		return startObject;
//	}
//
//	/**
//	 * 
//	 * @param startObject
//	 */
//	public void setStartObject(MyMTObject startObject) {
//		this.startObject = startObject;
//	}
//
//	/**
//	 * 
//	 * @return
//	 */
//	public MyMTObject getEndObject() {
//		return endObject;
//	}
//
//	/**
//	 * 
//	 * 
//	 * @param endObject MyMTOb
//	 */
//	public void setEndObject(MyMTObject endObject) {
//		this.endObject = endObject;
//	}
	
}
