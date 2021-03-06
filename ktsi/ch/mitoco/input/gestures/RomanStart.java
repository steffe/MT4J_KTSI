package ch.mitoco.input.gestures;

import org.mt4j.MTApplication;

import processing.core.PApplet;



/** TestDatei um GesturesMen� aufzubauen. */

public class RomanStart extends MTApplication {
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
		addScene(new MenuGesture(this, "Scene f�r Own MT-Gestures"));
	}
}
