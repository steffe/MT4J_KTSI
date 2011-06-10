package ch.mitoco.components.visibleComponents;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.smartcardio.ATR;

import org.apache.batik.anim.SetAnimation;
import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.MTEvent;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.gestureAction.DefaultRotateAction;
import org.mt4j.input.gestureAction.DefaultScaleAction;
import org.mt4j.input.gestureAction.InertiaDragAction;
import org.mt4j.input.inputData.AbstractCursorInputEvt;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.lassoProcessor.ILassoable;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.ToolsMath;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.input.inputProcessors.componentProcessors.Group3DProcessorNew.IClusterEventListener;

import ch.mitoco.components.visibleComponents.widgets.Attributes;
import ch.mitoco.components.visibleComponents.widgets.Attributes;
import ch.mitoco.components.visibleComponents.widgets.MTColorPickerGroup;
import ch.mitoco.components.visibleComponents.widgets.MTDropDownList;
import ch.mitoco.components.visibleComponents.widgets.MTNumField;
import ch.mitoco.components.visibleComponents.widgets.MTPictureBox;
import ch.mitoco.components.visibleComponents.widgets.MTTextAttribut;
import ch.mitoco.model.ModelAttributContent;
import ch.mitoco.model.ModelAttributDefinition;
import ch.mitoco.model.ModelMtAttributs;
import ch.mitoco.model.ModelMtObjects;

import processing.core.PImage;

/**
 * 
 * The Class MyMTObject. Standard Class for all Object on the Table. 
 * 
 * @author tandrich
 */

public class MyMTObject extends MTRoundRectangle implements ILassoable {

	
	/** MTApplication. */
	private MTApplication pApplet;
	
	/** Baserect is the base structur for all Attributs. */
	private MTRoundRectangle baserect;

	/** Object Label. */
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
	
	/** MTImageButton for Max/Min Modus. */
	private MTImageButton buttonMaxMin;
	
	/** MTImageButton for Rotate Modus.*/
	private MTImageButton buttonRotate;

	/** Int for minmaxModus. */
	private int minmaxModus;
	
	/** Boolean for Rotation. */
	private boolean updownrotate;
	
	/** Default Color for Border. */
	private MTColor greypez = new MTColor(12, 12, 12, 34);

	/** Default Color for stroke. */
	private MTColor speblue = new MTColor(51, 102, 255);
	
	/** Object ID for identification and Objectlabel. */
	private int id;
	
	/** Font for Content. */
	private IFont fontArialMini;
	
	/** Font for Label. */
	private IFont labelfont;
	
	/** List with all Attributes.*/
	private List<Attributes>myAttributs;
		
	/** List for Attributes. */	
	private List<ModelMtAttributs> attributesmodel;
	
	/** Data Model for this object.*/
	private ModelMtObjects objectmodel;
	
	/** Standard Font. */
	private IFont fontArial;
	
	/** Color Picker. */
	private MTColorPickerGroup colorpicker;

	/** Tagged Boolean.*/
	private boolean tagged;
	
	/** before Tagged Stroke Color. */
	private MTColor befortaggedColor;
	
	/** Clip selected. */
	private boolean selected;
	
	
	/** Constructor MyMTObject.
	 * 
	 * Im Konstruktor wird das Grundobjekt mit dem Bilderrahmen und den Grundfunktionen aufgebaut. 
	 * 
	 * 
	 * Änderungen die Model noch nötig sind:
	 * - Labelfont
	 * - Normalerfont (vorhanden)
	 * - Objekt grösse (Breite und Höhe)
	 * - Konstruktion des Objekt in eine init Methode auslagern
	 * 
	 * 
	 * @param pApplet2 MTApplication
	 * @param model ModelMTObjects
	 * @param objectID INT ObjectID  
	 * 
	 */
	public MyMTObject(final MTApplication pApplet2, final ModelMtObjects model, final int objectID) {
		super(pApplet2, 0, 0, 0, 0, 0, 5, 5);

		
		pApplet = pApplet2;
		this.id = objectID;
		this.selected = false;
		
		// Data Model 
		this.objectmodel = model;
		attributesmodel = model.getObjectattributs();
		
		obSizeMinWidth = 300; // Grösse Min
		obSizeMinHeight = 300; // Grösse Min
		obSizeMaxWidth = 300;
		obSizeMaxHeight = 400;
		
		obDifSizeHeight = obSizeMaxHeight - obSizeMinHeight;
	
		fontArial = FontManager.getInstance().createFont(pApplet, "arial.ttf", 
				15, 	//Font size
				MTColor.BLACK);
		
		this.setSizeLocal(obSizeMinWidth, obSizeMinHeight);
		this.setStrokeWeight(1);
		this.setName("Object "+id);
				
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
		baserect.setPickable(false);
		
		readData();
		
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
					dataWrite();
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
					dataWrite();
				if (minmaxModus == 0) {
					
					setSizeLocal(obSizeMaxWidth, obSizeMaxHeight);
					baserect.setSizeLocal(obSizeMaxWidth - 20, obSizeMaxHeight - 20);
					if (updownrotate) {
						baserect.translate(new Vector3D(0, obDifSizeHeight));
					}	
					buttonMaxMin.translate(new Vector3D(obSizeMaxWidth - w, 0));
					buttonRotate.translate(new Vector3D(0, obSizeMaxHeight - h));

					setMax();
					minmaxModus = 1;
				} else {
					setSizeLocal(w, h);
					
					baserect.setSizeLocal(w - 20, h - 20);
					if (updownrotate) {
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

		// ColorPicker
		colorpicker = new MTColorPickerGroup(pApplet, this, baserect);
		colorpicker.translate(new Vector3D(0, -35, 0));
		colorpicker.setVisible(false);
		
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
		saveOnEvent();		
		this.addChild(baserect);
		this.addChild(buttonRotate);
		this.addChild(buttonMaxMin);
		this.addChild(colorpicker);
	}
	
	/** Create attributes.
	 * 
	 * Anhanden der IDs die im attributesmodel gespeichert sind, wird das Objekt aufgebaut.
	 * 
	 * Gibt es weiter Attribute, muss die Case Anweisung um die gewünschte ID erweitern werden. 
	 * Welche ID welches Attribut ist muss definiert werden und kann nicht einfach so geändert werden.
	 * ID 0: 	MTTextAttribut
	 * ID 1: 	MTNumField
	 * ID 2: 	MTDropDown
	 * ID 3: 	MTPictureBox
	 * ID 4:    futur MTTextBox
	 * ID 5:	futur MTPainter
	 * ID 6:	futur MTBrowser
	 * ID 7:	futur MTPDFReader
	 * 
	 * TODO: Interface bauen.
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
			
				myAttributs.add(new MTNumField(pApplet, attributesmodel.get(i), fontArialMini, 250, 30, true, "1111134.34", "TestFeld", labelfont));
				myAttributs.get(i).setPositionRelativeToParent(new Vector3D(this.getWidthXY(TransformSpace.LOCAL) / 2, x));
			
				break;
			case(2):
				System.out.println(i + "Attributs MTDropDown " + it);
			
				myAttributs.add(new MTDropDownList(pApplet, attributesmodel.get(i), fontArialMini, 250, 30, "Projekt Wichtigkeit", labelfont));
				myAttributs.get(i).setPositionRelativeToParent(new Vector3D(this.getWidthXY(TransformSpace.LOCAL) / 2, x));

				break;
				
			case(3):
				System.out.println(i + "Attributs MTPictureBox " + it);
				
				myAttributs.add(new MTPictureBox(pApplet, attributesmodel.get(i), 250, 100, "Picuture", labelfont));
				myAttributs.get(i).setPositionRelativeToParent(new Vector3D(this.getWidthXY(TransformSpace.LOCAL) / 2, x + 40));



			
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
		colorpicker.setVisible(true);
	}
	
	/**
	 * Private setMin for all Attributes.
	 */
	private void setMin() {
		for (Attributes it : myAttributs) {
			it.setMin();
		}
		colorpicker.setVisible(false);
	}
	
	/**
	 * Read data from object Model.
	 */
	private void readData() {		
		
		// Model filled color Border parameters
		if (this.objectmodel.getObjectcolor() == null) {
			this.setFillColor(greypez);
		} else {
		this.setFillColor(this.objectmodel.getObjectcolor());
		}
		
		// Object linie color
		if (this.objectmodel.getObjectlinecolor() == null) {
			this.setStrokeColor(speblue);
		} else {
		this.setStrokeColor(this.objectmodel.getObjectlinecolor());
		}
		
		// Base Backround Color
		System.out.println("Fill Color");
		if (this.objectmodel.getObjectFillcolor() == null) {
			System.out.println("No Fill Color found");
			baserect.setFillColor(MTColor.GRAY);
			baserect.setStrokeColor(MTColor.GRAY);
		} else {
			System.out.println("Fill Color found");
		baserect.setStrokeColor(this.objectmodel.getObjectFillcolor());
		baserect.setFillColor(this.objectmodel.getObjectFillcolor());
		}
		
		// Object Position with random when no position is stored
		System.out.println("Position");
		if (this.objectmodel.getObjectposition() == null) {
			this.setPositionGlobal(new Vector3D(ToolsMath.nextRandomInt(140, 800), ToolsMath.nextRandomInt(140, 700)));
		} else {
			this.setPositionGlobal(this.objectmodel.getObjectposition());
		}
		
		// Object Font
		System.out.println("Font");
		if (this.objectmodel.getObjectfont() == null) {
			fontArial = FontManager.getInstance().createFont(pApplet, "arial.ttf", 
					15, 	//Font size
					MTColor.BLACK);
		} else {
			fontArial = this.objectmodel.getObjectfont();
		}
		
		
	}
	
	/** 
	 * Write data in Datamodel.
	 */
	private void dataWrite() {
		// Save Position
		this.objectmodel.setObjectposition(this.getCenterPointGlobal());
		// Save Color
		this.objectmodel.setObjectcolor(this.getFillColor());
		this.objectmodel.setObjectFillcolor(baserect.getFillColor());
		this.objectmodel.setObjectlinecolor(this.getStrokeColor());
		// Save Font (Not in use)
	
		// Save Directions
		
	}
	
	/** Position Dedection for MTObjects.*/
	// TODO Postition Dedection
	private void saveOnEvent() {
		
		this.addInputListener(new IMTInputEventListener() {
			
			public boolean processInputEvent(MTInputEvent inEvt) {
			
				if (inEvt instanceof AbstractCursorInputEvt) {
					AbstractCursorInputEvt cursorInputEvt = (AbstractCursorInputEvt) inEvt;
					
						switch (cursorInputEvt.getId()) {
						case AbstractCursorInputEvt.INPUT_STARTED:
							
							break;
						case AbstractCursorInputEvt.INPUT_UPDATED:
			
							break;
						case AbstractCursorInputEvt.INPUT_ENDED:
							System.out.println("Write Data after change the position");
							dataWrite();
							
								for (Attributes it : myAttributs) {
								it.dataWrite(); // daten der Attribute werden geschrieben.
								}
							
							
							break;
						default:
							break;
						}
					
					
				} else {
					// handle other input events stuff
				}
				return false;
			}
		}
		);
		
	}
	
	/**
	 * Setzen des Object Namens. String als Übergabe-Parameters.
	 * @param titelname String
	 */
	public final void setTitelName(final String titelname) {
	textField.setText(titelname);	
	}
	
	/**
	 * Zerstört das Objekt.
	 * 
	 */
	public final void destroyMyObject() {
	this.destroy();
	}
	
	/**
	 * Get Object ID back.
	 * @return id int
	 */
	public final int getID() {
	return id;
	}

	/** 
	 * Print the content from selected datamodel. 
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
			
			/*
			it j = 0;
			for (ModelAttributDefinition at : attribut.getAttributdefinition()) {
				System.out.println("              " + j + " Attr. Definiton: " + at.getDefinition());
				System.out.println("              " + j + " Attr. Value: " + at.getValue());
				j++;	
			}
			*/
			
		}
		System.out.println("\n");
	}
	
	/** Testmethode für den LinkController. 
	 * Set the Stroke Color for MyMTObject.
	 * 
	 * @param strokecolor MTColor
	 * */ 
	public void setTaggedColor(final MTColor strokecolor) {
		System.out.println("SET Tagged COLOR STROKE!!!!!!!");
		befortaggedColor = this.getStrokeColor();
		this.setStrokeColor(strokecolor);
		this.setStrokeWeight(3);
	}
	
	/** Testmethode für den LinkController. 
	 * Set the Stroke Color for MyMTObject.
	 * 
	 * */ 
	public final void setNormalColor() {
		System.out.println("SET Normal COLOR STROKE!!!!!!!");
		this.setStrokeColor(befortaggedColor);
		this.setStrokeWeight(1);
	}
	
	/** 
	 * Get the flag if the object tagged.
	 * 
	 * @return tagged boolean
	 * */
	public final boolean getTagFlag() {
		return tagged;
	}
	
	/** Testmethode für den LinkController. 
	 * 
	 * Set the Flag tag.
	 * @param tag boolean
	 * */
	public final void setTagFlag(final boolean tag) {
		tagged = tag;
	}
	
	/* (non-Javadoc)
	 * @see com.jMT.input.inputAnalyzers.clusterInputAnalyzer.IdragClusterable#isSelected()
	 */
	public boolean isSelected() {
		return selected;
	}

	/* (non-Javadoc)
	 * @see com.jMT.input.inputAnalyzers.clusterInputAnalyzer.IdragClusterable#setSelected(boolean)
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	
	/**
	 * Setzt die DefaultGesture Action für das Objekt. Dies durch die Änderung in den Attributen.
	 */
	@Override
	protected void setDefaultGestureActions() {
//		super.setDefaultGestureActions();
		
		this.addGestureListener(DragProcessor.class, new InertiaDragAction());
		
		DragProcessor dp = new DragProcessor(getRenderer());
//		dp.setLockPriority(0.5f);
		registerInputProcessor(dp);
		addGestureListener(DragProcessor.class, new DefaultDragAction());
		dp.setBubbledEventsEnabled(true); 
		
		RotateProcessor rp = new RotateProcessor(getRenderer());
//		rp.setLockPriority(0.8f);
		registerInputProcessor(rp);
		addGestureListener(RotateProcessor.class, new DefaultRotateAction());
		rp.setBubbledEventsEnabled(true);  
		
		ScaleProcessor sp = new ScaleProcessor(getRenderer());
//		sp.setLockPriority(0.8f);
		registerInputProcessor(sp);
		addGestureListener(ScaleProcessor.class, new DefaultScaleAction());
		sp.setBubbledEventsEnabled(true);  
	}
	 
	
}

