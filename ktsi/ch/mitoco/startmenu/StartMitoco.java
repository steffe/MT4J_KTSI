package ch.mitoco.startmenu;

import org.mt4j.MTApplication;

/**Initialize the start of the Menuchooser.
 * 
 * @author steffe
 *
 */
public class StartMitoco extends MTApplication {
	private static final long serialVersionUID = 1L;

	/**Main Method wicht inizialize the startUp() Method.
	 * 
	 * @param args
	 */
	public static void main(String args[]){
		initialize();
	}
	
	/**Overrides the startUp, with the defined Scene witch you want to start.
	 * 
	 */
	@Override
	public void startUp(){
		this.addScene(new SceneMitoco(this, "MiCoCo"));
	}
	
}