package store;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mt4j.MTApplication;

import ch.mitoco.model.ModelMtObjects;
import ch.mitoco.model.ModelScence;

import com.thoughtworks.xstream.XStream;


/**Loading a XML File which was generated with xstream and the MiToCo Datamodel
 * 
 * @author steffe
 *
 */
public class LoadXML {
	
	/**
	 * 
	 */
	public ModelScence DataModel;
	
	/**
	 * 
	 */
	public List<MyMTObject> xmlmyobjectlist;
	
	/**
	 * 
	 */
	private List<Integer> tempAttributs;
	
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
	public LoadXML(final MTApplication mtAppl, final String filename) {
		this.mtApplication = mtAppl;
		XStream xstream = new XStream();
		try {
			DataModel = (ModelScence) xstream.fromXML(new FileInputStream(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int counter = 0;
		xmlmyobjectlist = new ArrayList<MyMTObject>();
		for (Iterator<ModelMtObjects> it = DataModel.getMtobjects().iterator(); it.hasNext();) {
			xmlmyobjectlist.add(new MyMTObject(mtApplication, it.next(), counter));
			System.out.println("Object Gen:" + counter);
			counter++;
			
		}
		
		
			//System.out.println(DataModel.getMtobjects().get(0).getObjectattributs().get(0).getLable());
		
	}
	public LoadXML(final String filename) {
		XStream xstream = new XStream();
		try {
			DataModel = (ModelScence) xstream.fromXML(new FileInputStream(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
