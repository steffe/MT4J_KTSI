package ch.mitoco.components.visibleComponents.objectlink;

import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.List;

import org.apache.batik.dom.events.EventListenerList;
import org.mt4j.AbstractMTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.interfaces.IMTComponent3D;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.gestureAction.DefaultLassoAction;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputData.AbstractCursorInputEvt;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.IInputProcessor;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.lassoProcessor.ILassoable;
import org.mt4j.input.inputProcessors.componentProcessors.lassoProcessor.LassoEvent;
import org.mt4j.input.inputProcessors.componentProcessors.lassoProcessor.LassoProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeEvent;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeUtils.UnistrokeGesture;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.camera.Icamera;
import org.mt4j.util.math.Ray;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;

import com.sun.org.apache.bcel.internal.generic.NEW;

import ch.mitoco.components.visibleComponents.MyMTObject;
import ch.mitoco.components.visibleComponents.widgets.Attributes;
import processing.core.PApplet;
import processing.core.PGraphics;

/** the MTLinkController Class control the links between the MyMTObjects and the eventHandling. */

public class MTLinkController {

	/** MTApplication. */
	private PApplet pApplet;
	
	/** AbstractMTApplication. */
	private AbstractMTApplication app;
	/** MTApplication canvas. */
	private MTCanvas canvas;
	
	/** List with all MyMTObject. */
	private List<MyMTObject> myobjectList;
		
	/** Link.*/
	private MTObjectLink link;
	
	/** Link Liste. */
	private List<MTObjectLink> linklist;
	
	/** List with MTSelectStoreObjects ID. */
	private List<MTSelectStoreObject> selectObjectID;
	
	/** List with valid pairs for link building. */
	private List<MTSelectStoreObject> validLinkPair;
	
	/** Lasso Processor.*/
	private LassoProcessor lassoProcessor;
	
	/** MTLinlController. 
	 * 
	 * @param pApplet 
	 * @param canvas
	 * @param 
	 * */
	public MTLinkController(AbstractMTApplication pApplet, MTCanvas canvas, List<MyMTObject> myobjectList) {
		this.pApplet = pApplet;
		this.app = pApplet;
		this.canvas = canvas;
		this.myobjectList = myobjectList;
		
		
		linklist = new ArrayList<MTObjectLink>();
		selectObjectID = new ArrayList<MTSelectStoreObject>();
		selectObjectID.add(new MTSelectStoreObject());
	
		validLinkPair = new ArrayList<MTSelectStoreObject>();
		lassoProcessor = new LassoProcessor(app, canvas, canvas.getViewingCamera());
		
		init();
	}

	/** Init Method.*/
	private void init() {
		System.out.println("MTLinkController init() ");
		link = new MTObjectLink(pApplet, new Vertex(new Vector3D(0, 0, 0)), new Vertex(new Vector3D(0, 0, 0)));
		canvas.addChild(link);
		
		eventObjectHandling();
		selectedLasso();
		canvas.registerInputProcessor(lassoProcessor);
		canvas.addGestureListener(LassoProcessor.class, new DefaultLassoAction(app, canvas.getClusterManager(), canvas));
		
	}
	
	/** Position Dedection for MTObjects.*/
	// TODO Postition Dedection
	private void eventObjectHandling() {
		if (!(myobjectList == null)) {
			canvas.addInputListener(new IMTInputEventListener() {
				
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
							link.destroy();
						}
						
					} else {
						// handle other input events stuff
					}
					return false;
				}
			}
			);
		} else {
			System.out.println("MyMTObjekt Liste ist LEER" );
			
		}
		
		
	}
	/** Test Methode to draw a linie.
	 *  @param input1 Vector3D 
	 *  @param input2 Vector3D
	 * */
	private void drawLinie(Vector3D input1, Vector3D input2) {
		link.setVertices(new Vertex[]{new Vertex(input1), new Vertex(input2)});
		canvas.addChild(link);
		
	}
	
	/**
	 *  Set the StrokeColor from the selected Object.
	 *  
	 *  @param obj MyMTObject
	 *   
	 * */	
	private void setSelectedObjectColor( final MyMTObject obj) {
		if (obj.getTagFlag() == true) {
			obj.setNormalColor();
			obj.setTagFlag(false);
			removeSelectObject(obj);
		} else {
			obj.setTaggedColor(MTColor.RED);
			obj.setTagFlag(true);
			storeSelectObject(obj);
		}		
	}
	
	/**
	 * 
	 * Add Gesture Listener (TapAndHoldProcessor) for all MyMTObjects.
	 * 
	 */
	//TODO Insert this in Model Controller when object Createt
	@Deprecated
	public final void setTapAndHoldListener() {
		for (MyMTObject it : myobjectList) {
			detectionObSelection(it);
			detectionObSelectionLasso(it);			
		}
	}
	
	/**
	 * 
	 * Add Gesture Listener (TapAndHoldProcessor) for one selected Object.
	 * 
	 * @param obj MyMTObject
	 */
	public final void setTapAndHoldListener(final MyMTObject obj) {
		detectionObSelection(obj);
		detectionObSelectionLasso(obj);
		linklist.add(obj.getID(), new MTObjectLink(pApplet, new Vertex(obj.getCenterPointLocal()), new Vertex(new Vector3D(obj.getWidthXY(TransformSpace.LOCAL) /2, -30))));
		obj.addChild(linklist.get(obj.getID()));
		
	}
	
	/** Object detection/selection with TapAndHold Processor.
	 * 
	 * Each param MyMTObject become a TapAndHoldProcessor and run by Gesture_Ended the methode setSelectedObjectColor.
	 * 
	 * @param obj MyMTObject 
	 */
	private void detectionObSelection(final MyMTObject obj) {
		obj.registerInputProcessor(new TapAndHoldProcessor(app, 1000));
		obj.addGestureListener(TapAndHoldProcessor.class, new TapAndHoldVisualizer(app, canvas));
		obj.addGestureListener(TapAndHoldProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapAndHoldEvent th = (TapAndHoldEvent) ge;
				switch (th.getId()) {
				case TapAndHoldEvent.GESTURE_STARTED:
					break;
				case TapAndHoldEvent.GESTURE_UPDATED:
					break;
				case TapAndHoldEvent.GESTURE_ENDED:
					if (th.isHoldComplete()) {
						setSelectedObjectColor(obj);
						
					}
					break;
				default:
					break;
				}
				return false;
			}
		});
	}
	
	/** Object detection/selection with LassoProcessor
	 * 
	 * @param obj
	 */
	private void detectionObSelectionLasso(final MyMTObject obj) {
		
		lassoProcessor.addLassoable(obj);

	}
	
	
	
	int i = 0;
	int j = 0; // Objecte 
	/** Save the selected MyMtObject.
	 * 
	 * @param obj MyMTObject
	 * */
	private void storeSelectObject(final MyMTObject obj) {
		
		
		
		if(i%2==0){
			selectObjectID.get(j).setEndObjectID(obj.getID());
			System.out.println("Object erstellt" +j +":"+i );
			i++;
		} else {
		
			selectObjectID.get(j).setStartObjectID(obj.getID());
			selectObjectID.get(j).setValid(true);
			System.out.println("Object erstellt" +j +":"+i );
			i++;
			
		}
		if(i%2==0){
			System.out.println("Dupplete Vorhanden" +j );
			j++;
			selectObjectID.add(new MTSelectStoreObject());
			System.out.println("Wurde erstellt: " +j );
			
		}
		
		
	}
	
	/** Save the selected MyMtObject.
	 * 
	 * @param obj MyMTObject
	 * */
	private void removeSelectObject(final MyMTObject obj) {
		if(i%2==0){
			System.out.println("Dupplete Vorhanden" +j );
			System.out.println("Wurde gelöscht: " +j );
			selectObjectID.remove(j);
			j--;

		}
		
		if(i%2==0){
			i--;
			//selectObjectID.get(0).setEndObjectID(obj.getID());
			System.out.println("Gelöscht Zahl ist gerade: " +j + ":"+i );
			
		} else {
			i--;
			//selectObjectID.get(0).setStartObjectID(obj.getID());
			System.out.println("Gelöscht Zahl ist ungerade: " +j + ":"+i );
		}
	
		for(MTSelectStoreObject it: selectObjectID) {
			if (it.getEndObjectID() == obj.getID()){
				it.setValid(false);
				it.setEndObjectID(-1);
			}
			if (it.getStartObjectID() == obj.getID()){
				it.setValid(false);
				it.setStartObjectID(-1);
			}
		}

		
		
	}
	/** 
	 * Print selected Object in the list.
	 * */
	public void printSelectedList() {
		
		for (MTSelectStoreObject it : selectObjectID) {
			System.out.println("Opject Paar gepeichert " + it.getEndObjectID() + ":" + it.getStartObjectID() + " PAAR ist Valid; " + it.isValid());
		}

	}
	
	/**
	 * 
	 * Store only Valid Link pair in validLinkPair.
	 * 
	 * */
	public final void storeValidPair() {
		for (MTSelectStoreObject it : selectObjectID) {
			if (it.isValid() ){
				validLinkPair.add(new MTSelectStoreObject(it.getStartObjectID(), it.getEndObjectID(), it.isValid()));		
			}
		}
		
		for (MTSelectStoreObject itt : validLinkPair) {
			System.out.println("VALID Opject Paar gepeichert " + itt.getEndObjectID() + ":" + itt.getStartObjectID() + " PAAR ist Valid; " + itt.isValid());
		}
		
	}
	
	
	/**
	 * 
	 */
	public final void selectedLasso() {
		
		
		lassoProcessor.addGestureListener(new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				// TODO Auto-generated method stub

				LassoEvent th = (LassoEvent) ge;
				switch (th.getId()) {
				case LassoEvent.GESTURE_STARTED:
					break;
				case LassoEvent.GESTURE_UPDATED:
					break;
				case LassoEvent.GESTURE_ENDED:
					System.out.println("Gesture wurde ausgeführt");
					
					
						if (!(myobjectList == null)) {
							for (MyMTObject it : myobjectList) {
								if (it.isSelected() == true) {
									System.out.println("@@@@@ Object ist gespeichert NR:" + it.getID());
									
								}
							}
						}
						
					
					break;
				default:
					break;
				}
			return false;
			}
		});		
	}
	
	
	
	
	
}
