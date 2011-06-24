package ch.mitoco.templateMode;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;

/** Hier wird der TemplateModi aufgebaut und gesteuert.
 * 
 *  @author rfeigenwinter
 *  
 *  Befindet sich noch im Aufbau, deswegen sind keine weiterführenden JavaDoc
 *  Kommentare in diesem Objekt.
 * 
 */

public class CreateScene extends AbstractScene  {
	
	private AbstractMTApplication app;

	public CreateScene(AbstractMTApplication mtApplication, String name) {
		super(mtApplication, name);
		this.app = mtApplication;
		this.setClearColor(new MTColor(126, 130, 168, 255));
		this.registerGlobalInputProcessor(new CursorTracer(app, this));
		
		float tw = mtApplication.width / 6f;
		float th = 0.6f * mtApplication.height;
		
		float tx1 = mtApplication.width / 3f;
		float ty1 = (mtApplication.height - th) / 2f;
		
		// Stack drop target on the left side.
		ObjectRectangle target1 = new ObjectRectangle(mtApplication, tx1, ty1, tw, th);
		
		target1.setFillColor(new MTColor(0, 255, 0, 255));
		target1.setStrokeColor(new MTColor(0, 255, 0, 255));
		
		this.getCanvas().addChild(target1);
		
		MTRectangle obj1 = new MTRectangle(app, 200, 200);
		
		this.getCanvas().addChild(obj1);
	}
}
