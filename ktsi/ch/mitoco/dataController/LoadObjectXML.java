package ch.mitoco.dataController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import ch.mitoco.model.ModelObjectTyps;

import com.thoughtworks.xstream.XStream;

/**Laden von Objektypen aus einem XML.
 * 
 * Diese Klasse liest Objekttemplates aus einem XML und speichert diese in dem Datenmodel ab.
 * 
 * @author steffe
 *
 */
public class LoadObjectXML {
	/**
	 * 
	 */
	private ModelObjectTyps ObjectModel;
	
	/**Konstruktor um eine spezifiziertes XML für die Objekttypen zu laden.
	 * 
	 */
	public LoadObjectXML() {
		XStream xstream = new XStream();
		try {
			setObjectModel(((ModelObjectTyps) xstream.fromXML(new FileInputStream("objectlist1.xml"))));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**Setzt das Datenmodel der Objettypen
	 * @param objectModel the objectModel to set
	 */
	public void setObjectModel(ModelObjectTyps objectModel) {
		ObjectModel = objectModel;
	}

	/**Gibt die Objekttypen aus dem XML zurück.
	 * @return the objectModel
	 */
	public ModelObjectTyps getObjectModel() {
		return ObjectModel;
	}

}
