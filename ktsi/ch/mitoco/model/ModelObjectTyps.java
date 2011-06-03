package ch.mitoco.model;

import java.util.ArrayList;
import java.util.List;

public class ModelObjectTyps {
	private List<ModelMtObjects> objecttyp;
	private List<ModelTypDescription> objectdescription;
	
	public ModelObjectTyps() {
		objecttyp = new ArrayList<ModelMtObjects>();
		setObjectdescription(new ArrayList<ModelTypDescription>());
	}
	
	/**
	 * @param objecttyp the objecttyp to set
	 */
	public void setObjecttyp(List<ModelMtObjects> objecttyp) {
		this.objecttyp = objecttyp;
	}

	/**
	 * @return the objecttyp
	 */
	public List<ModelMtObjects> getObjecttyp() {
		return objecttyp;
	}

	/**
	 * @param objectdescription the objectdescription to set
	 */
	public void setObjectdescription(List<ModelTypDescription> objectdescription) {
		this.objectdescription = objectdescription;
	}

	/**
	 * @return the objectdescription
	 */
	public List<ModelTypDescription> getObjectdescription() {
		return objectdescription;
	}

}
