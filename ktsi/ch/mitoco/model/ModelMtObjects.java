package ch.mitoco.model;

import java.util.ArrayList;
import java.util.List;

import org.mt4j.util.MTColor;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;
/**Datamodel for the Object on a Scene.
 * Contains a List for all Attributs.
 * 
 * The Objecttype ID is very Important for other Classes
 * @author steffe
 *
 */
public class ModelMtObjects {
	/**Identification Number for the Object. */
	private long id;
	/**IMPORTENT The Objecttyp Number is defined in a other List. */
	private int objecttyp;
	/**Color of the Object. */
	private MTColor objectcolor;
	/**Line Color of the Object. */
	private MTColor objectlinecolor;
	/**Fillcolor of the Object. */
	private MTColor objectFillcolor;
	/**Fonttyp of the Attributs in the Object. */
	private IFont objectfont;
	/**Fonttyp of the Objectlabel. */
	private IFont labelfont;
	/**List of the Datamodel for all Attributs in this Object. */
	private List<ModelMtAttributs> objectattributs;
	/**Posision of the Object on a Scene. */
	private Vector3D objectposition;
	/**Name of the Object. */
	private String objectlable;
	/**Direction of the content.	 */
	private boolean direction;
	/**Size of the Object.	 */
	private int zoom;
	
	
	/**Datamodel of the Object.
	 * 
	 */
	public ModelMtObjects(){
		objectattributs = new ArrayList<ModelMtAttributs>();
	}

	/**
	 * 
	 * @return
	 */
	public long getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 
	 * @return objectcolor
	 */
	public MTColor getObjectcolor() {
		return objectcolor;
	}

	/**
	 * 
	 * @param objectcolor
	 */
	public void setObjectcolor(MTColor objectcolor) {
		this.objectcolor = objectcolor;
	}

	/**
	 * 
	 * @return objectfont
	 */
	public IFont getObjectfont() {
		return objectfont;
	}

	/**
	 * 
	 * @param objectfont
	 */
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


	/**
	 * @param direction the direction to set
	 */
	public void setDirection(boolean direction) {
		this.direction = direction;
	}


	/**
	 * @return the direction
	 */
	public boolean isDirection() {
		return direction;
	}


	/**
	 * @param zoom the zoom to set
	 */
	public void setZoom(int zoom) {
		this.zoom = zoom;
	}


	/**
	 * @return the zoom
	 */
	public int getZoom() {
		return zoom;
	}

}
