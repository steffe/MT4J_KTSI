package ch.mitoco.components.visibleComponents.widgets;

import java.util.ArrayList;
import java.util.HashMap;

import org.mt4j.AbstractMTApplication;
import org.mt4j.MTApplication;
import org.mt4j.components.StateChange;
import org.mt4j.components.StateChangeEvent;
import org.mt4j.components.StateChangeListener;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTColorPicker;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.MTTextArea.ExpandDirection;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;
import ch.mitoco.components.visibleComponents.widgets.keyboard.MTNumKeyboard;
import ch.mitoco.model.ModelAttributContent;
import ch.mitoco.model.ModelMtAttributs;


/**MtList Attribut.
 * MTList FieldAttribut, kann eine Liste mit verschiedenem Inhalt anzeigen und Scrollen.
 * @author rfeigenwinter
 *
 */
public class MTListAttribut extends Attributes {
	/** Main Textfield. */
	private MTTextArea textarea;
	
	/** Attribut height. */
	private int height;
	
	/** Attribut width. */
	private int width;
	
	/** Fontsize from textarea. */
	private int fontsize;

	/** Textfield font.*/
	private IFont iF;
	
	/** Label Font.*/
	private String fname;
		
	/** The MTTextAre Numbers. */	
	private MTTextArea numKeyText;
	
	/** The Numb Label. */
	private MTTextArea label;
	
	/** Label Font.*/
	private IFont labelfont;
	
	/** Transparenz Color. */
	 private MTColor trans = new MTColor(0, 0, 0, 10);
	 
	 /** Default Color for Fill Rectangle. */
	 private MTColor blue1 = new MTColor(51, 102, 204, 180);
	 
	/** Attribut Model Object. */
	 private ModelMtAttributs model;
	 
	/** MTColorPicker. 	 */
	 private MTColorPicker colorpicker;
	 
	 /** MTButton for ColorPicker. */
	 private MTImageButton colPickButton;
	 
	 /** Abstract MT Application.*/
	 private AbstractMTApplication app1;
	 
	private IFont fontArialMini;
	private ArrayList<MTTextArea> al;
	private HashMap<String, MTTextArea> hm;
	private int i;


	 
	 //public MTListAttribut(final AbstractMTApplication app, ModelMtAttributs model, final IFont fontArialMini, final int width, final int height, final String labeltext, final IFont labelfont) {
	 public MTListAttribut(final AbstractMTApplication app, final IFont fontArialMini, final int width, final int height, final String labelname, final IFont labelfont) {
			super(app);
			//this.rAlign = rightalign;
			//stringvalue = defaultString;
			//fname = labeltext;
			
			this.model = model;
			this.fname = labelname;
			this.labelfont = labelfont;
			System.out.println("Model from TextAttributs -> " + model);
			
			//dataRead(defaultString, labeltext);
			
			this.setName(labelname);
			this.height = height;
			this.width = width;
			this.init(app, fontArialMini);	
		}
	 
		private void init(final AbstractMTApplication app, final IFont font) {
			this.iF = font;
			app1 = app;
			fontsize = iF.getOriginalFontSize();		
			
			this.setSizeLocal(width, height);
			this.setFillColor(blue1);
			this.setStrokeColor(MTColor.BLACK);
			
			label = new MTTextArea(app, 0, -labelfont.getOriginalFontSize(), width, height, labelfont);
			label.setInnerPadding(0);
			label.setNoFill(true);
			label.setStrokeColor(MTColor.LIME);
			label.setNoStroke(true);
			label.setText(fname);
			label.setPickable(false);
			label.setVisible(false);
						
			MTColor col = new MTColor(MTColor.BLACK);
			
			//18 MTTextAreas bauen für die verschiedenen Löcher
			al = new ArrayList<MTTextArea>();
			al.add(new MTTextArea(app));
			al.add(new MTTextArea(app));
			al.add(new MTTextArea(app));
			al.add(new MTTextArea(app));
			al.add(new MTTextArea(app));
			for (MTTextArea a : al) {
				a.setFontColor(new MTColor(MTColor.BLACK));
				a.setText("0");
				a.setNoFill(true);
				a.setNoStroke(true);
			}
			
			/*
			hm = new HashMap<String, MTTextArea>();
			hm.put("Loch 1: ", new MTTextArea(app));
			hm.put("Loch 2: ", new MTTextArea(app));
			for (String a : hm.keySet()) {
				hm.get(a).setFontColor(new MTColor(MTColor.BLACK));
				hm.get(a).setText("0");
			}
			*/
			fontArialMini = FontManager.getInstance().createFont(app, "arial.ttf", 
					14, 	//Font size
					MTColor.BLACK);
			
			MTList lista = new MTList(app, 0, 0, width, height, 5);
			lista.setNoFill(true);
			lista.setNoStroke(true);
			//lista.addGestureListener(DragProcessor.class, new InertiaDragAction());
			
			//6x hm durch al ersetzen je nach dem
			for (i = 0; i < al.size(); i++) {
			//for (i = 0; i < hm.size(); i++) {
				
				MTListCell cell = new MTListCell(app, al.get(i).getWidthXY(TransformSpace.LOCAL) + 100, al.get(i).getHeightXY(TransformSpace.LOCAL));
				//cell.setPositionRelativeToParent(new Vector3D(50, 50));
				cell.setPositionGlobal(new Vector3D(50, 50));
				cell.setNoFill(true);
				//cell.setPositionRelativeToParent(new Vector3D(this.getWidthXY(TransformSpace.LOCAL) / 2, x + 200));
				//MTListCell cell = new MTListCell(app, hm.get(i).getWidthXY(TransformSpace.LOCAL), hm.get(i).getHeightXY(TransformSpace.LOCAL));
				cell.registerInputProcessor(new TapProcessor(app, 15));
				cell.setName(Integer.toString(i));
				cell.addGestureListener(TapProcessor.class, new IGestureEventListener() {
					public boolean processGestureEvent(final MTGestureEvent ge) {
						if (ge instanceof TapEvent) {
							TapEvent te = (TapEvent) ge;
							System.out.println(te.getTapID() + " (" + te.getId() + ")");
							if (te.getTapID() == TapEvent.TAPPED) {
								System.out.println("Button Clicked");
								
								//Bevor wir einen neuen Wert schreiben löschen wir den bestehenden Inhalt
								al.get(Integer.valueOf(ge.getCurrentTarget().getName())).clear();
						
								MTNumKeyboard textKeyboard = new MTNumKeyboard(app);
							
								numKeyText = new MTTextArea(app, 0, 0, 300, 50, fontArialMini);
								numKeyText.setExpandDirection(ExpandDirection.UP);
								textKeyboard.setNoStroke(true);
								numKeyText.setFillColor(new MTColor(205, 200, 177, 255));
								numKeyText.unregisterAllInputProcessors();
								numKeyText.setEnableCaret(true);
							
								textKeyboard.snapToKeyboard(numKeyText);
							
								/* Try and Catch Example (catch ist interessant)
								try {
									//System.out.println("Out: " + list.get(ge.getCurrentTarget().getID()).getText());
									System.out.println(list.get(Integer.valueOf(ge.getCurrentTarget().getName())).getText());
								
								} catch (IndexOutOfBoundsException ioobe){
									System.err.println("Fehler!!");
								}
								 */
							
								numKeyText.setText(al.get(Integer.valueOf(ge.getCurrentTarget().getName())).getText());
								//numKeyText.setText(hm.get(Integer.valueOf(ge.getCurrentTarget().getName())).getText());
								textKeyboard.addTextInputListener(numKeyText);
							
								addChild(textKeyboard);
							
								textKeyboard.setPositionRelativeToParent(new Vector3D(textKeyboard.getWidthXY(TransformSpace.LOCAL) / 2, textKeyboard.getWidthXY(TransformSpace.LOCAL) / 2 + 50));
							
								textKeyboard.addStateChangeListener(StateChange.COMPONENT_DESTROYED, new StateChangeListener() {
									@Override
									public void stateChanged(final StateChangeEvent evt) {
										al.get(Integer.valueOf(ge.getCurrentTarget().getName())).setText(numKeyText.getText());
										//hm.get(Integer.valueOf(ge.getCurrentTarget().getName())).setText(numKeyText.getText());
									}
								});
							}
						}	
						return false;
					}
				});
				cell.addChild(al.get(i));
				//cell.addChild(hm.get(i));
				lista.addListElement(cell);
			}
			this.addChild(label);
			this.addChild(lista);
			
			this.setVisible(true);
			createColorPicker();
			
		}
		
		
		
		/** 
		 * Colorpicker.
		 */
		private void createColorPicker() {
	   
	        PImage colPick = app1.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  "colorcircletr.png");
	        colorpicker = new MTColorPicker(app1, 0, 0, colPick);
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
	        
	        PImage colPickIcon = app1.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  "ColorPickerIcon.png");
	        colPickButton = new MTImageButton(app1, colPickIcon);
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
		 * Set Attribut to min Modus.
		 */
		@Override
		public final void setMin() {
			label.setVisible(false);
			colPickButton.setVisible(false);
		}
		
		/**
		 * Set Attribut to max Modus.
		 */
		@Override
		public final void setMax() {
			label.setVisible(true);
			colPickButton.setVisible(true);
		}
		


		/** 
		 * Set Value Text.
		 * @param value Double
		 */
		public final void setValue(final double value) {
			textarea.setText(Double.toString(value));
		}
		
		/** 
		 *Get Value Text.
		 *@return dString Double 
		 */
		public final double getValue() {
			return Double.valueOf(textarea.getText()).doubleValue();
		}
	 
}
