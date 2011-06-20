package ch.mitoco.components.visibleComponents.widgets;

import java.util.ArrayList;

import org.mt4j.AbstractMTApplication;
import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTColorPicker;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;
import ch.mitoco.model.ModelAttributContent;
import ch.mitoco.model.ModelMtAttributs;


/**MtList Attribut.
 * MTList FieldAttribut, kann eine Liste mit verschiedenem Inhalt anzeigen und Scrollen.
 * @author steffe
 *
 */
public class MTListAttribut extends Attributes{
	/** Main Textfield. */
	private MTTextArea textarea;
	
	/** ListElement. */
	private MTList Listelement;
	
	/** Attribut height. */
	private int height;
	
	/** Attribut width. */
	private int width;
	
	/** Fontsize from textarea. */
	private int fontsize;

	/** Textfield font.*/
	private IFont iF;
	
	/** Set Align to right side.*/
	private boolean rAlign;
	
	/** Default double.*/
	private String stringvalue;
	
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

	 
	 public MTListAttribut(final AbstractMTApplication app, ModelMtAttributs model, final IFont fontArialMini, final int width, final int height, final boolean rightalign, final String defaultString, final String labeltext, final IFont labelfont) {
			super(app);
			this.rAlign = rightalign;
			stringvalue = defaultString;
			fname = labeltext;
			
			this.model = model;
			System.out.println("Model from TextAttributs -> " + model);
			
			dataRead(defaultString, labeltext);
			
			this.height = height;
			this.width = width;
			this.labelfont = labelfont;
			this.init(app, fontArialMini);	
		}
	 
		private void init(final AbstractMTApplication app, final IFont font) {
			this.iF = font;
			app1 = app;
			fontsize = iF.getOriginalFontSize();		
			
			this.setSizeLocal(width, height);
			this.setFillColor(blue1);
			this.setStrokeColor(MTColor.BLACK);
			setAlign(rAlign);
			textarea.setText("Test2");
			ArrayList<MTTextArea> listaImagenes = new ArrayList<MTTextArea>();
			listaImagenes.add(textarea);
			listaImagenes.add(textarea);
			listaImagenes.add(textarea);
			listaImagenes.add(textarea); 

			MTList lista = new MTList(app1, app1.width - 220,app1.height - 503, app1.width - 800, app1.height - 269);
			

			for (int i = 0; i < listaImagenes.size(); i++) {
			  MTListCell cell = new MTListCell(app1, listaImagenes.get(i).getWidthXY(TransformSpace.LOCAL), listaImagenes.get(i).getHeightXY(TransformSpace.LOCAL));
			  cell.addChild(listaImagenes.get(i));
			  lista.addListElement(cell);
			} 
			
			label = new MTTextArea(app, 0, -labelfont.getOriginalFontSize(), width, height, labelfont);
			label.setInnerPadding(0);
			label.setNoFill(true);
			label.setStrokeColor(MTColor.LIME);
			label.setNoStroke(true);
			label.setText(fname);
			label.setPickable(false);
			label.setVisible(false);
			
			// Add Object to base Object
			
			//this.addChild(textarea);
			this.addChild(label);
			this.setVisible(true);
			createColorPicker();
			
		}
		
		
		/** 
		 * Read from datamodel and insert in the gui elements.
		 *
		 * @param defaultString String 
		 * @param defaultlabeltext String
		 */
		private void dataRead(final String defaultString, final String defaultlabeltext) {
			// Data transfer for value
			if (model.getAttributcontent() == null) {
				stringvalue = defaultString;
				//Double.valueOf(textarea.getText()).doubleValue();
			} else {
				for (ModelAttributContent it : model.getAttributcontent()) {
					if (it.getType().equalsIgnoreCase("Double")) {
						System.out.println(" Value zu Typ String " + it.getValue());
						stringvalue = it.getValue();
						break;
					} else {
						System.out.println(" Value zu Typ String NICHT GEFUNDEN ");
						stringvalue = defaultString;
					}
				}
			}
			
			// Data transfer for Align
			if (model.getAttributcontent() == null) {
				rAlign = Boolean.parseBoolean(defaultString);
			} else {
				for (ModelAttributContent it : model.getAttributcontent()) {
					if (it.getType().equalsIgnoreCase("Align")) {
						System.out.println(" Value zu Typ Align " + it.getValue());
						rAlign = Boolean.parseBoolean(it.getValue());
						break;
					} else {
						System.out.println(" Value zu Typ Align NICHT GEFUNDEN ");
						rAlign = Boolean.parseBoolean(defaultString);
					}
					
				}
			}
			
			// Data transfer for labeltext
			if (model.getLable() == null) {
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
		 * Write data in Datamodel.
		 */
		public final void dataWrite() {
			for (ModelAttributContent it : model.getAttributcontent()) {
				if (it.getType().equalsIgnoreCase("Double")) {
					it.setValue(textarea.getText());
				} else {
					//TODO Was ist zu tun wenn dieses Werte Paar noch nicht exitiert???
				}
			}
			model.setAttcolor(getFillColor());
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
	        colPickButton.translate(new Vector3D(width - height, 0, 0));
	        colPickButton.setNoStroke(true);
	        colPickButton.setSizeLocal(height, height);
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

		/** Set Textalign from textarea.
		 * @param align (true=right)
		 * */
		public final void setAlign(final boolean align) {
			
			if (align == true) {
				float textwidth = textarea.getTextWidth();	
				System.out.println("Text Breite: = " + textwidth);
				System.out.println("Verschiebsatz: = " + (width - ((int) textwidth + 5)));
				textarea.setInnerPaddingLeft((width - ((int) textwidth + 5)));
			} else {
				textarea.setInnerPaddingLeft(5);
			}
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
