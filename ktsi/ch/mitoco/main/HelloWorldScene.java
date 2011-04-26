package ch.mitoco.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.components.visibleComponents.widgets.MTSuggestionTextArea;

import ch.mitoco.components.visibleComponents.MyMTObject;

import processing.core.PImage;

public class HelloWorldScene extends AbstractScene {

	private MTApplication mtApplication;
	private MyMTObject t1;
	private List<MyMTObject> myobjectList;
	private int counter;
	
	/**
	 * List mit MyObjects
	 * 
	 * Erstellen:
	 * private List<MyMTObjects> myobjectList=new ArrayList<MyMTObject>();
	 * 
	 * Objekt Hinzufügen
	 * myobjectList.add( new MyMTObject(mtApplication) );
	 */
	
	/** 
	 * Legt max Objecte für die Applikations fest
	 */
	private int maxMyObjects;
	
	public HelloWorldScene(MTApplication mtAppl, String name) {
		super(mtAppl, name);
		this.mtApplication=mtAppl;
		this.setClearColor(new MTColor(100, 100, 100, 255));
		
		//Show touches
		this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));
		
		maxMyObjects=30;
		counter=0;
		myobjectList=new ArrayList<MyMTObject>();
		
		
		
		//New Object
		PImage buttonNewImage = mtApplication.loadImage("ch" + MTApplication.separator + "mitoco"+ MTApplication.separator + "data" + MTApplication.separator +  "buttonNew.png");
		MTImageButton buttonNew= new MTImageButton(mtApplication, buttonNewImage);

		buttonNew.setSizeLocal(40, 40);
		buttonNew.setFillColor(new MTColor(255,255,255,200));
		buttonNew.setName("KeyboardImage");
		buttonNew.setNoStroke(true);
		buttonNew.setPositionGlobal(new Vector3D(20,20));
		
		buttonNew.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				switch(te.getTapID()){
				case TapEvent.TAPPED:
					//MyMTObject t1 = new MyMTObject(mtApplication,1);
					//getCanvas().addChild(t1.getMyObjectBack());
					myobjectList.add( new MyMTObject(mtApplication,counter) );				
					getCanvas().addChild(myobjectList.get(counter).getMyObjectBack());	
					System.out.println("Zähler vor Add: " +counter);
					counter++;
					System.out.println("Zähler nach Add: " +counter);
					break;
				
				default:
					break;
				}
				
				
				
				
			return false;
			}
		});
	
		//New Object
		PImage buttonDELImage = mtApplication.loadImage("ch" + MTApplication.separator + "mitoco"+ MTApplication.separator + "data" + MTApplication.separator +  "buttonDel.png");
		MTImageButton buttonDel= new MTImageButton(mtApplication, buttonDELImage);
		buttonDel.setSizeLocal(40, 40);
		buttonDel.setFillColor(new MTColor(255,255,255,200));
		buttonDel.setName("KeyboardImage");
		buttonDel.setNoStroke(true);
		buttonDel.setPositionGlobal(new Vector3D(mtAppl.getWidth()-20,20));
		
		buttonDel.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				// TODO Auto-generated method stub
				TapEvent te = (TapEvent)ge;
				switch(te.getTapID()){
				case TapEvent.TAPPED:
					if(counter != 0){
					
					getCanvas().removeChild(myobjectList.get(counter-1).getMyObjectBack()); // Entfernen des Objekts auf dem Canvas
					myobjectList.remove(counter-1); // Remove Object from arryeList
					
					System.out.println("Zähler rem vor: " +counter);
					counter--;
				}
					else {
					System.out.println("Kein Objeckt zum enfernen vorhanden " +counter);	
					}
					break;
				
				default:
					break;
				}
			return false;
			}
		});
		
		/*
		final String[] animals = { "sehr wichtig", "wichtig", "nötig","vorhanden", "unnötig", "nicht benötigt"};
		final List<String> list = Arrays.asList( animals);
		MTSuggestionTextArea sa = new MTSuggestionTextArea(mtAppl, 300, list);
		sa.setPickable(true);
		
		// Font for Textfield
		IFont fontArialMini = FontManager.getInstance().createFont(mtAppl, "arial.ttf", 
				14, 	//Font size
				MTColor.BLACK);
		
		MTDropDownList d1 = new MTDropDownList(mtAppl, fontArialMini, 200, 40, 4, "Test", fontArialMini);
		*/
		
		
		//this.getCanvas().addChild(sa);
		//this.getCanvas().addChild(d1);
		this.getCanvas().addChild(buttonDel);
		this.getCanvas().addChild(buttonNew);
		
	}
	@Override
	public void init() {
		
		
	}
	@Override
	public void shutDown() {
		
		
	}
}
