package store;

import java.util.ArrayList;
import java.util.Iterator;

import org.mt4j.MTApplication;

import ch.mitoco.model.ModelMtObjects;
import ch.mitoco.model.ModelScence;

public class DataController {
	public LoadXML LoadXML;
	
	private ModelScence dataModel;
	
	private MTApplication mtApplication;
	
	private ArrayList<MyMTObject> myobjectList;
	
	private int objectcounter;

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
	
	public void clearScene() {
		System.out.println("ClearScene: " + objectcounter);
		myobjectList.removeAll(getMyobjectList());
		objectcounter = 0;
		dataModel = null;
	}
	
	public void loadObjectXML() {
		
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

	
	//GUI Object kopieren (Neues GUI Object / DatenModel Kopieren
	//Objecte Verknüpfen set links get links

}
