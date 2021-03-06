package ch.mitoco.components.visibleComponents.widgets;

import org.mt4j.AbstractMTApplication;
import org.mt4j.MTApplication;
import org.mt4j.components.StateChange;
import org.mt4j.components.StateChangeEvent;
import org.mt4j.components.StateChangeListener;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.widgets.MTColorPicker;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;
import ch.mitoco.components.visibleComponents.filechooser.FileChooser;
import ch.mitoco.main.MitocoScene;
import ch.mitoco.model.ModelAttributContent;
import ch.mitoco.model.ModelMtAttributs;
/**
 * MTDropDownList provide a DropDownList with five import levels.
 * 
 * @author tandrich
 *
 */

public class MTPictureBox extends Attributes {
	
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
	
	/** Picture Box.*/
	private MTRectangle pictureBox;
	
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
	private boolean loadButton = false;
	
	/** Factor for Thumbnail picture.*/
	private float factor;
	
	/** MTImageButton for change the picture.*/
	private MTImageButton buttonLoad;
	private MTImageButton buttonSet;
	
	/** The MTTextAre Numbers. */	
	//private MTTextArea pathText;
	
	/** MTColorPicker. 	 */
	 private MTColorPicker colorpicker;
	
	 /** MTButton for ColorPicker. */
	 private MTImageButton colPickButton;
	 
	 /**Filechooser objekt. */
	private FileChooser fileChooser;
	private String fileChooserPath;
	
	private MTTextArea error;
	 
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
	public MTPictureBox(final AbstractMTApplication app, ModelMtAttributs model, final int width, final int height, final String labelname, final IFont labelfont) {
		super(app);
		this.width = width;
		this.height = height;
		this.fname = labelname;
		this.labelfont = labelfont;
		this.app = app;
		this.model = model;
		
		
		dataRead(labelname); // Daten lesen
		fileChooserPath = "";
		
		this.init(app);
	}
	/** 
	 * Init MTPictureBox.
	 * @param app AbststractMTApplication
	 * */
	private void init(final AbstractMTApplication app) {		
		this.setSizeLocal(width, height);
		this.setStrokeColor(MTColor.BLACK);
		
		//setPath("Default");
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
		//FileChooser test = new FileChooser(app.getComponent(1));
		this.addChild(label);
		//this.addChild(buttonLoad);
		/*
		if (!getPath().equals(null)|| !getPath().equals("default")) {
			setPath();
		}
		*/
		
		createColorPicker();
		createLoadButton();
		createSetButton();
		loadImage();
	}
	
	/**Load Images for picturebox.
	 * Methode ist verantwortlich f�r das erstellen und laden eines Bilds.
	 */
	public void loadSelectedImage() {
		
		try {
		image = app.loadImage(getPath());	
		pictureBox = new MTRectangle(app, image);
		} catch (NullPointerException ex) {
			
			System.out.println("Wrong Path or picture not exits: " + ex);
			pictureBox = new MTRectangle(app, width, height);
			pictureBox.setFillColor(MTColor.RED);
			pictureBox.setNoStroke(true);
			error = new MTTextArea(app);
			error.setNoFill(true);
			error.setNoStroke(true);
			error.setText("Missing file or wrong path");
			error.setPickable(false);
			pictureBox.addChild(error);
		}
		
		picheight = pictureBox.getHeightXY(TransformSpace.LOCAL);
		picwidth = pictureBox.getWidthXY(TransformSpace.LOCAL);
			
		float factorHeight = picheight / (height - 5);
		float factorWidht = picwidth / (width - 5);
		
		System.out.println("H�he Factor: " + factorHeight + "Bild H�he : " + picheight + " Attribut H�he "+  height);
		System.out.println("Breiten Factor: " + factorWidht + "Bild Breite: " + picwidth + " Attribut breite " + width);
		
		
		
		if (factorHeight > factorWidht) {
			factor = factorHeight;
		} else {
			factor = factorWidht;
		}
		
		pictureBox.setSizeLocal(picwidth / factor, picheight / factor);
		pictureBox.setAnchor(PositionAnchor.CENTER);
		System.out.println("H�he " + pictureBox.getHeightXY(TransformSpace.LOCAL) + " Breite " + pictureBox.getWidthXY(TransformSpace.LOCAL));
		pictureBox.setPositionRelativeToParent(new Vector3D(width / 2, height / 2));
		pictureBox.setPickable(true);
		
		pictureBox.setGestureAllowance(DragProcessor.class, false);
		pictureBox.setGestureAllowance(ScaleProcessor.class, false);
		pictureBox.setGestureAllowance(RotateProcessor.class, false);
		pictureBox.setGestureAllowance(TapProcessor.class, true);
		//this.addChild(sa);
		
		pictureBox.registerInputProcessor(new TapProcessor(app, 25, true, 350));
		pictureBox.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				if (ge instanceof TapEvent) {
					TapEvent te = (TapEvent) ge;
					if (te.getTapID() == TapEvent.TAPPED) {
						if (minmax) {
						pictureBox.setSizeLocal(picwidth, picheight);
						
						buttonLoad.setVisible(true);
						
						minmax = false;
						} else {
						pictureBox.setSizeLocal(picwidth / factor, picheight / factor);
						
						buttonLoad.setVisible(false);
						
						minmax = true;
						}
					}	
				}
				return false;
			}
		});
		
		
		
		this.addChild(pictureBox);

	}
	
	/**
	 * Methode to change the picture.
	 * 
	 * �ber ein Button im Max-Modus besteht die M�glichkeit den Dateipfad des Bild zu �nderung und diese zu laden.
	 * 
	 * TODO: Keyboard mit dem Pfad durch ein Dateibrowser ersetzten.
	 */
	private void createLoadButton() {
		
		// Button for Rotate
		PImage buttonImage = app.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  "buttonLoadImage.png");
		buttonLoad = new MTImageButton(app, buttonImage);
		buttonLoad.setSizeLocal(30, 30);
		buttonLoad.translate(new Vector3D(0,0,0));
		buttonLoad.setFillColor(new MTColor(255, 255, 255, 200));
		buttonLoad.setName("KeyboardButton");
		buttonLoad.setNoStroke(true);
		//keyboardButton.translateGlobal(new Vector3D(-2,mtApplication.height-keyboardButton.getWidthXY(TransformSpace.GLOBAL)+2,0));
		buttonLoad.setPositionGlobal(new Vector3D(15, 15));
		
		buttonLoad.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				switch(te.getTapID()) {
				case TapEvent.TAPPED:
			
//					setPath("default");
					MitocoScene.drawFilechooser("image");
					loadButton = true;
					error.setVisible(false);
					MitocoScene.getFc().addStateChangeListener(StateChange.COMPONENT_DESTROYED, new StateChangeListener() {
						@Override
						public void stateChanged(final StateChangeEvent evt) {
							//if (!MitocoScene.getFilechooserPath().equals(null) || !getPath().equals(null)) {
								System.out.println("MtPictureBox: CloseFilechooser draw Image");
								setPath(MitocoScene.getFilechooserPath());
								try {
									image = app.loadImage(getPath());	
									pictureBox = new MTRectangle(app, image);
									} catch (NullPointerException ex) {
										
										System.out.println("Wrong Path or picture not exits: " + ex);
										pictureBox = new MTRectangle(app, width, height);
										pictureBox.setFillColor(MTColor.RED);
										pictureBox.setNoStroke(true);
										MTTextArea error = new MTTextArea(app);
										error.setNoFill(true);
										error.setNoStroke(true);
										error.setText("Missing file or wrong path");
										error.setPickable(false);
										pictureBox.addChild(error);
									}
									
									picheight = pictureBox.getHeightXY(TransformSpace.LOCAL);
									picwidth = pictureBox.getWidthXY(TransformSpace.LOCAL);
										
									float factorHeight = picheight / (height - 5);
									float factorWidht = picwidth / (width - 5);
									
									System.out.println("H�he Factor: " + factorHeight + "Bild H�he : " + picheight + " Attribut H�he "+  height);
									System.out.println("Breiten Factor: " + factorWidht + "Bild Breite: " + picwidth + " Attribut breite " + width);
									
									
									
									if (factorHeight > factorWidht) {
										factor = factorHeight;
									} else {
										factor = factorWidht;
									}
									
									pictureBox.setSizeLocal(picwidth / factor, picheight / factor);
									pictureBox.setAnchor(PositionAnchor.CENTER);
									System.out.println("H�he " + pictureBox.getHeightXY(TransformSpace.LOCAL) + " Breite " + pictureBox.getWidthXY(TransformSpace.LOCAL));
									pictureBox.setPositionRelativeToParent(new Vector3D(width / 2, height / 2));
									pictureBox.setPickable(true);
									
									pictureBox.setGestureAllowance(DragProcessor.class, false);
									pictureBox.setGestureAllowance(ScaleProcessor.class, false);
									pictureBox.setGestureAllowance(RotateProcessor.class, false);
									pictureBox.setGestureAllowance(TapProcessor.class, true);
									//this.addChild(sa);
									
									pictureBox.registerInputProcessor(new TapProcessor(app, 25, true, 350));
									pictureBox.addGestureListener(TapProcessor.class, new IGestureEventListener() {
										
										@Override
										public boolean processGestureEvent(MTGestureEvent ge) {
											if (ge instanceof TapEvent) {
												TapEvent te = (TapEvent) ge;
												if (te.getTapID() == TapEvent.TAPPED) {
													if (minmax) {
													pictureBox.setSizeLocal(picwidth, picheight);
													
													buttonLoad.setVisible(true);
													
													minmax = false;
													} else {
													pictureBox.setSizeLocal(picwidth / factor, picheight / factor);
													
													buttonLoad.setVisible(false);
													
													minmax = true;
													}
												}	
											}
											return false;
										}
									});
									
									
									
									addChild(pictureBox);

									
							}
						//}
					}
					
					);
					
					//if(getPath().equals("default") || getPath().equals(test)){
					//	setPath(test);
					//}
					//fileChooser = new FileChooser("",));
					//pictureBox.destroy();
					//loadImage();
					
				
					//fileChooser = new FileChooser("C:\\Workspace\\MT4J_KTSI", this);	
					//addChild(fileChooser.getUI());
		      		//fileChooser.sendToFront();
		          	//fileChooser.toggleFileChooser("xml");
		      		//fileChooser.getUI().sendToFront();
					
					/*
					textKeyboard.addStateChangeListener(StateChange.COMPONENT_DESTROYED, new StateChangeListener() {
						@Override
						public void stateChanged(final StateChangeEvent evt) {
							setPath(pathText.getText());
							pictureBox.destroy();
							loadImage();
						}
					}
					
					);*/
					break;	
				default:
					break;
				}
			return false;
			}
		});
		buttonLoad.setVisible(false);
		this.addChild(buttonLoad);
		
	}
	
	/**Laods the Image from path.
	 * 
	 */
	private void loadImage() {
		if (!MitocoScene.getFilechooserPath().equals(null) || !getPath().equals(null)) {
			if(loadButton){
				setPath(MitocoScene.getFilechooserPath());
			}
		
			System.out.println("Load Image SetButton " + getPath());
			try {
				
				image = app.loadImage(getPath());
				pictureBox = new MTRectangle(app, image);
				}
			catch (NullPointerException ex) {
					
					System.out.println("Wrong Path or picture not exits: " + ex);
					pictureBox = new MTRectangle(app, width - 200, height - 70);
					pictureBox.setFillColor(MTColor.FUCHSIA);
					pictureBox.setNoStroke(true);
					error = new MTTextArea(app);
					error.setNoFill(true);
					error.setNoStroke(true);
					error.setText("Please choose File");
					error.setPickable(false);
					pictureBox.addChild(error);
				}
				
				picheight = pictureBox.getHeightXY(TransformSpace.LOCAL);
				picwidth = pictureBox.getWidthXY(TransformSpace.LOCAL);
					
				float factorHeight = picheight / (height - 5);
				float factorWidht = picwidth / (width - 5);
				
				System.out.println("H�he Factor: " + factorHeight + "Bild H�he : " + picheight + " Attribut H�he "+  height);
				System.out.println("Breiten Factor: " + factorWidht + "Bild Breite: " + picwidth + " Attribut breite " + width);
				
				
				
				if (factorHeight > factorWidht) {
					factor = factorHeight;
				} else {
					factor = factorWidht;
				}
				
				pictureBox.setSizeLocal(picwidth / factor, picheight / factor);
				pictureBox.setAnchor(PositionAnchor.CENTER);
				System.out.println("H�he " + pictureBox.getHeightXY(TransformSpace.LOCAL) + " Breite " + pictureBox.getWidthXY(TransformSpace.LOCAL));
				pictureBox.setPositionRelativeToParent(new Vector3D(width / 2, height / 2));
				pictureBox.setPickable(true);
				
				pictureBox.setGestureAllowance(DragProcessor.class, false);
				pictureBox.setGestureAllowance(ScaleProcessor.class, false);
				pictureBox.setGestureAllowance(RotateProcessor.class, false);
				pictureBox.setGestureAllowance(TapProcessor.class, true);
				//this.addChild(sa);
				
				pictureBox.registerInputProcessor(new TapProcessor(app, 25, true, 350));
				pictureBox.addGestureListener(TapProcessor.class, new IGestureEventListener() {
					
					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						if (ge instanceof TapEvent) {
							TapEvent te = (TapEvent) ge;
							if (te.getTapID() == TapEvent.TAPPED) {
								if (minmax) {
								pictureBox.setSizeLocal(picwidth, picheight);
								
								buttonLoad.setVisible(true);
								
								minmax = false;
								} else {
								pictureBox.setSizeLocal(picwidth / factor, picheight / factor);
								
								//buttonLoad.setVisible(false);
								
								minmax = true;
								}
							}	
						}
						return false;
					}
				});
				
				
				
				addChild(pictureBox);

				
		}
		
	}
	
	
	private void createSetButton() {
		
		// Button for Rotate
		PImage buttonImage = app.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  "buttonSetImage.png");
		buttonSet = new MTImageButton(app, buttonImage);
		buttonSet.translate(new Vector3D(0,30,0));
		buttonSet.setSizeLocal(30, 30);
		buttonSet.setFillColor(new MTColor(255, 255, 255, 200));
		buttonSet.setName("SetImage");
		buttonSet.setNoStroke(true);
		//buttonSet.translateGlobal(new Vector3D(45, 45));
		//buttonSet.setPositionGlobal(new Vector3D(45, 15));
		
		buttonSet.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				switch(te.getTapID()) {
				case TapEvent.TAPPED:
					error.setVisible(false);
					loadImage();
					//if(imagePath.equals(null)){
					//	setPath("Default");
					//}
					
					//pictureBox.destroy();
					//loadSelectedImage();
					break;	
				default:
					break;
				}
			return false;
			}
		});
		buttonSet.setVisible(false);
		this.addChild(buttonSet);
		
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
	 * Element "Path" f�r den Speicherort des Bildpfads
	 * 
	 * @param defaultlabeltext String
	 */
	private void dataRead(final String defaultlabeltext) {
		
		// Data transfer for value
		if (model.getAttributcontent() == null) {
			//setPath("Default");
			System.out.println("PictureBox: no Attributcontent found ");
			
			//Double.valueOf(textarea.getText()).doubleValue();
		} else {
			for (ModelAttributContent it : model.getAttributcontent()) {
				if (it.getType().equalsIgnoreCase("Path")) {
					System.out.println("PictureBox: Value zu Typ String " + it.getValue());
					setPath(it.getValue());
					break;
				} else {
					System.out.println("PictureBox: Value zu Typ Path NICHT GEFUNDEN ");
					//setPath("Default");
				}
			}
		}
		
		// Data transfer for labeltext
		if (model.getLable() == null || model.getLable().isEmpty() ) {
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
		
		
	}
	
	
	/**
	 * Write changed Date in the datamodel.
	 */
	public void dataWrite() {
		for (ModelAttributContent it : model.getAttributcontent()) {
			if (it.getType().equalsIgnoreCase("Path")) {
				it.setValue(getPath());
			} else {
				//TODO 
			}
		}
		model.setAttcolor(getFillColor());
	}
	
	/**
	 * Set Attribut to min Modus.
	 */
	public final void setMin() {
		label.setVisible(false);
		colPickButton.setVisible(false);
		buttonLoad.setVisible(false);
		buttonSet.setVisible(false);
	}
	
	/**
	 * Set Attribut to max Modus.
	 */
	public final void setMax() {
		label.setVisible(true);
		colPickButton.setVisible(true);
		buttonLoad.setVisible(true);
		buttonSet.setVisible(true);
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
