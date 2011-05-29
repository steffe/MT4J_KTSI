package ch.mitoco.components.visibleComponents.widgets;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;

/**
 * Abstract Attribut.
 * @author tandrich
 *
 */
public abstract class Attributes extends MTRoundRectangle {
	
	/**
	 * Construtor for Attributes Object extends from MTRoundRectangle. The basis of all Objects.
	 * @param app AbstractMTApplication
	 */
	public Attributes(final AbstractMTApplication app) {
		super(app, 0, 0, 0, 0, 0, 5, 5);
	}
	
	/** Method for all Attributs set Max. */
	public void setMax() {
		
	}
	
	/** Method for all Attributs set Max. */
	public void setMin() {
		
	}
}
