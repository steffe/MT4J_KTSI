package ch.mitoco.model;

import java.util.ArrayList;
import java.util.List;

import org.mt4j.util.MTColor;

public class ModelMtAttributs {
	private int id;
	private List<ModelAttributContent> attributcontent;
	private MTColor attcolor;
	private String Lable;
	private boolean minMax;
	private int textlenght;
	
	public ModelMtAttributs(){
		
		attributcontent = new ArrayList<ModelAttributContent>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<ModelAttributContent> getAttributcontent() {
		return attributcontent;
	}

	public void setAttributcontent(List<ModelAttributContent> attributcontent) {
		this.attributcontent = attributcontent;
	}

	public MTColor getAttcolor() {
		return attcolor;
	}

	public void setAttcolor(MTColor attcolor) {
		this.attcolor = attcolor;
	}

	public String getLable() {
		return Lable;
	}

	public void setLable(String lable) {
		Lable = lable;
	}

	public boolean isMinMax() {
		return minMax;
	}

	public void setMinMax(boolean minMax) {
		this.minMax = minMax;
	}

	public void setTextlenght(int textlenght) {
		this.textlenght = textlenght;
	}

	public int getTextlenght() {
		return textlenght;
	}

}
