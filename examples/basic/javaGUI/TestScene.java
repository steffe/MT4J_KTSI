package basic.javaGUI;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.input.gestureAction.InertiaDragAction;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.ToolsMath;
import org.mt4j.util.math.Vector3D;

public class TestScene extends AbstractScene {
	
	public TestScene(MTApplication mtApplication, String name) {
		super(mtApplication, name);
		CursorTracer c = new CursorTracer(mtApplication, this);
		registerGlobalInputProcessor(c);
		int count = 2;
		for (int i = 0; i < count; i++) {
			MTRectangle r = new MTRectangle(0,0,ToolsMath.getRandom(50, 250),ToolsMath.getRandom(50, 250),mtApplication);
			r.setFillColor(new MTColor(ToolsMath.getRandom(50,255),ToolsMath.getRandom(50,255),ToolsMath.getRandom(50,255)));
			r.addGestureListener(DragProcessor.class, new InertiaDragAction());
			getCanvas().addChild(r);
			r.setPositionGlobal(new Vector3D(ToolsMath.getRandom(0, mtApplication.width), ToolsMath.getRandom(0, mtApplication.height)));
		}
	}
	
	@Override
	public void init() {	}

	@Override
	public void shutDown() {	}

}

