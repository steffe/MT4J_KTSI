package store;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.mt4j.MTApplication;
import org.mt4j.util.MTColor;

import ch.mitoco.model.ModelMtObjects;
import ch.mitoco.model.ModelObjectTyps;
import ch.mitoco.model.ModelScence;

import com.thoughtworks.xstream.XStream;

public class DataController {
	public LoadXML LoadXML;
	public LoadObjectXML loadObjectXML;
	
	private ModelScence dataModel;
	
	private MTApplication mtApplication;
	
	private ArrayList<MyMTObject> myobjectList;
	private ModelObjectTyps objectetyps;
	
	private int objectcounter;
	
	private SaveXML save;

	public DataController(final MTApplication mtAppl) {
		this.mtApplication = mtAppl;
		objectcounter = 0;
	}
	
	public void createObject() {
		ModelMtObjects object = new ModelMtObjects();
		object.setId(objectcounter);
		dataModel.getMtobjects().add(object);
		myobjectList.add(new MyMTObject(mtApplication, dataModel.getMtobjects().get(objectcounter), objectcounter));
		objectcounter++;
	}
	
	//Delete GUI Object (Datenmodel)
	public void deleteObject() {
		// Object anklicken, welches gelöscht werden soll
		//System.out.println("ClearScene: " + objectcounter);
		//myobjectList.removeAll(getMyobjectList());
		//objectcounter = 0;
	}
	
	public void createObjectList() {
		setMyobjectList(new ArrayList<MyMTObject>());
	}
	
	public void loadScene() {
		
	}
	
	public void loadObject() {
		
	}
	
	public ModelScence createDataModel(String scenename) {
		dataModel = new ModelScence();
		dataModel.setId(0);
		dataModel.setName(scenename);
		return dataModel;
	}
	
	public Boolean loadSceneXML() {
		LoadXML = new LoadXML("xstream.xml");
			
		if (objectcounter > 0) {
			System.out.println("Grösser Null " + objectcounter);
			//Objects are already drawn, you have to clean
			return false; 
		}
		else {
			dataModel = LoadXML.getDataModel();
			for (Iterator<ModelMtObjects> it = dataModel.getMtobjects().iterator(); it.hasNext();) {
				myobjectList.add(new MyMTObject(mtApplication, it.next(), objectcounter));
				System.out.println("Object Gen:" + objectcounter);
				objectcounter++;
			}
			return true;
		}
		
	}
	
	public void saveSceneXML() {
		save = new SaveXML("savetest1.xml", dataModel);
		
	}
	
	public void clearScene() {
		System.out.println("ClearScene: " + objectcounter);
		myobjectList.removeAll(getMyobjectList());
		objectcounter = 0;
		dataModel = null;
	}
	
	public void loadObjectXML() {
		
		loadObjectXML = new LoadObjectXML();
		this.objectetyps = loadObjectXML.getObjectModel();
		
		
		
	}
	
	public void saveObjectXML() {
		XStream xstreamSave = new XStream();
		
		objectetyps = new ModelObjectTyps();
		ModelMtObjects test1 = new ModelMtObjects();
		test1.setObjecttyp(0);
		MTColor greypez = new MTColor(12, 12, 12, 34);
		test1.setObjectcolor(greypez);
		//objectetyps.getObjecttyp().add(test1);
		try {
			xstreamSave.toXML(objectetyps, new FileOutputStream("objectlist.xml"));
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
		return objectetyps;
	}

	public int getObjectcounter() {
		return objectcounter;
	}

	
	//GUI Object kopieren (Neues GUI Object / DatenModel Kopieren
	//Objecte Verknüpfen set links get links

}
