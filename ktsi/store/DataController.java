package store;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.mt4j.MTApplication;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import ch.mitoco.components.visibleComponents.MyMTObject;
import ch.mitoco.model.ModelAttributContent;
import ch.mitoco.model.ModelMtAttributs;
import ch.mitoco.model.ModelMtObjects;
import ch.mitoco.model.ModelObjectTyps;
import ch.mitoco.model.ModelScence;
import ch.mitoco.model.ModelTypDescription;

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
	
	public void createObject(int objecttype) {
		ModelMtObjects object = new ModelMtObjects();
		
		for (Iterator<ModelMtObjects> it = getObjectetyps().getObjecttyp().iterator(); it.hasNext();) {
			if (it.next().getObjecttyp() == objecttype) {
				object = it.next();
				
			}
			break;
		}
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
	/**
	 * 
	 */
	public void loadObject() {
		
	}
	
	/**
	 * 
	 * @param scenename
	 * @return
	 */
	public ModelScence createDataModel(String scenename) {
		dataModel = new ModelScence();
		dataModel.setId(1);
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
				
		ModelMtObjects objects3 = new ModelMtObjects();
		objects3.setObjecttyp(0);
		MTColor greypez1 = new MTColor(12, 12, 12, 34);
		objects3.setObjectcolor(greypez1);
		objects3.setObjectposition(new Vector3D(200, 200));
		ModelMtAttributs mtattr2 = new ModelMtAttributs();
		mtattr2.setAttcolor(greypez1);
		mtattr2.setId(0);
		mtattr2.setLable("");
		ModelAttributContent attrbc2 = new ModelAttributContent();
		attrbc2.setType("String");
		attrbc2.setValue("");
		mtattr2.getAttributcontent().add(attrbc2);
		ModelMtAttributs mtattr3 = new ModelMtAttributs();
		mtattr3.setAttcolor(greypez1);
		mtattr3.setId(1);
		mtattr3.setLable("");
		ModelAttributContent attrbc3 = new ModelAttributContent();
		attrbc3.setType("String");
		attrbc3.setValue("");
		mtattr3.getAttributcontent().add(attrbc3);
		objects3.getObjectattributs().add(mtattr2);
		objects3.getObjectattributs().add(mtattr3);
		
		
		//Objecttyp 2
		ModelMtObjects objects4 = new ModelMtObjects();
		objects4.setObjecttyp(1);
		MTColor blue = new MTColor(12, 60, 60, 34);
		objects4.setObjectcolor(blue);
		objects4.setObjectposition(new Vector3D(200, 200));
		ModelMtAttributs mtattr5 = new ModelMtAttributs();
		mtattr5.setAttcolor(greypez1);
		mtattr5.setId(2);
		mtattr5.setLable("");
		ModelAttributContent attrbc5 = new ModelAttributContent();
		attrbc5.setType("String");
		attrbc5.setValue("");
		mtattr5.getAttributcontent().add(attrbc5);
		ModelMtAttributs mtattr6 = new ModelMtAttributs();
		mtattr6.setAttcolor(blue);
		mtattr6.setId(0);
		mtattr6.setLable("");
		ModelAttributContent attrbc6 = new ModelAttributContent();
		attrbc6.setType("String");
		attrbc6.setValue("");
		mtattr6.getAttributcontent().add(attrbc6);
		objects4.getObjectattributs().add(mtattr5);
		objects4.getObjectattributs().add(mtattr6);
		
		//Objecttyp 3
		ModelMtObjects objects5 = new ModelMtObjects();
		objects5.setObjecttyp(2);
		MTColor blue1 = new MTColor(112, 160, 60, 34);
		objects5.setObjectcolor(blue1);
		objects5.setObjectposition(new Vector3D(300, 200));
		ModelMtAttributs mtattr51 = new ModelMtAttributs();
		mtattr51.setAttcolor(greypez1);
		mtattr51.setId(2);
		mtattr51.setLable("");
		ModelAttributContent attrbc51 = new ModelAttributContent();
		attrbc51.setType("String");
		attrbc51.setValue("");
		mtattr51.getAttributcontent().add(attrbc51);
		ModelMtAttributs mtattr52 = new ModelMtAttributs();
		mtattr52.setAttcolor(blue1);
		mtattr52.setId(0);
		mtattr52.setLable("");
		ModelAttributContent attrbc52 = new ModelAttributContent();
		attrbc52.setType("String");
		attrbc52.setValue("");
		mtattr52.getAttributcontent().add(attrbc52);
		objects5.getObjectattributs().add(mtattr52);
		ModelMtAttributs mtattr53 = new ModelMtAttributs();
		mtattr53.setAttcolor(greypez1);
		mtattr53.setId(1);
		mtattr53.setLable("");
		ModelAttributContent attrbc53 = new ModelAttributContent();
		attrbc53.setType("String");
		attrbc53.setValue("");
		mtattr53.getAttributcontent().add(attrbc53);
		objects5.getObjectattributs().add(mtattr53);
		objects5.getObjectattributs().add(mtattr51);
		
		
		
		// Attribut id = 0 MTTextAttribut, id =1 MtNumeFiel, id =2 MtDropDown
		//ObjecttypDescription
		ModelTypDescription Desc1 = new ModelTypDescription();
		ModelTypDescription Desc2 = new ModelTypDescription();
		ModelTypDescription Desc3 = new ModelTypDescription();
		Desc1.setObjectypeid(0);
		Desc1.setObjectdescription("Object 1");
		Desc2.setObjectypeid(1);
		Desc2.setObjectdescription("Object 2");
		Desc3.setObjectypeid(3);
		Desc3.setObjectdescription("Object 3");
		
		
		objectetyps.getObjectdescription().add(Desc1);
		objectetyps.getObjectdescription().add(Desc2);
		objectetyps.getObjectdescription().add(Desc3);
		//Zusammenführen
		
		objectetyps.getObjecttyp().add(objects3);
		objectetyps.getObjecttyp().add(objects4);
		objectetyps.getObjecttyp().add(objects5);
		
		
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
		
		
		
		
		//objectetyps.getObjecttyp().add(objects3);
		//objectetyps.getObjecttyp().add(objects4);
		loadObjectXML();		
		return objectetyps;
	}

	public int getObjectcounter() {
		return objectcounter;
	}

	
	//GUI Object kopieren (Neues GUI Object / DatenModel Kopieren
	//Objecte Verknüpfen set links get links

}
