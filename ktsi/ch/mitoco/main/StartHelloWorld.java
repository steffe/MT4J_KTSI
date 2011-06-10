package ch.mitoco.main;

import org.mt4j.MTApplication;

/**
 * StartHelloWorld Applikatione. 
 * 
 * @author mitoco
 *
 */
public class StartHelloWorld extends MTApplication {
	
	/** Serial Version UID.*/
	private static final long serialVersionUID = 1L;
	
	/**
	 * Main Function.
	 * @param args
	 */
	public static void main(String[] args) {
		initialize();
	}
	
	@Override
	public void startUp() {
		addScene(new HelloWorldScene(this, "Hello World Scene"));
	}
}
