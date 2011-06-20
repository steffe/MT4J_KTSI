package ch.mitoco.dataController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mt4j.MTApplication;

import ch.mitoco.components.visibleComponents.MyMTObject;
import ch.mitoco.model.ModelFunctionList;
import ch.mitoco.model.ModelMtObjects;
import ch.mitoco.model.ModelScence;

import com.thoughtworks.xstream.XStream;


/**Loading a XML File which was generated with xstream and the MiToCo Datamodel
 * 
 * Erstellen des Scenen Datenmodels mithilfe iner bereits generierten XML Datei.
 * 
 * @author steffe
 *
 */
public class LoadXML {
	
	/**stores the Scene Data.
	 * 
	 */
	private ModelScence dataModel;
	
	/**Datenmodel für SceneListe
	 * 
	 */
	private ModelFunctionList sceneListe;
		
	/**
	 * 
	 */
	private List<MyMTObject> xmlmyobjectlist;
	
	/**
	 * 
	 */
	private MTApplication mtApplication;
	
	/**Constructor of loadXML, you have to provide it with the Filename which you want to load.
	 * You have to give the information about the MtApplication Instance
	 * This Class will generate all Object on the desired Scene and save it to the list xmlmyobjectlist
	 * 
	 * @param mtAppl MTApplication
	 * @param filename String
	 */
	@Deprecated
	public LoadXML(final MTApplication mtAppl, final String filename) {
		this.mtApplication = mtAppl;
		XStream xstream = new XStream();
		try {
			dataModel = (ModelScence) xstream.fromXML(new FileInputStream(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int counter = 0;
		xmlmyobjectlist = new ArrayList<MyMTObject>();
		for (Iterator<ModelMtObjects> it = dataModel.getMtobjects().iterator(); it.hasNext();) {
		//	xmlmyobjectlist.add(new MyMTObject(mtApplication, it.next(), counter));
			System.out.println("Object Gen:" + counter);
			counter++;
			
		}
		
		
			//System.out.println(DataModel.getMtobjects().get(0).getObjectattributs().get(0).getLable());
		
	}
	
	/**Loads the specified XML file.
	 * 
	 * loads the XML Content to the Scene dataModel
	 * 
	 * @param filename String
	 * @param typ String
	 *
	 *
	 *	 */
	public LoadXML(final String filename, final String typ) {
		XStream xstream = new XStream();
		
		if (typ.equals("objekttyp")){
			
		}
		else if (typ.equals("scenedata")){
			try {
				dataModel = (ModelScence) xstream.fromXML(new FileInputStream(filename));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		else if (typ.equals("sceneliste")){
			try {
				setSceneListe((ModelFunctionList) xstream.fromXML(new FileInputStream(filename)));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			
		}
		
		
	
	}
	/**Returns the Scene DataModel.
	 * 
	 * @return ModelScence
	 */
	public ModelScence getDataModel() {
		return dataModel;
	}
	
	/**For Store Data in the Scene DataModel.
	 * 
	 * @param dataModel ModelScence
	 */
	public void setDataModel(ModelScence dataModel) {
		this.dataModel = dataModel;
	}

	/**
	 * @param sceneListe the sceneListe to set
	 */
	public void setSceneListe(ModelFunctionList sceneListe) {
		this.sceneListe = sceneListe;
	}

	/**
	 * @return the sceneListe
	 */
	public ModelFunctionList getSceneListe() {
		return sceneListe;
	}

}
