package ch.mitoco.reporting;

import gov.pnnl.components.visibleComponents.widgets.radialMenu.examples.MTRadialMenuExampleScene;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.LinkedList;
import java.util.Comparator;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTLine;
import org.mt4j.components.visibleComponents.widgets.MTBackgroundImage;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.inputData.MTFingerInputEvt;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;
import org.mt4jx.components.visibleComponents.layout.MTColumnLayout2D;
import org.mt4jx.util.animation.AnimationUtil;

import ch.mitoco.components.visibleComponents.MyMTObject;
import ch.mitoco.components.visibleComponents.objectlink.MTLinkController;
import ch.mitoco.components.visibleComponents.widgets.MTTextAttribut;
import ch.mitoco.dataController.DataController;
import ch.mitoco.main.MitocoScene;
import ch.mitoco.model.ModelAttributContent;
import ch.mitoco.model.ModelMtAttributs;
import ch.mitoco.model.ModelMtObjects;

public class MitocoReporting extends AbstractScene {
	
	private MitocoScene scene;
	private ArrayList<MyMTObject> mtobjs;
	private int attributCounter;
	private int objectCounter;
	private Map<String, Integer> playerTotCount;
	private Map<Integer, Integer> objTotCount;
	private boolean translate = true;
	private MTColumnLayout2D colLayout;
	private int sumCount;
	private MTLinkController linker;
	private List<Integer> linkedObjects;
	
	public MitocoReporting(AbstractMTApplication mtApplication, String name, MitocoScene scene, DataController dataController, int reportId) {
		super(mtApplication, name);
		this.scene = scene;
		final AbstractMTApplication app = mtApplication;
		scene.getGuiOverlay();
		switch(reportId) {
		case(1):
			golfReporting(app, dataController);
			break;
		
		default:
			break;
		}
	}
	
	/**
	 * Method to calculate the best golfer on total score basis.
	 * 
	 * @param app MTApplication Object
	 * @param dataController DataController Object to get hand on the Object Content
	 */
	private void golfReporting(final AbstractMTApplication app, final DataController dataController) {
		objectCounter = 0;
		colLayout = new MTColumnLayout2D(app);
		mtobjs = dataController.getMyobjectList();
		linker = dataController.getLinkController();
		playerTotCount = new LinkedHashMap<String, Integer>();
		objTotCount = new LinkedHashMap<Integer, Integer>();
		for (final MyMTObject it : mtobjs) {
			if (it.isVisible()) {
				//Counter jeweils für jeden Spieler wieder auf 0 setzen
				sumCount = 0;
				attributCounter = 0;
				
				String ol = dataController.getModelScene().getMtobjects().get(objectCounter).getObjectlable();
				
				//Nur Objekte vom Typ Player werden weiter bearbeitet
				if (ol.equalsIgnoreCase("Player")) {
					//Get Name for Player
					String vorname = dataController.getModelScene().getMtobjects().get(objectCounter).getObjectattributs().get(0).getAttributcontent().get(0).getValue();
					String nachname = dataController.getModelScene().getMtobjects().get(objectCounter).getObjectattributs().get(1).getAttributcontent().get(0).getValue();
					Integer id = (int) dataController.getModelScene().getMtobjects().get(objectCounter).getId();
					System.out.println("********************************22" + id);
					//String Concat fuer einen eindeutigeren key
					String name = vorname.concat(" " + nachname);
					//Search for all linked Objects -> 1 per Player
					linkedObjects = linker.getLinkListObject(it.getID());
					System.out.println("LinkedObjecs Inhalt: ******************333" + linkedObjects);
					for (Integer ite : linkedObjects) {
						for (ModelAttributContent iterat : dataController.getModelScene().getMtobjects().get(ite).getObjectattributs().get(attributCounter).getAttributcontent()) {
//							System.out.println("**************************************" + iterat.getValue());
							sumCount += Integer.parseInt(iterat.getValue());
							attributCounter++;
						}
					}
					//HashMap für Auswertungs-Objekt
					playerTotCount.put(name, sumCount);
					//HashMap für grafische Ausrichtung
					objTotCount.put(id, sumCount);
					
				}	
				objectCounter++;
			}
		}
		
		//Neues Objekt generieren für die Auswertung
		ModelMtObjects template = new ModelMtObjects();
			
		//Keys anhand von Values sortieren
        List<String> sortedKeysPlayer = getSortedKeys(playerTotCount);
        List<String> sortedKeysObject = getSortedKeysO(objTotCount);
        System.out.println(sortedKeysPlayer);

        //Hier wird das Datenmodell des Auswertungstyps erstellt.
        //template.setId(3);
        template.setObjectlable("Auswertung");
        template.setObjecttyp(3);
        
         //sortierte Map ausgeben
        for (String key : sortedKeysPlayer) {
        	System.out.println("key [" + key + "] --> value [" + playerTotCount.get(key) + "]");
        	ModelMtAttributs mtattr1 = new ModelMtAttributs();
        	mtattr1.setId(0);
    		mtattr1.setLable("Name und Total");
    		
    		ModelAttributContent attrbc1 = new ModelAttributContent();
    		attrbc1.setType("String");
    		attrbc1.setValue(key + " Total Score: " + playerTotCount.get(key));
    		mtattr1.getAttributcontent().add(attrbc1);
    		template.getObjectattributs().add(mtattr1);
        }
				
		//Auswertungs Objekt generieren und Nummer merken
		int objAuswertungId = dataController.createObject(template);

		//Auswertungs-Objekt neu zeichnen
        scene.getCanvas().addChild(dataController.getMyobjectList().get(objAuswertungId));
		
        float height = 0;
        objectCounter = 0;
        
        for (String key : sortedKeysObject) {
			Integer k = Integer.parseInt(key);
			for (final MyMTObject ite : mtobjs) {
	        	int id = ite.getID();
	        	if (k == id) {
//	        		linker.setVisibleOne(false, id);
					System.out.println("Matching ID: " + k + " " + id);
					Vector3D aktVec01 = new Vector3D();
					aktVec01.translate(new Vector3D(ite.getWidthXY(TransformSpace.GLOBAL) / 2, ite.getHeightXY(TransformSpace.GLOBAL) / 2 + height, 0));
					//ite.tweenTranslate(aktVec01, 400, 0.1f, 0.6f);
					//AnimationUtil.translate(ite, 0, 0);
					ite.setPositionGlobal(aktVec01);
//					linker.setVisibleOne(true, id);
					linker.drawLinie();
					float heightObj = ite.getHeightXY(TransformSpace.GLOBAL);
					System.out.println("Höhe:" + height);
					height += heightObj;
					System.out.println("Höhe:" + height);
				}				
        	}
        }
}
	
	/**
	 * Statische Methode um eine Map<String, Integer> nach Values zu sortieren.
	 * 
	 * @param map Eine Key/Value map mit beliebigen Inhalten die nach den Values sortiert wird
	 * @return Eine Liste mit den Keys wird sortiert zurück gegeben. Mit dem Inhalt dieser Liste wird wieder auf die Values der Original Map zugegriffen
	 */
    public static List<String> getSortedKeys(final Map<String, Integer> map) {

        //Map umdrehen
        // originale Map: key --> value
        // neue Map: value --> Liste von keys

        Map<Integer, List<String>> invertedMap = new HashMap<Integer, List<String>>();
        for (String key : map.keySet()) {
                Integer value = map.get(key);

                //wenn die Map für diesen Key noch keine Liste enthält: leere Liste einfügen
                if (invertedMap.containsKey(value) == false) {
                        invertedMap.put(value, new ArrayList<String>());
                }

                //aktuellen Key an die Liste anhängen
                List<String> keys = invertedMap.get(value);
                keys.add(key);
        }

        //Keys aus der invertedMap holen und sortieren
        Collection<Integer> values = invertedMap.keySet();

        //in Liste verfrachten
        List<Integer> valueList = new ArrayList<Integer>();
        valueList.addAll(values);

        //List der Werte sortieren
        Collections.sort(valueList);

        //nun anhand der sortierten values wieder die Keys holen und in einer Liste ablegen
        List<String> sortedKeys = new ArrayList<String>();
        for (Integer i : valueList) {
                List<String> keyList = invertedMap.get(i);
                for (String s : keyList) {
                        sortedKeys.add(s);
                }
        }
        return sortedKeys;
    }
    
	/**
	 * Statische Methode um eine Map<Integer, Integer> nach Values zu sortieren.
	 * 
	 * @param map Eine Key/Value map mit beliebigen Inhalten die nach den Values sortiert wird
	 * @return Eine Liste mit den Keys wird sortiert zurück gegeben. Mit dem Inhalt dieser Liste wird wieder auf die Values der Original Map zugegriffen
	 */
    public static List<String> getSortedKeysO(final Map<Integer, Integer> map) {

        //Map umdrehen
        // originale Map: key --> value
        // neue Map: value --> Liste von keys

        Map<Integer, List<String>> invertedMap = new HashMap<Integer, List<String>>();
        for (Integer key : map.keySet()) {
                Integer value = map.get(key);

                //wenn die Map für diesen Key noch keine Liste enthält: leere Liste einfügen
                if (invertedMap.containsKey(value) == false) {
                        invertedMap.put(value, new ArrayList<String>());
                }

                //aktuellen Key an die Liste anhängen
                List<String> keys = invertedMap.get(value);
                String k = key.toString();
                keys.add(k);
        }

        //Keys aus der invertedMap holen und sortieren
        Collection<Integer> values = invertedMap.keySet();

        //in Liste verfrachten
        List<Integer> valueList = new ArrayList<Integer>();
        valueList.addAll(values);

        //List der Werte sortieren
        Collections.sort(valueList);

        //nun anhand der sortierten values wieder die Keys holen und in einer Liste ablegen
        List<String> sortedKeys = new ArrayList<String>();
        for (Integer i : valueList) {
                List<String> keyList = invertedMap.get(i);
                for (String s : keyList) {
                        sortedKeys.add(s);
                }
        }
        return sortedKeys;
    }
}
