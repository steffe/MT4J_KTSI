package ch.mitoco.components.visibleComponents.widgets.drawing;

import java.awt.AWTException;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.mt4j.AbstractMTApplication;
import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTColorPicker;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.components.visibleComponents.widgets.MTSceneTexture;
import org.mt4j.components.visibleComponents.widgets.MTSlider;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.IPreDrawAction;
import org.mt4j.sceneManagement.Iscene;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;
import org.mt4j.util.opengl.GLFBO;
import org.w3c.css.sac.SACMediaList;

import com.sun.opengl.impl.mipmap.Image;
import com.sun.opengl.impl.mipmap.Type_Widget;

import processing.core.PImage;

public class MainDrawingScene extends MTComponent {
	private AbstractMTApplication pa;
	private MTRectangle textureBrush;
	private MTEllipse pencilBrush;
	private DrawSurfaceScene drawingScene;
	private MTSceneTexture sceneTexture;
	private MTRoundRectangle frame;
//	private String imagesPath = System.getProperty("user.dir")+File.separator + "examples"+  File.separator +"advanced"+ File.separator + File.separator +"drawing"+ File.separator + File.separator +"data"+ File.separator +  File.separator +"images" + File.separator ;
	private String imagesPath = "advanced" + AbstractMTApplication.separator + "drawing" + AbstractMTApplication.separator + "data" + AbstractMTApplication.separator + "images" + AbstractMTApplication.separator;

	public MainDrawingScene(AbstractMTApplication mtApplication, String name) {
		super(mtApplication, name);
		this.pa = mtApplication;
		
		//TODO: Entfernen des Drag, Zoom usw Prozessors
		
		if (!(MT4jSettings.getInstance().isOpenGlMode() && GLFBO.isSupported(pa))){
			System.err.println("Drawing example can only be run in OpenGL mode on a gfx card supporting the GL_EXT_framebuffer_object extension!");
			return;
		}
		//this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));
		
		//Create window frame
		frame = new MTRoundRectangle(pa, -50, -50, 0, pa.width + 100, pa.height + 100, 25, 25);
       
		System.out.println("MainDrawingScene: PA Grösse Breite: " + pa.width + " und Höhe: " + pa.height);
		
        //frame.setSizeXYGlobal(pa.width-10, pa.height-10);
        frame.setSizeXYGlobal(230, 200);

        // 390 300
        frame.translate(new Vector3D( - ((pa.width/2)-100) , -((pa.height/2)-100)));
        
        this.addChild(frame);
        //Create the scene in which we actually draw
        drawingScene = new DrawSurfaceScene(pa, "DrawSurface Scene");
        drawingScene.setClear(false);
       
        //Create texture brush
        PImage brushImage = pa.loadImage(imagesPath + "brush1.png");
		textureBrush = new MTRectangle(pa, brushImage);
		textureBrush.setPickable(false);
		textureBrush.setNoFill(false);
		textureBrush.setNoStroke(true);
		textureBrush.setDrawSmooth(true);
		textureBrush.setFillColor(new MTColor(0,0,0));
		//Set texture brush as default
		drawingScene.setBrush(textureBrush);
		
		//Create pencil brush
		pencilBrush = new MTEllipse(pa, new Vector3D(brushImage.width/2f,brushImage.height/2f,0), brushImage.width/2f, brushImage.width/2f, 60);
		pencilBrush.setPickable(false);
		pencilBrush.setNoFill(false);
		pencilBrush.setNoStroke(false);
		pencilBrush.setDrawSmooth(true);
		pencilBrush.setStrokeColor(new MTColor(0, 0, 0, 255));
		pencilBrush.setFillColor(new MTColor(0, 0, 0, 255));
		
        //Create the frame/window that displays the drawing scene through a FBO
//        final MTSceneTexture sceneWindow = new MTSceneTexture(0,0, pa, drawingScene);
		//We have to create a fullscreen fbo in order to save the image uncompressed
//		final MTSceneTexture sceneTexture = new MTSceneTexture(pa,0, 0, pa.width, pa.height, drawingScene);
	     
		sceneTexture = new MTSceneTexture(pa,0, 0, pa.width, pa.height, drawingScene);
        sceneTexture.getFbo().clear(true, 255, 255, 255, 0, true);
        sceneTexture.setStrokeColor(new MTColor(155,155,155));
        frame.addChild(sceneTexture);
       
//        frame.setTexture(newTexImage)
        
        //Eraser button
        PImage eraser = pa.loadImage(imagesPath + "Kde_crystalsvg_eraser.png");
        MTImageButton b = new MTImageButton(pa, eraser);
        b.setNoStroke(true);
        b.translate(new Vector3D(-50,0,0));
        b.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
//					//As we are messing with opengl here, we make sure it happens in the rendering thread
					pa.invokeLater(new Runnable() {
						public void run() {
							sceneTexture.getFbo().clear(true, 255, 255, 255, 0, true);		
						}
					});
				}
				return true;
			}
        });
        frame.addChild(b);
        
  
        
        //Pen brush selector button
        PImage penIcon = pa.loadImage(imagesPath + "pen.png");
        final MTImageButton penButton = new MTImageButton(pa, penIcon);
        frame.addChild(penButton);
        penButton.translate(new Vector3D(-50f, 65,0));
        penButton.setNoStroke(true);
        penButton.setStrokeColor(new MTColor(0,0,0));
        
        //Texture brush selector button
        PImage brushIcon = pa.loadImage(imagesPath + "paintbrush.png");
        final MTImageButton brushButton = new MTImageButton(pa, brushIcon);
        frame.addChild(brushButton);
        brushButton.translate(new Vector3D(-50f, 130,0));
        brushButton.setStrokeColor(new MTColor(0,0,0));
        brushButton.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
					drawingScene.setBrush(textureBrush);
					brushButton.setNoStroke(false);
					penButton.setNoStroke(true);
				}
				return true;
			}
        });
        
        penButton.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
					drawingScene.setBrush(pencilBrush);
					penButton.setNoStroke(false);
					brushButton.setNoStroke(true);
				}
				return true;
			}
        });
        
        //Save to file button
        PImage floppyIcon = pa.loadImage(imagesPath + "floppy.png");
        final MTImageButton floppyButton = new MTImageButton(pa, floppyIcon);
        frame.addChild(floppyButton);
        floppyButton.translate(new Vector3D(-50f, 260,0));
        floppyButton.setNoStroke(true);
        floppyButton.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
//					pa.invokeLater(new Runnable() {
//						public void run() {
//							drawingScene.getCanvas().drawAndUpdateCanvas(pa.g, 0);
//							pa.saveFrame();
//							clear(pa.g);
//						}
//					});
					drawingScene.registerPreDrawAction(new IPreDrawAction() {
						public void processAction() {
							//drawingScene.getCanvas().drawAndUpdateCanvas(pa.g, 0);
							pa.saveFrame();
						}
						public boolean isLoop() {
							return false;
						}
					});
				}
				return true;
			}
        });
        
        /////////////////////////
        //ColorPicker and colorpicker button
        PImage colPick = pa.loadImage(imagesPath + "colorcircle.png");
//        final MTColorPicker colorWidget = new MTColorPicker(0, pa.height-colPick.height, colPick, pa);
        final MTColorPicker colorWidget = new MTColorPicker(pa, 0, 0, colPick);
        colorWidget.translate(new Vector3D(0f, 135,0));
        colorWidget.setStrokeColor(new MTColor(0,0,0));
        colorWidget.addGestureListener(DragProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				if (ge.getId()== MTGestureEvent.GESTURE_ENDED){
					if (colorWidget.isVisible()){
						colorWidget.setVisible(false);
					}
				}else{
					drawingScene.setBrushColor(colorWidget.getSelectedColor());
				}
				return false;
			}
		});
        frame.addChild(colorWidget);
        colorWidget.setVisible(false);
        
        PImage colPickIcon = pa.loadImage(imagesPath + "ColorPickerIcon.png");
        MTImageButton colPickButton = new MTImageButton(pa, colPickIcon);
        frame.addChild(colPickButton);
        colPickButton.translate(new Vector3D(-50f, 195,0));
        colPickButton.setNoStroke(true);
        colPickButton.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
					if (colorWidget.isVisible()){
						colorWidget.setVisible(false);
					}else{
						colorWidget.setVisible(true);
						colorWidget.sendToFront();
					}				
				}
				return true;
			}
        });
        
        //Add a slider to set the brush width
        MTSlider slider = new MTSlider(pa, 0, 0, 200, 38, 0.05f, 2.0f);
        slider.setValue(1.0f);
        frame.addChild(slider);
        slider.rotateZ(new Vector3D(), 90, TransformSpace.LOCAL);
        slider.translate(new Vector3D(-7, 325));
        slider.setStrokeColor(new MTColor(0,0,0));
        slider.setFillColor(new MTColor(220,220,220));
        slider.getKnob().setFillColor(new MTColor(70,70,70));
        slider.getKnob().setStrokeColor(new MTColor(70,70,70));
        slider.addPropertyChangeListener("value", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent p) {
				drawingScene.setBrushScale((Float)p.getNewValue());
			}
		});
        //Add triangle in slider to indicate brush width
        MTPolygon p = new MTPolygon(pa, 
        		new Vertex[]{
        		new Vertex(2 + slider.getKnob().getWidthXY(TransformSpace.LOCAL), slider.getHeightXY(TransformSpace.LOCAL)/2f, 0),
        		new Vertex(slider.getWidthXY(TransformSpace.LOCAL)-3, slider.getHeightXY(TransformSpace.LOCAL)/4f +2, 0),
        		new Vertex(slider.getWidthXY(TransformSpace.LOCAL)-1, slider.getHeightXY(TransformSpace.LOCAL)/2f, 0),
        		new Vertex(slider.getWidthXY(TransformSpace.LOCAL)-3, -slider.getHeightXY(TransformSpace.LOCAL)/4f -2 + slider.getHeightXY(TransformSpace.LOCAL), 0),
        		new Vertex(2, slider.getHeightXY(TransformSpace.LOCAL)/2f, 0),
        });
        p.setFillColor(new MTColor(150,150,150, 150));
        p.setStrokeColor(new MTColor(160,160,160, 190));
        p.unregisterAllInputProcessors();
        p.setPickable(false);
        slider.getOuterShape().addChild(p);
        slider.getKnob().sendToFront(); 
	}

	public void onEnter() {}
	
	public void onLeave() {	}
	
//	@Override
//	public boolean destroy() {
//		boolean destroyed = super.destroy();
//		if (destroyed){
//			drawingScene.destroy(); //Destroy the scene manually since it isnt destroyed in the MTSceneTexture atm!
//		}
//		return destroyed;
//	}
	
	
	public void createImage() {
		pa.invokeLater(new Runnable() {
			public void run() {
				pa.createImage(800, 600, pa.RGB);
				PImage save2 = pa.createImage(540,400, pa.ARGB);
			
				
				int width = (int)frame.getWidthXY(TransformSpace.GLOBAL);
				int height = (int)frame.getHeightXY(TransformSpace.GLOBAL);
				Vector3D v1 = frame.getCenterPointGlobal();
				Vector3D v2 = frame.getCenterPointLocal();
				Vector3D v3 = frame.getCenterPointRelativeToParent();
				
				try {
					
					Rectangle screen = new Rectangle(pa.getLocationOnScreen().x + (int) v2.x - height / 2, pa.getLocationOnScreen().y + (int) v2.y - width / 2, 540, 400);
					
					Robot robot = new Robot();
					
					BufferedImage image = robot.createScreenCapture(screen);
					File file = new File("E:/jetty/htdocs/test.png");
					ImageIO.write(image, "png", file);
					
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

				
				System.out.println("MainDrawingScene: Position Global: " +  v1.x + ":" + v1.y);
				System.out.println("MainDrawingScene: Grösse des Objekts: " +  frame.getWidthXY(TransformSpace.GLOBAL) + ":" + frame.getHeightXY(TransformSpace.GLOBAL) );
				System.out.println("MainDrawingScene: Position Local: " +  v2.x + ":" + v2.y);
				System.out.println("MainDrawingScene: Position Relativ: " +  v3.x + ":" + v3.y);
				
				save2 = pa.get((int)v2.x, (int)v2.y, 540, 400);
				save2.save("test2.tif");
				
				
				PImage savePicture = pa.createImage(pa.width, pa.height, pa.ARGB);
				savePicture = sceneTexture.getTexture();
		        System.out.println("MainDrawingScene: " +  savePicture);
		        savePicture.save("tesgt.tif");
		        
		        
			/*
		        try
		        {        
//		        		BufferedImage b1 = (BufferedImage)savePicture.getImage();
		        		
//		        		int w = savePicture.width;
//		        	    int h = savePicture.height;
//		        	    int type = BufferedImage.TYPE_INT_ARGB; // other options
//		        	    BufferedImage dest = new BufferedImage(w, h, type);
//		        	    Graphics2D g2 = dest.createGraphics();
//		        	    g2.drawImage(savePicture.getImage(), 0, 0, null);
//		        	    g2.dispose();
		        		
		        		//BufferedImage bi = (BufferedImage)savePicture.getImage();
//		                File imageFile = new File("E:/jetty/htdocs/test.tif");
//		                ImageIO.write(bi, "tif", imageFile);
		        } catch(IOException e){
		            e.printStackTrace();
		        }
				*/
				
				
//				MTImage m1 = new MTImage(pa, savePicture);  // Image ist im savePicture vorhanden.
//				m1.translate(new Vector3D(200,200));
//				addChild(m1);
				
				

			}
		});
        
    
        //savePicture.save("testforMe.png");
		
		
	}
	
	boolean modus = false;
	/**
	 * 
	 * @param modus
	 */
	public void setSize(boolean test) {
        //this.scaleGlobal(0.5f, 0.5f, 0, new Vector3D(0,0,0));
        int difh = 170;
		int difw = 100;
		
		if(!modus){
			// Max Modus
			frame.setSizeXYGlobal(540, 400); // Width /heigth
	        this.translate(new Vector3D(difh, difw));
	      
			modus = true;
		} else {
			// Min Modus
			frame.setSizeXYGlobal(240, 200);
	        this.translate(new Vector3D(-difh, -difw));
			modus = false;
		}
		createImage();
	}
	
}
