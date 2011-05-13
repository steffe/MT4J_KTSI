/**
 * Doc for Package.
 */
package ch.mitoco.components.visibleComponents.widgets;


import org.mt4j.AbstractMTApplication;
import org.mt4j.components.StateChange;
import org.mt4j.components.StateChangeEvent;
import org.mt4j.components.StateChangeListener;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.MTTextArea.ExpandDirection;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTTextKeyboard;
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

public class MTTextField extends MTRoundRectangle {

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
	private double doublevalue;
	
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
	
	
	public MTTextField(final AbstractMTApplication app, final IFont fontArialMini, final int width, final int height, final boolean rightalign, final int defaultString, final String labeltext, final IFont labelfont) {
		super(app, 0, 0, 0, 0, 0, 5, 5);
		this.rAlign = rightalign;
		doublevalue = defaultString;
		fname = labeltext;
		this.setUserData("FieldValue", defaultString);
		this.setUserData("Label", labeltext);
		
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
		final MTColor blue1 = new MTColor(51, 102, 204, 180);
		this.iF = font;
		final AbstractMTApplication app1 = app;
		fontsize = iF.getOriginalFontSize();		
		
		this.setSizeLocal(width, height);
		this.setFillColor(blue1);
		this.setStrokeColor(MTColor.BLACK);
		
		textarea = new MTTextArea(app, 0, (height - fontsize) / 2, width, height, font);
		
		textarea.setInnerPadding(0);
		textarea.setInnerPaddingLeft(0);
		textarea.setInnerPaddingTop(0);
		textarea.setText(Double.toString(doublevalue));
		textarea.setFillColor(new MTColor(0, 0, 0, 0));
		textarea.setNoStroke(true);
		//textarea.setPickable(false);
	
		
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
				
				
				if (te.getTapID() == TapEvent.TAPPED) {
					System.out.println("Button Clicked");
				
					MTTextKeyboard textKeyboard = new MTTextKeyboard(app1);
					textKeyboard.setFillColor(trans);
					
					numKeyText = new MTTextArea(app1, 0, 0, width, height, iF);
					numKeyText.setExpandDirection(ExpandDirection.UP);
					textKeyboard.setNoStroke(true);
					numKeyText.setFillColor(new MTColor(205, 200, 177, 255));
					numKeyText.unregisterAllInputProcessors();
					numKeyText.setEnableCaret(true);
					
					textKeyboard.snapToKeyboard(numKeyText);
					
					numKeyText.setText(textarea.getText());
					textKeyboard.addTextInputListener(numKeyText);
					
					addChild(textKeyboard);
					
					textKeyboard.setPositionRelativeToParent(new Vector3D(textKeyboard.getWidthXY(TransformSpace.LOCAL) / 2, textKeyboard.getWidthXY(TransformSpace.LOCAL) / 2 + 50));
					
					textKeyboard.addStateChangeListener(StateChange.COMPONENT_DESTROYED, new StateChangeListener() {
						
						@Override
						public void stateChanged(final StateChangeEvent evt) {
							textarea.setInnerPaddingLeft(0);
							textarea.setText(numKeyText.getText());
							setUserData("FieldValue", (numKeyText.getText()));
							setAlign(rAlign);
							
							
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
		return (String) this.getUserData("Label");
	}

	/** 
	 * Set Value Text.
	 * @param value Double
	 */
	public final void setValue(final double value) {
		doublevalue = value;
	}
	
	/** 
	 *Get Value Text.
	 *@return dString Double 
	 */
	public final double getValue() {
		return doublevalue;
	}
	
}
