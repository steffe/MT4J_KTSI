package store;

import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import ch.mitoco.model.ModelAttributContent;
import ch.mitoco.model.ModelMtAttributs;
import ch.mitoco.model.ModelMtObjects;
import ch.mitoco.model.ModelScence;


/**This Class will generate a ExampleXML File, serialized by xstream.
 * 
 * @author steffe
 *
 */
public class ExampleXML {
	/**a static Object of the Datamodel.
	 * 
	 */
	public static ModelScence MODELSCENCE;
	
	/**Generation of the Datamodel for xml seralization.
	 * 
	 */
	public ExampleXML() {
		
		MODELSCENCE = new ModelScence();
		MODELSCENCE.setId(1);
		ModelMtObjects objects1 = new ModelMtObjects();
		ModelMtObjects objects2 = new ModelMtObjects();
		ModelMtObjects objects3 = new ModelMtObjects();
		objects1.setId(1);
		objects2.setId(2);
		objects3.setId(3);
		
		MTColor greypez = new MTColor(12, 12, 12, 34);
		objects1.setObjectcolor(greypez);
		objects2.setObjectcolor(greypez);
			
		//objects1.setObjectfont(new IFont());
		objects1.setObjectposition(new Vector3D(100, 200));
		objects1.setObjectposition(new Vector3D(200, 200));
		
		ModelMtAttributs mtattr1 = new ModelMtAttributs();
		ModelMtAttributs mtattr2 = new ModelMtAttributs();
		mtattr1.setAttcolor(greypez);
		mtattr1.setId(2);
		mtattr1.setLable("Name");
		
		mtattr2.setAttcolor(greypez);
		mtattr2.setId(2);
		mtattr2.setLable("Name");
		
		ModelAttributContent attrbc1 = new ModelAttributContent();
		attrbc1.setType("String");
		attrbc1.setValue("Stefan");
		mtattr1.getAttributcontent().add(attrbc1);
		
		ModelAttributContent attrbc2 = new ModelAttributContent();
		attrbc2.setType("String");
		attrbc2.setValue("Roman");
		mtattr2.getAttributcontent().add(attrbc2);
		
		ModelAttributContent attrb3 = new ModelAttributContent();
		
		objects1.getObjectattributs().add(mtattr1);
		objects2.getObjectattributs().add(mtattr2);
		
		MODELSCENCE.getMtobjects().add(objects1);
		MODELSCENCE.getMtobjects().add(objects2);
		MODELSCENCE.getMtobjects().add(objects3);
	}
	/** Getter Method to return the DataModel.
	 * 
	 * @return MODELSCENCE
	 */
	public final ModelScence getObject() {
		
		
		return MODELSCENCE;
	}

		
	
	
}
