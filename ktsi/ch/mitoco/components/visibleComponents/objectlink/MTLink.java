package ch.mitoco.components.visibleComponents.objectlink;

import org.mt4j.AbstractMTApplication;
import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTLine;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTColorPicker;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;

import processing.core.PApplet;
import processing.core.PImage;

/** MTObjectLink for connect two myMTObjects with a MTLine. */
public class MTLink extends MTLine {
	
	/** Start Object. */
	private int startObjectID;
	
	/** End Object. */
	private int endObjectID;
	
	/** Linkfarbe. */
	private MTColor linkcolor;
	
	/** Beschreibung des Links.*/
	private String linkDescription;
	
	/** MTColorPicker. 	 */
	 private MTColorPicker colorpicker;
	 
	 /** Abstract MT Application.*/
	 private PApplet app1;
	 
	 /** MTButton for ColorPicker. */
	 private MTImageButton colPickButton;
	
	
	/** 
	 * Einfacher MTLink Constructor.
	 * 
	 * @param pApplet PApplet
	 * @param startPoint vertex
	 * @param endPoint vertex
	 */
	public MTLink(PApplet pApplet, Vertex startPoint, Vertex endPoint) {
		super(pApplet, startPoint, endPoint);
		this.app1 = pApplet;
		init();
		createColorPicker();
	}

	/**
	 * MTLink Konstruktur mit ID für Start und End-Objekte.
	 * 
	 * @param pApplet PApplet
	 * @param startPoint Vertex
	 * @param endPoint Vertex
	 * @param startObjectID int
	 * @param endObjectID int
	 */
	public MTLink(PApplet pApplet, Vertex startPoint, Vertex endPoint, int startObjectID, int endObjectID) {
		super(pApplet, startPoint, endPoint);
		this.startObjectID = startObjectID;
		this.endObjectID = endObjectID;
		this.app1 = pApplet;
		init();
		createColorPicker();
	}
	
	/** Init Method.*/
	private void init() {
		System.out.println("MTLINK: init: Link generated ");
		
		this.setStrokeColor(MTColor.GREEN);
		this.setStrokeWeight(4);
		this.setVisible(true);
		this.translateGlobal(new Vector3D(0, 0, -3));
	
		this.setGestureAllowance(TapProcessor.class, true);
		this.registerInputProcessor(new TapProcessor(app1));
		this.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				if (ge instanceof TapEvent) {
					
					TapEvent te = (TapEvent) ge;
					
					if (te.getTapID() == TapEvent.TAPPED) {
						colPickButton.setVisible(true);
					}
					
					
				}
				
				return false;
			}
		});
		
		
		
	}

	/** 
	 * Set the Link Description. 
	 * 
	 * @param text String
	 * */
	public void setDescription(final String text) {
		this.setName(text);
	}

	/**
	 * Gibt die ID des Start-Objekts zurück.
	 * 
	 * @return startObjectID int
	 */
	public final int getStartObjectID() {
		return startObjectID;
	}

	/**
	 * Setzt die ID des Start-Objekts.
	 * 
	 * @param startObjectID int
	 */
	public final void setStartObjectID(final int startObjectID) {
		this.startObjectID = startObjectID;
	}

	/**
	 * Gibt die ID des End-Objekts zurück.
	 * 
	 * @return endObjectID int
	 */
	public final int getEndObjectID() {
		return endObjectID;
	}

	/**
	 * Setzt die ID des End-Objekts.
	 * 
	 * @param endObjectID int
	 */
	public final void setEndObjectID(final int endObjectID) {
		this.endObjectID = endObjectID;
	}
	
	/** 
	 * Colorpicker.
	 */
	private void createColorPicker() {
   
        PImage colPick = app1.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  "colorcircletr.png");    
        colorpicker = new MTColorPicker(app1, 0, 0, colPick);
        colorpicker.translate(this.getCenterPointGlobal());
        colorpicker.setNoStroke(true);
        colorpicker.addGestureListener(DragProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(final MTGestureEvent ge) {
				if (ge.getId() == MTGestureEvent.GESTURE_ENDED) {
					if (colorpicker.isVisible()) {
						colorpicker.setVisible(false);
					}
				} else {
					setStrokeColor(colorpicker.getSelectedColor());
					colPickButton.setVisible(false);
					//dataWrite();
				}
				return false;
			}
		});
        
        PImage colPickIcon = app1.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  "ColorPickerIcon.png");
        colPickButton = new MTImageButton(app1, colPickIcon);
        colPickButton.translate(this.getCenterPointGlobal());
        colPickButton.setNoStroke(true);
        colPickButton.setSizeLocal(30, 30);
        colPickButton.sendToFront();
        colPickButton.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				if (te.isTapped()) {
					if (colorpicker.isVisible()) {
						colorpicker.setVisible(false);
					} else {
						colorpicker.setVisible(true);
						colorpicker.sendToFront();
					}				
				}
				return true;
			}
        });
        colorpicker.setVisible(false);
	    colPickButton.setVisible(false);
	
        this.addChild(colPickButton);
		this.addChild(colorpicker);
	}
	
	
}
