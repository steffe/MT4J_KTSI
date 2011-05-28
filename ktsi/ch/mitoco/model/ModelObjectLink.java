package ch.mitoco.model;

import java.util.ArrayList;
import java.util.List;

public class ModelObjectLink {
	private int firstObject;
	private int secondObject;
	private List<ModelLinkDescription> objectlinkdescription;
	
	public ModelObjectLink() {
		setObjectlinkdescription(new ArrayList<ModelLinkDescription>());
				
	}

	public void setFirstObject(int firstObject) {
		this.firstObject = firstObject;
	}

	public int getFirstObject() {
		return firstObject;
	}

	public void setSecondObject(int secondObject) {
		this.secondObject = secondObject;
	}

	public int getSecondObject() {
		return secondObject;
	}

	public void setObjectlinkdescription(List<ModelLinkDescription> objectlinkdescription) {
		this.objectlinkdescription = objectlinkdescription;
	}

	public List<ModelLinkDescription> getObjectlinkdescription() {
		return objectlinkdescription;
	}

}
