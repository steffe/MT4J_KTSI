package ch.mitoco.model;

import java.util.ArrayList;
import java.util.List;

public class ModelFunctionList {
	private List<ModelSceneList> sceneliste;
	
	public ModelFunctionList(){
		sceneliste = new ArrayList<ModelSceneList>();
		
	}

	/**
	 * @return the sceneliste
	 */
	public List<ModelSceneList> getSceneliste() {
		return sceneliste;
	}

	/**
	 * @param sceneliste the sceneliste to set
	 */
	public void setSceneliste(List<ModelSceneList> sceneliste) {
		this.sceneliste = sceneliste;
	}

}
