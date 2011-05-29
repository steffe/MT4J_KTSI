package store;



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
import ch.mitoco.model.ModelScence;
import ch.mitoco.store.generated.Customer;


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
	public ModelScence dataModel;
	
	private DataController dataController;
	

	/** XML Customer. */
	public Customer readcustomer;
	public static ModelScence MODELSCENCE;
	public ModelScence test1;
	public LoadXML LoadXML;
	
	
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
		
		
		dataController = new DataController(mtAppl);
		dataController.createDataModel("Scenename");
		dataController.createObjectList();
		
		
		//Show touches
		this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));
		
		counter = 0;
		myobjectList = new ArrayList<MyMTObject>();
			
		
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
					/*
					dataController.saveObjectXML();
					dataController.loadObjectXML();
					dataController.getObjectetyps();
					
					for (Iterator<ModelMtObjects> it = dataController.getObjectetyps().getObjecttyp().iterator(); it.hasNext();) {
						System.out.println(it.next().getObjecttyp());
					}
					*/
					dataController.createObject();
					
					getCanvas().addChild(dataController.getMyobjectList().get(counter));	
					
					//getCanvas().addChild(myobjectList.get(counter).getMyObjectBack());	
					counter++;
					
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
						myobjectList.remove(counter - 1); // Remove Object from arryeList
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
					dataController.saveSceneXML();
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
					if (!dataController.loadSceneXML()) {
						for (Iterator<MyMTObject> it = dataController.getMyobjectList().iterator(); it.hasNext();) {
							getCanvas().removeChild(dataController.getMyobjectList().get(it.next().getID()));	
						}
						dataController.clearScene();
					}
					else {
						for (Iterator<MyMTObject> it = dataController.getMyobjectList().iterator(); it.hasNext();) {
							getCanvas().addChild(dataController.getMyobjectList().get(it.next().getID()));	
						}
						counter = dataController.getObjectcounter();
					}
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
}
