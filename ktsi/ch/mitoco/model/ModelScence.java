package ch.mitoco.model;

import java.util.ArrayList;
import java.util.List;

public class ModelScence {
	
	 private long id;
	 private String name;
	 private List<ModelMtObjects> mtobjects;
	 private List<ModelLink> mtlinks;
	 
	    public ModelScence() {
	    	mtobjects = new ArrayList<ModelMtObjects>();
	    	setMtobjectlinks(new ArrayList<ModelLink>());
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
		public void setMtobjectlinks(List<ModelLink> mtobjectlinks) {
			this.mtlinks = mtobjectlinks;
		}

		/**
		 * @return the mtobjectlinks
		 */
		public List<ModelLink> getMtobjectlinks() {
			return mtlinks;
		}

}
