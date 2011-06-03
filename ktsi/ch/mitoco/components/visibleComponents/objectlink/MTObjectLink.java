package ch.mitoco.components.visibleComponents.objectlink;

import org.mt4j.components.visibleComponents.shapes.MTLine;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;

import ch.mitoco.components.visibleComponents.MyMTObject;

import processing.core.PApplet;
/** MTObjectLink for connect two myMTObjects with a MTLine. */
public class MTObjectLink extends MTLine {
	
	/** Start Object. */
	private MyMTObject startObject;
	
	/** End Object. */
	private MyMTObject endObject;
	
	/** MTObjectLink Constructor.
	 * 
	 * @param pApplet PApplet
	 * @param startPoint vertex
	 * @param endPoint vertex
	 */
	public MTObjectLink(PApplet pApplet, Vertex startPoint, Vertex endPoint) {
		super(pApplet, startPoint, endPoint);
		// TODO Auto-generated constructor stub
		init();
	}

	/** Init Method.*/
	private void init() {
		System.out.println("Link generated ");
		this.setStrokeColor(MTColor.NAVY);
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

	public MyMTObject getStartObject() {
		return startObject;
	}

	public void setStartObject(MyMTObject startObject) {
		this.startObject = startObject;
	}

	public MyMTObject getEndObject() {
		return endObject;
	}

	public void setEndObject(MyMTObject endObject) {
		this.endObject = endObject;
	}
	
	
}
