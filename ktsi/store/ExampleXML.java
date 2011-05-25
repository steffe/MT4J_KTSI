package store;

import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import ch.mitoco.model.ModelAttributContent;
import ch.mitoco.model.ModelMtAttributs;
import ch.mitoco.model.ModelMtObjects;
import ch.mitoco.model.ModelScence;



public class ExampleXML {
	public static ModelScence MODELSCENCE;
	
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
			
		//objects1.setObjectfont(new IFont());
		objects1.setObjectposition(new Vector3D(100, 200));
		
		ModelMtAttributs mtattr1 = new ModelMtAttributs();
		mtattr1.setAttcolor(greypez);
		mtattr1.setId(2);
		mtattr1.setLable("Name");
		
		ModelAttributContent attrbc1 = new ModelAttributContent();
		attrbc1.setType("String");
		attrbc1.setValue("Stefan");
		mtattr1.getAttributcontent().add(attrbc1);
		
		ModelAttributContent attrb2 = new ModelAttributContent();
		ModelAttributContent attrb3 = new ModelAttributContent();
		
		objects1.getObjectattributs().add(mtattr1);
		MODELSCENCE.getMtobjects().add(objects1);
		MODELSCENCE.getMtobjects().add(objects2);
		MODELSCENCE.getMtobjects().add(objects3);
	}
	public final ModelScence getObject() {
		
		
		return MODELSCENCE;
	}

}
