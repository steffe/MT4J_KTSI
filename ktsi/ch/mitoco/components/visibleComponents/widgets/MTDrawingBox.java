package ch.mitoco.components.visibleComponents.widgets;

import org.mt4j.AbstractMTApplication;
import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.widgets.MTColorPicker;
import org.mt4j.components.visibleComponents.widgets.MTSceneTexture;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;
import ch.mitoco.components.visibleComponents.filechooser.FileChooser;
import ch.mitoco.components.visibleComponents.widgets.drawing.MainDrawingScene;
import ch.mitoco.main.MitocoScene;
import ch.mitoco.model.ModelAttributContent;
import ch.mitoco.model.ModelMtAttributs;
/**
 * MTDropDownList provide a DropDownList with five import levels.
 * 
 * @author tandrich
 *
 */

public class MTDrawingBox extends Attributes {
	
	/** The MTApplication. */
	private AbstractMTApplication app;
	
	/** Width. */
	private int width;
	
	/** Height. */
	private int height;
	
	/** Attribut Model Object. */
	 private ModelMtAttributs model;
	 
	/** Fieldname. */
	private String fname;
	
	/** Standard Color. */
	 private MTColor blue1 = new MTColor(51, 102, 204, 180);
	
	/** Textare Label.*/
	private MTTextArea label;
	
	/** Label Font. */
	private IFont labelfont;
	
	/** Picture File Path.*/
	//private String filePath;
	
	/** Drawing Area.*/
	private  MainDrawingScene m1;
	
	/** Picture width. */
	private float picwidth;
	
	/** Picture Height.*/
	private float picheight;
	
	/** Image path as String.*/
	private String imagePath;
	
	/** Image. */
	private PImage image;
	
	/** Picture MinMax modus. 
	 * true = min
	 * */
	private boolean minmax = true;
		
	/** The MTTextAre Numbers. */	
	//private MTTextArea pathText;
	
	/** MTColorPicker. 	 */
	 private MTColorPicker colorpicker;
	
	 /** MTButton for ColorPicker. */
	 private MTImageButton colPickButton;
	 
	 /**Filechooser objekt. */
	private FileChooser fileChooser;
	private String fileChooserPath;
	 
	/** 
	 * Construtor MTPictureBox.
	 * 
	 * @param app AbstractMTApplication
	 * @param width int
	 * @param height int
	 * @param labelname String
	 * @param labelfont IFont
	 * 
	 * */
	public MTDrawingBox(final AbstractMTApplication app, ModelMtAttributs model, final int width, final int height, final String labelname, final IFont labelfont) {
		super(app);
		this.width = width;
		this.height = height;
		this.fname = labelname;
		this.labelfont = labelfont;
	
		this.app = app;
		this.model = model;
		
		
		dataRead(labelname); // Daten lesen
		
		
		this.init(app);
	}
	/** 
	 * Init MTPictureBox.
	 * @param app AbststractMTApplication
	 * */
	private void init(final AbstractMTApplication app) {		
		this.setSizeLocal(width, height);
		this.setStrokeColor(MTColor.BLACK);
		this.setNoFill(true);
		setPath("Default");
		//loadSelectedImage();
		//changePicture();
	
		label = new MTTextArea(app, 0, -labelfont.getOriginalFontSize(), width, height, labelfont);
		label.setInnerPadding(0);
		label.setNoFill(true);
		label.setStrokeColor(MTColor.LIME);
		label.setNoStroke(true);
		label.setText(fname);
		label.setPickable(false);
		label.setVisible(false);
		
		this.addChild(label);
		//this.addChild(buttonLoad);
		createColorPicker();
		createDrawingScene();
	
	//	this.registerInputProcessor(new CursorTracer(app, ));
	}

	/**
	 * Erstellen der Zeichungsscene.
	 */
	private void createDrawingScene() {
		
		
        m1 = new MainDrawingScene(app, "Drawing Side");
        //m1.scale( 0.5f, 0.5f  , 0, new Vector3D(0,0), TransformSpace.LOCAL);
       
        m1.translate(new Vector3D(30,0));
       
        this.addChild(m1);
        
        //Max Min button
        PImage minmax = app.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  "buttonMaxMin.png");
        MTImageButton mm = new MTImageButton(app, minmax);
        mm.setNoStroke(true);
        mm.setSizeLocal(30, 30);
//        mm.translate(new Vector3D(0,0,0));
        mm.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
//					//As we are messing with opengl here, we make sure it happens in the rendering thread
					System.out.println("MTDrawingBox: Tapped ");
					m1.setSize(true);
				}
				return true;
			}
        });
        this.addChild(mm);
        
      //Max Min button
        String imagesPath = "advanced" + AbstractMTApplication.separator + "drawing" + AbstractMTApplication.separator + "data" + AbstractMTApplication.separator + "images" + AbstractMTApplication.separator;

        
        PImage draw = app.loadImage(imagesPath + "floppy.png");
        MTImageButton drawButton = new MTImageButton(app, draw);
        drawButton.setNoStroke(true);
        drawButton.setSizeLocal(30, 30);
        drawButton.translate(new Vector3D(0, + 30, 0));
        drawButton.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
//					//As we are messing with opengl here, we make sure it happens in the rendering thread
					System.out.println("MTDrawingBox: Tapped ");
					m1.createImage();
				}
				return true;
			}
        });
        this.addChild(drawButton);
	}
	
	
	/** 
	 * Colorpicker.
	 */
	private void createColorPicker() {
   
        PImage colPick = app.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  "colorcircletr.png");
        colorpicker = new MTColorPicker(app, 0, 0, colPick);
        colorpicker.translate(new Vector3D(0, 0, 0));
        colorpicker.setNoStroke(true);
        colorpicker.addGestureListener(DragProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(final MTGestureEvent ge) {
				if (ge.getId() == MTGestureEvent.GESTURE_ENDED) {
					if (colorpicker.isVisible()) {
						colorpicker.setVisible(false);
					}
				} else {
					setFillColor(colorpicker.getSelectedColor());
					dataWrite();
				}
				return false;
			}
		});
        
        PImage colPickIcon = app.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  "ColorPickerIcon.png");
        colPickButton = new MTImageButton(app, colPickIcon);
        colPickButton.translate(new Vector3D(width - 30, 0, 0));
        colPickButton.setNoStroke(true);
        colPickButton.setSizeLocal(30, 30);
        colPickButton.sendToFront();
        colPickButton.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				if (te.isTapped()) {
					if (colorpicker.isVisible()) {
						colorpicker.setVisible(false);
					} else {
						colorpicker.setVisible(true);
						colorpicker.sendToFront();
					}				
				}
				return true;
			}
        });
        colorpicker.setVisible(false);
	    colPickButton.setVisible(false);
	
        this.addChild(colPickButton);
		this.addChild(colorpicker);
	}
	
	/**
	 * Set the new Path.
	 * @param path String
	 */
	private void setPath(final String path) {
		if (path.equalsIgnoreCase("Default")) {
			imagePath = "ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator + "pictures" + MTApplication.separator +  "f5e2.jpg";
		} else {
			imagePath = path;
		}
	}
	
	/**
	 * Get Methode for image Path.
	 * @return String
	 */
	private String getPath() {
		return imagePath;
	}
	
	
	/**
	 * Read from datamodel and insert in the gui elements. 
	 * 
	 * Element "Path" für den Speicherort des Bildpfads
	 * 
	 * @param defaultlabeltext String
	 */
	private void dataRead(final String defaultlabeltext) {
		
	try {
		// Data transfer for value
		if (model.getAttributcontent() == null) {
			setPath("Default");
			System.out.println("PictureBox: no Attributcontent found ");
			
		} else {
			for (ModelAttributContent it : model.getAttributcontent()) {
				if (it.getType().equalsIgnoreCase("Path")) {
					System.out.println("PictureBox: Value zu Typ String " + it.getValue());
					setPath(it.getValue());
					break;
				} else {
					System.out.println("MTDrawingBox: Value zu Typ String NICHT GEFUNDEN ");
					setPath("Default");
				}
			}
		}
		
		// Data transfer for labeltext
		if (model.getLable() == null || model.getLable().isEmpty()) {
			fname = defaultlabeltext;
		} else {
			fname = model.getLable();
		}
		
		// Color for Rectangle Fill Color
		if (model.getAttcolor() == null) {
			this.setFillColor(blue1);
		} else {
			this.setFillColor(model.getAttcolor());
		}
		
	} catch (ArrayIndexOutOfBoundsException ae) {
		System.err.println("MTDrawingBox: ArrayIndexOutOfBoundsException: " + ae);
	} catch (NullPointerException ne) {
		System.err.println("MTDrawingBox: NullPointerException: " + ne);
	} catch (NumberFormatException nfe) {
		System.err.println("MTDrawingBox: NumberFormatException" + nfe);
	} catch (Exception ex) {
		System.err.println("MTDrawingBox: Allgemein Exception " + ex);
	}
		
		
		
		
	}
	
	
	/**
	 * Write changed Date in the datamodel.
	 */
	public void dataWrite() {
//		for (ModelAttributContent it : model.getAttributcontent()) {
//			if (it.getType().equalsIgnoreCase("Path")) {
//				it.setValue(getPath());
//			} else {
//				//TODO Was ist zu tun wenn dieses Werte Paar noch nicht exitiert???
//			}
//		}
//		model.setAttcolor(getFillColor());
	}
	
	/**
	 * Set Attribut to min Modus.
	 */
	public final void setMin() {
		label.setVisible(false);
		colPickButton.setVisible(false);
		
	}
	
	/**
	 * Set Attribut to max Modus.
	 */
	public final void setMax() {
		label.setVisible(true);
		colPickButton.setVisible(true);
	}
	
	/** 
	 * Set Label Text.
	 * @param labeltext String
	 */
	public final void setLabel(final String labeltext) {
		fname = labeltext;
	}
	
	/** 
	 * Get Label Text.
	 * @return fname String
	 */
	public final String getLabel() {
		return fname;
	}

	/** 
	 * Set File Path Value.
	 * @param value String[]
	 */
	public final void setValue(final String value) {
		
		imagePath = value;
	}
	
	/** 
	 *Get Value Text.
	 *@return dString String 
	 */
	public final String getValue() {
		return imagePath;
	}

	
	
}
