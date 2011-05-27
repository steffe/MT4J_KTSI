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


public class loadXML {

	public ModelScence DataModel;
	public List<MyMTObject> xmlmyobjectlist;
	private List<Integer> tempAttributs;
	private MTApplication mtApplication;
	
	
	public loadXML(final MTApplication mtAppl, String filename) {
		this.mtApplication = mtAppl;
		XStream xstream = new XStream();
		try {
			DataModel = (ModelScence) xstream.fromXML(new FileInputStream(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		tempAttributs = new ArrayList<Integer>();
		tempAttributs.add(0);
		tempAttributs.add(1);
		tempAttributs.add(2);
		*/
		int counter = 0;
		xmlmyobjectlist = new ArrayList<MyMTObject>();
		
		//	Author author = (Author) it.next();
		for (Iterator<ModelMtObjects> it = DataModel.getMtobjects().iterator(); it.hasNext();) {
			xmlmyobjectlist.add(new MyMTObject(mtApplication, it.next(), counter));
			System.out.println("Object Gen:" + counter);
			counter++;
			
		}
		
			//System.out.println(DataModel.getMtobjects().get(0).getObjectattributs().get(0).getLable());
		
	}

}
