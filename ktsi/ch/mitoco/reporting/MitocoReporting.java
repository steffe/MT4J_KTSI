package ch.mitoco.reporting;

import org.mt4j.AbstractMTApplication;
import org.mt4j.sceneManagement.AbstractScene;

import ch.mitoco.main.MitocoScene;

public class MitocoReporting extends AbstractScene{
	
	private MitocoScene scene;

	public MitocoReporting(AbstractMTApplication mtApplication, String name, MitocoScene scene) {
		super(mtApplication, name);
		this.scene = scene;
		scene.getGuiOverlay();
	}

}
