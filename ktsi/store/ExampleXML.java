package store;

import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import ch.mitoco.model.ModelAttributContent;
import ch.mitoco.model.ModelMtAttributs;
import ch.mitoco.model.ModelMtObjects;
import ch.mitoco.model.ModelObjectTyps;
import ch.mitoco.model.ModelScence;
import ch.mitoco.model.ModelTypDescription;


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
	
	/**A Object witch describe all possible Object on a Scene.
	 * 
	 */
	private ModelObjectTyps objectype;
	
	/**Generation of a test the Datamodel for xml seralization.
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
	

	/**Returns 3 Example Objecttyps.
	 * 
	 * For testing with already created Objecttyps
	 * 
	 * @return ModelObjectTyps
	 */
	public final ModelObjectTyps getObjectType() {
		
		objectype = new ModelObjectTyps();
		
		ModelMtObjects objects3 = new ModelMtObjects();
		objects3.setObjecttyp(0);
		MTColor greypez1 = new MTColor(12, 12, 12, 34);
		objects3.setObjectcolor(greypez1);
		objects3.setObjectposition(new Vector3D(200, 200));
		ModelMtAttributs mtattr2 = new ModelMtAttributs();
		mtattr2.setAttcolor(greypez1);
		mtattr2.setId(0);
		mtattr2.setLable("");
		ModelAttributContent attrbc2 = new ModelAttributContent();
		attrbc2.setType("String");
		attrbc2.setValue("");
		mtattr2.getAttributcontent().add(attrbc2);
		ModelMtAttributs mtattr3 = new ModelMtAttributs();
		mtattr3.setAttcolor(greypez1);
		mtattr3.setId(1);
		mtattr3.setLable("");
		ModelAttributContent attrbc3 = new ModelAttributContent();
		attrbc3.setType("String");
		attrbc3.setValue("");
		mtattr3.getAttributcontent().add(attrbc3);
		objects3.getObjectattributs().add(mtattr2);
		objects3.getObjectattributs().add(mtattr3);
		
		
		//Objecttyp 2
		ModelMtObjects objects4 = new ModelMtObjects();
		objects4.setObjecttyp(1);
		MTColor blue = new MTColor(12, 60, 60, 34);
		objects4.setObjectcolor(blue);
		objects4.setObjectposition(new Vector3D(200, 200));
		ModelMtAttributs mtattr5 = new ModelMtAttributs();
		mtattr5.setAttcolor(greypez1);
		mtattr5.setId(2);
		mtattr5.setLable("");
		ModelAttributContent attrbc5 = new ModelAttributContent();
		attrbc5.setType("String");
		attrbc5.setValue("");
		mtattr5.getAttributcontent().add(attrbc5);
		ModelMtAttributs mtattr6 = new ModelMtAttributs();
		mtattr6.setAttcolor(blue);
		mtattr6.setId(0);
		mtattr6.setLable("");
		ModelAttributContent attrbc6 = new ModelAttributContent();
		attrbc6.setType("String");
		attrbc6.setValue("");
		mtattr6.getAttributcontent().add(attrbc6);
		objects4.getObjectattributs().add(mtattr5);
		objects4.getObjectattributs().add(mtattr6);
		
		//Objecttyp 3
		ModelMtObjects objects5 = new ModelMtObjects();
		objects5.setObjecttyp(2);
		MTColor blue1 = new MTColor(112, 160, 60, 34);
		objects5.setObjectcolor(blue1);
		objects5.setObjectposition(new Vector3D(300, 200));
		ModelMtAttributs mtattr51 = new ModelMtAttributs();
		mtattr51.setAttcolor(greypez1);
		mtattr51.setId(2);
		mtattr51.setLable("");
		ModelAttributContent attrbc51 = new ModelAttributContent();
		attrbc51.setType("String");
		attrbc51.setValue("");
		mtattr51.getAttributcontent().add(attrbc51);
		ModelMtAttributs mtattr52 = new ModelMtAttributs();
		mtattr52.setAttcolor(blue1);
		mtattr52.setId(0);
		mtattr52.setLable("");
		ModelAttributContent attrbc52 = new ModelAttributContent();
		attrbc52.setType("String");
		attrbc52.setValue("");
		mtattr52.getAttributcontent().add(attrbc52);
		objects5.getObjectattributs().add(mtattr52);
		ModelMtAttributs mtattr53 = new ModelMtAttributs();
		mtattr53.setAttcolor(greypez1);
		mtattr53.setId(1);
		mtattr53.setLable("");
		ModelAttributContent attrbc53 = new ModelAttributContent();
		attrbc53.setType("String");
		attrbc53.setValue("");
		mtattr53.getAttributcontent().add(attrbc53);
		objects5.getObjectattributs().add(mtattr53);
		objects5.getObjectattributs().add(mtattr51);
		
		
		
		// Attribut id = 0 MTTextAttribut, id =1 MtNumeFiel, id =2 MtDropDown
		//ObjecttypDescription
		ModelTypDescription Desc1 = new ModelTypDescription();
		ModelTypDescription Desc2 = new ModelTypDescription();
		ModelTypDescription Desc3 = new ModelTypDescription();
		Desc1.setObjectypeid(0);
		Desc1.setObjectdescription("Object 1");
		Desc2.setObjectypeid(1);
		Desc2.setObjectdescription("Object 2");
		Desc3.setObjectypeid(3);
		Desc3.setObjectdescription("Object 3");
		
		
		objectype.getObjectdescription().add(Desc1);
		objectype.getObjectdescription().add(Desc2);
		objectype.getObjectdescription().add(Desc3);
		//Zusammenführen
		
		objectype.getObjecttyp().add(objects3);
		objectype.getObjecttyp().add(objects4);
		objectype.getObjecttyp().add(objects5);
		
		
		return objectype;
	}
	/** Getter Method to return the DataModel.
	 * 
	 * @return MODELSCENCE
	 */
	public final ModelScence getObject() {
		
		
		return MODELSCENCE;
	}

		
	
	
}
