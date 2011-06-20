package ch.mitoco.startmenu;

import org.mt4j.MTApplication;

/**
 * 
 * @author steffe
 *
 */
public class StartMitoco extends MTApplication {
	private static final long serialVersionUID = 1L;

	public static void main(String args[]){
		initialize();
	}
	
	@Override
	public void startUp(){
		this.addScene(new SceneMitoco(this, "MiCoCo"));
	}
	
}