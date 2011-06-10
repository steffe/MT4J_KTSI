package ch.mitoco.templateMode;

import org.mt4j.MTApplication;

import processing.core.PApplet;

/** Startet die TemplateModi Scene.
 * 
 *  @author rfeigenwinter
 *  @version 0.2
 * 
 */
public class TemplateStart extends MTApplication {
	/** Variabel die von MTApplication vorausgesetzt wird. */
	private static final long serialVersionUID = 1L;
	
	/** Main Methode.
	 * @param args tada
	 *  */
	public static void main(final String[] args) {
		initialize();
		new PApplet();
	}

	@Override
	public final void startUp() {
		addScene(new CreateScene(this, "Create your own MindMap Objects"));
	}
}
