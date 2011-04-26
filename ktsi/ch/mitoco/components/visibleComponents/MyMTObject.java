package ch.mitoco.components.visibleComponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.MTTextArea.ExpandDirection;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTKeyboard;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import ch.mitoco.components.visibleComponents.widgets.MTDropDownList;
import ch.mitoco.components.visibleComponents.widgets.MTNumField;

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
	private IFont fontArialMini;
	
	/** Test Attribut */
	private MTNumField p1;

	/** Test Attribut */
	private MTDropDownList d1;
	
	public MyMTObject(MTApplication pApplet_2, int id_2)  {
		super (pApplet_2);
		pApplet=pApplet_2;
		this.id = id_2;
		
		// TODO Auto-generated constructor stub
		MTColor speblue = new MTColor(51,102,255);
		obSizeWidht = 300; // Grösse Min
		obSizeHeight = 200; // Grösse Min
		numberAttribut =2; // Anzahl Attribute (Bild, Text) im Objekt vorhanden.
	
		IFont fontArial = FontManager.getInstance().createFont(pApplet, "arial.ttf", 
				15, 	//Font size
				MTColor.BLACK);
	
		//Create a textfield
		textField = new MTTextArea(pApplet, fontArial); 
		textField.setNoStroke(true);
		textField.setNoFill(true);
		textField.setText("Object"+" ID:"+id);
		//Center the textfield on the screen
		//textField.setPositionGlobal(new Vector3D(mtApplication.width/2f, mtApplication.height/2f));
		//Add the textfield to our canvas
		textField.setPickable(false);
		textField.translate(new Vector3D(5,5));
		
		// Base Rect 
		
		baserect = new MTRoundRectangle(pApplet, 10,10,0,obSizeWidht-20,obSizeHeight-20,5,5);
		baserect.setNoStroke(true);
		baserect.setStrokeColor(MTColor.GRAY);
		baserect.setFillColor(MTColor.GRAY);
		baserect.setPickable(false);
		
		
		// Add Round Rectangle
		round_Rect = new MTRoundRectangle(pApplet, 0, 0, 0, obSizeWidht, obSizeHeight, 10, 10);
		round_Rect.setPositionGlobal(new Vector3D(600,300));
		round_Rect.setFillColor(greypez);
		round_Rect.setStrokeWeight(1);
		round_Rect.setStrokeColor(speblue);
	
		
			
		

		
		// Button for Rotate
		PImage buttonImage = pApplet.loadImage("ch" + MTApplication.separator + "mitoco"+ MTApplication.separator + "data" + MTApplication.separator +  "buttonRotate.png");
		buttonRotate = new MTImageButton(pApplet, buttonImage);
		buttonRotate.setSizeLocal(30,30);
		buttonRotate.setFillColor(new MTColor(255,255,255,200));
		buttonRotate.setName("KeyboardButton");
		buttonRotate.setNoStroke(true);
		//keyboardButton.translateGlobal(new Vector3D(-2,mtApplication.height-keyboardButton.getWidthXY(TransformSpace.GLOBAL)+2,0));
		buttonRotate.setPositionGlobal(new Vector3D(20,obSizeHeight-20));
		
		buttonRotate.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				switch(te.getTapID())
				{
				case TapEvent.TAPPED:
					baserect.rotateZ(new Vector3D((baserect.getWidthXY(TransformSpace.LOCAL)/2)+10,(baserect.getHeightXY(TransformSpace.LOCAL)/2)+10), 180);
					break;
				
					
				default:
					break;
				}
			return false;
			}
		});
		


		
		
		//Button Max/Min Modus
		PImage buttonMaxMinImage = pApplet.loadImage("ch" + MTApplication.separator + "mitoco"+ MTApplication.separator + "data" + MTApplication.separator +  "buttonMaxMin.png");
		buttonMaxMin= new MTImageButton(pApplet, buttonMaxMinImage);
		buttonMaxMin.setSizeLocal(30, 30);
		buttonMaxMin.setFillColor(new MTColor(255,255,255,200));
		buttonMaxMin.setName("KeyboardImage");
		buttonMaxMin.setNoStroke(true);
		buttonMaxMin.setPositionGlobal(new Vector3D(obSizeWidht-20,20));

		buttonMaxMin.addGestureListener(TapProcessor.class, new IGestureEventListener() {
		
		float h= round_Rect.getHeightXY(TransformSpace.RELATIVE_TO_PARENT);
		float w= round_Rect.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
		MTColor cFill = new MTColor(round_Rect.getFillColor());
		MTColor cRRFill= new MTColor(baserect.getFillColor());
		
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent ke = (TapEvent)ge;	
				switch (ke.getTapID()) {
				case TapEvent.TAPPED:

				if(minmaxModus==0){
					obSizeHeight = 400;
					obSizeWidht = 300;
					round_Rect.setSizeLocal(obSizeWidht, obSizeHeight);
					round_Rect.setFillColor(cFill);
					baserect.setSizeLocal(obSizeWidht-20, obSizeHeight-20);
					baserect.setFillColor(cRRFill);
					buttonMaxMin.translate(new Vector3D(obSizeWidht-w,0));
					buttonRotate.translate(new Vector3D(0,obSizeHeight-h));
					
				//	d1.setMax();
					p1.setMax();
					minmaxModus=1;
					}
				else{
					round_Rect.setSizeLocal(w, h);
					round_Rect.setFillColor(cFill);
					baserect.setSizeLocal(w-20, h-20);
					baserect.setFillColor(cRRFill);
					buttonMaxMin.translate(new Vector3D(w-obSizeWidht,0));
					buttonRotate.translate(new Vector3D(0,h-obSizeHeight));
					
				//	d1.setMin();
					p1.setMin();
					minmaxModus=0;
					
					}
					break;

				default:
					break;
				}
				
				return false;
			}
		});
	
	
		// Font for Textfield
		fontArialMini = FontManager.getInstance().createFont(pApplet, "arial.ttf", 
				14, 	//Font size
				MTColor.BLACK);
		// Font for Label
		IFont labelfont = FontManager.getInstance().createFont(pApplet, "arial.ttf", 
				22, 	//Font size
				MTColor.WHITE);
		
		// Testattribut 
		p1 = new MTNumField(pApplet_2,fontArialMini,250,30,true,"1111","TestFeld", labelfont);
		p1.setPositionRelativeToParent(new Vector3D(round_Rect.getWidthXY(TransformSpace.LOCAL)/2,60));
		//p1.setPickable(false);
		
		d1 = new MTDropDownList(pApplet_2, fontArial, 250, 30, 5, "Projekt Wichtigkeit",labelfont);
		d1.setPositionRelativeToParent(new Vector3D(round_Rect.getWidthXY(TransformSpace.LOCAL)/2,100));
		d1.setPickable(false);


		
		// Add MTComponets to Canvas
		// --------------------------------
		
		baserect.addChild(p1);
			
		baserect.addChild(d1);
		
		baserect.addChild(textField); //Titeltext
		round_Rect.addChild(baserect);
		round_Rect.addChild(buttonRotate);
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
