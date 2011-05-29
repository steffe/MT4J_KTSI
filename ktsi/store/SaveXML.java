package store;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import ch.mitoco.model.ModelObjectTyps;
import ch.mitoco.model.ModelScence;

import com.thoughtworks.xstream.XStream;
/**
 * 
 * @author steffe
 *
 */
public class SaveXML {
	private ModelScence saveModel;
	private ModelObjectTyps saveObjectModel;
	
	/**
	 * 
	 * @param filename String
	 * @param dataModel ModelScence
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

}
