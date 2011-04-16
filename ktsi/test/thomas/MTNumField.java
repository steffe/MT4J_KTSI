
package test.thomas;

import java.util.List;
import java.util.Arrays;

import org.mt4j.MTApplication;
import org.mt4j.components.StateChange;
import org.mt4j.components.StateChangeEvent;
import org.mt4j.components.StateChangeListener;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTSlider;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.MTTextArea.ExpandDirection;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.IFont;
import org.mt4j.util.font.IFontCharacter;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.components.visibleComponents.widgets.MTForm;
import org.mt4jx.components.visibleComponents.widgets.MTSuggestionTextArea;

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

	MTRoundRectangle baseOb;
	MTTextArea textarea;
	
	// Object size
	int height;
	int width;
	int fontsize;
	boolean edit;
	PImage nullImage;
	final MTApplication app;
	private IFont iF;
	public boolean rAlign;
	private String dString;
	private String fname;
	
	/**
	 * Constructor MTNumField. Fix size (40, 200)
	 * 
	 * @param pApplet MTApplication
	 * @param fontArialMini IFont
	 * @param rightalign boolean
	 * @param defaultString String
	 * @param fieldname String
	 */
	
	
	public MTNumField(MTApplication pApplet, IFont fontArialMini, boolean rightalign, String defaultString, String fieldname) {
		super(pApplet,0,0,0,0,0,0,0);
		app=pApplet;
		iF=fontArialMini;
		this.rAlign=rightalign;
		fontsize= iF.getOriginalFontSize();
		dString = defaultString;
		fname = fieldname;
		
		final MTColor blue1 = new MTColor(51,102,204,180);
		height= 40;
		width= 220;
		
		System.out.println("Font Size = " +fontsize);
		
		this.setName(fname);
		
		baseOb = new MTRoundRectangle(pApplet,0, 0, 0, width, height, 5, 5);	
		baseOb.setFillColor(blue1);
		baseOb.setStrokeColor(MTColor.BLACK);
		

		textarea = new MTTextArea(pApplet, 0,(height-fontsize)/2,width,height, fontArialMini);
		
		textarea.setInnerPadding(0);
		textarea.setInnerPaddingLeft(0);
		textarea.setInnerPaddingTop(0);
		textarea.setText(new String(dString));
		textarea.setFillColor(new MTColor(0,0,0,0));
		textarea.setPickable(false);
		textarea.setNoStroke(true);
		
		this.setAlign(rightalign);
		
		baseOb.registerInputProcessor(new TapProcessor(app, height, true, width));
		baseOb.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if(te.isDoubleTap()){
					MTNumKeyboard numkeyboard = new MTNumKeyboard(app);
					numkeyboard.setFillColor(blue1);
					baseOb.addChild(numkeyboard);
					
					System.out.println("Button Clicked");
					System.out.println("Text width :"+ textarea.getTextWidth());
					
					
					final MTTextArea numtextArea = new MTTextArea(app,iF);
					numtextArea.setExpandDirection(ExpandDirection.UP);
					numtextArea.setStrokeColor(new MTColor(0,0 , 0, 255));
					numtextArea.setFillColor(new MTColor(205,200,177, 255));
					numtextArea.unregisterAllInputProcessors();
					numtextArea.setEnableCaret(true);
					
					// Zusätzliche Methode im MTTextArea erfoderlich, mit dem Objecttyp MTNumKeyboard
					numkeyboard.snapToKeyboard(numtextArea);
					
					numtextArea.setText(textarea.getText());
					numkeyboard.addTextInputListener(numtextArea);
				
				
					numkeyboard.addStateChangeListener(StateChange.COMPONENT_DESTROYED, new StateChangeListener() {
						
						@Override
						public void stateChanged(StateChangeEvent close) {
								textarea.setInnerPaddingLeft(0);
								textarea.setText(numtextArea.getText());
								setAlign(rAlign);
		
						}
					});
						
				}
				
				return false;
			}
		});
	
		
		// Add Object to base Object
		baseOb.addChild(textarea);

		final String[] animals = { "sehr wichtig", "wichtig", "nötig","vorhanden", "unnötig", "nicht benötigt"};
		final List<String> list = Arrays.asList( animals);
		MTSuggestionTextArea sa = new MTSuggestionTextArea(app, width, list);
		sa.setFontColor(MTColor.BLACK);
		sa.setFillColor(blue1);
		sa.setFont(iF);
		sa.setNoStroke(true);

		baseOb.addChild(sa);
		
		
	}
	
	public MTRoundRectangle getMyAttributBack(){
	return baseOb;
	}
	
	public void setEdited(){
		
	}
	
	/**
	 * Set Attribut to min Modus
	 */
	public void setMin(){
		
	}
	
	/**
	 * Set Attribut to max Modus
	 */
	public void setMax(){
		
	}
	
	public void setAlign(boolean align){
		
		if(align==true){
			float text_width = textarea.getTextWidth();	
			System.out.println("Text Breite: = " +text_width);
			System.out.println("Verschiebsatz: = " +(width-((int)text_width+5)));
			textarea.setInnerPaddingLeft((width-((int)text_width+5)));
		}
		else{
			textarea.setInnerPaddingLeft(5);
		}
	}

	
	
}
