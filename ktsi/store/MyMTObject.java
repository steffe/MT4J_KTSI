package store;

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

import processing.core.PImage;
import ch.mitoco.components.visibleComponents.widgets.MTDropDownList;
import ch.mitoco.components.visibleComponents.widgets.MTNumField;
import ch.mitoco.store.generated.Customer;

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
	
	/** */
	private MTImageButton buttonMaxMin;
	
	/** */
	private MTImageButton buttonRotate;

	/** */
	private int minmaxModus;
	
	/** */
	private MTColor greypez = new MTColor(12, 12, 12, 34);

	/** */
	private int id;
	
	/** */
	private IFont fontArialMini;
	
	/** The default drag action. */
	private DefaultDragAction defaultDragAction;
	
	/** Test Attribut. */
	private MTNumField p1;

	/** Test Attribut. */
	private MTDropDownList d1;
	
	/** Public BLBL MyObject.
	 * 
	 * @param pApplet2 MTApplication
	 * @param objectID INT ObjectID  
	 */
	public MyMTObject(final MTApplication pApplet2, final int objectID) {
		super(pApplet2, 0, 0, 0, 0, 0, 5, 5);
	
		pApplet = pApplet2;
		this.id = objectID;
		defaultDragAction 	= new DefaultDragAction();
		
		/*
		try {
			JAXBContext jc = JAXBContext.newInstance( "ch.mitoco.store.generated" );
			Unmarshaller u = jc.createUnmarshaller();
			for( int i=0; i<1; i++ ){
				PurchaseOrder at = (PurchaseOrder)u.unmarshal(new File("AttributTemplates1.xml"));
			           	        System.out.println( "Attribut: " );
		        
		        //display the shipping address
			    //int Attributs = at.getID();
			     //      	     Attributs Attributs = at.getName();
			    //System.out.println(Attributs);
		        
			    customer = at.getCustomer();
			    
	            display( customer );
	            
			    System.out.println(at);
			    //display( Attributs );
		        
		        //display the items
		        //List <LineItem> items = po.getLineItem();
		        //displayItems( items );
		        //displayTotal( items );
			} 
		}
			catch (JAXBException je) {
		        je.printStackTrace();
		    }
		*/
		
		// TODO Auto-generated constructor stub
		MTColor speblue = new MTColor(51, 102, 255);
		obSizeMinWidth = 300; // Grösse Min
		obSizeMinHeight = 300; // Grösse Min
		
		obSizeMaxWidth = 300;
		obSizeMaxHeight = 400;
		
		IFont fontArial = FontManager.getInstance().createFont(pApplet, "arial.ttf", 
				15, 	//Font size
				MTColor.BLACK);
		
		this.setSizeLocal(obSizeMinWidth, obSizeMinHeight);
		this.setPositionGlobal(new Vector3D(600, 300));
		this.setFillColor(greypez);
		this.setStrokeWeight(1);
		this.setStrokeColor(speblue);
	
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
					buttonMaxMin.translate(new Vector3D(obSizeMaxWidth - w, 0));
					buttonRotate.translate(new Vector3D(0, obSizeMaxHeight - h));
					
					d1.setMax();
					p1.setMax();
					minmaxModus = 1;
				} else {
					setSizeLocal(w, h);
					baserect.setSizeLocal(w - 20, h - 20);
					buttonMaxMin.translate(new Vector3D(w - obSizeMaxWidth, 0));
					buttonRotate.translate(new Vector3D(0, h - obSizeMaxHeight));
					
					d1.setMin();
					p1.setMin();
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
		IFont labelfont = FontManager.getInstance().createFont(pApplet, "arial.ttf", 
				14, 	//Font size
				MTColor.WHITE);
		//Integer test = Integer.valueOf(customer.getName()).intValue();
		//readcustomer.getName();
		// Testattribut 
		p1 = new MTNumField(pApplet2, fontArialMini, 250, 30, true, 1234, "TestFeld", labelfont);
		p1.setPositionRelativeToParent(new Vector3D(this.getWidthXY(TransformSpace.LOCAL) / 2, 60));
		p1.setPickable(false);

			
		d1 = new MTDropDownList(pApplet2, fontArialMini, 250, 30, "Projekt Wichtigkeit", labelfont);
		d1.setPositionRelativeToParent(new Vector3D(this.getWidthXY(TransformSpace.LOCAL) / 2, 100));
		d1.setPickable(false);
		

		// Add MTComponets to Canvas
		// --------------------------------
		p1.setUserData("FieldValue", p1.getValue());
		this.addChild(baserect);
		this.addChild(buttonRotate);
		this.addChild(buttonMaxMin);
		
		baserect.addChild(d1);
		baserect.addChild(p1);
		baserect.addChild(textField); //Titeltext
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
	roundRect.destroy();
	}
	
	/**
	 * Get Object ID back.
	 * @return id int
	 */
	public final int getID() {
	return id;
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
	
	public static void display( final Customer atr ) {
	    System.out.println( "\t" + atr.getName() );
	    System.out.println( "\t" + atr.getAddress() + "\n"); 
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
