package ch.mitoco.components.visibleComponents;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.smartcardio.ATR;

import org.apache.batik.anim.SetAnimation;
import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.gestureAction.DefaultRotateAction;
import org.mt4j.input.gestureAction.DefaultScaleAction;
import org.mt4j.input.gestureAction.InertiaDragAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import ch.mitoco.components.visibleComponents.widgets.Attributes;
import ch.mitoco.components.visibleComponents.widgets.Attributes;
import ch.mitoco.components.visibleComponents.widgets.MTDropDownList;
import ch.mitoco.components.visibleComponents.widgets.MTNumField;
import ch.mitoco.components.visibleComponents.widgets.MTTextAttribut;
import ch.mitoco.model.ModelAttributContent;
import ch.mitoco.model.ModelMtAttributs;
import ch.mitoco.model.ModelMtObjects;

import processing.core.PImage;

/**
 * 
 * The Class MyMTObject. Standard Class for all Object on the Table. 
 * 
 * @author tandrich
 */

public class MyMTObject extends MTRoundRectangle {

	
	/** */
	private MTApplication pApplet;

	/** */
	private MTRoundRectangle roundRect;
	
	/** */
	private MTRoundRectangle baserect;

	/** */
	private MTTextArea textField;

	/** Standard Object Width in Min Modus.*/
	private int obSizeMinWidth;
	
	/** Standard Object Height in Min Modus. */
	private int obSizeMinHeight;

	/** Object Width in Max Modus. */
	private int obSizeMaxWidth;
	
	/** Object Height in Max Modus. */
	private int obSizeMaxHeight;
	
	/** Object Size dif max to min. */
	private int obDifSizeHeight;
	
	/** */
	private MTImageButton buttonMaxMin;
	
	/** */
	private MTImageButton buttonRotate;

	/** Int for minmaxModus. */
	private int minmaxModus;
	
	/** Boolean for Rotation. */
	private boolean updownrotate;
	
	/** */
	private MTColor greypez = new MTColor(12, 12, 12, 34);

	/** */
	private int id;
	
	/** Font for Content. */
	private IFont fontArialMini;
	
	/** Font for Label. */
	private IFont labelfont;
	
	/** The default drag action. */
	private DefaultDragAction defaultDragAction;
	

	/** List with all Attributes.*/
	private List<Attributes>myAttributs;
	
	/** Space Value for X definition. */
	private int spaceglobal;
	
	/** List for Attributes. */	
	private List<ModelMtAttributs> attributesmodel;
	
	/** Data Model for this object.*/
	public ModelMtObjects objectmodel;
	
	/** Standard Font */
	private IFont fontArial;
	
	/** Public BLBL MyObject.
	 * 
	 * @param pApplet2 MTApplication
	 * @param model ModelMTObjects
	 * @param objectID INT ObjectID  
	 * 
	 * �nderungen die Model noch n�tig sind:
	 * - Labelfont
	 * - Normalerfont (vorhanden)
	 * - Objekt gr�sse (Breite und H�he)
	 * - Stroke Color
	 * - FIlecolor
	 */
	public MyMTObject(final MTApplication pApplet2, final ModelMtObjects model, final int objectID) {
		super(pApplet2, 0, 0, 0, 0, 0, 5, 5);
	
		pApplet = pApplet2;
		this.id = objectID;
		defaultDragAction 	= new DefaultDragAction();
		
		// Data Model 
		this.objectmodel = model;
		attributesmodel = model.getObjectattributs();
		
		
		// TODO Auto-generated constructor stub
		MTColor speblue = new MTColor(51, 102, 255);
		obSizeMinWidth = 300; // Gr�sse Min
		obSizeMinHeight = 300; // Gr�sse Min
		obSizeMaxWidth = 300;
		obSizeMaxHeight = 400;
		
		obDifSizeHeight = obSizeMaxHeight - obSizeMinHeight;
		
		IFont fontArial = FontManager.getInstance().createFont(pApplet, "arial.ttf", 
				15, 	//Font size
				MTColor.BLACK);
		
		this.setSizeLocal(obSizeMinWidth, obSizeMinHeight);
		this.setStrokeWeight(1);
		this.setStrokeColor(speblue);
	
		// Model filled parameters
		if (this.objectmodel.getObjectcolor() == null) {
			this.setFillColor(greypez);
		} else {
		this.setFillColor(this.objectmodel.getObjectcolor());
		}
		
		if (this.objectmodel.getObjectposition() == null) {
			this.setPositionGlobal(new Vector3D(300, 300));
		} else {
			this.setPositionGlobal(this.objectmodel.getObjectposition());
		}
		
		if (this.objectmodel.getObjectfont() == null) {
			fontArial = FontManager.getInstance().createFont(pApplet, "arial.ttf", 
					15, 	//Font size
					MTColor.BLACK);
		} else {
			fontArial = this.objectmodel.getObjectfont();
		}
		
		//Create a textfield
		textField = new MTTextArea(pApplet, fontArial); 
		textField.setNoStroke(true);
		textField.setNoFill(true);
		textField.setText("Object" + " ID:" + id);
		textField.setPickable(false);
		textField.translate(new Vector3D(5, 5));
		
		// Base Rect 
		baserect = new MTRoundRectangle(pApplet, 10, 10, 0, obSizeMinWidth - 20, obSizeMinHeight - 20, 5, 5);
		baserect.setNoStroke(true);
		baserect.setStrokeColor(MTColor.GRAY);
		baserect.setFillColor(MTColor.GRAY);
		baserect.setPickable(false);
		
		
		// Button for Rotate
		PImage buttonImage = pApplet.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  "buttonRotate.png");
		buttonRotate = new MTImageButton(pApplet, buttonImage);
		buttonRotate.setSizeLocal(30, 30);
		buttonRotate.setFillColor(new MTColor(255, 255, 255, 200));
		buttonRotate.setName("KeyboardButton");
		buttonRotate.setNoStroke(true);
		//keyboardButton.translateGlobal(new Vector3D(-2,mtApplication.height-keyboardButton.getWidthXY(TransformSpace.GLOBAL)+2,0));
		buttonRotate.setPositionGlobal(new Vector3D(20, obSizeMinHeight - 20));
		
		buttonRotate.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				switch(te.getTapID()) {
				case TapEvent.TAPPED:
					baserect.rotateZ(new Vector3D((baserect.getWidthXY(TransformSpace.LOCAL) / 2) + 10, (baserect.getHeightXY(TransformSpace.LOCAL) / 2) + 10), 180);
					updownrotate = !updownrotate;
					printMTObjectModel(objectmodel);
					break;	
				default:
					break;
				}
			return false;
			}
		});
		
		//Button Max/Min Modus
		PImage buttonMaxMinImage = pApplet.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  "buttonMaxMin.png");
		buttonMaxMin = new MTImageButton(pApplet, buttonMaxMinImage);
		buttonMaxMin.setSizeLocal(30, 30);
		buttonMaxMin.setFillColor(new MTColor(255, 255, 255, 200));
		buttonMaxMin.setName("KeyboardImage");
		buttonMaxMin.setNoStroke(true);
		buttonMaxMin.setPositionGlobal(new Vector3D(obSizeMinWidth - 20, 20));
		
		buttonMaxMin.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			
			private float h = getHeightXY(TransformSpace.RELATIVE_TO_PARENT);
			private float w = getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
			
			@Override
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent ke = (TapEvent) ge;	
				switch (ke.getTapID()) {
				case TapEvent.TAPPED:

				if (minmaxModus == 0) {
					
					setSizeLocal(obSizeMaxWidth, obSizeMaxHeight);
					baserect.setSizeLocal(obSizeMaxWidth - 20, obSizeMaxHeight - 20);
					if (updownrotate == true) {
						baserect.translate(new Vector3D(0, obDifSizeHeight));
					}	
					buttonMaxMin.translate(new Vector3D(obSizeMaxWidth - w, 0));
					buttonRotate.translate(new Vector3D(0, obSizeMaxHeight - h));

					setMax();
					minmaxModus = 1;
				} else {
					setSizeLocal(w, h);
					
					baserect.setSizeLocal(w - 20, h - 20);
					if (updownrotate == true) {
						baserect.translate(new Vector3D(0, -obDifSizeHeight));
					}
					
					buttonMaxMin.translate(new Vector3D(w - obSizeMaxWidth, 0));
					buttonRotate.translate(new Vector3D(0, h - obSizeMaxHeight));
					
					setMin();
					minmaxModus = 0;
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
		labelfont = FontManager.getInstance().createFont(pApplet, "arial.ttf", 
				14, 	//Font size
				MTColor.WHITE);
		
		// Crate ArrayList for Attributs
		myAttributs = new ArrayList<Attributes>();
		
		// Add MTComponets to Canvas
		// --------------------------------

		baserect.addChild(textField); //Titeltext
		createAttributes();
		addToBaserect(); // Add all Attributes to the base Rect Object
		setPickableAttributes();
		
		this.addChild(baserect);
		this.addChild(buttonRotate);
		this.addChild(buttonMaxMin);
		
	}
	
	/**
	 *  Create Attributes.
	 */
	private void createAttributes() {
	int i = 0;
	int x = 60;
	int difx = 50;
		for (ModelMtAttributs it : attributesmodel) {
			switch(it.getId()) {
			case(0):
				System.out.println(i + "Attributs MTTextAttribut " + it);
				
				myAttributs.add(new MTTextAttribut(pApplet, attributesmodel.get(i), fontArialMini, 250, 30, "ee2eee3ww fw3ewf", "TestFeld Text", labelfont));
				myAttributs.get(i).setPositionRelativeToParent(new Vector3D(this.getWidthXY(TransformSpace.LOCAL) / 2, x));
			
				break;
			case(1):	
				System.out.println(i + "Attributs MTNumField " + it);
			
				myAttributs.add(new MTNumField(pApplet, attributesmodel.get(i), fontArialMini, 250, 30, true, 1111134.34, "TestFeld", labelfont));
				myAttributs.get(i).setPositionRelativeToParent(new Vector3D(this.getWidthXY(TransformSpace.LOCAL) / 2, x));
			
				break;
			case(2):
				System.out.println(i + "Attributs MTDropDown " + it);
			
				myAttributs.add(new MTDropDownList(pApplet, attributesmodel.get(i), fontArialMini, 250, 30, "Projekt Wichtigkeit", labelfont));
				myAttributs.get(i).setPositionRelativeToParent(new Vector3D(this.getWidthXY(TransformSpace.LOCAL) / 2, x));

				break;
			default:
				break;
			}
		i++;
		x += difx;
		}
		
	}
	
	
	/**
	 *  Set Pickable False for all Attributes.
	 */
	private void setPickableAttributes() {
		for (Attributes it : myAttributs) {
			it.setPickable(false);
		}
	}
	
	
	/**
	 * Add Attributs to baserect.
	 */
	private void addToBaserect() {
		
		for (int j = myAttributs.size() - 1; j >= 0; j--) {
			  System.out.println("addChild " + j);
			  baserect.addChild(myAttributs.get(j));
			}
	}
	
	/**
	 * Private setMax for all Attributes.
	 */
	private void setMax() {
		for (Attributes it : myAttributs) {
			it.setMax();
		}
			
	}
	
	/**
	 * Private setMin for all Attributes.
	 */
	private void setMin() {
		for (Attributes it : myAttributs) {
			it.setMin();
		}
			
	}
		
	/**
	 * Setzen des Object Namens. String als �bergabe-Parameters.
	 * @param titelname String
	 */
	public final void setTitelName(final String titelname) {
	textField.setText(titelname);	
	}
	
	/**
	 * Zerst�rt das Objekt.
	 * 
	 */
	public final void destroyMyObject() {
	roundRect.destroy();
	}
	
	/**
	 * Get Object ID back.
	 * @return id int
	 */
	public final int getID() {
	return id;
	}

	/** 
	 * Print the content from select Modelobject. 
	 * 
	 * @param obmodel ModelMtAttributs
	 */
	private void printMTObjectModel(final ModelMtObjects obmodel) {
		
		System.out.println("\n");
		System.out.println("Object ID:       " + obmodel.getId());
		System.out.println("Object Color:    " + obmodel.getObjectcolor());
		System.out.println("Object Font:     " + obmodel.getObjectfont());
		System.out.println("Object Position: " + obmodel.getObjectposition());
		
		for (ModelMtAttributs attribut : obmodel.getObjectattributs()) {
			System.out.println("   Attribut ID:  " + attribut.getId());
			System.out.println("   Color:      " + attribut.getAttcolor());
			System.out.println("   Label:      " + attribut.getLable());
			System.out.println("   Zoom:       " + attribut.getZoom());
			int i = 0;
			for (ModelAttributContent it : attribut.getAttributcontent()) {
				System.out.println("              " + i + " Attr. Typ: " + it.getType());
				System.out.println("              " + i + " Attr. Value: " + it.getValue());
				i++;
				
			}
		}
		System.out.println("\n");
	}
	
	
	@Override
	protected void setDefaultGestureActions() {
//		super.setDefaultGestureActions();
		
		this.addGestureListener(DragProcessor.class, new InertiaDragAction());
		
		DragProcessor dp = new DragProcessor(getRenderer());
//		dp.setLockPriority(0.5f);
		registerInputProcessor(dp);
		addGestureListener(DragProcessor.class, new DefaultDragAction());
		dp.setBubbledEventsEnabled(true); //FIXME TEST
		
		RotateProcessor rp = new RotateProcessor(getRenderer());
//		rp.setLockPriority(0.8f);
		registerInputProcessor(rp);
		addGestureListener(RotateProcessor.class, new DefaultRotateAction());
		rp.setBubbledEventsEnabled(true);  //FIXME TEST
		
		ScaleProcessor sp = new ScaleProcessor(getRenderer());
//		sp.setLockPriority(0.8f);
		registerInputProcessor(sp);
		addGestureListener(ScaleProcessor.class, new DefaultScaleAction());
		sp.setBubbledEventsEnabled(true);  //FIXME TEST
	}
	
	
}
/**
 * Struktur des Objekts
 * ---------------------
 * Alle Attribute sind unter Objekte und beinhalte ihre spezifischen Eigenschaften. M�ssen �ber eine XML Schnittstelle anbindbar sein
 * Max/Min Modus ??? 
 * 
 * Wie soll Anzahl Attribute und die Gr�sse des gesammt Objekt gehandhabt werden? 
 * 1 Fixe Max/Min Gr�sse -> Attribute Teilen sich den vorhanden Platz. Mehre Attribute -> weniger Platz!
 * 2.Attribute bekommen definierte Gr�sse zugeschrieben. Mehere Attribute ergeben ein gr�sser Objekt!
 * 
 * - Textarea (Textmenge gr�sser wie der vorgesehner Platz-> Abschneiden, Scrollbalgen, Schriftgr�sse Anpassen)
 * - Bild (JPEG vorhanden)
 * - Zeichung (zum selber zeichnen)
 * - Textlinie
 * - Number, Data, W�hrung
 * 
 * Kontextmen�:
 *  Anstatt Scene wird ein MTMyObject (roundRect) �bergeben. CloseButton zerst�rt das Object und Restore �bernimmt die Funktion des MIN/Max 
 * 
 * 
		this.removeAllChildren();
		this.removeAllGestureEventListeners();
		this.unregisterAllInputProcessors();
		
		this.removeAllGestureEventListeners(DragProcessor.class);
		 this.addGestureListener(DragProcessor.class, defaultDragAction);
		 
		this.unregisterAllInputProcessors();
		this.setGestureAllowance(DragProcessor.class, false);
		this.setGestureAllowance(RotateProcessor.class, false);
		this.setGestureAllowance(ScaleProcessor.class, false);
		this.setGestureAllowance(TapProcessor.class, true);
		
		
		textarea.unregisterAllInputProcessors();
		textarea.setPickable(false);
 * 
*/
