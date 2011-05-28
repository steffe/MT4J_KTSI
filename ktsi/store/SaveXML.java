package store;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import ch.mitoco.model.ModelScence;

import com.thoughtworks.xstream.XStream;
/**
 * 
 * @author steffe
 *
 */
public class SaveXML {
	/**
	 * 
	 * @param filename String
	 * @param dataModel ModelScence
	 */
	public SaveXML(final String filename, final ModelScence dataModel) {
		
		XStream xstreamSave = new XStream();
		
		//myobjectList.get(0).model.getObjectattributs().get(0).getAttributcontent().get(0).getValue()
		//dataModel.getMtobjects().add();
		
		try {
			xstreamSave.toXML(dataModel, new FileOutputStream(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
