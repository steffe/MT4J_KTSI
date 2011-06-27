package ch.mitoco.main;


import gov.pnnl.components.visibleComponents.widgets.radialMenu.MTRadialMenu;

import java.util.Iterator;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTLine;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.widgets.MTOverlayContainer;
import org.mt4j.input.gestureAction.DefaultPanAction;
import org.mt4j.input.gestureAction.DefaultZoomAction;
import org.mt4j.input.inputData.InputCursor;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.panProcessor.PanProcessorTwoFingers;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeEvent;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeUtils.Direction;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeUtils.UnistrokeGesture;
import org.mt4j.input.inputProcessors.componentProcessors.zoomProcessor.ZoomProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimation;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.ani.AniAnimation;

import ch.mitoco.components.visibleComponents.MyMTObject;
import ch.mitoco.components.visibleComponents.filechooser.FileChooser;
import ch.mitoco.components.visibleComponents.filechooser.WBErrorMessage;
import ch.mitoco.dataController.DataController;
import ch.mitoco.model.ModelScence;
import ch.mitoco.model.ModelSceneList;


/** Hello Word Scene. */

public class MitocoScene extends AbstractScene {


	
	/**
	 * List mit MyObjects
	 * 
	 * Erstellen:
	 * private List<MyMTObjects> myobjectList=new ArrayList<MyMTObject>();
	 * 
	 * Objekt Hinzufügen
	 * myobjectList.add( new MyMTObject(mtApplication) );
	 */
	
	/** MTApplication. */
	private MTApplication mtApplication;
		
	/** Object Counter and Object ID. */
	private int counter;
	
	/** Line between Objects.*/
	private MTLine l1;
	
	/** Data Controller Object. */
	private DataController dataController;
	
	/**RadialMenu.	*/
	private MTRadialMenu mtRadialMenu1;
	
	/**InputCursor for RadialMenu.	*/
	private InputCursor ic;
	
	/**Filechooser objekt. */
	private static FileChooser fileChooser;
	
	private MTOverlayContainer guiOverlay;
	
	private BuildRadialMenu buildRadialMenu;
	
	/** Test: Linker Controller. */
	//private MTLinkController linker; //TODO: Test
	
	/** Spezifische ScenenDaten	 */
	private ModelSceneList sceneData;
	
	
	/** Test Objektausrichtung */
	private float endxGestureVector;
	private float startxGestureVector;
	private float endyGestureVector;
	private float startyGestureVector;
	
	/** 
	 * Hello Word Scene. 
	 * 
	 * Die Grundgesten werden erstellten und mit dem XMLController, RadialMenü und TextKlasse Linker verbunden.
	 * 
	 * @param mtAppl MTApplication 
	 * @param name String
	 */
	public MitocoScene(final MTApplication mtAppl, final String name, final ModelSceneList SceneData) {
		super(mtAppl, name);
		
		this.mtApplication = mtAppl;
		this.setClearColor(new MTColor(100, 100, 100, 255));
		this.sceneData = SceneData;
		
		dataController = new DataController(mtAppl, getCanvas());
		dataController.createDataModel(sceneData.getScenename());
		dataController.createObjectList();
		
		
		ModelScence test2 = new ModelScence();
		test2.setId(2);
		//SaveXML test = new SaveXML("arrayTest.xml");
		
		//linker = new MTLinkController(mtAppl, getCanvas(), dataController.getMyobjectList()); //TODO: Test
		
		
		
		//Show touches
		this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));
		
		// Counter for Objects
		counter = 0;
		
		//Test for Linies betweens the objects
		l1 = new MTLine(mtApplication, 0, 0, 0, 0);
		l1.setStrokeColor(MTColor.BLUE);
		l1.setStrokeWeight(5);
		l1.setPickable(false);
		
		//StrokeListener
		UnistrokeProcessor up = new UnistrokeProcessor(getMTApplication());
		up.addTemplate(UnistrokeGesture.CIRCLE, Direction.CLOCKWISE);
		up.addTemplate(UnistrokeGesture.CIRCLE, Direction.COUNTERCLOCKWISE);
		up.addTemplate(UnistrokeGesture.V, Direction.CLOCKWISE);
			
		getCanvas().registerInputProcessor(up);
		getCanvas().addGestureListener(UnistrokeProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(final MTGestureEvent ge) {
				UnistrokeEvent ue = (UnistrokeEvent) ge;
				//final TapAndHoldEvent th = (TapAndHoldEvent) ge;
				//final IMTComponent3D target = th.getTarget();
				switch (ue.getId()) {
				case UnistrokeEvent.GESTURE_STARTED:
					//Draws yellow Gesture lines
					//TODO: Disabled because of Lasso: getCanvas().addChild(ue.getVisualization());
				
					
					
					//Beim ersten GESTURE_STARTED Event existiert das Objekt noch nicht, daher darf es auch nicht destroyed werden.
					//if (mtRadialMenu1 != null) {
					//	mtRadialMenu1.destroy();
					//}
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
						
						startyGestureVector = ic.getStartPosY();
						startxGestureVector = ic.getStartPosX();
						endyGestureVector = ic.getCurrentEvtPosY();
						endxGestureVector = ic.getCurrentEvtPosX();
						System.out.println("Direction " + ic.getDirection());
						System.out.println("Direction " + ic.getPosition());
						System.out.println("Speed " + ic.getVelocityVector());
						if ((MitocoScene.this.mtRadialMenu1 != null) && !MitocoScene.this.mtRadialMenu1.isVisible())
			              {
							MitocoScene.this.mtRadialMenu1.destroy();
							MitocoScene.this.mtRadialMenu1 = null;
			              }
			              if (MitocoScene.this.mtRadialMenu1 == null) {
			                // Build list of menu items
			            	  buildRadialMenu = new BuildRadialMenu(mtApplication, dataController, MitocoScene.this, sceneData, ic);
			              } else {
			            	  //Workaround, damit das Menü nach einemal daneben Klicken wieder gezeichnet werden kann.
			            	  buildRadialMenu = new BuildRadialMenu(mtApplication, dataController, MitocoScene.this, sceneData, ic);
			              }
					getCanvas().removeChild(ue.getVisualization());
					}
					break;
				default:
					break;
				}
				return false;
			}				
		});
		
		fileChooser = new FileChooser("C:\\", this);
		getCanvas().addChild(fileChooser.getUI());
		
		// Standard Default ZoomAction and Pan TwoFingers
		this.getCanvas().registerInputProcessor(new ZoomProcessor(mtApplication));
		this.getCanvas().addGestureListener(ZoomProcessor.class, new DefaultZoomAction());
		this.getCanvas().registerInputProcessor(new PanProcessorTwoFingers(mtApplication));
		this.getCanvas().addGestureListener(PanProcessorTwoFingers.class, new DefaultPanAction());
	}

	@Override
	public void init() {
		
	}
	
	@Override
	public void shutDown() {
	
	}

	/**Returns the Filechooser objekt.
	 * so you can show the filechooser from other Classes
	 * 
	 * @return Filechooser
	 */
	public static FileChooser getFc() { return fileChooser; }
	// Method to retrieve the gui overlay
	
	/**Returns the Main Canvas. */
	public MTComponent getGuiOverlay() { return this.getCanvas(); }
	
	
	
	
	/**For Loading XML Scene File.
	 * 
	 * creates MtObjects to the List and draw it on the Canvas
	 * 
	 * @param filename
	 */
	public void drawXMLload(String filename) {
	
		if (!dataController.loadSceneXML(fileChooser.getSelectionPath())) {
			WBErrorMessage errormessage = new WBErrorMessage(MitocoScene.this,"Fehler beim Laden von XML");
			errormessage.setVisible(true);
			getCanvas().addChild(errormessage);
			for (Iterator<MyMTObject> it = dataController.getMyobjectList().iterator(); it.hasNext();) {
				getCanvas().removeChild(dataController.getMyobjectList().get(it.next().getID()));	
			}
			dataController.clearScene();
			} else {
			for (Iterator<MyMTObject> it = dataController.getMyobjectList().iterator(); it.hasNext();) {
				getCanvas().addChild(dataController.getMyobjectList().get(it.next().getID()));	
			}
			counter = dataController.getObjectcounter();
		}
		
		//linker.setTapAndHoldListener();
		
	}
	
	
	/**Einblenden von aktivierten Objekten
	 * 
	 * @param as
	 */
	  public static void showObjects(final MTPolygon as) {
		    final float width = as.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
		    final IAnimation closeAnim = new AniAnimation(1, width, 500, AniAnimation.ELASTIC_IN, as);

		    closeAnim.addAnimationListener(new IAnimationListener() {
		      @Override
		      public void processAnimationEvent(final AnimationEvent ae) {
		        switch (ae.getId()) {
		          case AnimationEvent.ANIMATION_UPDATED:
		            final float currentVal = ae.getValue();
		            as.setWidthXYRelativeToParent(currentVal);
		            break;
		          default:
		            break;
		        }// switch
		      }// processanimation
		    });

		    closeAnim.start();
		  }
	  
	  
	  	/**Ausblenden von aktivierten Objekten
		 * 
		 * @param as
		 */
		  public static void hideObjects(final MTPolygon as) {
			    final float width = as.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
			    final IAnimation closeAnim = new AniAnimation(1, width, 500, AniAnimation.EXPO_OUT, as);

			    closeAnim.addAnimationListener(new IAnimationListener() {
			      @Override
			      public void processAnimationEvent(final AnimationEvent ae) {
			        switch (ae.getId()) {
			          case AnimationEvent.ANIMATION_UPDATED:
			            final float currentVal = ae.getValue();
			            as.setWidthXYRelativeToParent(currentVal);
			            break;
			          case AnimationEvent.ANIMATION_ENDED:
			        	  as.setVisible(false);
			          default:
			            break;
			        }// switch
			      }// processanimation
			    });
			    
			    closeAnim.start();
			    //as.setVisible(false);
			 }
	
		  /**Aufrufen des Datei-Explorers
		   * the following filters are possible
		   * Filter: 	xml
		   * 			image
		   * 			movies
		   * 			pdf
		   * Um den Pfad der selektieren Datei zu erhalten mus die Methode getFilechooserPath
		   * aufgerufen werden.
		   * 
		   * @param filter setzt Sichtbarkeit von Dateien
		   */
		  public static void drawFilechooser(String filter) {
      		//fileChooser.sendToFront();
          	fileChooser.toggleFileChooser(filter);
      		fileChooser.getUI().sendToFront();  
      		
		  }
		  
		  /**Gibt den Pfad mit der ausgewählten datei des Filechoosers zurück
		   * 
		   * @return string
		   */
		  public static String getFilechooserPath(){
		  
		  		return fileChooser.getSelectionPath();
		}
		  

		  

}
