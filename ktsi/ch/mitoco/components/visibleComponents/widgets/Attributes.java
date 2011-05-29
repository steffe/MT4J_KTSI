package ch.mitoco.components.visibleComponents.widgets;

import org.mt4j.AbstractMTApplication;
import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTColorPicker;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;

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
