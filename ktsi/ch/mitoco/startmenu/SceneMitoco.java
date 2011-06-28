package ch.mitoco.startmenu;

import java.awt.event.KeyEvent;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTSceneMenu;
import org.mt4j.components.visibleComponents.widgets.MTSceneWindow;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.gestureAction.InertiaDragAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.Iscene;
import org.mt4j.sceneManagement.transition.BlendTransition;
import org.mt4j.sceneManagement.transition.FadeTransition;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.logging.ILogger;
import org.mt4j.util.logging.MTLoggerFactory;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;
import org.mt4j.util.opengl.GLFBO;

import processing.core.PApplet;
import processing.core.PImage;
import advanced.mtShell.MTShellScene;
import ch.mitoco.dataController.LoadXML;
import ch.mitoco.main.MitocoScene;
import ch.mitoco.model.ModelFunctionList;
import ch.mitoco.model.ModelSceneList;
import ch.mitoco.templateMode.CreateScene;
import ch.mitoco.webserver.FileServer;

/**Startmenü.
 * 
 * Erstell das Startmenü nach der SceneList XML
 * 
 * @author steffe
 *
 */
public class SceneMitoco extends AbstractScene{
	/** The Constant logger. */
	private static final ILogger logger = MTLoggerFactory.getLogger(MTShellScene.class.getName());
	static{
//		logger.setLevel(ILogger.WARN);
//		logger.setLevel(ILogger.DEBUG);
		logger.setLevel(ILogger.INFO);
	}
	
	/** The app. */
	private MTApplication app;
	/** The has fbo. */
	private boolean hasFBO;
	/** The switch directly to scene. */
	private boolean switchDirectlyToScene = false;
	
	/** The list. */
	private MTList list;
	
	/** The font. */
	private IFont font;
	
	/** The preferred icon height. */
	private int preferredIconHeight;
	
	/** The gap between icon and reflection. */
	private int gapBetweenIconAndReflection;
	
	/** The display height of reflection. */
	private float displayHeightOfReflection;
	
	/** The list width. */
	private float listWidth;
	
	/** The list height. */
	private int listHeight;
	
	/** The preferred icon width. */
	private int preferredIconWidth;
	
	/** ModelFunctionListe. */
	private ModelFunctionList modelfunctionList;
	
	/** Wert für die anzahl der Plazuhalter Icons. */
	private int emptyPlaceHolder;
	
	private String exportPath;
	
	/**Anzeigen des Startmenü mit allen Scene welche in der SceneListe.xml erfasst sind.
	 * 
	 * Scrollbare Scenenauswahl, berechnung für zentrale Ausrichtung
	 * 
	 * @param mtApplication
	 * @param name
	 */
	
	public SceneMitoco(MTApplication mtApplication, String name) {
		super(mtApplication, name);
		
		// TODO Auto-generated constructor stub
		this.app = mtApplication;
		this.hasFBO = GLFBO.isSupported(app);
//		this.hasFBO = false; // TEST
		//IF we have no FBO directly switch to scene and ignore setting
		this.switchDirectlyToScene = !this.hasFBO || switchDirectlyToScene;
		exportPath = "C:\\webserver\\";
		
		//thread Picture save
		Thread picturSave = new pictureSave(this, exportPath);
		picturSave.start();
		
		this.registerGlobalInputProcessor(new CursorTracer(app, this));
		this.setClearColor(new MTColor(0, 0, 0, 255));
		
		//Instanzieren des Datenmodels für die FunktionsListe
		modelfunctionList = new ModelFunctionList();
		LoadXML scenelist = new LoadXML("sceneListe.xml", "sceneliste");
		modelfunctionList = scenelist.getSceneListe();
		
		PImage closeNewImage = mtApplication.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  "closeButton64.png");
		MTImageButton buttonClose = new MTImageButton(mtApplication, closeNewImage);

		buttonClose.setSizeLocal(40, 40);
		buttonClose.setFillColor(new MTColor(255, 255, 255, 200));
		buttonClose.setName("CloseUpdate");
		buttonClose.setNoStroke(true);
		buttonClose.setPositionGlobal(new Vector3D(20, 20));
		
		buttonClose.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				switch(te.getTapID()) {
				case TapEvent.TAPPED:
					Runtime.getRuntime().exit(0);
					break;
				
				default:
					break;
				}
				
				
				
				
			return false;
			}
		});
		
		Thread thread = new FileServer(0, exportPath);
		thread.start();
		
		
		preferredIconWidth = 256;
		preferredIconHeight = 192;
		gapBetweenIconAndReflection = 9;
		displayHeightOfReflection = preferredIconHeight * 0.6f;
		
		System.out.println("app.width " + app.width);
		System.out.println("app.anzahl Funk " + scenelist.getSceneListe().getSceneliste().size());
		System.out.println("app.width durch Icon" + app.width / preferredIconWidth);
		
		if(app.width / preferredIconWidth>=scenelist.getSceneListe().getSceneliste().size()){
			emptyPlaceHolder = ((app.width / preferredIconWidth) - scenelist.getSceneListe().getSceneliste().size()-1);
			System.out.println("Placeholder" + emptyPlaceHolder);
		}
		else {
			emptyPlaceHolder = 0;
		}
		
		
		
		//CREATE LIST
		listWidth = preferredIconHeight + displayHeightOfReflection + gapBetweenIconAndReflection;
//		listHeight = app.width - 50;
		listHeight = app.width;
		list = new MTList(mtApplication, 0, 0, listWidth, listHeight, 40);
		list.setFillColor(new MTColor(150, 150, 150, 200));
		list.setNoFill(true);
		list.setNoStroke(true);
		
		font = FontManager.getInstance().createFont(app, "SansSerif", 18, MTColor.WHITE);
		
		//Only for Performance Reader, this way, the fonts are loaded to the cache.
		FontManager.getInstance().createFont(mtApplication, "arial.ttf", 16, new MTColor(255, 255, 255, 255), true);
		FontManager.getInstance().createFont(mtApplication, "arial.ttf", 16, new MTColor(255, 255, 255, 255), false);
		FontManager.getInstance().createFont(mtApplication, "arial.ttf", 15, MTColor.BLACK);
		FontManager.getInstance().createFont(mtApplication, "arial.ttf", 14, MTColor.BLACK);
		FontManager.getInstance().createFont(mtApplication, "keys.svg", 30, new MTColor(0, 0, 0, 255)); 
		FontManager.getInstance().createFont(mtApplication, "SansSerif", 16, MTColor.WHITE, false);
		//FontManager.getInstance().createFont(mtApplication, "SansSerif", 14, MTColor.WHITE, false);
		//FontManager.getInstance().createFont(mtApplication, "SansSerif.Bold", 15, MTColor.WHITE);
		//FontManager.getInstance().createFont(mtApplication, "SansSerif", 18, MTColor.WHITE);
		final MTColor w = new MTColor(255, 255, 255, 255);
		FontManager.getInstance().createFont(mtApplication, "SansSerif", 16, w);
		//FontManager.getInstance().createFont(mtApplication,"SansSerif.bold", 16,w);
		//FontManager.getInstance().createFont(mtApplication,"SansSerif.italic", 16,w);
		//FontManager.getInstance().createFont(mtApplication,"SansSerif.bolditalic", 16,w);
		//FontManager.getInstance().createFont(mtApplication,"SansSerif", 16,w);
		MTColor white = new MTColor(255, 255, 255);
		FontManager.getInstance().createFont(mtApplication, "arial.ttf", 50, white);
		FontManager.getInstance().createFont(mtApplication, "arial.ttf", 35, white);
		FontManager.getInstance().createFont(mtApplication, "arial.ttf", 24, new MTColor(0, 0, 0));
		
		
		
	

		//for (ModelTypDescription it2 : dataController.getObjectetyps().getObjectdescription()) {
		for (int i = 0; i < emptyPlaceHolder; i++) {
			this.addScene(new ICreateScene() {
				public Iscene getNewScene() {
					return new MitocoScene(app, "", modelfunctionList.getSceneliste().get(0));
				}
				public String getTitle() {
					return "";
				}
			}, app.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  "SceneListeTrans.png"));
		}
		for (final ModelSceneList it : modelfunctionList.getSceneliste()) {
			this.addScene(new ICreateScene() {
				public Iscene getNewScene() {
					return new MitocoScene(app, it.getScenename(), it);
				}
				public String getTitle() {
					return it.getScenename();
				}
			}, app.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  it.getPicturepath()));
			
			getCanvas().addChild(list);
		}
		
		
			this.addScene(new ICreateScene() {
				public Iscene getNewScene() {
					return new CreateScene(app, "Template");
				}
				public String getTitle() {
					return "Templates";
				}
			}, app.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  "templatemodus.jpg"));
			
			getCanvas().addChild(list);
	

		for (int i = 0; i < emptyPlaceHolder; i++) {
			this.addScene(new ICreateScene() {
				public Iscene getNewScene() {
					return new MitocoScene(app, "", modelfunctionList.getSceneliste().get(0));
				}
				public String getTitle() {
					return "";
				}
			}, app.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  "SceneListeTrans.png"));
		}
		
		
		list.rotateZ(list.getCenterPointLocal(), -90, TransformSpace.LOCAL);
//		list.setPositionGlobal(new Vector3D(app.width/2f, app.height - list.getHeightXY(TransformSpace.GLOBAL) - 1));
		list.setPositionGlobal(new Vector3D(app.width/2f, app.height/2f));
//		list.setAnchor(PositionAnchor.UPPER_LEFT);
//		list.setPositionGlobal(new Vector3D(app.width/2f - list.getWidthXY(TransformSpace.GLOBAL)/2f, app.height - 20));
		getCanvas().setFrustumCulling(true); 
		
		//Scene transition effect
		if (this.hasFBO){
			this.setTransition(new BlendTransition(app, 730));	
		}else{
			this.setTransition(new FadeTransition(app, 730));	
		}
		
		
		getCanvas().addChild(buttonClose);
	}
	
	/** Creates the Tap Prozessor for each Scene
	 *
	 * 
	 * @param cell the cell
	 * @param createScene the create scene
	 */
	private void addTapProcessor(MTListCell cell, final ICreateScene createScene){
		cell.registerInputProcessor(new TapProcessor(app, 15));
		cell.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
					//System.out.println("Clicked cell: " + te.getTargetComponent());
					final Iscene scene = createScene.getNewScene();
							
					if (!switchDirectlyToScene){//We have FBO support -> show scene in a window first
						
						if (hasFBO && scene instanceof AbstractScene){
							((AbstractScene) scene).setTransition(new BlendTransition(app, 300));	
						}
						
						final MTSceneWindow sceneWindow = new MTSceneWindow(app, scene,100, 50);
						sceneWindow.setFillColor(new MTColor(50,50,50,200));
						sceneWindow.scaleGlobal(0.5f, 0.5f, 0.5f, sceneWindow.getCenterPointGlobal());
						sceneWindow.addGestureListener(DragProcessor.class, new InertiaDragAction());
						getCanvas().addChild(sceneWindow);
					}else{
						//No FBO available -> change to the new scene fullscreen directly
						
						float menuWidth = 64;
						float menuHeight = 64;
						MTSceneMenu sceneMenu = 
						//new MTSceneMenu(this, app.width-menuWidth/2f, 0-menuHeight/2f, menuWidth, menuHeight, app);
						new MTSceneMenu(app, scene, app.width-menuWidth, app.height-menuHeight, menuWidth, menuHeight);
						sceneMenu.addToScene();
						
						app.addScene(scene);
						app.pushScene();
						app.changeScene(scene);
					}
				}
				return false;
			}
		});
	}
	
	
	
	/**Adds the Scene to an MTList Object
	 * 
	 * 
	 * @param sceneToCreate the scene to create
	 * @param icon the icon
	 */
	public void addScene(ICreateScene sceneToCreate, PImage icon){
//		System.out.println("Width: " + width + " Height:" + height);
		
		//Create reflection image
		PImage reflection = this.getReflection(getMTApplication(), icon);
		
		float border = 1;
		float bothBorders = 2 * border;
		float topShift = 30;
		float reflectionDistanceFromImage = topShift + gapBetweenIconAndReflection; //Gap width between image and reflection
		
		float listCellWidth = listWidth;		
		float realListCellWidth = listCellWidth - bothBorders;
//		float listCellHeight = preferredIconWidth - border;
		float listCellHeight = preferredIconWidth;
		
		MTListCell cell = new MTListCell(app ,  realListCellWidth, listCellHeight);
		cell.setNoFill(true);
		cell.setNoStroke(true);
		//cell.rotateY(cell.getCenterPointLocal(), 45);
		
//		/*
		Vertex[] vertices = new Vertex[]{
				new Vertex(realListCellWidth-topShift, 				border,		  		0, 0,0),
				new Vertex(realListCellWidth-topShift, 				 listCellHeight -border,	0, 1,0),
				new Vertex(realListCellWidth-topShift - icon.height, listCellHeight -border,	0, 1,1),
				new Vertex(realListCellWidth-topShift - icon.height,	border,		  		0, 0,1),
				new Vertex(realListCellWidth-topShift, 				border,		  		0, 0,0),
		};
		MTPolygon p = new MTPolygon(getMTApplication(), vertices);
		p.setTexture(icon);
		p.setNoStroke(true);
//		p.setStrokeColor(new MTColor(150,150,150, 255));
		p.setStrokeColor(new MTColor(80, 80, 80, 255));
		
		Vertex[] verticesRef = new Vertex[]{
				new Vertex(listCellWidth - icon.height - reflectionDistanceFromImage, 				 					border,	0, 	0,0),
				new Vertex(listCellWidth - icon.height - reflectionDistanceFromImage,						listCellHeight -border,	0, 	1,0),
				new Vertex(listCellWidth - icon.height - reflection.height - reflectionDistanceFromImage, 	listCellHeight -border,	0, 	1,1),
				new Vertex(listCellWidth - icon.height - reflection.height - reflectionDistanceFromImage,				border,	0, 	0,1),
				new Vertex(listCellWidth - icon.height - reflectionDistanceFromImage, 									border,	0, 	0,0),
		};
		MTPolygon pRef = new MTPolygon(getMTApplication(), verticesRef);
		pRef.setTexture(reflection);
		pRef.setNoStroke(true);
		
		cell.addChild(p);
		cell.addChild(pRef);
		
		list.addListElement(cell);
		addTapProcessor(cell, sceneToCreate);
		
		///Add scene title
		MTTextArea text = new MTTextArea(app, font);
		text.setFillColor(new MTColor(150,150,250,200));
		text.setNoFill(true);
		text.setNoStroke(true);
		text.setText(sceneToCreate.getTitle());
		text.rotateZ(text.getCenterPointLocal(), 90, TransformSpace.LOCAL);
		cell.addChild(text);

		
		text.setPositionRelativeToParent(cell.getCenterPointLocal());
		text.translate(new Vector3D(realListCellWidth*0.5f - text.getHeightXY(TransformSpace.LOCAL)*0.5f, 0));
		///
	}
	
	
	/**
	 * Creates the reflection image.
	 * 
	 * @param pa the pa
	 * @param image the image
	 * @return the reflection image
	 */
	private PImage getReflection(PApplet pa, PImage image) {
		int width =  image.width; 
		int height = image.height;
		
		PImage copyOfImage = pa.createImage(image.width, image.height, PApplet.ARGB);
		image.loadPixels();
		copyOfImage.loadPixels();
		   
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int imageIndex = y*image.width+x;
//				int currA = (image.pixels[imageIndex] >> 32) & 0xFF;
				int currR = (image.pixels[imageIndex] >> 16) & 0xFF;
			    int currG = (image.pixels[imageIndex] >> 8) & 0xFF;
			    int currB = image.pixels[imageIndex] & 0xFF;
			    
			    int col = image.pixels[imageIndex];
			    float alpha = pa.alpha(col);
			    
			    int reflectImageIndex = (image.height-y-1) * image.width+x;
			    if (alpha <= 0.0f){
			    	copyOfImage.pixels[reflectImageIndex] = pa.color(currR , currG , currB , 0.0f); 
			    }else{
			    	copyOfImage.pixels[reflectImageIndex] = pa.color(currR , currG , currB , Math.round(y*y*y * (0.00003f) - 60)); //WORKS	
			    }
			}
		} 
		copyOfImage.updatePixels();
		return copyOfImage;
	}
	
	
	public void onEnter() {
		getMTApplication().registerKeyEvent(this);
	}
	
	public void onLeave() {	
		getMTApplication().unregisterKeyEvent(this);
	}
	
	/**
	 * Key event.
	 * 
	 * @param e the e
	 */
	public void keyEvent(KeyEvent e){
		int evtID = e.getID();
		if (evtID != KeyEvent.KEY_PRESSED)
			return;
		switch (e.getKeyCode()){
		case KeyEvent.VK_F:
			System.out.println("FPS: " + getMTApplication().frameRate);
			break;
		case KeyEvent.VK_M:
			System.out.println("Max memory: " + Runtime.getRuntime().maxMemory() + " <-> Free memory: " + Runtime.getRuntime().freeMemory());
			break;	
		case KeyEvent.VK_C:
			getMTApplication().invokeLater(new Runnable() {
				public void run() {
//					System.gc();
//					GC.maxObjectInspectionAge();
//					System.runFinalization();
				}
			});
			break;
		default:
			break;
		}
	}
	

}
