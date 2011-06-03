package org.mt4jx.input.gestureAction.dnd.example;

import org.mt4j.MTApplication;

public class StartDnDExample extends MTApplication {
	
	public static void main(String[] args) {
		initialize();
	}
	@Override
	public void startUp() {
		addScene(new DnDScene(this, "DnD Scene"));
	}

}
