package ch.mitoco.input.gestures;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.inputData.InputCursor;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeEvent;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeUtils.Direction;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeUtils.UnistrokeGesture;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

public class MenuGesture extends AbstractScene {
	private AbstractMTApplication app;
	private MTEllipse ellipse; // = new MTEllipse(app, new Vector3D(0, 0, 0), 60, 40);$
	private MTComponent dd;
	boolean ie = false;
	private InputCursor ic;
	
	
	public MenuGesture(final AbstractMTApplication mtApplication, final String name) {
		super(mtApplication, name);
		this.app = mtApplication;
		this.setClearColor(new MTColor(126, 130, 168, 255));
		this.registerGlobalInputProcessor(new CursorTracer(app, this));
		
		UnistrokeProcessor up = new UnistrokeProcessor(getMTApplication());
		up.addTemplate(UnistrokeGesture.CIRCLE, Direction.CLOCKWISE);
		up.addTemplate(UnistrokeGesture.CIRCLE, Direction.COUNTERCLOCKWISE);
		up.addTemplate(UnistrokeGesture.V, Direction.CLOCKWISE);
		
		//my own
		getCanvas().registerInputProcessor(up);
		getCanvas().addGestureListener(UnistrokeProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(final MTGestureEvent ge) {
				UnistrokeEvent ue = (UnistrokeEvent) ge;
				switch (ue.getId()) {
				case UnistrokeEvent.GESTURE_STARTED:
					getCanvas().addChild(ue.getVisualization());
					//Beim ersten GESTURE_STARTED Event existiert das Objekt noch nicht, daher darf es auch nicht destroyed werden.
					if (ellipse != null) {
						ellipse.destroy();
					}
					//System.out.println(ellipse);
					//clearCanvas();
					break;
				case UnistrokeEvent.GESTURE_UPDATED:
					break;
				case UnistrokeEvent.GESTURE_ENDED:
					UnistrokeGesture g = ue.getGesture();
					System.out.println("Recognized gesture: " + g);
					if (g.equals(UnistrokeGesture.V)) {
						ic = ue.getCursor();
						dd = buildMenu();
						//println scheint sehr oft ueberladen, so das der Inhalt verschiedenster Variabeln ganz normal ausgegeben werden kann
						//System.out.println(ic.getPosition());
						//ellipse = new MTEllipse(app, new Vector3D(ic.getPosition()), 60, 40);
						//ellipse.setPositionGlobal(ic.getPosition());
						//getCanvas().addChild(ellipse);
						getCanvas().addChild(dd);
						MTEllipse ellipse6 = new MTEllipse(app,new Vector3D(100, 10, 10), 0, 0);
						getCanvas().addChild(ellipse6);
						
						//recogCircle();
					}
					break;
				default:
					break;
				}
				return false;
			}				
		});
	}
	
	private void clearAllGestures(final MTComponent comp) {
		comp.unregisterAllInputProcessors();
		comp.removeAllGestureEventListeners();
	}
	
	private void recogCircle() {
		//MTEllipse ellipse = new MTEllipse(app, new Vector3D(100, 100, 0), 60, 40);
		//ellipse.translate(new Vector3D(0, 0, 0), TransformSpace.RELATIVE_TO_PARENT);
		ie = true;
		getCanvas().addChild(ellipse);
	}
	private MTComponent buildMenu() {
		MTEllipse ellipse1 = new MTEllipse(app, new Vector3D(ic.getPosition()), 60, 40);
		MTEllipse ellipse2 = new MTEllipse(app, new Vector3D(ic.getPosition()), 60, 40);
		MTEllipse ellipse3 = new MTEllipse(app, new Vector3D(ic.getPosition()), 60, 40);
		MTEllipse ellipse4 = new MTEllipse(app, new Vector3D(ic.getPosition()), 60, 40);
		MTEllipse ellipse5 = new MTEllipse(app, new Vector3D(ic.getPosition()), 60, 40);
		
		//ellipse2.translate(new Vector3D(ic.getCurrentEvtPosY() + 100, ic.getCurrentEvtPosX() - 20, 0), TransformSpace.GLOBAL);
		ellipse2.setPositionGlobal(new Vector3D(ic.getCurrentEvtPosX() - 20, ic.getCurrentEvtPosY() + 100, 0));
		ellipse3.setPositionGlobal(new Vector3D(ic.getCurrentEvtPosX() + 70, ic.getCurrentEvtPosY() + 200, 0));
		ellipse4.setPositionGlobal(new Vector3D(ic.getCurrentEvtPosX() + 170, ic.getCurrentEvtPosY() + 100, 0));
		ellipse5.setPositionGlobal(new Vector3D(ic.getCurrentEvtPosX() + 140, ic.getCurrentEvtPosY(), 0));
		ellipse5.setStrokeColor(MTColor.AQUA);
		ellipse5.setFillColor(MTColor.GRAY);
		
		ellipse1.setPickable(true);
		ellipse2.setPickable(true);
		ellipse3.setPickable(true);
		ellipse4.setPickable(true);
		ellipse5.setPickable(true);

		MTComponent mtc = new MTComponent(app);
		mtc.addChild(ellipse1);
		mtc.addChild(ellipse2);
		mtc.addChild(ellipse3);
		mtc.addChild(ellipse4);
		mtc.addChild(ellipse5);
		/*Wenn die Zeile mit setComposite true ist, dann sind zwar die Ellipse zu einem drag Element verbunden, 
		einzelne Ellipsen lassen sich dann aber nicht mehr steuern irgendwie.....
		*/
		//mtc.setComposite(true);
		this.clearAllGestures(mtc);
		mtc.registerInputProcessor(new DragProcessor(app));
		mtc.addGestureListener(DragProcessor.class, new DefaultDragAction());
		this.clearAllGestures(ellipse1);
		ellipse1.registerInputProcessor(new TapProcessor(app));
		ellipse1.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				switch (te.getId()) {
				case MTGestureEvent.GESTURE_STARTED:
					break;
				case MTGestureEvent.GESTURE_UPDATED:
					break;
				case MTGestureEvent.GESTURE_ENDED:
					if (te.isTapped()){
						System.out.println("Tapped");
					}
					break;
				}
				return false;
			}
		});
		return mtc;
		
	}			
}
