package ch.mitoco.main;

import org.mt4j.MTApplication;

import ch.mitoco.model.ModelSceneList;

/**
 * StartHelloWorld Applikatione. 
 * 
 * @author mitoco
 *
 */
public class StartMitocoMain extends MTApplication {
	
	/** Serial Version UID.*/
	private static final long serialVersionUID = 1L;
	
	/** Spezifische ScenenDaten	 */
	private ModelSceneList sceneData;
	
	/**
	 * Main Function.
	 * @param args
	 */
	public static void main(String[] args) {
		initialize();
	}
	
	@Override
	public void startUp() {
		sceneData = new ModelSceneList();
		sceneData.setScenename("MiToCo");
		addScene(new MitocoScene(this, "Hello World Scene", sceneData));
	}
}
