package org.mt4jx.input.gestureAction.dnd.example.stack;

import org.mt4j.MTApplication;

public class StartComponentStackScene extends MTApplication {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public static void main(String args[]){
		initialize();
	}
	
	@Override
	public void startUp(){
		this.addScene(new ComponentStackScene(this, "Stack Tests"));
	}
	
}
