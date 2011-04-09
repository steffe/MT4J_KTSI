package test.thomas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.MTTextArea.ExpandDirection;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.components.visibleComponents.widgets.buttons.MTSvgButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;

import processing.core.PImage;


public class MTNumField extends MTRoundRectangle{

	MTRoundRectangle baseOb;
	MTTextArea textarea;
	
	// Object size
	int height;
	int width;
	int fontsize;
	boolean edit;
	PImage nullImage;
	MTApplication app;
	IFont iF;
	
	public MTNumField(MTApplication pApplet, IFont fontArialMini) {
		super(0,0,0,0,0,0,0,pApplet);
		app=pApplet;
		iF=fontArialMini;
		
		MTColor blue1 = new MTColor(51,102,204,180);
		height= 40;
		width= 220;

		
		fontsize= fontArialMini.getOriginalFontSize();
		
		System.out.println("Font Size = " +fontsize);
		
		baseOb = new MTRoundRectangle(0, 0, 0, width, height, 5, 5, pApplet);	
		baseOb.setFillColor(blue1);
		baseOb.setStrokeColor(MTColor.BLACK);
		
		textarea = new MTTextArea(5,(height-fontsize)/2,width,height, fontArialMini, pApplet);
		textarea.setInnerPadding(0);
		textarea.setInnerPaddingLeft(0);
		textarea.setInnerPaddingTop(0);
		
		textarea.setText(new String("12323"));
		textarea.setFillColor(new MTColor(0,0,0,0));
		textarea.setStrokeColor(new MTColor(0,0,0,0));
		textarea.setPickable(false);
		
		baseOb.registerInputProcessor(new TapProcessor(app, height, true, width));
		baseOb.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if(te.isDoubleTap()){
					MTNumKeyboard n1 = new MTNumKeyboard(app);
					baseOb.addChild(n1);
					
					System.out.println("Button Clicked");
					
					final MTTextArea t = new MTTextArea(app, FontManager.getInstance().createFont(app, "arial.ttf", 50, 
			        		new MTColor(0,0,0,255), //Fill color 
							new MTColor(0,0,0,0))); //Stroke color
			        t.setExpandDirection(ExpandDirection.UP);
					t.setStrokeColor(new MTColor(0,0 , 0, 255));
					t.setFillColor(new MTColor(205,200,177, 255));
					t.unregisterAllInputProcessors();
					t.setEnableCaret(true);
					t.snapToKeyboard(n1);
					
					t.setText(textarea.getText());
					n1.addTextInputListener(t);
					
					// Close Button from Num Keybord  
					n1.keybCloseSvg.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent close) {
							switch(close.getID()){
							case TapEvent.BUTTON_CLICKED:
								textarea.setText(t.getText());
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
	
}
