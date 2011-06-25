package ch.mitoco.model;

import java.util.ArrayList;

public class ModelTypDescription {
	private int objectypeid;
	private String objectdescription;
	private int kardinalitaet;
	private ArrayList<Integer> objectdenylink;
	
	public ModelTypDescription(){
		setObjectdenylink(new ArrayList<Integer>());
	}
	
	/**
	 * @param objectypeid the objectypeid to set
	 */
	public void setObjectypeid(int objectypeid) {
		this.objectypeid = objectypeid;
	}
	/**
	 * @return the objectypeid
	 */
	public int getObjectypeid() {
		return objectypeid;
	}
	/**
	 * @param objectdescription the objectdescription to set
	 */
	public void setObjectdescription(String objectdescription) {
		this.objectdescription = objectdescription;
	}
	/**
	 * @return the objectdescription
	 */
	public String getObjectdescription() {
		return objectdescription;
	}

	/**
	 * @param objectdenylink the objectdenylink to set
	 */
	public void setObjectdenylink(ArrayList<Integer> objectdenylink) {
		this.objectdenylink = objectdenylink;
	}

	/**
	 * @return the objectdenylink
	 */
	public ArrayList<Integer> getObjectdenylink() {
		return objectdenylink;
	}

	/**
	 * @param kardinalitaet the kardinalitaet to set
	 */
	public void setKardinalitaet(int kardinalitaet) {
		this.kardinalitaet = kardinalitaet;
	}

	/**
	 * @return the kardinalitaet
	 */
	public int getKardinalitaet() {
		return kardinalitaet;
	}

}
