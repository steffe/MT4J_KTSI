

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTKeyboard;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;

public class HelloWorldScene extends AbstractScene {

	public HelloWorldScene(MTApplication mtApplication, String name) {
		super(mtApplication, name);
		
		MTColor white = new MTColor(0,0,0);
		MTColor myColor = new MTColor(234,122,121);
		
		// Add Keyboard
		MTKeyboard keyb = new MTKeyboard(mtApplication);
		keyb.setPositionGlobal(new Vector3D(400,400));
		keyb.setSizeXYGlobal(400, 200);
		
		this.setClearColor(new MTColor(146, 150, 188, 255));
		//Show touches
		this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));
		
		IFont fontArial = FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
				30, 	//Font size
				MTColor.BLACK);	//Font outline color
		//Create a textfield
		MTTextArea textField = new MTTextArea(mtApplication, fontArial); 
		textField.setNoStroke(true);
		textField.setNoFill(true);
		textField.setText("Hello World!");
		//Center the textfield on the screen
		//textField.setPositionGlobal(new Vector3D(mtApplication.width/2f, mtApplication.height/2f));
		//Add the textfield to our canvas
		textField.setPickable(false);
		textField.translate(new Vector3D(0,0,0));
		
		// Add Rectangle 
		MTRectangle rect = new MTRectangle(mtApplication,0, 0, 0, 100, 100 );
		rect.setPositionGlobal(new Vector3D(200,200));
		rect.setFillColor(myColor);
		rect.setNoFill(false);
		rect.setNoStroke(false);
		rect.setStrokeColor(MTColor.GREEN);
		rect.setPositionGlobal(new Vector3D(200,300));
		
		// Add Round Rectangle
		MTRoundRectangle round_Rect = new MTRoundRectangle(mtApplication,0, 0, 0, 300, 200, 15, 15 );
		round_Rect.setPositionGlobal(new Vector3D(600,300));
		round_Rect.setFillColor(new MTColor(12,12,12,34));
		round_Rect.setStrokeWeight(32);
		round_Rect.setStrokeColor(MTColor.MAROON);
		
		// Button
		PImage keyboardImg = mtApplication.loadImage("advanced" + MTApplication.separator + "flickrMT"+ MTApplication.separator + "data"+ MTApplication.separator + "keyb128.png");
		final MTImageButton keyboardButton = new MTImageButton(mtApplication,keyboardImg);
		keyboardButton.setSizeLocal(60,60);
		keyboardButton.setFillColor(new MTColor(255,255,255,200));
		keyboardButton.setName("KeyboardButton");
		keyboardButton.setNoStroke(true);
		keyboardButton.translateGlobal(new Vector3D(-2,mtApplication.height-keyboardButton.getWidthXY(TransformSpace.GLOBAL)+2,0));
		
		// Show Image
		PImage f5e = mtApplication.loadImage("ktsi/f5e2.png");
		
		
		
		this.getCanvas().addChild(keyboardButton);
		this.getCanvas().addChild(rect);
		round_Rect.addChild(textField);
		this.getCanvas().addChild(round_Rect);
		
		//this.getCanvas().addChild(textField);
		//this.getCanvas().addChild(keyb);
	}
	@Override
	public void init() {}
	@Override
	public void shutDown() {}
}
