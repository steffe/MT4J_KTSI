package store;

import org.mt4j.MTApplication;

import ch.mitoco.model.ModelScence;

public class DataController {
	public LoadXML LoadXML;
	
	private ModelScence dataModel;
	
	private MTApplication mtApplication;

	public DataController(final MTApplication mtAppl) {
		this.mtApplication = mtAppl;
	}
	
	public void createObject() {
		
	}
	
	public void loadScene() {
		
	}
	
	public void loadObject() {
		
	}
	
	public ModelScence createDataModel() {
		dataModel = new ModelScence();
		return dataModel;
	}
	
	public void loadSceneXML() {
		LoadXML = new LoadXML("xstream.xml");
		 
	}
	
	public void loadObjectXML() {
		
	}
	
	//GUI Object kopieren (Neues GUI Object / DatenModel Kopieren
	//Delete GUI Object (Datenmodel)
	//Objecte Verknüpfen set links get links

}
