package ch.mitoco.components.visibleComponents.widgets;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mt4j.AbstractMTApplication;
import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTColorPicker;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;
import ch.mitoco.components.visibleComponents.MTSuggestionTextArea;
import ch.mitoco.model.ModelAttributContent;
import ch.mitoco.model.ModelMtAttributs;

/**
 * MTDropDownList provide a DropDownList with five import levels.
 * 
 * @author tandrich
 *
 */

public class MTDropDownList extends Attributes {
	
	/** The MTApplication. */
	private AbstractMTApplication app;
	
	/** The Font.  */
	private IFont ifont;
	
	/** Width. */
	private int width;
	
	/** Height. */
	private int height;
	
	/** Fieldname. */
	private String fname;
	
	/** Standard Color. */
	 private MTColor blue1 = new MTColor(51, 102, 204, 180);
	
	/** Transparenz Color. */
	 private MTColor trans = new MTColor(0, 0, 0, 10);
	
	/** Textare Label.*/
	private MTTextArea label;
	
	/** Label Font. */
	private IFont labelfont;
	
	/** MTSugestionTextArea. */
	private MTSuggestionTextArea sa;
	
	/** Dropdown List String.*/
	private String[] values;

	/** List for Strings. */
	private List<String> list;
	
	/** Attribut Model Object. */
	 private ModelMtAttributs model;
	
	/** MTColorPicker. 	 */
	 private MTColorPicker colorpicker;
	
	 /** MTButton for ColorPicker. */
	 private MTImageButton colPickButton;
	 
	 
	/** 
	 * Construtor MTDropDownList.
	 * 
	 * @param app AbstractMTApplication
	 * @param font Ifont
	 * @param width int
	 * @param height int
	 * @param arc int 
	 * @param labelname String
	 * @param labelfont IFont
	 * 
	 * */
	public MTDropDownList(final AbstractMTApplication app, ModelMtAttributs model ,final IFont font, final int width, final int height, final String labelname, final IFont labelfont) {
		super(app);
		this.ifont = font;
		this.width = width;
		this.height = height;
		this.fname = labelname;
		this.labelfont = labelfont;
		this.model = model;
		this.app = app;
		System.out.println("Model from TextAttributs -> " + model);
		
		list = new ArrayList<String>(); // Listenelement für Textinhalt
		
		this.setSizeLocal(width, height);
		this.setFillColor(blue1);
		this.setName(labelname);
		this.setStrokeColor(MTColor.BLACK);
		this.init(app);
		dataRead(labelname);
	}
	/** 
	 * Init MTDropDown List.
	 * @param app2 AbststractMTApplication
	 * */

	private void init(final AbstractMTApplication app2) {		
	
		sa = new MTSuggestionTextArea(app2, width, list);
		sa.setPickable(true);
		sa.setFontColor(MTColor.BLACK);
		sa.setFillColor(trans);
		sa.setFont(ifont);
		sa.setNoStroke(true);
		sa.setWidthLocal(width);
	
		sa.setGestureAllowance(DragProcessor.class, false);
		sa.setGestureAllowance(ScaleProcessor.class, false);
		sa.setGestureAllowance(RotateProcessor.class, false);
		sa.setGestureAllowance(TapProcessor.class, true);
		this.addChild(sa);
		
		label = new MTTextArea(app, 0, -labelfont.getOriginalFontSize(), width, height, labelfont);
		label.setInnerPadding(0);
		label.setNoFill(true);
		label.setStrokeColor(MTColor.LIME);
		label.setNoStroke(true);
		label.setText(fname);
		label.setPickable(false);
		label.setVisible(false);
		
		this.addChild(label);
		createColorPicker();
		
	}
	
	/** 
	 * Read from datamodel and insert in the gui elements.
	 *
	 * @param defaultString String 
	 * @param defaultlabeltext String
	 */
	private void dataRead(final String defaultlabeltext) {
		// Data transfer for value
		
		if (model.getAttributcontent() == null) {
			System.out.println("Load standard list ");
			loadDefaultList();
		} else {
			for (ModelAttributContent it : model.getAttributcontent()) {
				if (it.getType().equalsIgnoreCase("ListContent")) {
					System.out.println(" Value zu Typ String " + it.getValue());
					list.add(it.getValue());
				} else if (it.getType().equalsIgnoreCase("DefaultValue")){
					sa.setDefaultValue(it.getValue());
					//TODO: Store select in GUI Element setSelected()
					break;
				} else {
					System.out.println(" Value zu Typ String NICHT GEFUNDEN ");
					loadDefaultList();
				}
				
			}
		}
	
		
		// Data transfer for labeltext
		if (model.getLable() == null) {
			System.out.println("Set Default Label");
			fname = defaultlabeltext;
		} else {
			System.out.println("Set Label "+ model.getLable());
			fname = model.getLable();
		}
		
		// Color for Rectangle Fill Color
		if (model.getAttcolor() == null) {
			this.setFillColor(blue1);
		} else {
			this.setFillColor(model.getAttcolor());
		}
		
	}
	
	/** Load Default list when no date store in the model.
	 * 
	 */
	private final void loadDefaultList() {
		final String[] defaultText = { "sehr wichtig", "wichtig", "nötig", "vorhanden", "unnötig", "nicht benötigt"};
		list = Arrays.asList(defaultText);
	}
	
	/** 
	 * Write data in Datamodel.
	 */
	public final void dataWrite() {
		
		
		for (ModelAttributContent it : model.getAttributcontent()) {
			if (it.getType().equalsIgnoreCase("DefaultValue")) {
				it.setValue(sa.getSelectedValue());
				break;
			} else {
				/*
				ModelAttributContent attrbc2 = new ModelAttributContent();
				attrbc2.setType("DefaultValue");
				attrbc2.setValue(sa.getSelectedValue());
				model.getAttributcontent().add(attrbc2);
				*/
				//TODO Was ist zu tun wenn dieses Werte Paar noch nicht exitiert???
			}
		}
		
		
		model.setAttcolor(getFillColor());
	}
	
	/** 
	 * Colorpicker.
	 */
	private void createColorPicker() {
   
        PImage colPick = app.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  "colorcircletr.png");
        colorpicker = new MTColorPicker(app, 0, 0, colPick);
        colorpicker.translate(new Vector3D(0, 0, 0));
        colorpicker.setNoStroke(true);
        colorpicker.addGestureListener(DragProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(final MTGestureEvent ge) {
				if (ge.getId() == MTGestureEvent.GESTURE_ENDED) {
					if (colorpicker.isVisible()) {
						colorpicker.setVisible(false);
					}
				} else {
					setFillColor(colorpicker.getSelectedColor());
					dataWrite();
				}
				return false;
			}
		});
        
        PImage colPickIcon = app.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  "ColorPickerIcon.png");
        colPickButton = new MTImageButton(app, colPickIcon);
        colPickButton.translate(new Vector3D(width - height, 0, 0));
        colPickButton.setNoStroke(true);
        colPickButton.setSizeLocal(height, height);
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
	
	
	
	/**
	 * Set Attribut to min Modus.
	 */
	public final void setMin() {
		label.setVisible(false);
		colPickButton.setVisible(false);
	}
	
	/**
	 * Set Attribut to max Modus.
	 */
	public final void setMax() {
		label.setVisible(true);
		colPickButton.setVisible(true);
		System.out.println("String MTDropDown: " + sa.getSelectedValue());
	}
	
}
