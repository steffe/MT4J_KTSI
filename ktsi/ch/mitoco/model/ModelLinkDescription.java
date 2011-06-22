package ch.mitoco.model;

import org.mt4j.util.MTColor;

public class ModelLinkDescription {
	private String linkdescription;
	private String linkvalue;
	private MTColor linkcolor;
	
	public ModelLinkDescription() {
		
	}

	public void setLinkdescription(String linkdescription) {
		this.linkdescription = linkdescription;
	}

	public String getLinkdescription() {
		return linkdescription;
	}

	public void setLinkvalue(String linkvalue) {
		this.linkvalue = linkvalue;
	}

	public String getLinkvalue() {
		return linkvalue;
	}

	/**
	 * @param linkcolor the linkcolor to set
	 */
	public void setLinkcolor(MTColor linkcolor) {
		this.linkcolor = linkcolor;
	}

	/**
	 * @return the linkcolor
	 */
	public MTColor getLinkcolor() {
		return linkcolor;
	}

}
