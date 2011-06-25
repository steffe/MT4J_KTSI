package ch.mitoco.dataController;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import ch.mitoco.model.ModelFunctionList;
import ch.mitoco.model.ModelObjectTyps;
import ch.mitoco.model.ModelScence;

import com.thoughtworks.xstream.XStream;

/**Speichern des Datenmodels einer Scene in ein XML File.
 * 
 * Diese Klasse Speichert das Datenmodel einer Scene in ein XML mittels xstream.
 * To-DO: Speichern von ObjektTypen in einem Seperaten XML.
 * 
 * @author steffe
 *
 */
public class SaveXML {
	private ModelScence saveModel;
	private ModelObjectTyps saveObjectModel;
	
	/**Datenmodel für SceneListe
	 * 
	 */
	private ModelFunctionList saveSceneListe;
	
	/**Konstruktor SaveXML
	 * 
	 * 
	 * 
	 * @param filename String wie der Dateiname lauten soll.
	 * @param dataModel ModelScence Datenmodel welches in das XML serialisiert wird
	 */
	public SaveXML(final String filename, final ModelScence dataModel) {
		this.saveModel = dataModel;
		XStream xstreamSave = new XStream();
		
		//myobjectList.get(0).model.getObjectattributs().get(0).getAttributcontent().get(0).getValue()
		//dataModel.getMtobjects().add();
		
		try {
			xstreamSave.toXML(saveModel, new FileOutputStream(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**Konstruktor für SceneListe Speichern
	 * 
	 * @param filename
	 * @param saveSceneListe
	 */
	public SaveXML(final String filename, final ModelFunctionList saveSceneListe) {
		this.saveSceneListe = saveSceneListe;
		XStream xstreamSave = new XStream();
			
			//myobjectList.get(0).model.getObjectattributs().get(0).getAttributcontent().get(0).getValue()
			//dataModel.getMtobjects().add();
			
		try {
			xstreamSave.toXML(saveSceneListe, new FileOutputStream(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		
	}
	
	public SaveXML(final String filename){
		
		XStream xstreamSave = new XStream();
		
		//myobjectList.get(0).model.getObjectattributs().get(0).getAttributcontent().get(0).getValue()
		//dataModel.getMtobjects().add();
		ExampleXML test = new ExampleXML();
		try {
			xstreamSave.toXML(test.getObjectType(), new FileOutputStream(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
