package store;



import gov.pnnl.components.visibleComponents.widgets.radialMenu.MTRadialMenu;
import gov.pnnl.components.visibleComponents.widgets.radialMenu.item.MTMenuItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mt4j.MTApplication;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.inputData.InputCursor;
import org.mt4j.input.inputData.MTFingerInputEvt;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeEvent;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeUtils.Direction;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeUtils.UnistrokeGesture;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import ch.mitoco.components.visibleComponents.MyMTObject;
import ch.mitoco.dataController.DataController;
import ch.mitoco.model.ModelTypDescription;


/** Hello Word Scene. */

public class HelloWorldScene extends AbstractScene {

	/** MTApplication. */
	private MTApplication mtApplication;
		

	/** Object Counter and Object ID. */
	private int counter;
	
	/**
	 * 
	 */
	private DataController dataController;
	/**RadialMenu.
	 * 
	 */
	  private MTRadialMenu mtRadialMenu1;
	  boolean translate = true;
	  
	  private InputCursor ic;
	  
	/** Hello Word Scene.
	 *  
	 * 
	 * @param mtAppl MTApplication 
	 * @param name String
	 */
	public HelloWorldScene(final MTApplication mtAppl, final String name) {
		super(mtAppl, name);
		
		this.mtApplication = mtAppl;
		this.setClearColor(new MTColor(100, 100, 100, 255));
		this.registerGlobalInputProcessor(new CursorTracer(mtAppl, this));
		
		dataController = new DataController(mtAppl, getCanvas());
		dataController.createDataModel("Scenename");
		dataController.createObjectList();
		
		//StrokeListener
		UnistrokeProcessor up = new UnistrokeProcessor(getMTApplication());
		//up.addTemplate(UnistrokeGesture.CIRCLE, Direction.CLOCKWISE);
		//up.addTemplate(UnistrokeGesture.CIRCLE, Direction.COUNTERCLOCKWISE);
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
						//mtRadialMenu1.destroy();
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
						
						if ((HelloWorldScene.this.mtRadialMenu1 != null) && !HelloWorldScene.this.mtRadialMenu1.isVisible())
			              {
							System.out.println("Recognized gesture 2if: " + g);
							HelloWorldScene.this.mtRadialMenu1.destroy();
							HelloWorldScene.this.mtRadialMenu1 = null;
			              }
						 
			              if (HelloWorldScene.this.mtRadialMenu1 == null)
			              {
			                // Build list of menu items
			               buildRadialMenu();
			              }
			              else
			              {
			            	  buildRadialMenu();
			              }
					}
					break;
				default:
					break;
				}
				return false;
			}				
		});
		
		//Show touches
		this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));
		
		counter = 0;
		//myobjectList = new ArrayList<MyMTObject>();
	}
	@Override
	public void init() {
		
		
	}
	@Override
	public void shutDown() {
		
		
	}
	
	/**
	 * 
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
		            	
		            	System.out.println("Object add Menu: \""+inEvt.getCurrentTarget().getName()+"\"");
		            	System.out.println("Counter befor creation: "+ counter);
		            	if (inEvt.getCurrentTarget().getName().equals("Object 1")){
		            		dataController.createObject(0);
		            		System.out.println("Object1");
		            	}else if (inEvt.getCurrentTarget().getName().equals("Object 2")){
		            		dataController.createObject(1);
		            		System.out.println("Object2");
		            	}else if (inEvt.getCurrentTarget().getName().equals("Object 3")){
		            		dataController.createObject(2);
		            		System.out.println("Object2");
		            	}
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
							HelloWorldScene.this.mtApplication.saveFrame();
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
									}
								else {
									for (Iterator<MyMTObject> it = dataController.getMyobjectList().iterator(); it.hasNext();) {
										getCanvas().addChild(dataController.getMyobjectList().get(it.next().getID()));	
									}
									counter = dataController.getObjectcounter();
								}
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
        			
        	//}));
        			                	
        	menu1.addSubMenuItem(subMenu11);
        	subMenu11.addInputEventListener(createObjectInput);
        	}
        //final MTMenuItem subMenu12 = new MTMenuItem("Object 2", null);
        //menu1.addSubMenuItem(subMenu11);
        //menu1.addSubMenuItem(subMenu12);
        
        //subMenu12.addInputEventListener(createObjectInput);
        //menu1.addSubMenuItem(new MTMenuItem("Object 3", null));
        //menu1.addSubMenuItem(new MTMenuItem("Object 4", null));

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
        //subMenu2.addSubMenuItem(new MTMenuItem("Load", null));
        //subMenu2.addSubMenuItem(new MTMenuItem("Save", null));
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
