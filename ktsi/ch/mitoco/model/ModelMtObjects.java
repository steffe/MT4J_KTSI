package ch.mitoco.model;

import java.util.ArrayList;
import java.util.List;

import org.mt4j.util.MTColor;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

public class ModelMtObjects {
	private long id;
	private MTColor objectcolor;
	private IFont objectfont;
	private List<ModelMtAttributs> objectattributs;
	private Vector3D objectposition;
	
	
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

}
