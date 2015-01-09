
package test.thomas;

import java.util.List;
import java.util.Arrays;

import org.mt4j.AbstractMTApplication;
import org.mt4j.MTApplication;
import org.mt4j.components.StateChange;
import org.mt4j.components.StateChangeEvent;
import org.mt4j.components.StateChangeListener;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.MTTextArea.ExpandDirection;
import org.mt4j.input.inputData.InputCursor;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.font.IFontCharacter;
import org.mt4j.util.math.Vector3D;

import com.sun.xml.internal.ws.api.model.ExceptionType;



import processing.core.PImage;
/**
 * MTNumField. Modified MTTextArea with MTNumKeyboard on double tap.
 * 
 * 
 * MTTextArea Methode setInnerPaddingLeft() verursacht einen Fehler bei der andwendung von getTextWidght()
 * 
 * @author tandrich 
 */

public class MTNumField extends MTRoundRectangle{

	public MTTextArea textarea;
	
	// Object size
	int height;
	int width;
	int fontsize;
	boolean edit;
	PImage nullImage;
	private IFont iF;
	public boolean rAlign;
	private String dString;
	
	/** Label Font*/
	private String fname;
	
	/** The MTApplication. */
	public AbstractMTApplication app;

	
	/** The MTTextAre Numbers */	
	public MTTextArea numKeyText;
	
	/** The Numb Label */
	private MTTextArea label;
	
	/** Label Font*/
	private IFont labelfont;
	
	/**
	 * Constructor MTNumField. 
	 * 
	 * @param pApplet MTApplication
	 * @param fontArialMini IFont
	 * @param width int
	 * @param hight int
	 * @param rightalign boolean
	 * @param defaultString String
	 * @param fieldname String
	 */
	
	
	public MTNumField(AbstractMTApplication app, IFont fontArialMini,int width, int height, boolean rightalign, String defaultString, String fieldname, IFont labelfont) {
		super(app,0,0,0,0,0,5,5);
		this.rAlign=rightalign;
		dString = defaultString;
		fname = fieldname;
		this.height= height;
		this.width= width;
		this.labelfont=labelfont;
		this.setName(fname);
		this.init(app, fontArialMini);
	}
	
	private void init(AbstractMTApplication app, IFont font){
		final MTColor blue1 = new MTColor(51,102,204,180);
		this.app =app;
		this.iF=font;
		final AbstractMTApplication app1 = app;
		fontsize= iF.getOriginalFontSize();		
		
		this.setSizeLocal(width, height);
		this.setFillColor(blue1);
		this.setStrokeColor(MTColor.BLACK);
	


		textarea = new MTTextArea(app, 0,(height-fontsize)/2,width,height, font);
		
		textarea.setInnerPadding(0);
		textarea.setInnerPaddingLeft(0);
		textarea.setInnerPaddingTop(0);
		textarea.setText(new String(dString));
		textarea.setFillColor(new MTColor(0,0,0,0));
		textarea.setPickable(false);
		textarea.setNoStroke(true);

		setAlign(rAlign);
	/*
		this.removeAllChildren();
		this.removeAllGestureEventListeners();
		this.unregisterAllInputProcessors();
	 */
		this.registerInputProcessor(new TapProcessor(app, 25, true, 350)); //height weid
		this.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				if(ge instanceof TapEvent){
				
				TapEvent te = (TapEvent)ge;
				System.out.println(te.getTapID() + " (" + te.getId() + ")");
				
				if(te.getTapID() == TapEvent.DOUBLE_TAPPED){
					System.out.println("Button Clicked");
					
					MTNumKeyboard numKeyboard = new MTNumKeyboard(app1);
					numKeyboard.setFillColor(blue1);
					
					numKeyText= new MTTextArea(app1,0,0,width,height,iF);
					numKeyText.setExpandDirection(ExpandDirection.UP);
					numKeyText.setStrokeColor(new MTColor(0,0 , 0, 255));
					numKeyText.setFillColor(new MTColor(205,200,177, 255));
					numKeyText.unregisterAllInputProcessors();
					numKeyText.setEnableCaret(true);
					
					numKeyboard.snapToKeyboard(numKeyText);
					
					numKeyText.setText(textarea.getText());
					numKeyboard.addTextInputListener(numKeyText);
					
					addChild(numKeyboard);
					
					numKeyboard.addStateChangeListener(StateChange.COMPONENT_DESTROYED, new StateChangeListener() {
						
						@Override
						public void stateChanged(StateChangeEvent evt) {
							textarea.setInnerPaddingLeft(0);
							textarea.setText(numKeyText.getText());
							setAlign(rAlign);
							
							
						}
					}
					);
				}
				}	
				return false;
			}
			
			});

	
		label = new MTTextArea(app,0,-labelfont.getOriginalFontSize(),width,height,labelfont);
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
	
	
	public void setEdited(){
		
	}
	
	/**
	 * Set Attribut to min Modus
	 */
	public void setMin(){
		label.setVisible(false);
	}
	
	/**
	 * Set Attribut to max Modus
	 */
	public void setMax(){
		label.setVisible(true);
	}
	
	public void setAlign(boolean align){
		
		if(align==true){
			float text_width = textarea.getTextWidth();	
			//float text_width = textarea.getLastLineWidth();
			System.out.println("Text Breite: = " +text_width);
			System.out.println("Verschiebsatz: = " +(width-((int)text_width+5)));
			textarea.setInnerPaddingLeft((width-((int)text_width+5)));
		}
		else{
			textarea.setInnerPaddingLeft(5);
		}
	}

	
	
}
