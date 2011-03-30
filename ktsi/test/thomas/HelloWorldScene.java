package test.thomas;

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.TabableView;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.MTTextArea.ExpandDirection;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.components.visibleComponents.widgets.buttons.MTSvgButton;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTKeyboard;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import com.sun.opengl.impl.packrect.Rect;
import com.sun.xml.internal.bind.v2.TODO;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation.ANONYMOUS;

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
		counter=1;
		myobjectList=new ArrayList<MyMTObject>();
		
		
		
		//New Object
		PImage buttonNewImage = mtApplication.loadImage("test" + MTApplication.separator + "thomas"+ MTApplication.separator + "image" + MTApplication.separator +  "buttonNew.png");
		MTImageButton buttonNew= new MTImageButton(buttonNewImage, mtApplication);
		buttonNew.setSizeLocal(40, 40);
		buttonNew.setFillColor(new MTColor(255,255,255,200));
		buttonNew.setName("KeyboardImage");
		buttonNew.setNoStroke(true);
		buttonNew.setPositionGlobal(new Vector3D(20,20));
		
		buttonNew.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				switch(e.getID()){
				case TapEvent.BUTTON_CLICKED:
					//MyMTObject t1 = new MyMTObject(mtApplication,1);
					myobjectList.add( new MyMTObject(mtApplication,counter) );				
					getCanvas().addChild(myobjectList.get(counter).getMyObjectBack());	
						
					counter++;
						
				break;
				
				default:
					break;
				};
				
				
			}
		});
		
		
		//New Object
		PImage buttonDELImage = mtApplication.loadImage("test" + MTApplication.separator + "thomas"+ MTApplication.separator + "image" + MTApplication.separator +  "buttonDel.png");
		MTImageButton buttonDel= new MTImageButton(buttonDELImage, mtApplication);
		buttonDel.setSizeLocal(40, 40);
		buttonDel.setFillColor(new MTColor(255,255,255,200));
		buttonDel.setName("KeyboardImage");
		buttonDel.setNoStroke(true);
		buttonDel.setPositionGlobal(new Vector3D(mtAppl.getWidth()-20,20));
		
		buttonDel.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				switch(e.getID()){
				case TapEvent.BUTTON_CLICKED:
					counter--;
					break;
				
				default:
					break;
				};
				
				
			}
		});


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
