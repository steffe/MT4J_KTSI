package store;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;
import ch.mitoco.store.generated.Customer;
import ch.mitoco.store.generated.ObjectFactory;
import ch.mitoco.store.generated.PurchaseOrder;

/** Hello Word Scene. */

public class HelloWorldScene extends AbstractScene {


	
	/**
	 * List mit MyObjects
	 * 
	 * Erstellen:
	 * private List<MyMTObjects> myobjectList=new ArrayList<MyMTObject>();
	 * 
	 * Objekt Hinzufügen
	 * myobjectList.add( new MyMTObject(mtApplication) );
	 */
	
	/** MTApplication. */
	private MTApplication mtApplication;
		
	/** List with all MyMTObject. */
	private List<MyMTObject> myobjectList;
	
	/** Object Counter and Object ID. */
	private int counter;
	
	
	/** 
	 * Hello Word Scene. 
	 * 
	 * @param mtAppl MTApplication 
	 * @param name String
	 */
	public HelloWorldScene(final MTApplication mtAppl, final String name) {
		super(mtAppl, name);
		this.mtApplication = mtAppl;
		this.setClearColor(new MTColor(100, 100, 100, 255));
		
		//Show touches
		this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));
		
		counter = 0;
		myobjectList = new ArrayList<MyMTObject>();
		
		//New Object
		PImage buttonNewImage = mtApplication.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  "buttonNew.png");
		MTImageButton buttonNew = new MTImageButton(mtApplication, buttonNewImage);

		buttonNew.setSizeLocal(40, 40);
		buttonNew.setFillColor(new MTColor(255, 255, 255, 200));
		buttonNew.setName("KeyboardImage");
		buttonNew.setNoStroke(true);
		buttonNew.setPositionGlobal(new Vector3D(20, 20));
		
		buttonNew.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				switch(te.getTapID()) {
				case TapEvent.TAPPED:
					//MyMTObject t1 = new MyMTObject(mtApplication,1);
					//getCanvas().addChild(t1.getMyObjectBack());
					myobjectList.add(new MyMTObject(mtApplication, counter));				
					
					getCanvas().addChild(myobjectList.get(counter));	
					
					//getCanvas().addChild(myobjectList.get(counter).getMyObjectBack());	
					System.out.println("Zähler vor Add: " + counter);
					counter++;
					System.out.println("Zähler nach Add: " + counter);
					break;
				
				default:
					break;
				}
				
				
				
				
			return false;
			}
		});
	
		//New Object
		PImage buttonDELImage = mtApplication.loadImage("ch" + MTApplication.separator + "mitoco" + MTApplication.separator + "data" + MTApplication.separator +  "buttonDel.png");
		MTImageButton buttonDel = new MTImageButton(mtApplication, buttonDELImage);
		buttonDel.setSizeLocal(40, 40);
		buttonDel.setFillColor(new MTColor(255, 255, 255, 200));
		buttonDel.setName("KeyboardImage");
		buttonDel.setNoStroke(true);
		buttonDel.setPositionGlobal(new Vector3D(mtAppl.getWidth() - 20, 20));
		
		buttonDel.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(final MTGestureEvent ge) {
				// TODO Auto-generated method stub
				TapEvent te = (TapEvent) ge;
				switch(te.getTapID()) {
				case TapEvent.TAPPED:
					if (counter != 0) {
					
						getCanvas().removeChild(myobjectList.get(counter - 1)); // Entfernen des Objekts auf dem Canvas
						
						
					//getCanvas().removeChild(myobjectList.get(counter-1).getMyObjectBack()); // Entfernen des Objekts auf dem Canvas
					myobjectList.remove(counter - 1); // Remove Object from arryeList
					
					System.out.println("Zähler rem vor: " + counter);
					counter--;
					} else {
					System.out.println("Kein Objeckt zum enfernen vorhanden " + counter);	
					}
					break;
				
				default:
					break;
				}
			return false;
			}
		});
		
		PImage buttonSAVImage = mtApplication.loadImage("test" + MTApplication.separator + "thomas"+ MTApplication.separator + "image" + MTApplication.separator +  "buttonSave.png");
		MTImageButton buttonSav= new MTImageButton(mtApplication, buttonSAVImage);
		buttonSav.setSizeLocal(40, 40);
		buttonSav.setFillColor(new MTColor(255, 255, 255, 200));
		buttonSav.setName("KeyboardImage");
		buttonSav.setNoStroke(true);
		buttonSav.setPositionGlobal(new Vector3D(mtAppl.getWidth() - 20, mtAppl.getHeight() - 20));
		
		buttonSav.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				// TODO Auto-generated method stub
				TapEvent te = (TapEvent)ge;
				switch(te.getTapID()){
				case TapEvent.TAPPED:
					ObjectFactory objFactory = new ObjectFactory();
					PurchaseOrder purchaseOrder = 
						   (PurchaseOrder) objFactory.createPurchaseOrder();
					Customer customer = objFactory.createCustomer();
					
					/**
					 * Zusammenbauen der Objekte
					 */
					
					/*
					for (int i = 0; i <= 3; i++) {
						for (int y = 0; y <= myobjectList.get(i).getChildCount(); y++) {
							System.out.println(myobjectList.get(i).getChildByIndex(y).getName());
							for (int z = 0; z == myobjectList.get(i).getChildByIndex(y).getChildCount(); z++) {
								System.out.println(myobjectList.get(i).getChildByIndex(y).getChildByIndex(z).getName());
							}
						}
						
					}*/
					
					System.out.println(myobjectList.get(0).getChildByIndex(0).getChildByIndex(1).getUserData("FieldValue"));
					customer.setName((myobjectList.get(0).getChildByIndex(0).getChildByIndex(1).getUserData("FieldValue")).toString());
					purchaseOrder.setCustomer(customer);
					Customer customer2 = objFactory.createCustomer();
					customer2.setName("Test");
					purchaseOrder.setCustomer(customer2);
					
					/**
					 * Speichern des XML Fiels
					 * 
					 */
					try {
						if (!purchaseOrder.equals(null)) {
						JAXBContext jaxbContext = JAXBContext.newInstance("ch.mitoco.store.generated");
						Marshaller marshaller = jaxbContext.createMarshaller();
						marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
								   new Boolean(true));
						marshaller.marshal(purchaseOrder,
								   new FileOutputStream("AttributTemplates1.xml"));
						}
						
					} catch (JAXBException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					break;
				
				default:
					break;
				}
			return false;
			}
		});
		
		
		this.getCanvas().addChild(buttonDel);
		this.getCanvas().addChild(buttonNew);
		this.getCanvas().addChild(buttonSav);
		
	}
	@Override
	public void init() {
		
		
	}
	@Override
	public void shutDown() {
		
		
	}
}
