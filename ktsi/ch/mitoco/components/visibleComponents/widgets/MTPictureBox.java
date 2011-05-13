package ch.mitoco.components.visibleComponents.widgets;


import java.util.Arrays;
import java.util.List;

import org.mt4j.AbstractMTApplication;
import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.IFont;
import org.mt4jx.components.visibleComponents.widgets.MTSuggestionTextArea;

import processing.core.PImage;
/**
 * MTDropDownList provide a DropDownList with five import levels.
 * 
 * @author tandrich
 *
 */

public class MTPictureBox extends MTRoundRectangle {
	
	/** The MTApplication. */
	private AbstractMTApplication app;
	
	/** Width. */
	private int width;
	
	/** Height. */
	private int height;
	
	/** Fieldname. */
	private String fname;
	
	/** Standard Color. */
	 private MTColor blue1 = new MTColor(51, 102, 204, 180);
	
	/** Textare Label.*/
	private MTTextArea label;
	
	/** Label Font. */
	private IFont labelfont;
	
	/** Picture File Path.*/
	private String filePath;
	
	/** Picture Box.*/
	private MTRectangle pictureBox;
	
	/** 
	 * Construtor MTPictureBox.
	 * 
	 * @param app AbstractMTApplication
	 * @param width int
	 * @param height int
	 * @param arc int 
	 * @param labelname String
	 * @param labelfont IFont
	 * 
	 * */
	public MTPictureBox(final AbstractMTApplication app, final int width, final int height, final int arc, final String labelname, final IFont labelfont) {
		super(app, 0, 0, 0, width, height, 5, 5);
		this.width = width;
		this.height = height;
		this.fname = labelname;
		this.labelfont = labelfont;
		this.setFillColor(blue1);
		this.setStrokeColor(MTColor.BLACK);
		this.init(app);
	}
	/** 
	 * Init MTPictureBox.
	 * @param app2 AbststractMTApplication
	 * */
	private void init(final AbstractMTApplication app2) {		
		
		pictureBox = new MTRectangle(app2, width - 5, height - 5);
		PImage buttonNewImage = app2.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  "f5e2.png");
				
		pictureBox.setTexture(new PImage());
		
		
		
		this.setGestureAllowance(DragProcessor.class, false);
		this.setGestureAllowance(ScaleProcessor.class, false);
		this.setGestureAllowance(RotateProcessor.class, false);
		this.setGestureAllowance(TapProcessor.class, true);
		//this.addChild(sa);
		
		label = new MTTextArea(app, 0, -labelfont.getOriginalFontSize(), width, height, labelfont);
		label.setInnerPadding(0);
		label.setNoFill(true);
		label.setStrokeColor(MTColor.LIME);
		label.setNoStroke(true);
		label.setText(fname);
		label.setPickable(false);
		label.setVisible(false);
		
		this.addChild(label);
		
	}
	
	/**
	 * Set Attribut to min Modus.
	 */
	public final void setMin() {
		label.setVisible(false);
	}
	
	/**
	 * Set Attribut to max Modus.
	 */
	public final void setMax() {
		label.setVisible(true);
	}
	
	/** 
	 * Set Label Text.
	 * @param labeltext String
	 */
	public final void setLabel(final String labeltext) {
		fname = labeltext;
	}
	
	/** 
	 * Get Label Text.
	 * @return fname String
	 */
	public final String getLabel() {
		return fname;
	}

	/** 
	 * Set File Path Value.
	 * @param value String[]
	 */
	public final void setValue(final String value) {
		
		filePath = value;
	}
	
	/** 
	 *Get Value Text.
	 *@return dString String 
	 */
	public final String getValue() {
		return filePath;
	}

	
	
}
