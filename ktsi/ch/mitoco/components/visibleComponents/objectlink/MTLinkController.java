package ch.mitoco.components.visibleComponents.objectlink;

import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.Collections;
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
	private List<MTObjectLink> linkHeadlist;
	
	/** List with MTSelectStoreObjects ID. */
	private List<MTSelectStoreObject> selectedObjectID;
	
	/** List with valid pairs for link building. */
	private List<MTSelectStoreObject> validLinkPair;
	
	/** Lasso Processor.*/
	private LassoProcessor lassoProcessor;
	
	/** Liste mit Links.*/
	private List<MTLink> linkliste;
	
	/** MTLinkController. 
	 * 
	 * @param pApplet 
	 * @param canvas
	 * @param 
	 * */
	public MTLinkController(AbstractMTApplication pApplet, MTCanvas canvas) {
		this.pApplet = pApplet;
		this.app = pApplet;
		this.canvas = canvas;
		
		System.out.println("MTLinkController: Konstruktor: ObjektListe " + this.myobjectList);
		linkHeadlist = new ArrayList<MTObjectLink>();
		
		selectedObjectID = new ArrayList<MTSelectStoreObject>(); // Alle Markierten Object IDs werden als Paar gespeichert
		selectedObjectID.add(new MTSelectStoreObject()); // Erste Object f�r Dupleten Speicher. Die n�chsten Objekte werden in der Methode storeSelectObject erstellt
		
		validLinkPair = new ArrayList<MTSelectStoreObject>();
		
		linkliste = Collections.synchronizedList(new ArrayList<MTLink>()); // Liste f�r alle Objekte LINK, Thread Safe TODO: Nutzen muss noch abgekl�rt werden
	
		init();
	}

	/** Init Method.*/
	private void init() {
		System.out.println("MTLinkController: init() ");
		//link = new MTObjectLink(pApplet, new Vertex(new Vector3D(0, 0, 0)), new Vertex(new Vector3D(0, 0, 0)),);
		//canvas.addChild(link);
		lassoProcessor = new LassoProcessor(app, canvas, canvas.getViewingCamera());
		canvas.registerInputProcessor(lassoProcessor);
		canvas.addGestureListener(LassoProcessor.class, new DefaultLassoAction(app, canvas.getClusterManager(), canvas));
		
		selectedLasso();
	}

	/**
	 * Objectliste wird gesetzt. 
	 * @param myobjectList
	 */
	public synchronized void setObjectList(List<MyMTObject> myobjectList){
		this.myobjectList = null;
		this.myobjectList = myobjectList;
		System.out.println("MTLinkController: setObjectList: ObjektListe " + this.myobjectList + " IM �bergabeParameter " + myobjectList);
		
	}
	
	/** Position Dedection for MTObjects.*/
	// TODO Postition Dedection
	private void eventObjectHandling() {
		
		for (MyMTObject it : myobjectList){
			it.addInputListener(new IMTInputEventListener() {
				
				public boolean processInputEvent(MTInputEvent inEvt) {
				
					if (inEvt instanceof AbstractCursorInputEvt) {
						AbstractCursorInputEvt cursorInputEvt = (AbstractCursorInputEvt) inEvt;
						if (!(linkliste.isEmpty())) {
							switch (cursorInputEvt.getId()) {
							case AbstractCursorInputEvt.INPUT_STARTED:
								drawLinie();	
								break;
							case AbstractCursorInputEvt.INPUT_UPDATED:
								drawLinie();
								break;
							case AbstractCursorInputEvt.INPUT_ENDED:
								drawLinie();
								break;
							default:
								break;
							}
						} else {
							//System.out.println("MTLinkController: eventObjectHandling: Keine Link Objekte vorhanden");	
						}
						
					} else {
						// handle other input events stuff
					}
					return false;
				}
			}
			);
		}
	}
	
	/** 
	 * Test Methode to draw a linie.
	 * 
	 * */
	private void drawLinie() {
		
		for (MTLink it: linkliste) {
			it.setVertices(new Vertex[]{
					new Vertex(myobjectList.get(it.getStartObjectID()).getCenterPointGlobal()),
					new Vertex(myobjectList.get(it.getEndObjectID()).getCenterPointGlobal())});
		}
		
		//link.setVertices(new Vertex[]{new Vertex(input1), new Vertex(input2)});
		//canvas.addChild(link);
		
	}
	
	
	
	/**
	 * 
	 * Add Gesture Listener (TapAndHoldProcessor) for all MyMTObjects.
	 * 
	 */
	//TODO Insert this in Model Controller when object Createt
	@Deprecated
	public final void setTapAndHoldListener() {
		System.out.println("MTLinkController: setTapAndHoldListener: f�r ListenFeld");
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
		System.out.println("MTLinkController: setTapAndHoldListener: f�r Objekt: " + obj);
		
		detectionObSelection(obj);
		detectionObSelectionLasso(obj);	
		linkHeadlist.add(obj.getID(), new MTObjectLink(pApplet, new Vertex( new Vector3D(10, 10)), new Vertex(new Vector3D(-40, 10)), obj.getID()));
		obj.addChild(linkHeadlist.get(obj.getID()));
		
	}
	
	/** Object detection/selection with TapAndHold Processor.
	 * 
	 * Each param MyMTObject become a TapAndHoldProcessor and run by Gesture_Ended the methode setSelectedObjectColor.
	 * 
	 * @param obj MyMTObject 
	 */
	private void detectionObSelection(final MyMTObject obj) {
		obj.registerInputProcessor(new TapAndHoldProcessor(app, 2000));
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
	 * Jedem Object wird dem LassoProcessor hinzugef�gt, damit es auch lassoable wird.
	 * 
	 * @param obj MyMTObject
	 */
	private void detectionObSelectionLasso(final MyMTObject obj) {
		
		lassoProcessor.addLassoable(obj);

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
	 *  Set the StrokeColor from the selected Object with Interger ID.
	 *  
	 *  @param objID int
	 *  @param setting boolean 
	 *  				1 = markieren
	 *  				0 = nicht markiert
	 *   
	 *   
	 * */	
	public void setSelectedObjectColor(final int objID, boolean setting) {
		
		System.out.println("MTLinkController: setSelectedObjectColor: Object ID " + objID );
		System.out.println("MTLinkController: setSelectedObjectColor: Object ID " + myobjectList.get(objID));
		
		
		if (myobjectList.get(objID).getTagFlag() == true && !setting) {
			myobjectList.get(objID).setNormalColor();
			myobjectList.get(objID).setTagFlag(false);
			//removeSelectObject(myobjectList.get(objID));
		} else if (setting) {
			myobjectList.get(objID).setTaggedColor(MTColor.RED);
			myobjectList.get(objID).setTagFlag(true);
			//storeSelectObject(myobjectList.get(objID));
		}	
		
	}

	int i = 0;
	int j = 0; // Objecte 
	/** Save the selected MyMtObject.
	 * 
	 * @param obj MyMTObject
	 * */
	private void storeSelectObject(final MyMTObject obj) {
		
		if(i%2==0){
			selectedObjectID.get(j).setEndObjectID(obj.getID());
			System.out.println("Object erstellt" +j +":"+i );
			i++;
		} else {
		
			selectedObjectID.get(j).setStartObjectID(obj.getID());
			selectedObjectID.get(j).setValid(true);
			System.out.println("Object erstellt" +j +":"+i );
			i++;
			
		}
		if(i%2==0){
			System.out.println("Dupplete Vorhanden" +j );
			j++;
			selectedObjectID.add(new MTSelectStoreObject());
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
			System.out.println("Wurde gel�scht: " +j );
			selectedObjectID.remove(j);
			j--;

		}
		
		if(i%2==0){
			i--;
			//selectObjectID.get(0).setEndObjectID(obj.getID());
			System.out.println("Gel�scht Zahl ist gerade: " +j + ":"+i );
			
		} else {
			i--;
			//selectObjectID.get(0).setStartObjectID(obj.getID());
			System.out.println("Gel�scht Zahl ist ungerade: " +j + ":"+i );
		}
	
		for(MTSelectStoreObject it: selectedObjectID) {
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
		
		for (MTSelectStoreObject it : selectedObjectID) {
			System.out.println("Opject Paar gepeichert " + it.getEndObjectID() + ":" + it.getStartObjectID() + " PAAR ist Valid; " + it.isValid());
		}

	}
	
	/**
	 * 
	 * Store only Valid Link pair in validLinkPair.
	 * 
	 * */
	public final void storeValidPair() {
		for (MTSelectStoreObject it : selectedObjectID) {
			if (it.isValid() ){
				validLinkPair.add(new MTSelectStoreObject(it.getStartObjectID(), it.getEndObjectID(), it.isValid()));		
			}
		}
		
		for (MTSelectStoreObject itt : validLinkPair) {
			System.out.println("VALID Opject Paar gepeichert " + itt.getEndObjectID() + ":" + itt.getStartObjectID() + " PAAR ist Valid; " + itt.isValid());
		}
		
	}
	
	public void showTaggedObject() {
		for (MyMTObject itt : myobjectList) {
			System.out.println("Objekt: " + itt.getID() +" Markiert " + itt.getTagFlag());
		}
		
	}
	
	/**
	 * Erstellt Link zwischen zwei Objekte. Soll ein Link erstellt werden der schon vorhanden ist wird dieser gel�scht. Zwischen zwei gleiche Objekte (ID Paar) kann nur ein Link bestehen.
	 * 
	 * @param node1 int
	 * @param node2 int
	 */
	public void createLink(int node1, int node2) {
		
		System.out.println("MTLinkController: createLink: Link for Objekt ID; " + node1 + " und " + node2);
		System.out.println("MTLinkController: createLink: Link for Objekt ID; " + myobjectList.get(node1).getCenterPointGlobal() + " und " + myobjectList.get(node2).getCenterPointGlobal());
		/**
		 * 1. Link erstellen 
		 *    - a. Abfrage: Link schon in der Liste 
		 *    - b. Wenn ja Link l�schen ansonsten erstellen
		 *
		 */

		Vertex vNode1 = new Vertex(new Vector3D(myobjectList.get(node1).getCenterPointGlobal()));
		Vertex vNode2 = new Vertex(new Vector3D(myobjectList.get(node2).getCenterPointGlobal()));
		
		boolean found = false;
		if (!(linkliste.isEmpty())) {
			
			
				for (int i = 0;i< linkliste.size();i++) {		
					MTLink it = linkliste.get(i);
					//if ((it.getStartObjectID() == node1 && it.getEndObjectID() == node2) || (it.getStartObjectID() == node2 && it.getEndObjectID() == node1)) {
			
					// TODO: Falsche Check liefert zu schnell ein vorhanden
					if ((it.getStartObjectID() == node1 && it.getEndObjectID() == node2) || (it.getStartObjectID() == node2 && it.getEndObjectID() == node1)) {
						System.out.println("MTLinkController: createLink: Link SCHON in der Liste vorhanden; " + node1 + " und " + node2 + "\n \t \t Vorhanderner LINK: " + it.getStartObjectID() + " und " + it.getEndObjectID());
						canvas.removeChild(it);
						linkliste.remove(i);
						found = true;
						
						
					} else {
						System.out.println("MTLinkController: createLink: NICHT GEFUNDEN");
					}
						
				}
				if (!found) {
					//Testen ist das Linksetzen �ber die Typen der zwei Objekte erlaubt (�ber eine Deny Liste)
				System.out.println("MTLinkController: createLink: Link noch NICHT in der Liste vorhanden; " + node1 + " und " + node2);
				MTLink l1 = new MTLink(pApplet, vNode1, vNode2, node1, node2);
				linkliste.add(l1);
				addMTLinktoCanvas(l1);
				}
			
			
		} else {
			System.out.println("MTLinkController: createLink: Linkliste ist leer; " + linkliste);
			MTLink l2 = new MTLink(pApplet, vNode1, vNode2, node1, node2);
			linkliste.add(l2);
			addMTLinktoCanvas(l2);
			
		}
		
	}
	
	/**
	 * MTLink auf dem Canvas Zeichnen.
	 * 
	 * @param obj MTLink
	 */
	private void addMTLinktoCanvas(final MTLink obj) {	
		System.out.println("MTLinkController: addMTLinktoCanvas: Link wird dem Canavas zugeordnet; ");
		canvas.addChild(obj);
		eventObjectHandling();
	}
	
	
	
	
	/**
	 * Which Object ist lassobaled.
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
					System.out.println("Gesture wurde ausgef�hrt");
					
					
						if (!(myobjectList == null)) {
							for (MyMTObject it : myobjectList) {
								if (it.isSelected() == true) {
									System.out.println("MTLinkController: @@@@@ Object ist gespeichert NR:" + it.getID());
									
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
