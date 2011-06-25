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
import ch.mitoco.model.ModelLink;
import ch.mitoco.model.ModelLinkDescription;
import ch.mitoco.model.ModelObjectTyps;
import ch.mitoco.model.ModelTypDescription;
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
		
	/** Datamodel with all Link verbindungen. */
	private List<ModelLink> modellinkList;
	
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
		selectedObjectID.add(new MTSelectStoreObject()); // Erste Object für Dupleten Speicher. Die nächsten Objekte werden in der Methode storeSelectObject erstellt
		
		validLinkPair = new ArrayList<MTSelectStoreObject>();
		
		linkliste = Collections.synchronizedList(new ArrayList<MTLink>()); // Liste für alle Objekte LINK, Thread Safe TODO: Nutzen muss noch abgeklärt werden
	
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
	 * Objectliste und das Data Model der Links wird gesetzt. 
	 * @param myobjectList
	 */
	public synchronized void setObjectList(List<MyMTObject> myobjectList , List<ModelLink> modellink, ModelObjectTyps modelObjectTyps){
		this.myobjectList = null;
		this.myobjectList = myobjectList;
		System.out.println("MTLinkController: setObjectList: ObjektListe und LinkModel " + this.myobjectList + " IM ÜbergabeParameter " + myobjectList);
		this.modellinkList = null;
		this.modellinkList = modellink;
		
		for (ModelTypDescription it : modelObjectTyps.getObjectdescription()) {
			//ArrayList<Integer> inn = it.getObjectdenylink();
			for (Integer itt : it.getObjectdenylink()) {
				System.out.println("MTLinkController: Denny Object ID: " + itt);
			}
		}
			
		for (ModelLink it : modellinkList) {
			System.out.println("MTLinkController: setObjectList: Linklist: First " + it.getFirstObject() + " Second Objekt : " + it.getSecondObject());
		}
		
	}
	
	/**
	 * Lesen der ModelDaten für die Links und Setzten.
	 */
	public void readDataAll() {
		for (ModelLink it : modellinkList) {
			System.out.println("MTLinkController: readDataAll: Links aus XML werden erstellt: 1: " + it.getFirstObject() + "  2: " + it.getSecondObject());
			createLink(it.getFirstObject(), it.getSecondObject());
			
		}
	}
	
	/**
	 * LinkListe werden aus dem Gui in das Datamodel geschrieben.
	 */
	public void writeDataAll() {
		
		for (MTLink it : linkliste) {
		ModelLink m1 = new ModelLink();
		m1.setFirstObject(it.getStartObjectID());
		m1.setSecondObject(it.getEndObjectID());
		modellinkList.add(m1);	
		}
	}
	
	public void writeDataOne(MTLink obj) {
		ModelLink m1 = new ModelLink();
		m1.setFirstObject(obj.getStartObjectID());
		m1.setSecondObject(obj.getEndObjectID());
		
		
		List<ModelLinkDescription> d1  = new ArrayList<ModelLinkDescription>();
		ModelLinkDescription el1 = new ModelLinkDescription();
		el1.setLinkcolor(obj.getStrokeColor());
		el1.setLinkdescription(obj.getName());
		d1.add(el1);
		
		m1.setObjectlinkdescription(d1);
		
		modellinkList.add(m1);	
	}
	
	/**
	 * 
	 * @param linkID
	 */
	public void dropLinkDataModel(int linkID) {
		
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
			it.setColorPickerVisible(false);
			it.setVertices(new Vertex[]{
					new Vertex(myobjectList.get(it.getStartObjectID()).getCenterPointGlobal()),
					new Vertex(myobjectList.get(it.getEndObjectID()).getCenterPointGlobal())});	
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
		System.out.println("MTLinkController: setTapAndHoldListener: für ListenFeld");
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
		System.out.println("MTLinkController: setTapAndHoldListener: für Objekt: " + obj);
		
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
	 * Jedem Object wird dem LassoProcessor hinzugefügt, damit es auch lassoable wird.
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
			System.out.println("Wurde gelöscht: " +j );
			selectedObjectID.remove(j);
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
	
	//Gibt das Linkpaar zurück 
	
	/**
	 * 
	 * Gibt zu eine Link ID zu zwei verlinkten Objekt IDs zurück
	 * 
	 * @param node2 int
	 * @return linkid int
	 */
	public int getLinkID(int node1, int node2) {
		int linkId =0;
		
		return linkId;
	}
	
	/**
	 * 
	 * Gibt alle IDs der versteckten Links zurück.
	 * 
	 * @return
	 */
	public int getHiddenLinks() {
		int hiddenObj=0;
		
		return hiddenObj;
	}
	
	/**
	 * 
	 * Gibt eine Liste von Link-IDs mit allen verlinkungen eines Objekt zurück.
	 * 
	 * @param searchObj int
	 * @return list List<Integer>
	 */
	public final List<Integer> getLinkListObjectID(final int searchObj) {
		List<Integer> list = new ArrayList<Integer>();
		//TODO: Ausprogrammieren

		return list; 
	}
	
	/**
	 * 
	 * Gibt eine Liste von Objekt IDs mit allen verlinkungen eines Objekt zurück.
	 * 
	 * @param searchObj int
	 * @return list List<Integer>
	 */ 
	public final List<Integer> getLinkListObject(final int searchObj) {
		List<Integer> list = new ArrayList<Integer>();
	
		
		for (MTLink it : linkliste) {
			if (it.getEndObjectID() == searchObj)
				list.add(it.getStartObjectID());
			if (it.getStartObjectID() == searchObj)
				list.add(it.getEndObjectID());
		}
	
		
		return list; 
	}
	
	/**
	 * Anzeigen oder verstecken aller Links.
	 * 
	 * visible = false = Link wird nicht angezeigt
	 * visible = true = Link wird angezeigt
	 * 
	 * @param visible boolean
	 */
	public void setVisibleAllLink(boolean visible){
		
	}
	
	/**
	 * Verstecken oder Anzeigen eines einzelnen Links über eine bekannte objID.
	 * 
	 * @param visible boolean
	 * @param objID int
	 */
	public void setVisibleOneLinkObj(boolean visible, final int objID) {
		
	}
	
	/**
	 * 
	 * Verstecken oder Anzeigen eines einzelnen Links über eine bekannte Link ID.
	 * 
	 * @param visible booelan
	 * @param lnkID int
	 */
	public void setVisibleOneLinkID(boolean visible, final int lnkID) {
		
	}
	

	
	
	/**
	 * -> Selector
	 * Gibt alle Object ID der über den ArcBall getaggten Objekte zurück.
	 */
	public void showTaggedObject() {
		for (MyMTObject itt : myobjectList) {
			System.out.println("Objekt: " + itt.getID() +" Markiert " + itt.getTagFlag());
			//TODO: Ausprogrammieren
		}
		
	}
	
	/**
	 * Erstellt Link zwischen zwei Objekte. Soll ein Link erstellt werden der schon vorhanden ist wird dieser gelöscht. Zwischen zwei gleiche Objekte (ID Paar) kann nur ein Link bestehen.
	 * 
	 * @param node1 int
	 * @param node2 int
	 */
	public void createLink(final int node1, final int node2) {
		
		System.out.println("MTLinkController: createLink: Link for Objekt ID; " + node1 + " und " + node2);
		System.out.println("MTLinkController: createLink: Link for Objekt ID; " + myobjectList.get(node1).getCenterPointGlobal() + " und " + myobjectList.get(node2).getCenterPointGlobal());
		/**
		 * 1. Link erstellen 
		 *    - a. Abfrage: Link schon in der Liste 
		 *    - b. Wenn ja Link löschen, ansonsten erstellen
		 *
		 */

		Vertex vNode1 = new Vertex(new Vector3D(myobjectList.get(node1).getCenterPointGlobal()));
		Vertex vNode2 = new Vertex(new Vector3D(myobjectList.get(node2).getCenterPointGlobal()));
		
		boolean found = false;
		if (!(linkliste.isEmpty())) {
			
			
				for (int i = 0;i< linkliste.size();i++) {		
					MTLink it = linkliste.get(i);
					//if ((it.getStartObjectID() == node1 && it.getEndObjectID() == node2) || (it.getStartObjectID() == node2 && it.getEndObjectID() == node1)) {
			
					
					if ((it.getStartObjectID() == node1 && it.getEndObjectID() == node2) || (it.getStartObjectID() == node2 && it.getEndObjectID() == node1)) {
						System.out.println("MTLinkController: createLink: Link SCHON in der Liste vorhanden; " + node1 + " und " + node2 + "\n \t \t Vorhanderner LINK: " + it.getStartObjectID() + " und " + it.getEndObjectID());
						canvas.removeChild(it);
						
						linkliste.remove(i);
						it.destroy();
						
						found = true;
						
						
					} else {
						System.out.println("MTLinkController: createLink: NICHT GEFUNDEN");
					}
						
				}
				if (!found) {
				//Testen ist das Linksetzen über die Typen der zwei Objekte erlaubt (Über eine Deny Liste)
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
					System.out.println("Gesture wurde ausgeführt");
					
					
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
