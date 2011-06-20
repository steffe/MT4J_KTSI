package ch.mitoco.model;

import java.util.ArrayList;
import java.util.List;

public class ModelSceneList {
	private long id;
	private String scenename;
	private String description;
	private String picturepath;
	private List<ModelTypDescription> sceneobjekte;
	

	public ModelSceneList() {
		sceneobjekte = new ArrayList<ModelTypDescription>();
	}

}
