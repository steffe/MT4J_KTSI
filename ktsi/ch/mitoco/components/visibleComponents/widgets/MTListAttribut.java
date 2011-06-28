package ch.mitoco.components.visibleComponents.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.mt4j.AbstractMTApplication;
import org.mt4j.MTApplication;
import org.mt4j.components.StateChange;
import org.mt4j.components.StateChangeEvent;
import org.mt4j.components.StateChangeListener;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTColorPicker;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.MTTextArea.ExpandDirection;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.gestureAction.InertiaDragAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.components.visibleComponents.layout.MTRowLayout2D;

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
	
	/** Default String.*/
	private String stringValue;
		
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
	 
	 /** Fonttype Attributtext. */
	 private IFont fontArialMini;
	 
	 /** ArrayList for Values in Scorecard. */
	 private ArrayList<MTTextArea> al;
	 
	 /** ArrayList for Keys in Scorecard. */
	 private ArrayList<MTTextArea> algt;
	
	 /** HashMap for actual Values from XML to be sorted*/
	 private HashMap<String, String> hm;
	 
	 /** Default String for MTList Keys. */
	 private String defaultString;
	 
	 /** Default Label Text for MTList. */
	 private String defaultLabelText;
	 
	 /** Counter which is needed in several methods to calculate the Hole Number. */
	 private int h = 1;
	 
	 /** ArrayList where Keys and Values from al and algt ArrayLists are stored. */
	 private ArrayList<MTRoundRectangle> alrr; 


	 /** Default Constructor. 
	  *  @param app MTApplication Object
	  *  @param model DataModel where Attributs are read 
	  *  @param fontArialMini FontType for Attributtext
	  *  @param width Width for this Object
	  *  @param height Height for this Object
	  *  @param labelname Name of the Label
	  *  @param labelfont FontType for the Label
	  * */
	public MTListAttribut(final AbstractMTApplication app, final ModelMtAttributs model, final IFont fontArialMini, final int width, final int height, final String labelname, final IFont labelfont) {
			super(app);
		
			this.model = model;
			
			defaultString = "Default";
			defaultLabelText = "DefaultLabel";
			
			//this.dataRead(defaultString, defaultLabelText); // Read Data from Model
			
			this.fname = labelname;
			this.labelfont = labelfont;
			System.out.println("Model from TextAttributs -> " + model);
			
			//dataRead(defaultString, labeltext);
			
			this.setName(labelname);
			this.height = height;
			this.width = width;
			this.init(app, fontArialMini);	
	}
	
	/** Init Method to create the Object.
	 * 
	 * @param app MTApplication Object
	 * @param font FontType for Attributtext
	 */
	 
	private void init(final AbstractMTApplication app, final IFont font) {
			this.iF = font;
			app1 = app;
			fontsize = iF.getOriginalFontSize();	
			
			defaultString = "Default";
			defaultLabelText = "DefaultLabel";
			
			this.setSizeLocal(width, height);
			this.setFillColor(blue1);
			this.setStrokeColor(MTColor.BLACK);
						
			label = new MTTextArea(app, 0, -labelfont.getOriginalFontSize(), width - 100, height, labelfont);
			label.setInnerPadding(0);
			label.setNoFill(true);
			label.setStrokeColor(MTColor.LIME);
			label.setNoStroke(true);
			label.setText(fname);
			label.setPickable(false);
			label.setVisible(false);
			
			/*
			 * 18 MTTextAreas bauen für die verschiedenen Löcher
			 * Wir brauchen 18 Felder. Diese müssen identisch sein 
			 * mit den Einträgen in der objectlist1.xml
			 */
			al = new ArrayList<MTTextArea>();
			for (int j = 0; j < 18; j++) {
				al.add(new MTTextArea(app));
			}

			for (MTTextArea a : al) {
				a.setFontColor(new MTColor(MTColor.BLACK));
				a.setNoFill(true);
				a.setNoStroke(true);
				a.setSizeLocal(90, 30);
			}
			
			algt = new ArrayList<MTTextArea>();
			for (int j = 0; j < 18; j++) {
				algt.add(new MTTextArea(app));
			}
			
			for (MTTextArea a : algt) {
				a.setFontColor(new MTColor(MTColor.BLACK));
				a.setNoFill(true);
				a.setNoStroke(true);
				a.setSizeLocal(90, 30);
			}
			
			alrr = new ArrayList<MTRoundRectangle>();
			for (int j = 0; j < 18; j++) {
				alrr.add(new MTRoundRectangle(app, 0, 0, 0, 200, 30, 5, 5));
				alrr.get(j).setFillColor(MTColor.GREEN);
			}
			
			//HashMap um via dataRead den Inhalt aus dem File zu lesen
			hm = new HashMap<String, String>();
			this.dataRead();
			
			//Sort
			Map sortedMap = new TreeMap(hm);
			
	        // Get hashmap in Set interface to get key and value
	        Set s = sortedMap.entrySet();
	        
	   	 	// Move next key and value of HashMap by iterator
	        Iterator it = s.iterator();
	        
	        //Counter for the ArrayLists
	        h = 0;
	        
	        while (it.hasNext()) {
	            // key=value separator this by Map.Entry to get key and value
	            Map.Entry m = (Map.Entry) it.next();

	            // getKey is used to get key of HashMap
	            String key = (String) m.getKey();

	            // getValue is used to get value of key in HashMap
	            String value = (String) m.getValue();
	            
	            //Value Add to ArrayList
	            al.get(h).setText(value);
	            
	            //Key Add to ArrayList
	            algt.get(h).setText(key);
	            	            
	            //Add to RR ArrayList
	            alrr.get(h).addChild(algt.get(h));
				alrr.get(h).addChild(al.get(h));
				
				//Move one Child that childs not lie on each other
				al.get(h).translate(new Vector3D(95, 0));
	            h++;
	        }

			fontArialMini = FontManager.getInstance().createFont(app, "arial.ttf", 
					14, 	//Font size
					MTColor.BLACK);
			
			MTList lista = new MTList(app, 0, 0, width, height, 5);
			lista.setNoFill(true);
			lista.setNoStroke(true);
			lista.addGestureListener(DragProcessor.class, new InertiaDragAction());
			
			for (int i = 0; i < al.size(); i++) {
				//Hier kann man Groesse der eigentlichen Zelle verändern
				MTListCell cell = new MTListCell(app, 200, 30);
				cell.setNoFill(true);
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
								textKeyboard.setFillColor(trans);
								textKeyboard.setNoStroke(true);
								textKeyboard.setPositionRelativeToParent(new Vector3D(textKeyboard.getWidthXY(TransformSpace.LOCAL) / 2, (textKeyboard.getHeightXY(TransformSpace.LOCAL) / 2) + 50));
							
								textKeyboard.addTextInputListener(al.get(Integer.valueOf(ge.getCurrentTarget().getName())));
								
								addChild(textKeyboard);
							
								textKeyboard.setPositionRelativeToParent(new Vector3D(textKeyboard.getWidthXY(TransformSpace.LOCAL) / 2, textKeyboard.getWidthXY(TransformSpace.LOCAL) / 2 + 50));
							
								textKeyboard.addStateChangeListener(StateChange.COMPONENT_DESTROYED, new StateChangeListener() {
									@Override
									public void stateChanged(final StateChangeEvent evt) {
										String key = String.valueOf(algt.get(Integer.valueOf(ge.getCurrentTarget().getName())).getText());
										String val = String.valueOf(al.get(Integer.valueOf(ge.getCurrentTarget().getName())).getText());
										dataWrite(key, val);
										
										
									}
								});
							}
						}	
						return false;
					}
				}); 
				cell.addChild(alrr.get(i));
				lista.addListElement(cell);
			}
			lista.setPickable(false);
			this.setPickable(false);
			this.addChild(label);
			this.addChild(lista);
			
			this.setVisible(true);
			createColorPicker();
		}
		
		/** 
		 * Write data in Datamodel.
		 * @param key Key to find the corresponding Entry in XML
		 * @param val Value to write to the corresponding Key Entry in XML
		 */
		public final void dataWrite(final String key, final String val) {
			for (ModelAttributContent it : model.getAttributcontent()) {
				if (it.getType().equalsIgnoreCase(key)) {
					System.out.println("If geht................................................" + key + "Val: " + val);
					it.setValue(val);
					break;
				} 
			}
			model.setAttcolor(getFillColor());
		}
		
		/** Read from datamodel and insert in the gui elements. */
		private void dataRead() {
			// Data transfer for value
			if (model.getAttributcontent() == null) {
				stringValue = defaultString;
			} else {
				for (ModelAttributContent it : model.getAttributcontent()) {
						hm.put(it.getType(), it.getValue());
						//hier muss Counter erhöht werden, damits beim nächsten Durchgang + 1 ist
						h++;
				}
			}
		}
		
		/** Colorpicker. */
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
