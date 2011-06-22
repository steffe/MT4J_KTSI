/**
 * Doc for Package ch.mitoco.components.visibleComponents.widgets;.
 */
package ch.mitoco.components.visibleComponents.widgets;



import org.mt4j.AbstractMTApplication;
import org.mt4j.MTApplication;
import org.mt4j.components.StateChange;
import org.mt4j.components.StateChangeEvent;
import org.mt4j.components.StateChangeListener;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTColorPicker;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.MTTextArea.ExpandDirection;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTTextKeyboard;
import org.mt4j.input.GestureEventSupport;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.inputData.MTInputEvent;
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

import com.sun.corba.se.spi.orbutil.fsm.Input;

import processing.core.PImage;

import ch.mitoco.model.ModelAttributContent;
import ch.mitoco.model.ModelMtAttributs;

/**
 * MTNumField. Modified MTTextArea with MTTextKeyboard on tap.
 * 
 * 
 * MTTextArea Methode setInnerPaddingLeft() verursacht einen Fehler bei der andwendung von getTextWidght()
 * 
 * @author tandrich 
 */
// TODO Keyboard Farbe anpassen (MTDropDownList) -> erledigt
// TODO Keyboard Standort anpassen -> erledigt
// TODO Einheiten hinzufügen (CHF, EURO, meter, Zeit usw)

public class MTTextAttribut extends Attributes {

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
		
	/** Default String.*/
	private String stringvalue;
	
	/** Label Font.*/
	private String fname;
		
	/** The MTTextAre Numbers. */	
	private MTTextArea numKeyText;
	
	/** The Numb Label. */
	private MTTextArea label;
	
	/** Label Font.*/
	private IFont labelfont;
	
	/** Transparenc Color for keyboard (fix). */
	 private MTColor trans = new MTColor(0, 0, 0, 10);
	 
	 /** Default Color for Fill Rectangle. */
	 private MTColor blue1 = new MTColor(51, 102, 204, 180);
	 
	/** Attribut Model Object. */
	 private ModelMtAttributs model;
	 
	/** MTColorPicker. 	 */
	 private MTColorPicker colorpicker;
	 
	 /** Abstract MT Application.*/
	 private AbstractMTApplication app1;
	 
	 /** MTButton for ColorPicker. */
	 private MTImageButton colPickButton;
	 
	/**
	 * Constructor MTNumField. 
	 * 
	 * @param app AbstractMTApplication
	 * @param model ModelMtAttributs
	 * @param fontArialMini IFont
	 * @param width int
	 * @param height int
	 * @param defaultString String
	 * @param defaultlabeltext String
	 * @param labelfont IFont
	 */
	public MTTextAttribut(final AbstractMTApplication app, ModelMtAttributs model, final IFont fontArialMini, final int width, final int height, final String defaultString, final String defaultlabeltext, final IFont labelfont) {
		super(app);
		this.model = model;
		
		this.dataRead(defaultString, defaultlabeltext); // Read Data from Model
	
		this.height = height;
		this.width = width;
		this.labelfont = labelfont;
		this.setName(fname);
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
		this.setStrokeColor(MTColor.BLACK);
		
		textarea = new MTTextArea(app, 0, (height - fontsize) / 2, width, height, font);
		textarea.setInnerPadding(0);
		textarea.setInnerPaddingLeft(3);
		textarea.setInnerPaddingTop(0);
		textarea.setText(stringvalue);
		textarea.setFillColor(new MTColor(0, 0, 0, 0));
		textarea.setNoStroke(true);
		
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
				
				
				if (te.getTapID() == TapEvent.TAPPED) {
					System.out.println("Button Clicked");
				
					MTTextKeyboard textKeyboard = new MTTextKeyboard(app1);
					textKeyboard.setFillColor(trans);
					textKeyboard.setNoStroke(true);
					textKeyboard.setPositionRelativeToParent(new Vector3D(textKeyboard.getWidthXY(TransformSpace.LOCAL) / 2, (textKeyboard.getHeightXY(TransformSpace.LOCAL) / 2) + 50));
					
					textKeyboard.addTextInputListener(textarea);
					
					addChild(textKeyboard);
						
					textKeyboard.addStateChangeListener(StateChange.COMPONENT_DESTROYED, new StateChangeListener() {
						
						@Override
						public void stateChanged(final StateChangeEvent evt) {
							dataWrite();
						}
					}
					);
					
					
					textKeyboard.addInputListener(new IMTInputEventListener() {
						
						@Override
						public boolean processInputEvent(MTInputEvent inEvt) {
							if (inEvt instanceof MTInputEvent) {
								System.out.println(" TextBreite " + textarea.getTextWidth());
								//textarea.setText(numKeyText.getText());
							}
							return false;
						}
					});
					
					
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
		createColorPicker();
		this.setVisible(true);
		
		
		
	}
	
	/** 
	 * Read from datamodel and insert in the gui elements.
	 *
	 * @param defaultString String 
	 * @param defaultlabeltext String
	 */
	private void dataRead(final String defaultString, final String defaultlabeltext) {
		// Data transfer for value
		if (model.getAttributcontent() == null) {
			stringvalue = defaultString;
		} else {
			for (ModelAttributContent it : model.getAttributcontent()) {
				if (it.getType().equalsIgnoreCase("String")) {
					System.out.println(" Value zu Typ String " + it.getValue());
					stringvalue = it.getValue();
					break;
				} else {
					System.out.println(" Value zu Typ String NICHT GEFUNDEN ");
					stringvalue = defaultString;
				}
			}
		}
		
		// Data transfer for labeltext
		if (model.getLable() == null || model.getLable().isEmpty() ) {
			System.out.println("Set Default Label");
			fname = defaultlabeltext;
		} else {
			System.out.println("Set Label " + model.getLable());
			fname = model.getLable();
		}
		
		// Color for Rectangle Fill Color
		if (model.getAttcolor() == null) {
			this.setFillColor(blue1);
		} else {
			this.setFillColor(model.getAttcolor());
		}
		
	}
	
	/** 
	 * Write data in Datamodel.
	 */
	public final void dataWrite() {
		for (ModelAttributContent it : model.getAttributcontent()) {
			if (it.getType().equalsIgnoreCase("String")) {
				it.setValue(textarea.getText());
				break;
			} else {
				//TODO Was ist zu tun wenn dieses Werte Paar noch nicht exitiert???
			}
		}
		model.setAttcolor(getFillColor());
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
	}

	/** 
	 * Set Label Text.
	 * @param labeltext String
	 */
	public final void setLabel(final String labeltext) {
		label.setText(labeltext);
	}
	
	/** 
	 * Get Label Text.
	 * @return fname String
	 */
	public final String getLabel() {
		return label.getText();
	}

	/** 
	 * Set Value Text.
	 * @param value Double
	 */
	public final void setValue(final String value) {
		textarea.setName(value);
	}
	
	/** 
	 *Get Value Text.
	 *@return dString Double 
	 */
	public final String getValue() {
		return textarea.getText();
	}
	
	
}
