package ch.mitoco.dataController;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.util.MT4jSettings;

import ch.mitoco.components.visibleComponents.MyMTObject;
import ch.mitoco.components.visibleComponents.objectlink.MTLinkController;
import ch.mitoco.model.ModelLink;
import ch.mitoco.model.ModelMtObjects;
import ch.mitoco.model.ModelObjectTyps;
import ch.mitoco.model.ModelScence;
import ch.mitoco.model.ModelTypDescription;
import ch.mitoco.startmenu.SceneMitoco;

import com.thoughtworks.xstream.XStream;

/**Diese Klasse ist für die Verwaltung des Datenmodells verantwortlich.
 * 
 * @author steffe
 * @version 0.8
 */
public class DataController {
	
	/**XML Scenen Loader Objekt.*/

	private LoadXML LoadXML;
	
	/**XML Objekttyp Loader Object.*/

	private LoadObjectXML loadObjectXML;
	
	/**Datenobjekt für eine Scene.*/

	private ModelScence dataModel;
	
	/**Abeleitetes mtApplication Objekt.*/

	private MTApplication mtApplication;
	
	/**ArrayListe in welchem Objekte abespeicher werden.*/

	private ArrayList<MyMTObject> myobjectList;
	
	/**Enthält Objettype die möglich sind.*/

	 
	private ModelObjectTyps objectetyps;
	
	/**Speichert verknüpfte Objekte. */
	private ModelLink modelLink;
	
	/**Anzahl Objekte welche auf der Scene sind.*/

	private int objectcounter;
	
	/**Speicher Objekt.*/

	private SaveXML save;
	
	/** Test: Linker Controller. */
	private MTLinkController linker; //TODO: Test
	
	private ModelTypDescription modeltTypeDesc;
	
	private MTCanvas canvas;

	/**Daten Kontroller Konstruktor.
	 * 
	 * Builds a DataController for the DataModel, creates also a linker controller
	 * 
	 * @param mtAppl MTApplication
	 */
	public DataController(final MTApplication mtAppl, MTCanvas canvas) {
		this.mtApplication = mtAppl;
		this.canvas = canvas;
		objectcounter = 0;
		
		linker = new MTLinkController(mtApplication, canvas); 
	}
	
	/**Erstellt ein Objekt auf einer Scene
	 * 
	 * Kann ein Objekt anhanden eine Template (Objekttypes( auf einer Scene erstellen, 
	 * und speichert das Objekt in der myobcetlist.
	 * 
	 * 
	 * @param objecttype
	 * 			Int entscheidet welcher Objekttype erstellt wird
	 * @return int objectindex. Wich position in the Objectlist Array(for drawing)
	 */
	public int createObject(int objecttype) {
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
		modeltTypeDesc = new ModelTypDescription();
		//modeltTypeDesc.setObjectdenylink(new ArrayList<Integer>());
		//getObjectetyps().getObjectdescription().add(modeltTypeDesc);
		myobjectList.add(new MyMTObject(mtApplication, dataModel.getMtobjects().get(objectcounter), objectcounter, linker));
		
		System.out.println("DataController:  ObjektListe lokal: "+ myobjectList );
		linker.setObjectList(myobjectList, dataModel.getMtobjectlinks(), getObjectetyps());
		linker.setTapAndHoldListener(myobjectList.get(objectcounter)); //TODO: Test
		Integer objectindex = new Integer(myobjectList.indexOf(myobjectList.get(objectcounter)));
		System.out.println("Objectindex: "+objectindex);
		objectcounter++;
		
		return objectindex;
	}
	
	
	/**Builds a Object in the Datamodel
	 * 
	 * Builds a Object based on the delivered object datamodel on the Scene.
	 * The Object will be saved in the myobjectlist.

	 * @param object The Datamodel as a Template for the Object
	 * 			Int entscheidet welcher Objekttype erstellt wird
	 * @return int objectindex. Wich position in the Objectlist Array(for drawing)
	 */
	public int createObject(ModelMtObjects object) {
		//ModelMtObjects object = new ModelMtObjects();
		object.setId(objectcounter);
		dataModel.getMtobjects().add(object);
		modeltTypeDesc = new ModelTypDescription();
		//modeltTypeDesc.setObjectdenylink(new ArrayList<Integer>());
		//getObjectetyps().getObjectdescription().add(modeltTypeDesc);
		myobjectList.add(new MyMTObject(mtApplication, dataModel.getMtobjects().get(objectcounter), objectcounter, linker));
		
		System.out.println("DataController:  ObjektListe lokal: "+ myobjectList );
		
		//Leere Scene --> Object link = 0/0 wieso?
		linker.setObjectList(myobjectList, dataModel.getMtobjectlinks(), getObjectetyps());
		linker.setTapAndHoldListener(myobjectList.get(objectcounter)); //TODO: Test
		Integer objectindex = new Integer(myobjectList.indexOf(myobjectList.get(objectcounter)));
		System.out.println("Objectindex: "+objectindex);
		objectcounter++;
		
		return objectindex;
	}
	
	/**delete the Selected GUI Objects and the Datamodel.
	 * 
	 */
	public boolean deleteObject() {
		// Object anklicken, welches gelöscht werden soll
		//System.out.println("ClearScene: " + objectcounter);
		//myobjectList.removeAll(getMyobjectList());
		//objectcounter = 0;
		//forschleife
		
		for (MyMTObject it : getMyobjectList()) {
  		  if (it.getTagFlag()) {
  			  it.destroyMyObject();
  			  //dataModel.getMtobjects().remove(it);
  			  //myobjectList.remove(it);
  		  }
		}
		
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
		//To-Do: IDs höchzählen 
		dataModel.setId(1);
		dataModel.setName(scenename);
		modelLink = new ModelLink();
		dataModel.getMtobjectlinks().add(modelLink);
		return dataModel;
	}
	
	/**Loading the XML File and build the myobjectList.
	 * 
	 * 
	 * @return Boolean returns true if the Loading was successfully
	 */
	public Boolean loadSceneXML(String filename) {
		LoadXML = new LoadXML(filename, "scenedata");
		if (!LoadXML.getReaderStatus()) {
			return false;
		}
			
		if (objectcounter > 0) {
			System.out.println("Grösser Null " + objectcounter);
			//Objects are already drawn, you have to clean
			return false;
			}
		else {
			dataModel = LoadXML.getDataModel();
			
			System.out.println("DataController Object Gen:" + dataModel.getMtobjectlinks());
			//Muss wegfallen, Links werden nicht geladen
			//dataModel.setMtobjectlinks(new ArrayList<ModelLink>());
			System.out.println("DataController Object Gen:" + dataModel.getMtobjectlinks());
			for (Iterator<ModelMtObjects> it = dataModel.getMtobjects().iterator(); it.hasNext();) {
				myobjectList.add(new MyMTObject(mtApplication, it.next(), objectcounter, linker));
				System.out.println("Object Gen:" + objectcounter);
				linker.setTapAndHoldListener(getMyobjectList().get(objectcounter)); //TODO: Test
				linker.setObjectList(myobjectList, dataModel.getMtobjectlinks(), getObjectetyps());
				objectcounter++;
				
			}
			linker.readDataAll();
			System.out.println("Datacontroller: LoadXML "+ dataModel.getMtobjectlinks());
			return true;
		}
		
	}
	
	/**Will save the actual dataModel with content to a XML File.
	 * 
	 */
	public void saveSceneXML() {
		save = new SaveXML(SceneMitoco.getExportPath() + "savetest1.xml", dataModel);
		
	}
	
	/**Delets all Objects on the Scene.
	 * 
	 */
	public void clearScene() {
		System.out.println("ClearScene: " + objectcounter);
		myobjectList.removeAll(getMyobjectList());
		objectcounter = 0;
		dataModel.getMtobjects().removeAll(myobjectList);
		dataModel.getMtobjectlinks().removeAll(getMyobjectList());
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
			xstreamSave.toXML(objectetyps, new FileOutputStream(MT4jSettings.getInstance().getDataFolderPath()+"objectlist1.xml"));
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
	
	/**Liefert das Datenmodel zurück.
	 * @return
	 */
	public ModelScence getModelScene() {
		return dataModel;
	}
	
	/**Liefert den Link Kontroller zurück.
	 * 
	 * @return linker MTLinkController
	 */
	public MTLinkController getLinkController() {
		return linker;
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

	/**
	 * @param modeltTypeDesc the modeltTypeDesc to set
	 */
	public void setModeltTypeDesc(ModelTypDescription modeltTypeDesc) {
		this.modeltTypeDesc = modeltTypeDesc;
	}

	/**
	 * @return the modeltTypeDesc
	 */
	public ModelTypDescription getModeltTypeDesc() {
		return modeltTypeDesc;
	}

	
	//GUI Object kopieren (Neues GUI Object / DatenModel Kopieren
	//Objecte Verknüpfen set links get links

}
