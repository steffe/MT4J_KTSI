
package test.thomas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.MTTextArea.ExpandDirection;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.components.visibleComponents.widgets.buttons.MTSvgButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation.ANONYMOUS;

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
	MTTextField textarea;
	
	// Object size
	int height;
	int width;
	int fontsize;
	boolean edit;
	PImage nullImage;
	MTApplication app;
	IFont iF;
	boolean rightalign;
	
	/**
	 * Constructor MTNumField. Fix size (40, 200)
	 * 
	 * @param pApplet MTApplication
	 * @param fontArialMini IFont
	 * @param rightalign boolean
	 */
	
	
	public MTNumField(MTApplication pApplet, IFont fontArialMini, boolean rightalign) {
		super(0,0,0,0,0,0,0,pApplet);
		app=pApplet;
		iF=fontArialMini;
		this.rightalign=rightalign;
		fontsize= iF.getOriginalFontSize();
		
		MTColor blue1 = new MTColor(51,102,204,180);
		height= 40;
		width= 220;
		
		System.out.println("Font Size = " +fontsize);
		
		this.setName("Unnamed NumField");
		
		baseOb = new MTRoundRectangle(0, 0, 0, width, height, 5, 5, pApplet);	
		baseOb.setFillColor(blue1);
		baseOb.setStrokeColor(MTColor.BLACK);
		

		textarea = new MTTextField(0,(height-fontsize)/2,width,height, fontArialMini, pApplet);
		
		textarea.setInnerPadding(0);
		textarea.setInnerPaddingLeft(0);
		textarea.setInnerPaddingTop(0);
		textarea.setText(new String("1"));
		textarea.setFillColor(new MTColor(0,0,0,0));
		textarea.setStrokeColor(new MTColor(0,0,0,0));
		textarea.setPickable(false);
		
		
		// Definition der Aussrichtung des Text 
		if(rightalign==true){
			float text_width = textarea.getTextWidth();	
			System.out.println("Text Breite: = " +text_width);
			textarea.setInnerPaddingLeft(width-((int)text_width+5));
			
		}
		else{
			textarea.setInnerPaddingLeft(5);
		}
		
		
		baseOb.registerInputProcessor(new TapProcessor(app, height, true, width));
		baseOb.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if(te.isDoubleTap()){
					MTNumKeyboard n1 = new MTNumKeyboard(app);
					baseOb.addChild(n1);
					
					System.out.println("Button Clicked");
					System.out.println("Text width :"+ textarea.getTextWidth());
					
					
					final MTTextArea t = new MTTextArea(app,iF);
					
			        t.setExpandDirection(ExpandDirection.UP);
					t.setStrokeColor(new MTColor(0,0 , 0, 255));
					t.setFillColor(new MTColor(205,200,177, 255));
					t.unregisterAllInputProcessors();
					t.setEnableCaret(true);
					// Zusätzliche Methode im MTTextArea erfoderlich, mit dem Objecttyp MTNumKeyboard
					t.snapToKeyboard(n1);
					
					t.setText(textarea.getText());
					n1.addTextInputListener(t);
					
					
					// Close Button from Num Keybord  
					n1.keybCloseSvg.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent close) {
							switch(close.getID()){
							case TapEvent.BUTTON_CLICKED:
								System.out.println("Text Breite 1: = " +textarea.getTextWidth());
								textarea.setInnerPaddingLeft(0);
								textarea.setText(t.getText());
								float f1 = textarea.getTextWidth();
								System.out.println("Text Breite 2: = " +textarea.getTextWidth());
								textarea.setInnerPaddingLeft(width-((int)f1+5));
							break;
							}
							
						}
					});
				}
				
				return false;
			}
		});
	
		
		// Add Object to base Object
		baseOb.addChild(textarea);
		
		
		
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
	
	/**
	 * 
	 * boolean = 0 => left
	 * boolean = 1 => right
	 * 
	 * @param boolean align
	 */
	public void setTextAlign(boolean align){
		float text_size = textarea.getTextWidth();
		
		System.out.println("Text width :"+ textarea.getTextWidth());
		
		if(align==true){
			textarea.setPositionRelativeToOther(baseOb,new Vector3D(0,0,0));
			//textarea.updateComponent(100);
		}
		else{
		textarea.setPositionRelativeToOther(baseOb,new Vector3D((width/2)+5,height-fontsize/2,0));		
		}
	}
	
	public float text_W(){
		return textarea.getTextWidth();	
	}
}
