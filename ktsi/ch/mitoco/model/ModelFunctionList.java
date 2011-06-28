package ch.mitoco.model;

import java.util.ArrayList;
import java.util.List;

public class ModelFunctionList {
	private List<ModelSceneList> sceneliste;
	private String exportPath;
	
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

	/**
	 * @param exportPath the exportPath to set
	 */
	public void setExportPath(String exportPath) {
		this.exportPath = exportPath;
	}

	/**
	 * @return the exportPath
	 */
	public String getExportPath() {
		return exportPath;
	}

}
