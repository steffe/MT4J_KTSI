package ch.mitoco.model;

import java.util.ArrayList;
import java.util.List;

import org.mt4j.util.MTColor;

/**
 * 
 * @author steffe
 *
 */
public class ModelMtAttributs {
	/**
	 * 
	 */
	private int id;
	/**
	 * 
	 */
	private List<ModelAttributContent> attributcontent;
	
	/**
	 * 
	 */
	private List<ModelAttributDefinition> attributdefinition;
	
	/**
	 * 
	 */
	private MTColor attcolor;
	/**
	 * 
	 */
	private String Lable;
	/**
	 * 
	 */
	private boolean minMax;
	

	
	/**Diese Variable definiert die Höhe eines einzelnen Attributs. */
	private int attributHight;
	
	/**ModelMtAttributs Konstruktor.
	 * Set the ArrayList for AttributContent and AttributDefinition
	 */
	public ModelMtAttributs() {
		
		attributcontent = new ArrayList<ModelAttributContent>();
		attributdefinition = new ArrayList<ModelAttributDefinition>();
	}
	/**Returns the Numeric Identification of this Attribute.
	 *  
	 * @return int id
	 */
	public final int getId() {
		return id;
	}
	
	/**
	 * 
	 * @return
	 */
	public final void setId(final int id) {
		this.id = id;
	}
	
	/**
	 * 
	 * @return
	 */
	public final List<ModelAttributContent> getAttributcontent() {
		return attributcontent;
	}
	
	/**
	 * 
	 * @return
	 */
	public final void setAttributcontent(final List<ModelAttributContent> attributcontent) {
		this.attributcontent = attributcontent;
	}
	
	/**
	 * 
	 * @return
	 */
	public final MTColor getAttcolor() {
		return attcolor;
	}
	
	/**
	 * 
	 * @return
	 */
	public final void setAttcolor(final MTColor attcolor) {
		this.attcolor = attcolor;
	}
	
	/**
	 * 
	 * @return
	 */
	public final String getLable() {
		return Lable;
	}
	
	/**
	 * 
	 * @return
	 */
	public final void setLable(final String lable) {
		Lable = lable;
	}

	/**
	 * 
	 * @return
	 */
	public final boolean isMinMax() {
		return minMax;
	}
	
	/**
	 * 
	 * @return
	 */
	public final void setMinMax(final boolean minMax) {
		this.minMax = minMax;
	}
	/**
	 * @param attributdefinition the attributdefinition to set
	 */
	public void setAttributdefinition(List<ModelAttributDefinition> attributdefinition) {
		this.attributdefinition = attributdefinition;
	}
	/**
	 * @return the attributdefinition
	 */
	public List<ModelAttributDefinition> getAttributdefinition() {
		return attributdefinition;
	}
	/**
	 * @param attributHight the attributHight to set
	 */
	public void setAttributHight(int attributHight) {
		this.attributHight = attributHight;
	}
	/**
	 * @return the attributHight
	 */
	public int getAttributHight() {
		return attributHight;
	}

}
