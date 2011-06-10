package ch.mitoco.model;

import java.util.ArrayList;
import java.util.List;

public class ModelScence {
	
	 private long id;
	 private String name;
	 private List<ModelMtObjects> mtobjects;
	 private List<ModelObjectLink> mtobjectlinks;
	 
	    public ModelScence() {
	    	mtobjects = new ArrayList<ModelMtObjects>();
	    	setMtobjectlinks(new ArrayList<ModelObjectLink>());
	    }

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<ModelMtObjects> getMtobjects() {
			return mtobjects;
		}

		public void setMtobjects(List<ModelMtObjects> mtobjects) {
			this.mtobjects = mtobjects;
		}

		/**
		 * @param mtobjectlinks ModelObjectLink
		 */
		public void setMtobjectlinks(List<ModelObjectLink> mtobjectlinks) {
			this.mtobjectlinks = mtobjectlinks;
		}

		/**
		 * @return the mtobjectlinks
		 */
		public List<ModelObjectLink> getMtobjectlinks() {
			return mtobjectlinks;
		}

}
