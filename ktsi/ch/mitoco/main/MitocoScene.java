package ch.mitoco.main;


import gov.pnnl.components.visibleComponents.widgets.radialMenu.MTRadialMenu;
import gov.pnnl.components.visibleComponents.widgets.radialMenu.item.MTMenuItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTLine;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.gestureAction.DefaultPanAction;
import org.mt4j.input.gestureAction.DefaultZoomAction;
import org.mt4j.input.inputData.InputCursor;
import org.mt4j.input.inputData.MTFingerInputEvt;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.panProcessor.PanProcessorTwoFingers;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeEvent;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeUtils.Direction;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeUtils.UnistrokeGesture;
import org.mt4j.input.inputProcessors.componentProcessors.zoomProcessor.ZoomProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.ToolsMath;
import org.mt4j.util.math.Vector3D;

import ch.mitoco.components.visibleComponents.MyMTObject;
import ch.mitoco.dataController.DataController;
import ch.mitoco.model.ModelSceneList;
import ch.mitoco.model.ModelTypDescription;


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
	
	/** Test: Linker Controller. */
	//private MTLinkController linker; //TODO: Test
	
	/** Spezifische ScenenDaten	 */
	private ModelSceneList sceneData;
	
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
					getCanvas().addChild(ue.getVisualization());
					
					
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
						
						if ((MitocoScene.this.mtRadialMenu1 != null) && !MitocoScene.this.mtRadialMenu1.isVisible())
			              {
							MitocoScene.this.mtRadialMenu1.destroy();
							MitocoScene.this.mtRadialMenu1 = null;
			              }
						 
			              if (MitocoScene.this.mtRadialMenu1 == null) {
			                // Build list of menu items
			              	buildRadialMenu();
			              } else {
			            	  //Workaround, damit das Menü nach einemal daneben Klicken wieder gezeichnet werden kann.
			            	  buildRadialMenu();
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

	/**
	 * Methode the build the RadialMenu.
	 */
	public void buildRadialMenu() {
		
		//InputEvents for RadialMenu
		final IMTInputEventListener createObjectInput = new IMTInputEventListener() {
		      @Override
		      public boolean processInputEvent(final MTInputEvent inEvt) {
		        // Most input events in MT4j are an instance of AbstractCursorInputEvt (mouse, multi-touch..)
		        if (inEvt instanceof MTFingerInputEvt) {
		          final MTFingerInputEvt cursorInputEvt = (MTFingerInputEvt) inEvt;
				switch (cursorInputEvt.getId()) {
		            case TapEvent.GESTURE_STARTED:
		            	
		            	/**
		            	 * To-Do: Laden der Objettypen von XML
		            	 */
		            	//System.out.println("Object add Menu: \"" + inEvt.getCurrentTarget().getName() + "\"");
		            	System.out.println("Counter befor creation: " + counter);
		            	
		            
		            	
		            	 	for (ModelTypDescription it2 : dataController.getObjectetyps().getObjectdescription()) {
		            	 		
		            	 		//System.out.println("Objecttyp Desc: " + it2.getObjectdescription());
			            		//System.out.println("Objecttyp Data: " + it2.getObjectypeid());
			            		//System.out.println("Objecttyp Menu: " + inEvt.getCurrentTarget().getName());
			            		
			            		
			            		if (inEvt.getCurrentTarget().getName().equals(it2.getObjectdescription())){
			            			dataController.createObject(it2.getObjectypeid());
				            		dataController.getMyobjectList().get(counter).setPositionGlobal(new Vector3D(ToolsMath.nextRandomInt(140, 800), ToolsMath.nextRandomInt(140, 700)));
				            		System.out.println("Objekttyp gefunden");
				            		break;
			            		}

		            	 	}
		            	 	
		            	
		            	/*	
		            	if (inEvt.getCurrentTarget().getName().equals("Object 1")) {
		            		dataController.createObject(0);
		            		dataController.getMyobjectList().get(counter).setPositionGlobal(new Vector3D(ToolsMath.nextRandomInt(140, 800), ToolsMath.nextRandomInt(140, 700)));
							//linker.setTapAndHoldListener(dataController.getMyobjectList().get(counter)); //TODO: Test
		            		System.out.println("Object1");
		            		
		            	} else if (inEvt.getCurrentTarget().getName().equals("Object 2")) {
		            		dataController.createObject(1);
		            		dataController.getMyobjectList().get(counter).setPositionGlobal(new Vector3D(ToolsMath.nextRandomInt(140, 800), ToolsMath.nextRandomInt(140, 700)));
							//linker.setTapAndHoldListener(dataController.getMyobjectList().get(counter)); //TODO: Test
		            		System.out.println("Object2");
		            	} else if (inEvt.getCurrentTarget().getName().equals("Object 3")) {
		            		dataController.createObject(2);
		            		dataController.getMyobjectList().get(counter).setPositionGlobal(new Vector3D(ToolsMath.nextRandomInt(140, 800), ToolsMath.nextRandomInt(140, 700)));
							//linker.setTapAndHoldListener(dataController.getMyobjectList().get(counter)); //TODO: Test
		            		System.out.println("Object2");
		            	}
		            	*/
		            	getCanvas().addChild(dataController.getMyobjectList().get(counter));	
		            	
						//getCanvas().addChild(myobjectList.get(counter).getMyObjectBack());	
						counter++;
		              break;
		            default:
		              break;
		          }
		        } else {
		          //LOG.warn("Some other event occured:" + inEvt);
		        }

		        return false;
		      }
		    };
		
		
	    final IMTInputEventListener exitButtonInput = new IMTInputEventListener() {
		      @Override
		      public boolean processInputEvent(final MTInputEvent inEvt) {
		        // Most input events in MT4j are an instance of AbstractCursorInputEvt (mouse, multi-touch..)
		        if (inEvt instanceof MTFingerInputEvt) {
		          final MTFingerInputEvt cursorInputEvt = (MTFingerInputEvt) inEvt;
		          switch (cursorInputEvt.getId()) {
		            case TapEvent.GESTURE_STARTED:
		            	Runtime.getRuntime().exit(0);
		              break;
		            default:
		              break;
		          }
		        } else {
		          //LOG.warn("Some other event occured:" + inEvt);
		        }

		        return false;
		      }
		    };
		    
		   final IMTInputEventListener saveButtonInput = new IMTInputEventListener() {
			      @Override
			      public boolean processInputEvent(final MTInputEvent inEvt) {
			        // Most input events in MT4j are an instance of AbstractCursorInputEvt (mouse, multi-touch..)
			        if (inEvt instanceof MTFingerInputEvt) {
			          final MTFingerInputEvt cursorInputEvt = (MTFingerInputEvt) inEvt;
			          switch (cursorInputEvt.getId()) {
			            case TapEvent.GESTURE_STARTED:
			            	dataController.saveSceneXML();
							MitocoScene.this.mtApplication.saveFrame();
			              break;
			            default:
			              break;
			          }
			        } else {
			          //LOG.warn("Some other event occured:" + inEvt);
			        }

			        return false;
			      }
			    }; 
			    
			    final IMTInputEventListener loadButtonInput = new IMTInputEventListener() {
				      @Override
				      public boolean processInputEvent(final MTInputEvent inEvt) {
				        // Most input events in MT4j are an instance of AbstractCursorInputEvt (mouse, multi-touch..)
				        if (inEvt instanceof MTFingerInputEvt) {
				          final MTFingerInputEvt cursorInputEvt = (MTFingerInputEvt) inEvt;
				          switch (cursorInputEvt.getId()) {
				            case TapEvent.GESTURE_STARTED:
				            	if (!dataController.loadSceneXML()) {
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
				            	//linker.setTapAndHoldListener(); //TODO: Test
								break;
								
				            default:
				              break;
				          }
				        } else {
				          //LOG.warn("Some other event occured:" + inEvt);
				        }

				        return false;
				      }
				    }; 
		
		
		// Build list of menu items
        final List<MTMenuItem> menuItems = new ArrayList<MTMenuItem>();
        final MTMenuItem menu1 = new MTMenuItem("New", null);
        
        for (Iterator<ModelTypDescription> it =  dataController.getObjectetyps().getObjectdescription().iterator(); it.hasNext();) {
        	final MTMenuItem subMenu11 = new MTMenuItem(it.next().getObjectdescription(), null);
        	//menu1.addSubMenuItem(new MTMenuItem(it.next().getObjectdescription(), null));
        	//final IMTInputEventListener createObjectInput = new IMTInputEventListener() {
        	//menu1.addSubMenuItem(new MTMenuItem(it.next().getObjectdescription(), new ConcurrentHashMap<Class<? extends IInputProcessor>, IGestureEventListener>(){
        	menu1.addSubMenuItem(subMenu11);
        	subMenu11.addInputEventListener(createObjectInput);
        	}

        final MTMenuItem subMenu5 = new MTMenuItem("View", null);
        subMenu5.addSubMenuItem(new MTMenuItem("All", null));
        subMenu5.addSubMenuItem(new MTMenuItem("New", null));
        subMenu5.addSubMenuItem(new MTMenuItem("Old", null));
        subMenu5.addSubMenuItem(new MTMenuItem("Color", null));
        subMenu5.addSubMenuItem(new MTMenuItem("Text", null));
        subMenu5.addSubMenuItem(new MTMenuItem("Special", null));

        menu1.addSubMenuItem(subMenu5);

        final MTMenuItem menu2 = new MTMenuItem("Action", null);
        final MTMenuItem subMenu2 = new MTMenuItem("File", null);
        final MTMenuItem subMenu221 = new MTMenuItem("Load", null);
        final MTMenuItem subMenu222 = new MTMenuItem("Save", null);
        subMenu2.addSubMenuItem(subMenu221);
        subMenu2.addSubMenuItem(subMenu222);
        
        subMenu221.addInputEventListener(loadButtonInput);
        subMenu222.addInputEventListener(saveButtonInput);
        
        subMenu2.addSubMenuItem(new MTMenuItem("Copy", null));
        subMenu2.addSubMenuItem(new MTMenuItem("Send", null));
        menu2.addSubMenuItem(subMenu2);
        menu2.addSubMenuItem(new MTMenuItem("Copy Objecet", null));
        menu2.addSubMenuItem(new MTMenuItem("Paste", null));
        menu2.addSubMenuItem(new MTMenuItem("Clear", null));
        menu2.addSubMenuItem(new MTMenuItem("Sub-Menu 5", null));
        menu2.addSubMenuItem(new MTMenuItem("Sub-Menu 6", null));
        final MTMenuItem menu3 = new MTMenuItem("Maximize", null);
        final MTMenuItem menu4 = new MTMenuItem("Minimize", null);
        final MTMenuItem menu5 = new MTMenuItem("Exit", null);
        menu5.addInputEventListener(exitButtonInput); 
     
        menuItems.add(menu1);
        menuItems.add(menu2);
        menuItems.add(menu3);
        menuItems.add(menu4);
        menuItems.add(menu5);

        // Create menu
        final Vector3D vector = new Vector3D(ic.getCurrentEvtPosX(), ic.getCurrentEvtPosY());

        final IFont font = FontManager.getInstance().createFont(mtApplication, "arial.ttf",
            16, // Font size
            new MTColor(255, 255, 255, 255), // Font fill color
            true); // Anti-alias
        this.mtRadialMenu1 = new MTRadialMenu(mtApplication, vector, font, 1f, menuItems);
        this.getCanvas().addChild(mtRadialMenu1);
      }
}
