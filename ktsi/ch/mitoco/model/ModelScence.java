package ch.mitoco.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The Main Datamodel for the whole Scene. In this Model, all Data for the Objects
 * and the content will be saved
 * 
 * The Model contain all Objects and all Links between the Objects
 * @author steffe
 *
 */
public class ModelScence {
	
	 private long id;
	 private String name;
	 
	 /**List with the Object Datamodel	*/
	 private List<ModelMtObjects> mtobjects;
	 /**List with the Link Datamodel	*/
	 private List<ModelLink> mtlinks;
	 
	 	/**
	 	 * Construktor generats a Object and Link list
	 	 */
	    public ModelScence() {
	    	mtobjects = new ArrayList<ModelMtObjects>();
	    	setMtobjectlinks(new ArrayList<ModelLink>());
	    }

	    /**Identification Number of the Scene
	     * 
	     * @return Retursn the Scene ID
	     */
		public long getId() {
			return id;
		}
		
		/**Set the Identificationnumber of the Scene
		 * 
		 * @param id Identification of the Scene
		 */
		public void setId(long id) {
			this.id = id;
		}

		/**Get the Scene Name
		 * 
		 * @return name String
		 */
		public String getName() {
			return name;
		}

		/**Set the Scene Name
		 * 
		 * @param name String
		 */
		public void setName(String name) {
			this.name = name;
		}
		
		/**Returns a List from the Object DataModel
		 * 
		 * @return ModelMtObjects
		 */
		public List<ModelMtObjects> getMtobjects() {
			return mtobjects;
		}
		
		/**Set the Datamodel for the Objectlist
		 * 
		 * @param mtobjects
		 */
		public void setMtobjects(List<ModelMtObjects> mtobjects) {
			this.mtobjects = mtobjects;
		}

		/**Set the Objectlink Datamodel
		 * @param mtobjectlinks ModelObjectLink
		 */
		public void setMtobjectlinks(List<ModelLink> mtobjectlinks) {
			this.mtlinks = mtobjectlinks;
		}

		/**Retursn the Objectlink Datamodel
		 * @return the mtobjectlinks
		 */
		public List<ModelLink> getMtobjectlinks() {
			return mtlinks;
		}

}
