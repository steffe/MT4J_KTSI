package ch.mitoco.model;

import java.util.ArrayList;
import java.util.List;

import org.mt4j.util.MTColor;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

public class ModelMtObjects {
	private long id;
	private int objecttyp;
	private MTColor objectcolor;
	private MTColor objectlinecolor;
	private MTColor objectFillcolor;
	private IFont objectfont;
	private IFont labelfont;
	private List<ModelMtAttributs> objectattributs;
	private Vector3D objectposition;
	private String objectlable;
	
	
	
	public ModelMtObjects(){
		objectattributs = new ArrayList<ModelMtAttributs>();
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public MTColor getObjectcolor() {
		return objectcolor;
	}


	public void setObjectcolor(MTColor objectcolor) {
		this.objectcolor = objectcolor;
	}


	public IFont getObjectfont() {
		return objectfont;
	}


	public void setObjectfont(IFont objectfont) {
		this.objectfont = objectfont;
	}


	/**
	 * @param labelfont the labelfont to set
	 */
	public void setLabelfont(IFont labelfont) {
		this.labelfont = labelfont;
	}


	/**
	 * @return the labelfont
	 */
	public IFont getLabelfont() {
		return labelfont;
	}


	public List<ModelMtAttributs> getObjectattributs() {
		return objectattributs;
	}


	public void setObjectattributs(List<ModelMtAttributs> objectattributs) {
		this.objectattributs = objectattributs;
	}


	public void setObjectposition(Vector3D objectposition) {
		this.objectposition = objectposition;
	}


	public Vector3D getObjectposition() {
		return objectposition;
	}


	/**
	 * @param objectlinecolor the objectlinecolor to set
	 */
	public void setObjectlinecolor(MTColor objectlinecolor) {
		this.objectlinecolor = objectlinecolor;
	}


	/**
	 * @return the objectlinecolor
	 */
	public MTColor getObjectlinecolor() {
		return objectlinecolor;
	}


	/**
	 * @param objectFillcolor the objectFillcolor to set
	 */
	public void setObjectFillcolor(MTColor objectFillcolor) {
		this.objectFillcolor = objectFillcolor;
	}


	/**
	 * @return the objectFillcolor
	 */
	public MTColor getObjectFillcolor() {
		return objectFillcolor;
	}


	/**
	 * @param objecttyp the objecttyp to set
	 */
	public void setObjecttyp(int objecttyp) {
		this.objecttyp = objecttyp;
	}


	/**
	 * @return the objecttyp
	 */
	public int getObjecttyp() {
		return objecttyp;
	}


	/**
	 * @param objectlable the objectlable to set
	 */
	public void setObjectlable(String objectlable) {
		this.objectlable = objectlable;
	}


	/**
	 * @return the objectlable
	 */
	public String getObjectlable() {
		return objectlable;
	}

}
