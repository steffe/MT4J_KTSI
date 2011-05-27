package store;



import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;
import ch.mitoco.model.ModelMtObjects;
import ch.mitoco.model.ModelScence;
import ch.mitoco.store.generated.Customer;

import com.thoughtworks.xstream.XStream;


/** Hello Word Scene. */

public class HelloWorldScene extends AbstractScene {


	
	/**
	 * List mit MyObjects
	 * 
	 * Erstellen:
	 * private List<MyMTObjects> myobjectList=new ArrayList<MyMTObject>();
	 * 
	 * Objekt Hinzufügen
	 * myobjectList.add( new MyMTObject(mtApplication) );
	 */
	
	/** MTApplication. */
	private MTApplication mtApplication;
		
	/** List with all MyMTObject. */
	private List<MyMTObject> myobjectList;
	
	/** Object Counter and Object ID. */
	private int counter;
	
	/** **/
	private ModelMtObjects datamodel;
	
	/** */
	private List<Integer> tempAttributs;
	
	/** XML Customer. */
	public Customer readcustomer;
	public static ModelScence MODELSCENCE;
	public ModelScence test1;
	public loadXML LoadXML;
	
	
	/** 
	 * Hello Word Scene. 
	 * 
	 * @param mtAppl MTApplication 
	 * @param name String
	 */
	public HelloWorldScene(final MTApplication mtAppl, final String name) {
		super(mtAppl, name);
		
		this.mtApplication = mtAppl;
		this.setClearColor(new MTColor(100, 100, 100, 255));
		
		//Show touches
		this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));
		
		counter = 0;
		myobjectList = new ArrayList<MyMTObject>();
		
		// Temp DataModel
		tempAttributs = new ArrayList<Integer>();
		tempAttributs.add(1);
		tempAttributs.add(0);
		tempAttributs.add(2);		
		
		//FileStream für xStream
		//ModelScence test1 = new ModelScence();
		
		
		
		//New Object
		PImage buttonNewImage = mtApplication.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  "buttonNew.png");
		MTImageButton buttonNew = new MTImageButton(mtApplication, buttonNewImage);

		buttonNew.setSizeLocal(40, 40);
		buttonNew.setFillColor(new MTColor(255, 255, 255, 200));
		buttonNew.setName("KeyboardImage");
		buttonNew.setNoStroke(true);
		buttonNew.setPositionGlobal(new Vector3D(20, 20));
		
		buttonNew.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				switch(te.getTapID()) {
				case TapEvent.TAPPED:
					//MyMTObject t1 = new MyMTObject(mtApplication,1);
					//getCanvas().addChild(t1.getMyObjectBack());
					if(!LoadXML.DataModel.getMtobjects().isEmpty()){
						myobjectList.add(new MyMTObject(mtApplication, LoadXML.DataModel.getMtobjects().get(counter), counter));				
					}
					else
					{
						myobjectList.add(new MyMTObject(mtApplication, LoadXML.DataModel.getMtobjects().get(0), counter));
					}
					getCanvas().addChild(myobjectList.get(counter));	
					
					//getCanvas().addChild(myobjectList.get(counter).getMyObjectBack());	
					System.out.println("Zähler vor Add: " + counter);
					counter++;
					System.out.println("Zähler nach Add: " + counter);
					break;
				
				default:
					break;
				}
				
				
				
				
			return false;
			}
		});
	
		//New Object
		PImage buttonDELImage = mtApplication.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  "buttonDel.png");
		MTImageButton buttonDel = new MTImageButton(mtApplication, buttonDELImage);
		buttonDel.setSizeLocal(40, 40);
		buttonDel.setFillColor(new MTColor(255, 255, 255, 200));
		buttonDel.setName("KeyboardImage");
		buttonDel.setNoStroke(true);
		buttonDel.setPositionGlobal(new Vector3D(mtAppl.getWidth() - 20, 20));
		
		buttonDel.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(final MTGestureEvent ge) {
				// TODO Auto-generated method stub
				TapEvent te = (TapEvent) ge;
				switch(te.getTapID()) {
				case TapEvent.TAPPED:
					if (counter != 0) {
					
						getCanvas().removeChild(myobjectList.get(counter - 1)); // Entfernen des Objekts auf dem Canvas
						
						
					//getCanvas().removeChild(myobjectList.get(counter-1).getMyObjectBack()); // Entfernen des Objekts auf dem Canvas
					myobjectList.remove(counter - 1); // Remove Object from arryeList
					
					System.out.println("Zähler rem vor: " + counter);
					counter--;
					} else {
					System.out.println("Kein Objeckt zum enfernen vorhanden " + counter);	
					}
					break;
				
				default:
					break;
				}
			return false;
			}
		});
		
		PImage buttonSAVImage = mtApplication.loadImage("test" + MTApplication.separator + "thomas"+ MTApplication.separator + "image" + MTApplication.separator +  "buttonSave.png");
		MTImageButton buttonSav= new MTImageButton(mtApplication, buttonSAVImage);
		buttonSav.setSizeLocal(40, 40);
		buttonSav.setFillColor(new MTColor(255, 255, 255, 200));
		buttonSav.setName("SaveButton");
		buttonSav.setNoStroke(true);
		buttonSav.setPositionGlobal(new Vector3D(mtAppl.getWidth() - 20, mtAppl.getHeight() - 20));
		
		buttonSav.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				// TODO Auto-generated method stub
				TapEvent te = (TapEvent)ge;
				switch(te.getTapID()){
				case TapEvent.TAPPED:
					
					XStream xstreamSave = new XStream();
					
					//ExampleXML  tralara = new ExampleXML();
					//tralara.getObject();
					System.out.println(myobjectList.get(0).model.getObjectattributs().get(0).getAttributcontent().get(0).getValue());
					//MODELSCENCE = new ModelScence();
					//MODELSCENCE.setId(2);
					//MODELSCENCE.getMtobjects().add(myobjectList.get(0).model);
					
						
					
					try {
						xstreamSave.toXML(MODELSCENCE,new FileOutputStream("xstream3.xml"));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					break;
				
				default:
					break;
				}
			return false;
			}
		});
		
		
		/**
		 * Load Button Befehle
		 * 
		 */
		PImage buttonLOADImage = mtApplication.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  "buttonEmpty.png");
		MTImageButton buttonLoad = new MTImageButton(mtApplication, buttonLOADImage);
		buttonLoad.setSizeLocal(40, 40);
		buttonLoad.setFillColor(new MTColor(255, 255, 255, 200));
		buttonLoad.setName("LoadButton");
		buttonLoad.setNoStroke(true);
		buttonLoad.setPositionGlobal(new Vector3D(20, mtAppl.getHeight() - 20));
		
		buttonLoad.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(final MTGestureEvent ge) {
				
				// TODO Auto-generated method stub
				TapEvent te = (TapEvent) ge;
				switch(te.getTapID()) {
				case TapEvent.TAPPED:
					LoadXML = new loadXML(mtAppl, "xstream.xml");
					for (Iterator<MyMTObject> it = LoadXML.xmlmyobjectlist.iterator(); it.hasNext();) {
					getCanvas().addChild(LoadXML.xmlmyobjectlist.get(it.next().getID()));	
					}
					//getCanvas().addChild(LoadXML.xmlmyobjectlist.get(1));
					/*XStream xstream = new XStream();
					try {
						test1 = (ModelScence) xstream.fromXML(new FileInputStream("xstream.xml"));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					*/
					//MODELSCENCE.getMtobjects().get(1).getObjectattributs().get(1).getId();
					//xstream.alias("ch.mitoco.model.ModelScence", test1.getClass());
					//System.out.println(test1.getMtobjects().get(0).getObjectattributs().get(0).getLable());
										
					break;
				
				default:
					break;
				}
			return false;
			}
		});
		
		
		this.getCanvas().addChild(buttonDel);
		this.getCanvas().addChild(buttonNew);
		this.getCanvas().addChild(buttonSav);
		this.getCanvas().addChild(buttonLoad);
		
	}
	@Override
	public void init() {
		
		
	}
	@Override
	public void shutDown() {
		
		
	}
	/**Soll mit einem .
	 * display Methode
	 * @param atr Customer
	 */
	public static void display(final Customer atr) {
	    System.out.println("\t" + atr.getName());
	    System.out.println("\t" + atr.getAddress() + "\n"); 
		}
}
