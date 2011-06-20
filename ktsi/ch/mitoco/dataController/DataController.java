package ch.mitoco.dataController;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;

import ch.mitoco.components.visibleComponents.MyMTObject;
import ch.mitoco.components.visibleComponents.objectlink.MTLinkController;
import ch.mitoco.model.ModelMtObjects;
import ch.mitoco.model.ModelObjectTyps;
import ch.mitoco.model.ModelScence;

import com.thoughtworks.xstream.XStream;

/**Diese Klasse ist für die Verwaltung des Datenmodells verantwortlich.
 * 
 * @author steffe
 * @version 0.8
 */
public class DataController {
	
	/**XML Scenen Loader Objekt.
	 * 
	 */
	private LoadXML LoadXML;
	
	/**XML Objekttyp Loader Object.
	 * 
	 */
	private LoadObjectXML loadObjectXML;
	
	/**Datenobjekt für eine Scene.
	 * 
	 */
	private ModelScence dataModel;
	
	/**Abeleitetes mtApplication Objekt.
	 * 
	 */
	private MTApplication mtApplication;
	
	/**ArrayListe in welchem Objekte abespeicher werden.
	 * 
	 */
	private ArrayList<MyMTObject> myobjectList;
	
	/**Enthält Objettype die möglich sind.
	 * 
	 */
	private ModelObjectTyps objectetyps;
	
	/**Anzahl Objekte welche auf der Scene sind.
	 * 
	 */
	private int objectcounter;
	
	/**Speicher Objekt.
	 * 
	 */
	private SaveXML save;
	
	/** Test: Linker Controller. */
	private MTLinkController linker; //TODO: Test
	

	/**Daten Kontroller Konstruktor.
	 * 
	 * @param mtAppl MTApplication
	 */
	public DataController(final MTApplication mtAppl, MTCanvas canvas) {
		this.mtApplication = mtAppl;
		objectcounter = 0;
		linker = new MTLinkController(mtAppl, canvas , getMyobjectList()); //TODO: Test
		
	}
	
	/**Erstellt ein Objekt auf einer Scene
	 * 
	 * Kann ein Objekt anhanden eine Template (Objekttypes( auf einer Scene erstellen, 
	 * und speichert das Objekt in der myobcetlist.
	 * 
	 * @param objecttype
	 * 			Int entscheidet welcher Objekttype erstellt wird
	 */
	public void createObject(int objecttype) {
		ModelMtObjects object = new ModelMtObjects();
		
		
		for(ModelMtObjects it : getObjectetyps().getObjecttyp()){
		//for (Iterator<ModelMtObjects> it = getObjectetyps().getObjecttyp().iterator(); it.hasNext();) {
			System.out.println("DataController Objekttyp: " + objecttype);
			System.out.println("DataController Objekttyp: " + it.getObjecttyp());
			
			if (it.getObjecttyp() == objecttype) {
				
				object = it;
				break;				
			}
			
		}
		object.setId(objectcounter);
		dataModel.getMtobjects().add(object);
		
		myobjectList.add(new MyMTObject(mtApplication, dataModel.getMtobjects().get(objectcounter), objectcounter, linker));
		linker.setTapAndHoldListener(getMyobjectList().get(objectcounter)); //TODO: Test
		
		objectcounter++;
		
		//returnn created Object
	}
	
	//Delete GUI Object (Datenmodel)
	/**delete the Selected GUI Objects and the Datamodel.
	 * 
	 */
	public boolean deleteObject() {
		// Object anklicken, welches gelöscht werden soll
		//System.out.println("ClearScene: " + objectcounter);
		//myobjectList.removeAll(getMyobjectList());
		//objectcounter = 0;
		return false;
	}
	
	/**Erstellt eine Objektliste.
	 * in dieser Liste werden alle Objekte auf einer Scene abgespeichert.
	 * 
	 */
	public void createObjectList() {
		setMyobjectList(new ArrayList<MyMTObject>());
	}
	
	/**Daten Modell einer Scene erstellen.
	 * 
	 * @param scenename String
	 * @return dataModel
	 */
	public ModelScence createDataModel(final String scenename) {
		dataModel = new ModelScence();
		dataModel.setId(1);
		dataModel.setName(scenename);
		return dataModel;
	}
	
	/**Loading the XML File and build the myobjectList.
	 * 
	 * @return
	 */
	public Boolean loadSceneXML() {
		LoadXML = new LoadXML("xstream.xml", "scenedata");
			
		if (objectcounter > 0) {
			System.out.println("Grösser Null " + objectcounter);
			//Objects are already drawn, you have to clean
			return false;
			}
		else {
			dataModel = LoadXML.getDataModel();
			for (Iterator<ModelMtObjects> it = dataModel.getMtobjects().iterator(); it.hasNext();) {
				myobjectList.add(new MyMTObject(mtApplication, it.next(), objectcounter, linker));
				System.out.println("Object Gen:" + objectcounter);
				linker.setTapAndHoldListener(getMyobjectList().get(objectcounter)); //TODO: Test
				objectcounter++;
			}
			return true;
		}
		
	}
	/**Load SceneListeXML
	 * 
	 * 
	 * @return
	 */
	public Boolean loadSceneListeXML() {
		LoadXML = new LoadXML("xstream.xml", "sceneliste");
			
		if (objectcounter > 0) {
			System.out.println("Grösser Null " + objectcounter);
			//Objects are already drawn, you have to clean
			return false;
			}
		else {
			dataModel = LoadXML.getDataModel();
			for (Iterator<ModelMtObjects> it = dataModel.getMtobjects().iterator(); it.hasNext();) {
				myobjectList.add(new MyMTObject(mtApplication, it.next(), objectcounter, linker));
				System.out.println("Object Gen:" + objectcounter);
				linker.setTapAndHoldListener(getMyobjectList().get(objectcounter)); //TODO: Test
				objectcounter++;
			}
			return true;
		}
		
	}
	
	/**Will save the actual dataModel with content to a XML File.
	 * 
	 */
	public void saveSceneXML() {
		save = new SaveXML("savetest1.xml", dataModel);
		
	}
	
	/**Delets all Objects on the Scene.
	 * 
	 */
	public void clearScene() {
		System.out.println("ClearScene: " + objectcounter);
		myobjectList.removeAll(getMyobjectList());
		objectcounter = 0;
		dataModel = null;
	}
	
	/**Load Objecttyps from XML.
	 * 
	 */
	public void loadObjectXML() {
		
		loadObjectXML = new LoadObjectXML();
		this.objectetyps = loadObjectXML.getObjectModel();
	}
	
	/**Saves the objectstyps in a XML.
	 * 
	 * for Template mode
	 * 
	 */
	public void saveObjectXML() {
		XStream xstreamSave = new XStream();
		
		
		
		
		try {
			xstreamSave.toXML(objectetyps, new FileOutputStream("objectlist1.xml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param myobjectList the myobjectList to set
	 */
	public void setMyobjectList(ArrayList<MyMTObject> myobjectList) {
		this.myobjectList = myobjectList;
	}

	/**
	 * @return the myobjectList
	 */
	public ArrayList<MyMTObject> getMyobjectList() {
		return myobjectList;
	}
	
	public void linkObjects(int object1, int object2) {
		
	}

	/**
	 * @param objectetyps the objectetyps to set
	 */
	public void setObjectetyps(ModelObjectTyps objectetyps) {
		this.objectetyps = objectetyps;
	}

	/**
	 * @return the objectetyps
	 */
	public ModelObjectTyps getObjectetyps() {
		objectetyps = new ModelObjectTyps();
		loadObjectXML();
		objectetyps = loadObjectXML.getObjectModel();
		return objectetyps;
	}

	/**
	 * 
	 * @return
	 */
	public int getObjectcounter() {
		return objectcounter;
	}

	
	//GUI Object kopieren (Neues GUI Object / DatenModel Kopieren
	//Objecte Verknüpfen set links get links

}
