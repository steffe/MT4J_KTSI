package store;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import ch.mitoco.model.ModelObjectTyps;

import com.thoughtworks.xstream.XStream;

public class LoadObjectXML {
	private ModelObjectTyps ObjectModel;
	
	public LoadObjectXML() {
		XStream xstream = new XStream();
		try {
			setObjectModel(((ModelObjectTyps) xstream.fromXML(new FileInputStream("objects.xml"))));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param objectModel the objectModel to set
	 */
	public void setObjectModel(ModelObjectTyps objectModel) {
		ObjectModel = objectModel;
	}

	/**
	 * @return the objectModel
	 */
	public ModelObjectTyps getObjectModel() {
		return ObjectModel;
	}

}
