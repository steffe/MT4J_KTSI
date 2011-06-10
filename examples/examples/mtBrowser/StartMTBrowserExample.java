package examples.mtBrowser;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.input.gestureAction.DefaultButtonClickAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.components.visibleComponents.widgets.MTWebBrowser;

import processing.core.PApplet;
import processing.core.PGraphics;

public class StartMTBrowserExample extends MTApplication {
	private static final long serialVersionUID = 1L;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		initialize(true);
	}

	@Override
	public void startUp() {
		this.addScene(new MTBrowserScene(this, "Browser example scene"));
	}
	
	
	
	
	
	private class MTBrowserScene extends AbstractScene{
		
		public MTBrowserScene(MTApplication mtApplication, String name) {
			super(mtApplication, name);
			setClearColor(new MTColor(80,80,70,255));
			registerGlobalInputProcessor(new CursorTracer(mtApplication, this));
			
			PlusButton button = new PlusButton(mtApplication, 5, 5, 0, 85, 85, 20, 20);
			getCanvas().addChild(button);
		}
		
		public void onEnter() {}
		
		public void onLeave() {}
		
		private class PlusButton extends MTRoundRectangle{
			private Vector3D centerLocal;
			private float widthLocal;
			private float border;
			private float heightLocal;
			private int barWidth;
			private float barLength;

			public PlusButton(PApplet pApplet, float x, float y, float z,
					float width, float height, float arcWidth, float arcHeight) {
				super(pApplet, x, y, z, width, height, arcWidth, arcHeight);
				
				unregisterAllInputProcessors();
				removeAllGestureEventListeners();
				setFillColor(MTColor.BLACK);
				registerInputProcessor(new TapProcessor(pApplet));
				addGestureListener(TapProcessor.class, new DefaultButtonClickAction(this));
				addGestureListener(TapProcessor.class, new IGestureEventListener() {
					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						if ( ((TapEvent)ge).isTapped() ){
							//Create a new browser component
							MTWebBrowser browser = new MTWebBrowser(getMTApplication(), 800, 600);
							browser.setFillColor(new MTColor(180,180,180,200));
							browser.setStrokeColor(MTColor.BLACK);
							getCanvas().addChild(browser);
							browser.setPositionGlobal(MT4jSettings.getInstance().getWindowCenter());
						}
						return false;
					}
				});
				
				centerLocal = this.getCenterPointLocal();
				widthLocal = this.getWidthXY(TransformSpace.LOCAL);
				heightLocal = this.getHeightXY(TransformSpace.LOCAL);
				border = arcWidth;
				barWidth = 10;
				barLength = widthLocal-2* border;
			}
			
			@Override
			public void drawComponent(PGraphics g) {
				super.drawComponent(g);
				//Draw the white cross
				g.fill(255);
				g.stroke(255);
				g.rect(	centerLocal.x - (widthLocal/2f) + border  , 
						centerLocal.y - barWidth/2f, 
						barLength, 
						barWidth);
				g.rect(	centerLocal.x - barWidth/2f, 
						centerLocal.y - (heightLocal/2f) + border , 
						barWidth, 
						barLength);
			}
		}
		
	}
	
	

}
