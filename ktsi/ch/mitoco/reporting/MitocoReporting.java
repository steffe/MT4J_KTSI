package ch.mitoco.reporting;

import gov.pnnl.components.visibleComponents.widgets.radialMenu.examples.MTRadialMenuExampleScene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.inputData.MTFingerInputEvt;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.components.visibleComponents.layout.MTColumnLayout2D;
import org.mt4jx.util.animation.AnimationUtil;

import ch.mitoco.components.visibleComponents.MyMTObject;
import ch.mitoco.components.visibleComponents.widgets.MTTextAttribut;
import ch.mitoco.dataController.DataController;
import ch.mitoco.main.MitocoScene;
import ch.mitoco.model.ModelAttributContent;
import ch.mitoco.model.ModelMtAttributs;
import ch.mitoco.model.ModelMtObjects;

public class MitocoReporting extends AbstractScene {
	
	private MitocoScene scene;
	private ArrayList<MyMTObject> mtobjs;
	private int attributCounter;
	private int objectCounter;
	private HashMap<String, Integer> playerCount;
	private boolean translate = true;
	private MTColumnLayout2D colLayout;
	
	public MitocoReporting(AbstractMTApplication mtApplication, String name, MitocoScene scene, DataController dataController, int reportId) {
		super(mtApplication, name);
		this.scene = scene;
		final AbstractMTApplication app = mtApplication;
		scene.getGuiOverlay();
		switch(reportId) {
		case(1):
			golfReporting(app, dataController);
			break;
		
		default:
			break;
		}
	}
	
	private void golfReporting(AbstractMTApplication app, DataController dataController) {
		attributCounter = 0;
		objectCounter = 0;
		colLayout = new MTColumnLayout2D(app);
		mtobjs = dataController.getMyobjectList();
		for (final MyMTObject it : mtobjs) {
			if (it.isVisible()) {
				
				//Brauche ich vielleicht mal um zurück in die Ausgangsposition zu kommen!
//				final float translateOrigX = it.getCenterPointLocal().getX();
//			    final float translateOrigY = it.getCenterPointLocal().getY();
				
				String ol = dataController.getModelScene().getMtobjects().get(objectCounter).getObjectlable();
				//Nur Objekte vom Typ Player werden momentan beachtet. 
				if (ol.equalsIgnoreCase("Player")) {
					for (ModelMtAttributs iterat : dataController.getModelScene().getMtobjects().get(0).getObjectattributs()) {
						String test = iterat.getAttributcontent().get(0).getValue();
						dataController.getModelScene().getMtobjects().get(0).getObjectlable();
					
						test = iterat.getLable();
						attributCounter++;
						//System.out.println("Value: " + test);
						
						//Fuer das Original stappeln wir einafch die Anzahl Objekte 
						//von oben nach Unten. teilweise auch überlappend.
						//Wenn überlappend dann wohl vom letzten zum grössten. Also unten anfangen?
						//Layout verwenden?
						
						colLayout.addChild(it);
						
						this.scene.getCanvas().addChild(colLayout);
						
		                 final Random random = new Random();
		                 float x = random.nextInt(app.width) / 2;
		                 if (x % 2 == 0){
		                	 x = -x;
		                 }

						 float y = random.nextInt(app.height) / 2;
						 if (y % 2 == 0){
							 y = -y;
						 }

						 //AnimationUtil.translate(it, x, y);
						 AnimationUtil.translate(colLayout, x, y);

						 setTranslate(false);
					}
				}	
				objectCounter++;
			}
		}
//		for (final MyMTObject it : mtobjs) {
//			if (it.isVisible()) {
//				ArrayList<MyMTObject> tada = colLayout.getChildren();
//			}
//		}
	}

	public boolean isTranslate() {
	    return this.translate;
	}

	public void setTranslate(final boolean translate) {
	    this.translate = translate;
	}

}
