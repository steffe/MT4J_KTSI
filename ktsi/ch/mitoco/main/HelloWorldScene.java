package ch.mitoco.main;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTLine;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.gestureAction.DefaultPanAction;
import org.mt4j.input.gestureAction.DefaultZoomAction;
import org.mt4j.input.inputData.AbstractCursorInputEvt;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.panProcessor.PanProcessorTwoFingers;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.zoomProcessor.ZoomProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;

import com.thoughtworks.xstream.XStream;

import ch.mitoco.components.visibleComponents.MyMTObject;
import ch.mitoco.model.ModelMtObjects;
import ch.mitoco.model.ModelScence;
import ch.mitoco.store.generated.Customer;

import processing.core.PImage;
import store.DataController;
import store.LoadXML;
import store.SaveXML;

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
	private List<MyMTObject> myobjectList; //TODO: Löschen
	
	/** Object Counter and Object ID. */
	private int counter;
	
	/** **/
	private ModelMtObjects datamodel; //TODO: Löschen
	
	/** Line between Objects.*/
	private MTLine l1;

	/** Data Model for scene. **/ //TODO: ModelScence -> ModelScene
	public ModelScence dataModel;
	
	/** Data Controller Object. */
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
		
		//Test for Linies betweens the objects
		l1 = new MTLine(mtApplication, 0, 0, 0, 0);
		l1.setStrokeColor(MTColor.BLUE);
		l1.setStrokeWeight(5);
		l1.setPickable(false);
		
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
					dataController.createObject();
					getCanvas().addChild(dataController.getMyobjectList().get(counter));	
					
					//getCanvas().addChild(myobjectList.get(counter).getMyObjectBack());	
					counter++;
					
					if (myobjectList.size() > 0) {
						eventObjectHandling();
					}
					break;
				
				default:
					break;
				}
				
				
				
				
			return false;
			}
		});
	
		//Del Object
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
				/*	AUCH NICHT ANGEPASST UND COUNTER AM FALSCHEN ORT
					if (counter != 0) {
					
						getCanvas().removeChild(myobjectList.get(counter - 1)); // Entfernen des Objekts auf dem Canvas
						
						
					//getCanvas().removeChild(myobjectList.get(counter-1).getMyObjectBack()); // Entfernen des Objekts auf dem Canvas
					myobjectList.remove(counter - 1); // Remove Object from arryeList
					
					System.out.println("Zähler rem vor: " + counter);
					counter--;
					} else {
					System.out.println("Kein Objeckt zum enfernen vorhanden " + counter);	
					}
				*/	
					
					break;
				
				default:
					break;
				}
			return false;
			}
		});
	
		
		// Save Button
		PImage buttonSAVImage = mtApplication.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  "saveXML.png");
		MTImageButton buttonSav = new MTImageButton(mtApplication, buttonSAVImage);
		buttonSav.setSizeLocal(40, 40);
		buttonSav.setFillColor(new MTColor(255, 255, 255, 200));
		buttonSav.setName("SaveButton");
		buttonSav.setNoStroke(true);
		buttonSav.setPositionGlobal(new Vector3D(mtAppl.getWidth() - 20, mtAppl.getHeight() - 20));
		
		buttonSav.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				// TODO Auto-generated method stub
				TapEvent te = (TapEvent) ge;
				switch(te.getTapID()) {
				case TapEvent.TAPPED:
					dataController.saveSceneXML();
					break;
				default:
					break;
				}
			return false;
			}
		});
		
		
		// Load XML
		PImage buttonLOADImage = mtApplication.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  "loadXML.png");
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
		
		// Standard Default ZoomAction and Pan TwoFingers
		this.getCanvas().registerInputProcessor(new ZoomProcessor(mtApplication));
		this.getCanvas().addGestureListener(ZoomProcessor.class, new DefaultZoomAction());
		this.getCanvas().registerInputProcessor(new PanProcessorTwoFingers(mtApplication));
		this.getCanvas().addGestureListener(PanProcessorTwoFingers.class, new DefaultPanAction());
		
		

	}
	
	/** Create object from the xml data.*/
	private void createObject() {
		// From XML Direct
		// From new Button
		
		
	}
		
	/** Position Dedection for MTObjects.*/
	// TODO Postition Dedection
	private void eventObjectHandling() {
		
		this.getCanvas().addInputListener(new IMTInputEventListener() {
			
			public boolean processInputEvent(MTInputEvent inEvt) {
			
				if (inEvt instanceof AbstractCursorInputEvt) {
					AbstractCursorInputEvt cursorInputEvt = (AbstractCursorInputEvt) inEvt;
					if (myobjectList.size() == 3) {
						switch (cursorInputEvt.getId()) {
						case AbstractCursorInputEvt.INPUT_STARTED:
							drawLinie(myobjectList.get(0).getCenterPointGlobal(), myobjectList.get(1).getCenterPointGlobal());
							break;
						case AbstractCursorInputEvt.INPUT_UPDATED:
							drawLinie(myobjectList.get(0).getCenterPointGlobal(), myobjectList.get(1).getCenterPointGlobal());
							break;
						case AbstractCursorInputEvt.INPUT_ENDED:
							drawLinie(myobjectList.get(0).getCenterPointGlobal(), myobjectList.get(1).getCenterPointGlobal());
							break;
						default:
							break;
						}
					} else {
						l1.destroy();
					}
					
				} else {
					// handle other input events stuff
				}
				return false;
			}
		}
		);
		
	}
	/** Test Methode to draw a linie.
	 *  @param input1 Vector3D 
	 *  @param input2 Vector3D
	 * */
	private void drawLinie(Vector3D input1, Vector3D input2) {
		System.out.println("Objekt Position Started " + input1);
		
		l1.setVertices(new Vertex[]{new Vertex(input1), new Vertex(input2)});
		getCanvas().addChild(l1);

	}
	
	
	@Override
	public void init() {
		
		
	}
	@Override
	public void shutDown() {
		
		
	}
}
