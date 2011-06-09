package ch.mitoco.components.visibleComponents.widgets;

import java.util.ArrayList;
import java.util.List;

import org.apache.batik.anim.SetAnimation;
import org.mt4j.AbstractMTApplication;
import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTColorPicker;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import ch.mitoco.components.visibleComponents.MyMTObject;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation.ANONYMOUS;


import processing.core.PApplet;
import processing.core.PImage;
/**
 * Class for Colorpicker.
 * 
 * @author tandrich
 *
 */

//TODO: Optimieren der Code Struktur (ArrayList und dynamisches Erstellen der colorPicker)

public class MTColorPickerGroup extends MTComponent {
	
	/** Abstract MT Application.*/
	 private AbstractMTApplication app1;
	
	/** MTColorPicker. 	 */
	 private MTColorPicker colorpickerStroke;
	
	 /** MTButton for ColorPicker. */
	 private MTImageButton colPickButtonStroke;
	 
	 /** MTColorPicker. 	 */
	 private MTColorPicker colorpickerBackground;
	
	 /** MTButton for ColorPicker. */
	 private MTImageButton colPickButtonBackground;
	 
	 /** MTColorPicker. 	 */
	 private MTColorPicker colorpickerBorder;
	
	 /** MTButton for ColorPicker. */
	 private MTImageButton colPickButtonBorder;
	 
	 /** Stroke Color. */
	 private MTColor strokeColor;
	 
	 /** Backround Color. */
	 private MTColor backgroundColor;
	 
	 /** Border Color. */
	 private MTColor borderColor;
	 
	 /** List for ColorPickers.*/
	 private List<MTColorPicker>colorPickers;
	 
	/** Transparenz Color. */
	 private MTColor trans = new MTColor(0, 0, 0, 20);
	 
	 /** MyMTObject. */
	 private MyMTObject myMTobject;
	 
	 /** MyMTObject Baserect. */
	 private MTRoundRectangle baserect;
	/**
	 * Constructur.
	 * 
	 * @param app
	 * @param baserect 
	 */
	public MTColorPickerGroup(final AbstractMTApplication app, MyMTObject myMTobject, MTRoundRectangle baserect) {
		super(app);
		this.app1 = app;
		this.myMTobject = myMTobject;
		this.baserect = baserect;
		this.setVisible(true);
		init();
	}
	
	/**
	 * inital Method.
	 */
	private void init() {
		
		colorPickers = new ArrayList<MTColorPicker>();
		System.out.println("ColorPickerGroup wird erstellt ");
		
    	IFont fontArial = FontManager.getInstance().createFont(app1, "arial.ttf", 
				15, 	//Font size
				MTColor.GREY);
		
		
        PImage colPick = app1.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  "colorcircletr_v2.png");
        colorpickerStroke = new MTColorPicker(app1, 0, 0, colPick);
        colorpickerStroke.translate(new Vector3D(-40, 30, 0));
        colorpickerStroke.setNoStroke(true);
        colorpickerStroke.addGestureListener(DragProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(final MTGestureEvent ge) {
				if (ge.getId() == MTGestureEvent.GESTURE_ENDED) {
					if (colorpickerStroke.isVisible()) {
						colorpickerStroke.setVisible(false);
					}
				} else {
					myMTobject.setStrokeColor(colorpickerStroke.getSelectedColor());
				}
				return false;
			}
		});
        
        MTTextArea strokelabel = new MTTextArea(app1, fontArial);
        strokelabel.setText("Stroke Color");
        strokelabel.setNoFill(true);
        strokelabel.setNoStroke(true);
        strokelabel.setPickable(false); 
        strokelabel.translate(new Vector3D( 35, 25, 0));
        colorpickerStroke.addChild(strokelabel);
        
        
        PImage colPickIcon = app1.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  "ColorPickerIcon.png");
        colPickButtonStroke = new MTImageButton(app1, colPickIcon);
        colPickButtonStroke.setNoStroke(true);
        colPickButtonStroke.translate(new Vector3D(100, 0, 0));
        colPickButtonStroke.setSizeLocal(35, 35);
        colPickButtonStroke.sendToFront();
        colPickButtonStroke.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				if (te.isTapped()) {
					if (colorpickerStroke.isVisible()) {
						colorpickerStroke.setVisible(false);
					} else {
						colorpickerStroke.setVisible(true);
						colorpickerStroke.sendToFront();
					}				
				}
				return true;
			}
        });
      
        colorpickerStroke.setVisible(false);
	    colPickButtonStroke.setVisible(true);
	
	    // Colorpicker for the Background
	    colorpickerBackground = new MTColorPicker(app1, 0, 0, colPick);
	    colorpickerBackground.translate(new Vector3D(40, 30, 0));
	    colorpickerBackground.setNoStroke(true);
	    colorpickerBackground.addGestureListener(DragProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(final MTGestureEvent ge) {
				if (ge.getId() == MTGestureEvent.GESTURE_ENDED) {
					if (colorpickerBackground.isVisible()) {
						colorpickerBackground.setVisible(false);
					}
				} else {
					baserect.setFillColor(colorpickerBackground.getSelectedColor());
				}
				return false;
			}
		});
        
        MTTextArea backgroundlabel = new MTTextArea(app1, fontArial);
        backgroundlabel.setText("Background Color");
        backgroundlabel.setNoFill(true);
        backgroundlabel.setNoStroke(true);
        backgroundlabel.setPickable(false); 
        backgroundlabel.translate(new Vector3D( 25, 25, 0));
        colorpickerBackground.addChild(backgroundlabel);
	    
	    
        PImage colPickIcon1 = app1.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  "ColorPickerIcon.png");
        colPickButtonBackground = new MTImageButton(app1, colPickIcon1);
        colPickButtonBackground.setNoStroke(true);
        colPickButtonBackground.translate(new Vector3D(140, 0, 0));
        colPickButtonBackground.setSizeLocal(35, 35);
        colPickButtonBackground.sendToFront();
        colPickButtonBackground.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				if (te.isTapped()) {
					if (colorpickerBackground.isVisible()) {
						colorpickerBackground.setVisible(false);
					} else {
						colorpickerBackground.setVisible(true);
						colorpickerBackground.sendToFront();
					}				
				}
				return true;
			}
        });
        colorpickerBackground.setVisible(false);
        colPickButtonBackground.setVisible(true);
	    
        
     // Colorpicker for the Background
	    colorpickerBorder = new MTColorPicker(app1, 0, 0, colPick);
	    colorpickerBorder.translate(new Vector3D(150, 30, 0));
	    colorpickerBorder.setNoStroke(true);
	    colorpickerBorder.addGestureListener(DragProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(final MTGestureEvent ge) {
				if (ge.getId() == MTGestureEvent.GESTURE_ENDED) {
					if (colorpickerBorder.isVisible()) {
						colorpickerBorder.setVisible(false);
					}
				} else {
					backgroundColor = colorpickerBorder.getSelectedColor();
					backgroundColor.setAlpha(34);
					myMTobject.setFillColor(backgroundColor);
					
				}
				return false;
			}
		});
	    
        MTTextArea borderlabel = new MTTextArea(app1, fontArial);
        borderlabel.setText("Border Color");
        borderlabel.setNoFill(true);
        borderlabel.setNoStroke(true);
        borderlabel.setPickable(false); 
        borderlabel.translate(new Vector3D( 35, 25, 0));
        colorpickerBorder.addChild(borderlabel);
        
        PImage colPickIcon2 = app1.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  "ColorPickerIcon.png");
        colPickButtonBorder = new MTImageButton(app1, colPickIcon2);
        colPickButtonBorder.setNoStroke(true);
        colPickButtonBorder.translate(new Vector3D(180, 0, 0));
        colPickButtonBorder.setSizeLocal(35, 35);
        colPickButtonBorder.sendToFront();
        colPickButtonBorder.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				if (te.isTapped()) {
					if (colorpickerBorder.isVisible()) {
						colorpickerBorder.setVisible(false);
					} else {
						colorpickerBorder.setVisible(true);
						colorpickerBorder.sendToFront();
					}				
				}
				return true;
			}
        });
        colorpickerBorder.setVisible(false);
        colPickButtonBorder.setVisible(true);
	    
        MTRoundRectangle base = new MTRoundRectangle(app1, 0, -2, 0, 220, 35, 5, 5);
	    base.setFillColor(trans);
        base.setPickable(false);
        base.setNoStroke(true);
        

        
        MTTextArea label = new MTTextArea(app1, fontArial);
        label.setText("Object Color");
        label.setNoFill(true);
        label.setNoStroke(true);
        label.setPickable(false); //TODO: Dragen auf dem Object geht an dieser Stelle nicht
        label.translate(new Vector3D(5, 5, 0));
        
        this.addChild(base);
        this.addChild(label);
        this.addChild(colPickButtonStroke);
		this.addChild(colPickButtonBackground);
		this.addChild(colPickButtonBorder);
        
		this.addChild(colorpickerStroke);
		this.addChild(colorpickerBackground);
		this.addChild(colorpickerBorder);
		
		
	}

	public MTColor getStrokeColor() {
		return strokeColor;
	}

	public MTColor getBackgroundColor() {
		return backgroundColor;
	}

	public MTColor getBorderColor() {
		return borderColor;
	}

 

	
}
