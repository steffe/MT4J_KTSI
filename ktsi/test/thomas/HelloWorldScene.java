package test.thomas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.text.TabableView;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.MTTextArea.ExpandDirection;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.components.visibleComponents.widgets.buttons.MTSvgButton;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTKeyboard;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import com.sun.opengl.impl.packrect.Rect;
import com.sun.xml.internal.bind.v2.TODO;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation.ANONYMOUS;

import processing.core.PImage;

public class HelloWorldScene extends AbstractScene {

	private MTRectangle rect;
	private MTApplication mtApplication;
	private MTRoundRectangle round_Rect;
	private MTTextArea infobox;
	private int h;
	private int w;
	private int zoomFac;
	
	public HelloWorldScene(MTApplication mtAppl, String name) {
		super(mtAppl, name);
		this.mtApplication=mtAppl;
		
		MTColor white = new MTColor(0,0,0);
		MTColor speblue = new MTColor(51,102,255);
		this.setClearColor(new MTColor(100, 100, 100, 255));
		//Show touches
		this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));
		
		IFont fontArial = FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
				30, 	//Font size
				MTColor.BLACK,  //Font fill color
				white);	//Font outline color
		//Create a textfield
		MTTextArea textField = new MTTextArea(mtApplication, fontArial); 
		textField.setNoStroke(true);
		textField.setNoFill(true);
		textField.setText("F5E Tiger");
		//Center the textfield on the screen
		//textField.setPositionGlobal(new Vector3D(mtApplication.width/2f, mtApplication.height/2f));
		//Add the textfield to our canvas
		textField.setPickable(false);
		textField.translate(new Vector3D(0,0,0));
		
		
		// Add Round Rectangle
		round_Rect = new MTRoundRectangle(0, 0, 0, 300, 200, 15, 15, mtApplication);
		round_Rect.setPositionGlobal(new Vector3D(600,300));
		round_Rect.setFillColor(new MTColor(12,12,12,34));
		round_Rect.setStrokeWeight(32);
		round_Rect.setStrokeColor(speblue);
		
		// Show Image
		PImage f5e = mtApplication.loadImage("test" + MTApplication.separator + "thomas"+ MTApplication.separator + "f5e2.png");
		h = f5e.height;
		w = f5e.width; 
		zoomFac=3;
		rect = new MTRectangle(0, 0, 0, w/zoomFac, h/zoomFac, mtApplication);
		//rect.setFillColor(new MTColor(1,1,1,0));
		rect.setNoFill(false);
		rect.setNoStroke(false);
		rect.setStrokeColor(MTColor.GRAY);
		rect.setPositionGlobal(new Vector3D(150,100));
		rect.setTexture(f5e);
		rect.setPickable(false);
		
		//rect.rotateZ(new Vector3D(100,100),90); // Drehen eines Objekts		
		
		// Textfield for Input
		IFont fontArialMini = FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
				15, 	//Font size
				MTColor.BLACK,  //Font fill color
				white);	//Font outline color
		
		infobox = new MTTextArea(180,20,80,140, fontArialMini,mtApplication); 
		infobox.setNoStroke(false);
		infobox.setNoFill(true);
		infobox.setText("xx");
		infobox.setPickable(false);
		infobox.translate(new Vector3D(0,0,0));
	//	infobox.setPositionGlobal(new Vector3D(200,20));
		infobox.setStrokeWeight(1);
		infobox.setStrokeColor(MTColor.GREY);
	//	infobox.setSizeLocal(50, 140);
		
		
		// Button
		PImage buttonImage = mtApplication.loadImage("test" + MTApplication.separator + "thomas"+ MTApplication.separator + "buttonRotate.png");
		final MTImageButton buttonRotate = new MTImageButton(buttonImage, mtApplication);
		buttonRotate.setSizeLocal(30,30);
		buttonRotate.setFillColor(new MTColor(255,255,255,200));
		buttonRotate.setName("KeyboardButton");
		buttonRotate.setNoStroke(true);
		//keyboardButton.translateGlobal(new Vector3D(-2,mtApplication.height-keyboardButton.getWidthXY(TransformSpace.GLOBAL)+2,0));
		buttonRotate.setPositionGlobal(new Vector3D(20,180));
		
		buttonRotate.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				switch(e.getID())
				{
				case TapEvent.BUTTON_CLICKED:
					rect.rotateZ(new Vector3D(w/zoomFac,h/zoomFac,0), 180); // 516 x 316 
					// rect.setSizeXYGlobal(120, 120); // Achtung auf Rückstellungen des Werts
					break;
				
				default:
					break;
				}
				
			}
		});
		
		// Button for Keyboard 
		PImage buttonKeyboardImage = mtApplication.loadImage("test" + MTApplication.separator + "thomas"+ MTApplication.separator + "keyb128.png");
		final MTImageButton buttonKeyboard= new MTImageButton(buttonKeyboardImage, mtApplication);
		buttonKeyboard.setSizeLocal(30, 30);
		buttonKeyboard.setFillColor(new MTColor(255,255,255,200));
		buttonKeyboard.setName("KeyboardImage");
		buttonKeyboard.setNoStroke(true);
		buttonKeyboard.setPositionGlobal(new Vector3D(280,180));
		
		buttonKeyboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ke) {
				switch(ke.getID()){
					case TapEvent.BUTTON_CLICKED:
						// Add Keyboard
						MTKeyboard keyb = new MTKeyboard(mtApplication);
						//keyb.setPositionGlobal(new Vector3D(400,400));
						//keyb.setSizeXYGlobal(400, 200);
						keyb.setFillColor(new MTColor(30,30,30,210));
						keyb.setStrokeColor(new MTColor(0,0,0,255));
						
				        final MTTextArea t = new MTTextArea(mtApplication, FontManager.getInstance().createFont(mtApplication, "arial.ttf", 50, 
				        		new MTColor(0,0,0,255), //Fill color 
								new MTColor(0,0,0,0))); //Stroke color
				        t.setExpandDirection(ExpandDirection.UP);
						t.setStrokeColor(new MTColor(0,0 , 0, 255));
						t.setFillColor(new MTColor(205,200,177, 255));
						t.unregisterAllInputProcessors();
						t.setEnableCaret(true);
						t.snapToKeyboard(keyb);
						t.setText(infobox.getText());
						keyb.addTextInputListener(t);
						round_Rect.addChild(keyb);
						
						// Save Button, save text
						/*
						MTSvgButton saveButton = new MTSvgButton("test" + MTApplication.separator + "thomas"+ MTApplication.separator + "buttonSave.svg",mtApplication);
						saveButton.scale(0.4f, 0.4f, 1, new Vector3D(0,0,0),TransformSpace.LOCAL);
						saveButton.translate(new Vector3D(0,0,0));
						saveButton.setBoundsPickingBehaviour(AbstractShape.BOUNDS_ONLY_CHECK);
						*/
						PImage saveButtonImage = mtApplication.loadImage("test" + MTApplication.separator + "thomas"+ MTApplication.separator + "buttonSave.png");
						final MTImageButton saveButton= new MTImageButton(saveButtonImage, mtApplication);
						saveButton.setSizeLocal(50,50);
						saveButton.setFillColor(new MTColor(255,255,255,200));
						saveButton.setName("saveButton");
						saveButton.setNoStroke(true);
						saveButton.setPositionGlobal(new Vector3D(25,25,0));
						
						// actionlistener for saveButton
						saveButton.addActionListener(new ActionListener() {
						
							public void actionPerformed(ActionEvent save) {
								if(save.getSource()instanceof MTComponent){ //ToDo: Why?? 
									switch(save.getID()){
									case TapEvent.BUTTON_CLICKED:
										infobox.setText(t.getText());
										
										break;
									
									}
									
								}
							}
						});
						                                          
						keyb.addChild(saveButton);
						break;
					default:
						break;
					
				}
				
				
			}
		});
		
		

		
		// Add MTComponets to Canvas
		// --------------------------------
		round_Rect.addChild(textField);
		round_Rect.addChild(rect);
		round_Rect.addChild(buttonRotate);
		round_Rect.addChild(buttonKeyboard);
		round_Rect.addChild(infobox);
		this.getCanvas().addChild(round_Rect);
	}
	@Override
	public void init() {}
	@Override
	public void shutDown() {}
}
