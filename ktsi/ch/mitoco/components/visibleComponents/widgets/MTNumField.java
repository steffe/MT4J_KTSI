/**
 * Doc for Package.
 */
package ch.mitoco.components.visibleComponents.widgets;


import org.mt4j.AbstractMTApplication;
import org.mt4j.MTApplication;
import org.mt4j.components.StateChange;
import org.mt4j.components.StateChangeEvent;
import org.mt4j.components.StateChangeListener;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTColorPicker;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.MTTextArea.ExpandDirection;
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

import ch.mitoco.components.visibleComponents.filechooser.WBErrorMessage;
import ch.mitoco.components.visibleComponents.widgets.keyboard.MTNumKeyboard;
import ch.mitoco.model.ModelAttributContent;
import ch.mitoco.model.ModelMtAttributs;

/**
 * MTNumField. Modified MTTextArea with MTNumKeyboard on double tap.
 * 
 * MTTextArea Methode setInnerPaddingLeft() verursacht einen Fehler bei der andwendung von getTextWidght()
 * 
 * @author tandrich 
 */
// TODO Keyboard Farbe anpassen (MTDropDownList) -> erledigt
// TODO Keyboard Standort anpassen -> erledigt
// TODO Einheiten hinzufügen (CHF, EURO, meter, Zeit usw)

public class MTNumField extends Attributes {

	/** Main Textfield. */
	private MTTextArea textarea;
	
	/** Attribut height. */
	private int height;
	
	/** Attribut width. */
	private int width;
	
	/** Fontsize from textarea. */
	private int fontsize;

	/** Textfield font.*/
	private IFont iF;
	
	/** Set Align to right side.*/
	private boolean rAlign;
	
	/** Default double.*/
	private String stringvalue;
	
	/** Label Font.*/
	private String fname;
		
	/** The MTTextAre Numbers. */	
	private MTTextArea numKeyText;
	
	/** The Numb Label. */
	private MTTextArea label;
	
	/** Label Font.*/
	private IFont labelfont;
	
	/** Transparenz Color. */
	 private MTColor trans = new MTColor(0, 0, 0, 10);
	 
	 /** Default Color for Fill Rectangle. */
	 private MTColor blue1 = new MTColor(51, 102, 204, 180);
	 
	/** Attribut Model Object. */
	 private ModelMtAttributs model;
	 
	/** MTColorPicker. 	 */
	 private MTColorPicker colorpicker;
	 
	 /** MTButton for ColorPicker. */
	 private MTImageButton colPickButton;
	 
	 /** Abstract MT Application.*/
	 private AbstractMTApplication app1;
	 
	 /** Boolean for only one Keyboard.*/
	 private boolean oneKeyboard;
	 
	/**
	 * Constructor MTNumField. 
	 * 
	 * @param app AbstractMTApplication
	 * @param fontArialMini IFont
	 * @param width int
	 * @param height int
	 * @param rightalign boolean
	 * @param defaultString String
	 * @param labeltext String
	 * @param labelfont IFont
	 */
	
	
	public MTNumField(final AbstractMTApplication app, ModelMtAttributs model, final IFont fontArialMini, final int width, final int height, final boolean rightalign, final String defaultString, final String labeltext, final IFont labelfont) {
		super(app);
		this.rAlign = rightalign;
		stringvalue = defaultString;
		fname = labeltext;
		
		this.model = model;
		System.out.println("Model from TextAttributs -> " + model);
		
		dataRead(defaultString, labeltext);
		
		this.height = height;
		this.width = width;
		this.labelfont = labelfont;
		this.init(app, fontArialMini);	
	}
	
	/** 
	 * Initial method.
	 * @param app AbstractMTApplication
	 * @param font IFont
	 */
	
	private void init(final AbstractMTApplication app, final IFont font) {
		this.iF = font;
		app1 = app;
		fontsize = iF.getOriginalFontSize();		
		
		this.setSizeLocal(width, height);
		//this.setFillColor(blue1);
		this.setStrokeColor(MTColor.BLACK);
		
		oneKeyboard = true; 
		
		textarea = new MTTextArea(app, 0, (height - fontsize) / 2, width, height, font);
		
		textarea.setInnerPadding(0);
		textarea.setInnerPaddingLeft(0);
		textarea.setInnerPaddingTop(0);
		textarea.setText(stringvalue);
		textarea.setFillColor(new MTColor(0, 0, 0, 0));
		textarea.setNoStroke(true);
		
		setAlign(rAlign);

		textarea.setGestureAllowance(DragProcessor.class, false);
		textarea.setGestureAllowance(RotateProcessor.class, false);
		textarea.setGestureAllowance(ScaleProcessor.class, false);
		
		textarea.setGestureAllowance(TapProcessor.class, true);
		textarea.registerInputProcessor(new TapProcessor(app, 25, true, 350)); //height weid
		textarea.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(final MTGestureEvent ge) {
				if (ge instanceof TapEvent) {
				
				TapEvent te = (TapEvent) ge;
				System.out.println(te.getTapID() + " (" + te.getId() + ")");
				
				
				if (te.getTapID() == TapEvent.TAPPED && oneKeyboard) {
					oneKeyboard = false;
					System.out.println("Button Clicked");
				
					MTNumKeyboard numKeyboard = new MTNumKeyboard(app1);
					numKeyboard.setFillColor(trans);
					
					numKeyText = new MTTextArea(app1, 0, 0, width, height, iF);
					numKeyText.setExpandDirection(ExpandDirection.UP);
					numKeyboard.setNoStroke(true);
					numKeyText.setFillColor(new MTColor(205, 200, 177, 255));
					numKeyText.unregisterAllInputProcessors();
					numKeyText.setEnableCaret(true);
					
					numKeyboard.snapToKeyboard(numKeyText);
					
					numKeyText.setText(textarea.getText());
					numKeyboard.addTextInputListener(numKeyText);
					
					addChild(numKeyboard);
					
					numKeyboard.setPositionRelativeToParent(new Vector3D(numKeyboard.getWidthXY(TransformSpace.LOCAL) / 2, numKeyboard.getWidthXY(TransformSpace.LOCAL) / 2 + 50));
					
					numKeyboard.addStateChangeListener(StateChange.COMPONENT_DESTROYED, new StateChangeListener() {
						
						@Override
						public void stateChanged(final StateChangeEvent evt) {
							textarea.setInnerPaddingLeft(0);
							textarea.setText(numKeyText.getText());
							setAlign(rAlign);
							dataWrite();
							oneKeyboard = true;
						}
					}
					);
				}
				}	
				return false;
			}
			
			});
		
		label = new MTTextArea(app, 0, -labelfont.getOriginalFontSize(), width, height, labelfont);
		label.setInnerPadding(0);
		label.setNoFill(true);
		label.setStrokeColor(MTColor.LIME);
		label.setNoStroke(true);
		label.setText(fname);
		label.setPickable(false);
		label.setVisible(false);
		
		// Add Object to base Object
		
		this.addChild(textarea);
		this.addChild(label);
		this.setVisible(true);
		createColorPicker();
		
		
		
	}
	
	/** 
	 * Read from datamodel and insert in the gui elements.
	 *
	 * @param defaultString String 
	 * @param defaultlabeltext String
	 */
	private void dataRead(final String defaultString, final String defaultlabeltext) {
		try {
			
			// Data transfer for value
			if (model.getAttributcontent() == null) {
				stringvalue = defaultString;
				//Double.valueOf(textarea.getText()).doubleValue();
			} else {
				for (ModelAttributContent it : model.getAttributcontent()) {
					if (it.getType().equalsIgnoreCase("Double")) {
						System.out.println(" Value zu Typ String " + it.getValue());
						stringvalue = it.getValue();
						break;
					} else {
						System.out.println(" Value zu Typ String NICHT GEFUNDEN ");
						stringvalue = defaultString;
					}
				}
			}
			
			// Data transfer for Align
			if (model.getAttributcontent() == null) {
				rAlign = Boolean.parseBoolean(defaultString);
			} else {
				for (ModelAttributContent it : model.getAttributcontent()) {
					if (it.getType().equalsIgnoreCase("Align")) {
						System.out.println(" Value zu Typ Align " + it.getValue());
						rAlign = Boolean.parseBoolean(it.getValue());
						break;
					} else {
						System.out.println(" Value zu Typ Align NICHT GEFUNDEN ");
						rAlign = Boolean.parseBoolean(defaultString);
					}
					
				}
			}
			
			// Data transfer for labeltext
			if (model.getLable() == null || model.getLable().isEmpty()) {
				fname = defaultlabeltext;
			} else {
				fname = model.getLable();
			}
			
			// Color for Rectangle Fill Color
			if (model.getAttcolor() == null) {
				this.setFillColor(blue1);
				System.out.println("MTNumField: KANN NICHT AUSGELESEN WERDEN");
				
			} else {
				System.out.println("MTNumField: Farbe wird ausgelesen = " + model.getAttcolor());
				//this.setFillColor(MTColor.BLACK);
				this.setFillColor(model.getAttcolor());
			}
			
		} catch (ArrayIndexOutOfBoundsException ae) {
			System.err.println("MTNumField: ArrayIndexOutOfBoundsException: " + ae);
		} catch (NullPointerException ne) {
			System.err.println("MTNumField: NullPointerException: " + ne);
		} catch (NumberFormatException nfe) {
			System.err.println("MTNumField: NumberFormatException" + nfe);
		} catch (Exception ex) {
			System.err.println("MTNumField: Allgemein Exception " + ex);
		}
	}
	
	/** 
	 * Write data in Datamodel.
	 */
	public final void dataWrite() {
		for (ModelAttributContent it : model.getAttributcontent()) {
			if (it.getType().equalsIgnoreCase("Double")) {
				it.setValue(textarea.getText());
			} else {
				//TODO Was ist zu tun wenn dieses Werte Paar noch nicht exitiert???
			}
		}
		model.setAttcolor(this.getFillColor());
	}
	
	/** 
	 * Colorpicker.
	 */
	private void createColorPicker() {
   
        PImage colPick = app1.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  "colorcircletr.png");
        colorpicker = new MTColorPicker(app1, 0, 0, colPick);
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
        
        PImage colPickIcon = app1.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  "ColorPickerIcon.png");
        colPickButton = new MTImageButton(app1, colPickIcon);
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
	@Override
	public final void setMin() {
		label.setVisible(false);
		colPickButton.setVisible(false);
	}
	
	/**
	 * Set Attribut to max Modus.
	 */
	@Override
	public final void setMax() {
		label.setVisible(true);
		colPickButton.setVisible(true);
	}

	/** Set Textalign from textarea.
	 * @param align (true=right)
	 * */
	public final void setAlign(final boolean align) {
		
		if (align == true) {
			float textwidth = textarea.getTextWidth();	
			System.out.println("Text Breite: = " + textwidth);
			System.out.println("Verschiebsatz: = " + (width - ((int) textwidth + 5)));
			textarea.setInnerPaddingLeft((width - ((int) textwidth + 5)));
		} else {
			textarea.setInnerPaddingLeft(5);
		}
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
	 * Set Value Text.
	 * @param value Double
	 */
	public final void setValue(final double value) {
		textarea.setText(Double.toString(value));
	}
	
	/** 
	 *Get Value Text.
	 *@return dString Double 
	 */
	public final double getValue() {
		return Double.valueOf(textarea.getText()).doubleValue();
	}
	
}
