package test.thomas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTSceneMenu;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.MTTextArea.ExpandDirection;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTKeyboard;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;

/**
 * 
 * The Class MyMTObject. Standard Class for all Object on the Table. 
 * 
 * @author tandrich
 */

public class MyMTObject extends MTComponent{

	private MTRectangle rect;
	private MTApplication pApplet;
	private MTRoundRectangle round_Rect;
	private MTRoundRectangle baserect;
	private MTTextArea infobox;
	private MTTextArea textField;
	private int h;
	private int w;
	private int zoomFac;
	private int obSizeWidht;
	private int obSizeHeight;
	private int numberAttribut;
	private MTImageButton buttonMaxMin;
	private MTImageButton buttonRotate;
	private MTImageButton buttonKeyboard;
	private int minmaxModus;
	private MTColor greypez = new MTColor(12,12,12,34);
	private MTTextField desc_info;
	private int id;
	
	public MyMTObject(MTApplication pApplet_2, int id_2)  {
		super (pApplet_2);
		pApplet=pApplet_2;
		this.id = id_2;
		// TODO Auto-generated constructor stub
		MTColor white = new MTColor(0,0,0);
		MTColor speblue = new MTColor(51,102,255);
		obSizeWidht = 300; // Grösse Min
		obSizeHeight = 200; // Grösse Min
		numberAttribut =2; // Anzahl Attribute (Bild, Text) im Objekt vorhanden.

		
		IFont fontArial = FontManager.getInstance().createFont(pApplet, "arial.ttf", 
				25, 	//Font size
				MTColor.BLACK,  //Font fill color
				white);	//Font outline color
		//Create a textfield
		textField = new MTTextArea(pApplet, fontArial); 
		textField.setNoStroke(true);
		textField.setNoFill(true);
		textField.setText("F5E Tiger"+" ID:"+id);
		//Center the textfield on the screen
		//textField.setPositionGlobal(new Vector3D(mtApplication.width/2f, mtApplication.height/2f));
		//Add the textfield to our canvas
		textField.setPickable(false);
		textField.translate(new Vector3D(5,5));
		
		// Base Rect 
		
		baserect = new MTRoundRectangle(10,10,0,obSizeWidht-20,obSizeHeight-20,5,5,pApplet);
		baserect.setNoStroke(true);
		baserect.setStrokeColor(MTColor.GRAY);
		baserect.setFillColor(MTColor.GRAY);
		baserect.setPickable(false);
		
		
		// Add Round Rectangle
		round_Rect = new MTRoundRectangle(0, 0, 0, obSizeWidht, obSizeHeight, 10, 10, pApplet);
		round_Rect.setPositionGlobal(new Vector3D(600,300));
		round_Rect.setFillColor(greypez);
		round_Rect.setStrokeWeight(1);
		round_Rect.setStrokeColor(speblue);
	
		
		// Show Image
		PImage f5e = pApplet.loadImage("test" + MTApplication.separator + "thomas"+ MTApplication.separator + "image" + MTApplication.separator + "f5e2.png");
		h = f5e.height;
		w = f5e.width; 
		zoomFac=3;
		rect = new MTRectangle(0, 0, 0, w/zoomFac, h/zoomFac, pApplet);
		//rect.setFillColor(new MTColor(1,1,1,0));
		rect.setNoFill(false);
		rect.setNoStroke(false);
		rect.setStrokeColor(MTColor.GRAY);
		rect.setPositionGlobal(new Vector3D(150,100));
		rect.setTexture(f5e);
		rect.setPickable(false);
		
		//rect.rotateZ(new Vector3D(100,100),90); // Drehen eines Objekts		
		
		// Textfield for Input
		IFont fontArialMini = FontManager.getInstance().createFont(pApplet, "arial.ttf", 
				15, 	//Font size
				MTColor.BLACK,  //Font fill color
				white);	//Font outline color
		
		infobox = new MTTextArea(20,(obSizeHeight/numberAttribut)+10,obSizeWidht-40,(obSizeHeight/numberAttribut)-30, fontArialMini,pApplet); 
		infobox.setNoStroke(false);
		infobox.setNoFill(true);
		infobox.setText("xx");
		infobox.setPickable(false);
		infobox.translate(new Vector3D(0,0,0));
	//	infobox.setPositionGlobal(new Vector3D(200,20));
		infobox.setStrokeWeight(1);
		infobox.setStrokeColor(MTColor.BLACK);
		infobox.setName("Inhalt");
	//	infobox.setSizeLocal(50, 140);
	
		// Description Field (Infobox)
	
		desc_info = new MTTextField(0,0, 50, 20,fontArialMini, pApplet);
		desc_info.setNoStroke(true);
		desc_info.setNoFill(true);
		desc_info.setText(infobox.getName());
		
		// Button for Rotate
		PImage buttonImage = pApplet.loadImage("test" + MTApplication.separator + "thomas"+ MTApplication.separator + "image" + MTApplication.separator +  "buttonRotate.png");
		buttonRotate = new MTImageButton(buttonImage, pApplet);
		buttonRotate.setSizeLocal(30,30);
		buttonRotate.setFillColor(new MTColor(255,255,255,200));
		buttonRotate.setName("KeyboardButton");
		buttonRotate.setNoStroke(true);
		//keyboardButton.translateGlobal(new Vector3D(-2,mtApplication.height-keyboardButton.getWidthXY(TransformSpace.GLOBAL)+2,0));
		buttonRotate.setPositionGlobal(new Vector3D(20,obSizeHeight-20));
		
		buttonRotate.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				switch(e.getID())
				{
				case TapEvent.BUTTON_CLICKED:
					round_Rect.rotateZ(new Vector3D(w/zoomFac,h/zoomFac,0), 180); // 516 x 316 
					// rect.setSizeXYGlobal(120, 120); // Achtung auf Rückstellungen des Wertes
					break;
				
				default:
					break;
				}
				
			}
		});
		
		// Button for Keyboard 
		PImage buttonKeyboardImage = pApplet.loadImage("test" + MTApplication.separator + "thomas"+ MTApplication.separator + "image" + MTApplication.separator + "keyb128.png");
		buttonKeyboard= new MTImageButton(buttonKeyboardImage, pApplet);
		buttonKeyboard.setSizeLocal(30, 30);
		buttonKeyboard.setFillColor(new MTColor(255,255,255,200));
		buttonKeyboard.setName("KeyboardImage");
		buttonKeyboard.setNoStroke(true);
		buttonKeyboard.setPositionGlobal(new Vector3D(obSizeWidht-20,obSizeHeight-20));
		
		buttonKeyboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ke) {
				switch(ke.getID()){
					case TapEvent.BUTTON_CLICKED:
						// Add Keyboard
						MTKeyboard keyb = new MTKeyboard(pApplet);
						//keyb.setPositionGlobal(new Vector3D(400,400));
						//keyb.setSizeXYGlobal(400, 200);
						keyb.setFillColor(new MTColor(30,30,30,210));
						keyb.setStrokeColor(new MTColor(0,0,0,255));
						keyb.setSizeXYGlobal(350, 200);
						keyb.translate(new Vector3D(-50,50));
						
						
				        final MTTextArea t = new MTTextArea(pApplet, FontManager.getInstance().createFont(pApplet, "arial.ttf", 50, 
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
						PImage saveButtonImage = pApplet.loadImage("test" + MTApplication.separator + "thomas"+ MTApplication.separator + "image" + MTApplication.separator + "buttonSave.png");
						final MTImageButton saveButton= new MTImageButton(saveButtonImage, pApplet);
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
		
		//Button Max/Min Modus
		PImage buttonMaxMinImage = pApplet.loadImage("test" + MTApplication.separator + "thomas"+ MTApplication.separator + "image" + MTApplication.separator +  "buttonMaxMin.png");
		buttonMaxMin= new MTImageButton(buttonMaxMinImage, pApplet);
		buttonMaxMin.setSizeLocal(30, 30);
		buttonMaxMin.setFillColor(new MTColor(255,255,255,200));
		buttonMaxMin.setName("KeyboardImage");
		buttonMaxMin.setNoStroke(true);
		buttonMaxMin.setPositionGlobal(new Vector3D(obSizeWidht-20,20));

		buttonMaxMin.addActionListener(new ActionListener() {
			
		float h= round_Rect.getHeightXY(TransformSpace.RELATIVE_TO_PARENT);
		float w= round_Rect.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
		MTColor cFill = new MTColor(round_Rect.getFillColor());
		MTColor cRRFill= new MTColor(baserect.getFillColor());
		
			public void actionPerformed(ActionEvent mm) {
				
				switch (mm.getID()) {
				case TapEvent.BUTTON_CLICKED:

				if(minmaxModus==0){
					obSizeHeight = 400;
					obSizeWidht = 300;
					round_Rect.setSizeLocal(obSizeWidht, obSizeHeight);
					round_Rect.setFillColor(cFill);
					baserect.setSizeLocal(obSizeWidht-20, obSizeHeight-20);
					baserect.setFillColor(cRRFill);
					buttonKeyboard.translate(new Vector3D(obSizeWidht-w,obSizeHeight-h));
					buttonMaxMin.translate(new Vector3D(obSizeWidht-w,0));
					buttonRotate.translate(new Vector3D(0,obSizeHeight-h));
					
					infobox.translate(new Vector3D(0,60)); 
					desc_info.translate(new Vector3D(0,-25));
					infobox.addChild(desc_info); // Text Beschrieb für Infobox wird der Box vererbt (einfachere Handhabung)
					minmaxModus=1;
					}
				else{
					round_Rect.setSizeLocal(w, h);
					round_Rect.setFillColor(cFill);
					baserect.setSizeLocal(w-20, h-20);
					baserect.setFillColor(cRRFill);
					buttonKeyboard.translate(new Vector3D(w-obSizeWidht,h-obSizeHeight));
					buttonMaxMin.translate(new Vector3D(w-obSizeWidht,0));
					buttonRotate.translate(new Vector3D(0,h-obSizeHeight));
					
					
					infobox.translate(new Vector3D(0,-60));
					desc_info.translate(new Vector3D(0,25));
					infobox.removeChild(desc_info);
					minmaxModus=0;
					
					}
					break;

				default:
					break;
				}
				
			}
		});
	
	
		
		//System.out.println("Widht = "+w);
		//System.out.println("Height = " +h);
		
		// Add MTComponets to Canvas
		// --------------------------------
		round_Rect.addChild(baserect);
		round_Rect.addChild(textField); //Titeltext
		round_Rect.addChild(infobox); // Text mit Eingabebox
		round_Rect.addChild(rect); // Bild 
		round_Rect.addChild(buttonRotate);
		round_Rect.addChild(buttonKeyboard);
		round_Rect.addChild(buttonMaxMin);
		
	}
	/**
	 * Gibt das Basis-Object zurück. (Als MTRound Rectangle)
	 * @return
	 */
	public MTRoundRectangle getMyObjectBack(){	
	return round_Rect;
	}
	/**
	 * Setzen des Object Namens. String als Übergabe-Parameters.
	 * @param titelname
	 */
	public void setTitelName(String titelname){
	textField.setText(titelname);	
	}
	
	/**
	 * Zerstört das Objekt 
	 */
	public void destroyMyObject(){
	round_Rect.destroy();
	}
	
	/**
	 * Gibt die Object ID zurück
	 */
	public int getID(){
	return id;
	}
}
/**
 * Struktur des Objekts
 * ---------------------
 * Alle Attribute sind unter Objekte und beinhalte ihre spezifischen Eigenschaften. Müssen über eine XML Schnittstelle anbindbar sein
 * Max/Min Modus ??? 
 * 
 * Wie soll Anzahl Attribute und die Grösse des gesammt Objekt gehandhabt werden? 
 * 1 Fixe Max/Min Grösse -> Attribute Teilen sich den vorhanden Platz. Mehre Attribute -> weniger Platz!
 * 2.Attribute bekommen definierte Grösse zugeschrieben. Mehere Attribute ergeben ein grösser Objekt!
 * 
 * - Textarea (Textmenge grösser wie der vorgesehner Platz-> Abschneiden, Scrollbalgen, Schriftgrösse Anpassen)
 * - Bild (JPEG vorhanden)
 * - Zeichung (zum selber zeichnen)
 * - Textlinie
 * - Number, Data, Währung
 * 
 * Kontextmenü:
 *  Anstatt Scene wird ein MTMyObject (roundRect) übergeben. CloseButton zerstört das Object und Restore übernimmt die Funktion des MIN/Max 
 * 
 * 
 * 
*/