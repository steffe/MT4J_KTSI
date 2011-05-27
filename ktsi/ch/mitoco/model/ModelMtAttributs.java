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
	//attributdefinition
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
	/**
	 * 
	 */
	private int textlenght;
	/**
	 * 
	 */
	private boolean direction;
	/**
	 * 
	 */
	private int zoom;
	
	/**ModelMtAttributs Konstruktor.
	 * Set the ArrayList for AttributContent and AttributDefinition
	 */
	public ModelMtAttributs() {
		
		attributcontent = new ArrayList<ModelAttributContent>();
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
	 * 
	 * 
	 */
	public final void setTextlenght(final int textlenght) {
		this.textlenght = textlenght;
	}
	/**
	 * 
	 * @return
	 */
	public final int getTextlenght() {
		return textlenght;
	}

	/**
	 * 
	 * @param direction
	 */
	public final void setDirection(final boolean direction) {
		this.direction = direction;
	}

	/**
	 * 
	 * @return
	 */
	public final boolean isDirection() {
		return direction;
	}
	
	/**
	 * 
	 * @param zoom
	 */
	public final void setZoom(final int zoom) {
		this.zoom = zoom;
	}

	/**
	 * 
	 * @return
	 */
	public final int getZoom() {
		return zoom;
	}

}
