package ch.mitoco.model;

import java.util.ArrayList;
import java.util.List;

public class ModelSceneList {
	private long id;
	private String scenename;
	private String description;
	private String picturepath;
	private List<ModelTypDescription> sceneobjekte;
	private String defautXML;
	private boolean showAll;
	

	public ModelSceneList() {
		sceneobjekte = new ArrayList<ModelTypDescription>();
	}


	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}


	/**
	 * @return the scenename
	 */
	public String getScenename() {
		return scenename;
	}


	/**
	 * @param scenename the scenename to set
	 */
	public void setScenename(String scenename) {
		this.scenename = scenename;
	}


	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	/**
	 * @return the picturepath
	 */
	public String getPicturepath() {
		return picturepath;
	}


	/**
	 * @param picturepath the picturepath to set
	 */
	public void setPicturepath(String picturepath) {
		this.picturepath = picturepath;
	}


	/**
	 * @return the sceneobjekte
	 */
	public List<ModelTypDescription> getSceneobjekte() {
		return sceneobjekte;
	}


	/**
	 * @param sceneobjekte the sceneobjekte to set
	 */
	public void setSceneobjekte(List<ModelTypDescription> sceneobjekte) {
		this.sceneobjekte = sceneobjekte;
	}


	/**
	 * @return the defautXML
	 */
	public String getDefautXML() {
		return defautXML;
	}


	/**
	 * @param defautXML the defautXML to set
	 */
	public void setDefautXML(String defautXML) {
		this.defautXML = defautXML;
	}


	/**
	 * @param showAll the showAll to set
	 */
	public void setShowAll(boolean showAll) {
		this.showAll = showAll;
	}


	/**
	 * @return the showAll
	 */
	public boolean getShowAll() {
		return showAll;
	}

}
