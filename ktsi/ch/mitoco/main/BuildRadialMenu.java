/**
 * 
 */
package ch.mitoco.main;

import gov.pnnl.components.visibleComponents.widgets.radialMenu.MTRadialMenu;
import gov.pnnl.components.visibleComponents.widgets.radialMenu.item.MTMenuItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.inputData.InputCursor;
import org.mt4j.input.inputData.MTFingerInputEvt;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.ToolsMath;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.components.visibleComponents.widgets.MTWebBrowser;

import ch.mitoco.components.visibleComponents.MyMTObject;
import ch.mitoco.components.visibleComponents.filechooser.FileChooser;
import ch.mitoco.components.visibleComponents.filechooser.PDFViewer;
import ch.mitoco.dataController.DataController;
import ch.mitoco.model.ModelSceneList;
import ch.mitoco.model.ModelTypDescription;
import ch.mitoco.reporting.MitocoReporting;

/**
 * @author steffe
 *
 */
public class BuildRadialMenu extends MTComponent{

	private DataController dataController;
	/**InputCursor for RadialMenu.	*/
	private InputCursor ic;
		
	/**RadialMenu.	*/
	private MTRadialMenu mtRadialMenu1;
	
	private MTApplication mtApplication;
	
	private MitocoScene Mitoco;
	
	/** Object Counter and Object ID. */
	private int counter;
	
	/**Filechooser objekt. */
	private static FileChooser fileChooser;
	
	/** Object Index */
	private int objectindex;
	
	/** Test Objektausrichtung */
	private float endxGestureVector;
	private float startxGestureVector;
	private float endyGestureVector;
	private float startyGestureVector;
	
	/** Spezifische ScenenDaten	 */
	private ModelSceneList sceneData;
	
	/**
	 * 
	 */
	public BuildRadialMenu(final MTApplication mtAppl, final DataController dataController, final MitocoScene Mitoco, final ModelSceneList sceneData, final InputCursor ic) {
		super(mtAppl);
			this.dataController = dataController;
			this.mtApplication = mtAppl;
			this.Mitoco = Mitoco;
			this.sceneData = sceneData;
			this.ic = ic;
			//InputEvents for RadialMenu
			final IMTInputEventListener createObjectInput = new IMTInputEventListener() {
			      @Override
			      public boolean processInputEvent(final MTInputEvent inEvt) {
			        // Most input events in MT4j are an instance of AbstractCursorInputEvt (mouse, multi-touch..)
			        if (inEvt instanceof MTFingerInputEvt) {
			          final MTFingerInputEvt cursorInputEvt = (MTFingerInputEvt) inEvt;
					switch (cursorInputEvt.getId()) {
			            case TapEvent.GESTURE_STARTED:
			            	
			            	/**
			            	 * To-Do: Laden der Objettypen von XML (Solved)
			            	 */
			            	//System.out.println("Object add Menu: \"" + inEvt.getCurrentTarget().getName() + "\"");
			            	System.out.println("Counter befor creation: " + counter);
			            	 	
			            		for (ModelTypDescription it2 : dataController.getObjectetyps().getObjectdescription()) {
			            	 		
			            	 		//System.out.println("Objecttyp Desc: " + it2.getObjectdescription());
				            		//System.out.println("Objecttyp Data: " + it2.getObjectypeid());
				            		//System.out.println("Objecttyp Menu: " + inEvt.getCurrentTarget().getName());
				            		
				            		
				            		if (inEvt.getCurrentTarget().getName().equals(it2.getObjectdescription())){
				            			objectindex = dataController.createObject(it2.getObjectypeid());
					            		dataController.getMyobjectList().get(objectindex).setPositionGlobal(new Vector3D(ToolsMath.nextRandomInt(140, 800), ToolsMath.nextRandomInt(140, 700)));
					            		System.out.println("Objekttyp gefunden");
					            		
					            		break;
				            		}

			            	 	}
			            	 	Mitoco.getCanvas().addChild(dataController.getMyobjectList().get(objectindex));	
			            	 
							//getCanvas().addChild(myobjectList.get(counter).getMyObjectBack());	
							counter++;
			              break;
			            default:
			              break;
			          }
			        } else {
			          //LOG.warn("Some other event occured:" + inEvt);
			        }

			        return false;
			      }
			    };
			
			
		    final IMTInputEventListener exitButtonInput = new IMTInputEventListener() {
			      @Override
			      public boolean processInputEvent(final MTInputEvent inEvt) {
			        // Most input events in MT4j are an instance of AbstractCursorInputEvt (mouse, multi-touch..)
			        if (inEvt instanceof MTFingerInputEvt) {
			          final MTFingerInputEvt cursorInputEvt = (MTFingerInputEvt) inEvt;
			          switch (cursorInputEvt.getId()) {
			            case TapEvent.GESTURE_STARTED:
			            	Runtime.getRuntime().exit(0);
			              break;
			            default:
			              break;
			          }
			        } else {
			          //LOG.warn("Some other event occured:" + inEvt);
			        }

			        return false;
			      }
			    };
			    
			   final IMTInputEventListener saveButtonInput = new IMTInputEventListener() {
				      @Override
				      public boolean processInputEvent(final MTInputEvent inEvt) {
				        // Most input events in MT4j are an instance of AbstractCursorInputEvt (mouse, multi-touch..)
				        if (inEvt instanceof MTFingerInputEvt) {
				          final MTFingerInputEvt cursorInputEvt = (MTFingerInputEvt) inEvt;
				          switch (cursorInputEvt.getId()) {
				            case TapEvent.GESTURE_STARTED:
				            	dataController.saveSceneXML();
								
				              break;
				            case TapEvent.GESTURE_ENDED:
				            	//TODO: MitocoScene.this.mtApplication.saveFrame("Output-###.png");
				            default:
				              break;
				          }
				        } else {
				          //LOG.warn("Some other event occured:" + inEvt);
				        }

				        return false;
				      }
				    }; 
				    
				    final IMTInputEventListener loadButtonInput = new IMTInputEventListener() {
					      @Override
					      public boolean processInputEvent(final MTInputEvent inEvt) {
					        // Most input events in MT4j are an instance of AbstractCursorInputEvt (mouse, multi-touch..)
					        if (inEvt instanceof MTFingerInputEvt) {
					          final MTFingerInputEvt cursorInputEvt = (MTFingerInputEvt) inEvt;
					          switch (cursorInputEvt.getId()) {
					            case TapEvent.GESTURE_STARTED:
					            	/*
					            	if (!dataController.loadSceneXML()) {
										for (Iterator<MyMTObject> it = dataController.getMyobjectList().iterator(); it.hasNext();) {
											getCanvas().removeChild(dataController.getMyobjectList().get(it.next().getID()));	
										}
										dataController.clearScene();
										} else {
										for (Iterator<MyMTObject> it = dataController.getMyobjectList().iterator(); it.hasNext();) {
											getCanvas().addChild(dataController.getMyobjectList().get(it.next().getID()));	
										}
										counter = dataController.getObjectcounter();
									}
					            	*/
					            	
					            	//Mitoco.getCanvas().addChild(fileChooser.getUI());
					            	//fileChooser.toggleFileChooser("xml");
					        		//fileChooser.getUI().sendToFront();
					            	fileChooser = new FileChooser("C:\\Workspace\\MT4J_KTSI", BuildRadialMenu.this.Mitoco);
					        		Mitoco.getCanvas().addChild(fileChooser.getUI());
					            	//Mitoco.drawFilechooser("xml");
					        		fileChooser.toggleFileChooser("xml");
					          		fileChooser.getUI().sendToFront(); 
					        		
					            	//linker.setTapAndHoldListener(); //TODO: Test
									break;
									
					            default:
					              break;
					          }
					        } else {
					          //LOG.warn("Some other event occured:" + inEvt);
					        }

					        return false;
					      }
					    };
					    
					    
					    final IMTInputEventListener loadButtonInput2 = new IMTInputEventListener() {
						      @Override
						      public boolean processInputEvent(final MTInputEvent inEvt) {
						        // Most input events in MT4j are an instance of AbstractCursorInputEvt (mouse, multi-touch..)
						        if (inEvt instanceof MTFingerInputEvt) {
						          final MTFingerInputEvt cursorInputEvt = (MTFingerInputEvt) inEvt;
						          switch (cursorInputEvt.getId()) {
						            case TapEvent.GESTURE_STARTED:
						            	
						            	if (!dataController.loadSceneXML("xstream.xml")) {
											for (Iterator<MyMTObject> it = dataController.getMyobjectList().iterator(); it.hasNext();) {
												Mitoco.getCanvas().removeChild(dataController.getMyobjectList().get(it.next().getID()));	
											}
											dataController.clearScene();
											} else {
											for (Iterator<MyMTObject> it = dataController.getMyobjectList().iterator(); it.hasNext();) {
												Mitoco.getCanvas().addChild(dataController.getMyobjectList().get(it.next().getID()));	
											}
											counter = dataController.getObjectcounter();
										}
						            						            	//linker.setTapAndHoldListener(); //TODO: Test
										break;
										
						            default:
						              break;
						          }
						        } else {
						          //LOG.warn("Some other event occured:" + inEvt);
						        }

						        return false;
						      }
						    };
					    
					    
					    final IMTInputEventListener startReporting = new IMTInputEventListener() {
						      @Override
						      public boolean processInputEvent(final MTInputEvent inEvt) {
						        // Most input events in MT4j are an instance of AbstractCursorInputEvt (mouse, multi-touch..)
						        if (inEvt instanceof MTFingerInputEvt) {
						          final MTFingerInputEvt cursorInputEvt = (MTFingerInputEvt) inEvt;
						          switch (cursorInputEvt.getId()) {
						            case TapEvent.GESTURE_STARTED:
						            	new MitocoReporting(mtApplication, "ReportingAp", Mitoco, dataController, 1);
										break;
										
						            default:
						              break;
						          }
						        } else {
						          //LOG.warn("Some other event occured:" + inEvt);
						        }

						        return false;
						      }
						    };
						    
						    final IMTInputEventListener showPDFlistener = new IMTInputEventListener() {
							      @Override
							      public boolean processInputEvent(final MTInputEvent inEvt) {
							        // Most input events in MT4j are an instance of AbstractCursorInputEvt (mouse, multi-touch..)
							        if (inEvt instanceof MTFingerInputEvt) {
							          final MTFingerInputEvt cursorInputEvt = (MTFingerInputEvt) inEvt;
							          switch (cursorInputEvt.getId()) {
							            case TapEvent.GESTURE_STARTED:
							            	PDFViewer testpdf = new PDFViewer(mtApplication, "Test", "D:/Test.pdf");
							            	Mitoco.getCanvas().addChild(testpdf);
							            	testpdf.setVisible(true);
							            	testpdf.sendToFront();
							              break;
							            default:
							              break;
							          }
							        } else {
							          //LOG.warn("Some other event occured:" + inEvt);
							        }

							        return false;
							      }
							    };    
						    
					    
					    final IMTInputEventListener showInputListener = new IMTInputEventListener() {
					        @Override
					        public boolean processInputEvent(final MTInputEvent inEvt) {
					          // Most input events in MT4j are an instance of AbstractCursorInputEvt (mouse, multi-touch..)
					          if (inEvt instanceof MTFingerInputEvt) {
					            final MTFingerInputEvt cursorInputEvt = (MTFingerInputEvt) inEvt;
					            switch (cursorInputEvt.getId()) {
					              case TapEvent.GESTURE_STARTED:
					            	  for (MyMTObject it : dataController.getMyobjectList()) {
					            		  if (it.getTagFlag()) {
					            			  
					            			  Mitoco.showObjects(it); 
					            			  //dataController.getMyobjectList()
					            			it.setVisible(true);
					            			//AnimationUtil.scaleIn(it);
					            		  }
					            		  System.out.println("hide2:"+it);
					            	  }
					            	  
					            	  //AnimationUtil.scaleIn(dataController.getMyobjectList().get(0));
					                
					                break;
					              default:
					                break;
					            }
					          } else {
					            //LOG.warn("Some other event occured:" + inEvt);
					          }

					          return false;
					        }
					      };
					      
					      final IMTInputEventListener hide2InputListener = new IMTInputEventListener() {
						        @Override
						        public boolean processInputEvent(final MTInputEvent inEvt) {
						          // Most input events in MT4j are an instance of AbstractCursorInputEvt (mouse, multi-touch..)
						          if (inEvt instanceof MTFingerInputEvt) {
						            final MTFingerInputEvt cursorInputEvt = (MTFingerInputEvt) inEvt;
						            switch (cursorInputEvt.getId()) {
						              case TapEvent.GESTURE_STARTED:
						            	  for (MyMTObject it : dataController.getMyobjectList()) {
						            		  if (it.getTagFlag()) {
						            			  //AnimationUtil.scaleOut(it, false);
						            			  Mitoco.hideObjects(it);
						            			  //it.setVisible(false);
						            		  }
						            		  System.out.println("hide2:"+it);
						            		  
						            	  }
						               
						                break;
						              default:
						                break;
						            }
						          } else {
						            //LOG.warn("Some other event occured:" + inEvt);
						          }

						          return false;
						        }
						      };
						      
						      final IMTInputEventListener browserListener = new IMTInputEventListener() {
						          @Override
						          public boolean processInputEvent(final MTInputEvent inEvt) {
						            // Most input events in MT4j are an instance of AbstractCursorInputEvt (mouse, multi-touch..)
						            if (inEvt instanceof MTFingerInputEvt) {
						              final MTFingerInputEvt cursorInputEvt = (MTFingerInputEvt) inEvt;
						              switch (cursorInputEvt.getId()) {
						                case TapEvent.GESTURE_STARTED:
						                	 MTWebBrowser browser = new MTWebBrowser(Mitoco.getMTApplication(), 800, 600);
												browser.setFillColor(new MTColor(180,180,180,200));
												browser.setStrokeColor(MTColor.BLACK);
												Mitoco.getCanvas().addChild(browser);
												browser.setPositionGlobal(MT4jSettings.getInstance().getWindowCenter());
						                   break;
						                 default:
						                      break;
						                  }
						                } else {
						                  //LOG.warn("Some other event occured:" + inEvt);
						                }
						                return false;
						              }
						        };

					      final IMTInputEventListener hideInputListener = new IMTInputEventListener() {
					          @Override
					          public boolean processInputEvent(final MTInputEvent inEvt) {
					            // Most input events in MT4j are an instance of AbstractCursorInputEvt (mouse, multi-touch..)
					            if (inEvt instanceof MTFingerInputEvt) {
					              final MTFingerInputEvt cursorInputEvt = (MTFingerInputEvt) inEvt;
					              switch (cursorInputEvt.getId()) {
					                case TapEvent.GESTURE_STARTED:
					                	//hideObjects(dataController.getMyobjectList().get(0));
					                   break;
					                 default:
					                      break;
					                  }
					                } else {
					                  //LOG.warn("Some other event occured:" + inEvt);
					                }
					                return false;
					              }
					        };
					    
					    
					    
			
			
			// Build list of menu items
	        final List<MTMenuItem> menuItems = new ArrayList<MTMenuItem>();
	        final MTMenuItem menu1 = new MTMenuItem("New", null);
	        
	        if (sceneData.getShowAll()) {
	        	for (Iterator<ModelTypDescription> it =  dataController.getObjectetyps().getObjectdescription().iterator(); it.hasNext();) {
	        		final MTMenuItem subMenu11 = new MTMenuItem(it.next().getObjectdescription(), null);
	        		//menu1.addSubMenuItem(new MTMenuItem(it.next().getObjectdescription(), null));
	        		//final IMTInputEventListener createObjectInput = new IMTInputEventListener() {
	        		//menu1.addSubMenuItem(new MTMenuItem(it.next().getObjectdescription(), new ConcurrentHashMap<Class<? extends IInputProcessor>, IGestureEventListener>(){
	        		menu1.addSubMenuItem(subMenu11);
	        		subMenu11.addInputEventListener(createObjectInput);
	        	}
	        	} else        {
	        	//for(ModelTypDescription it : dataController.getObjectetyps().getObjectdescription()){
	        	for (ModelTypDescription it : sceneData.getSceneobjekte()) {
	        		//if(sceneData.getSceneobjekte().get(index)){
	        			final MTMenuItem subMenu11 = new MTMenuItem(it.getObjectdescription(), null);
	            		//menu1.addSubMenuItem(new MTMenuItem(it.next().getObjectdescription(), null));
	            		//final IMTInputEventListener createObjectInput = new IMTInputEventListener() {
	            		//menu1.addSubMenuItem(new MTMenuItem(it.next().getObjectdescription(), new ConcurrentHashMap<Class<? extends IInputProcessor>, IGestureEventListener>(){
	            		
	        			menu1.addSubMenuItem(subMenu11);
	            		subMenu11.addInputEventListener(createObjectInput);
	        		//}
	        		
	        	}
	        	
	        }
	        final MTMenuItem subMenu5 = new MTMenuItem("View", null);
	        subMenu5.addSubMenuItem(new MTMenuItem("All", null));
	        subMenu5.addSubMenuItem(new MTMenuItem("New", null));
	        subMenu5.addSubMenuItem(new MTMenuItem("Old", null));
	        subMenu5.addSubMenuItem(new MTMenuItem("Color", null));
	        //subMenu5.addSubMenuItem(new MTMenuItem("Browser", null));
	        final MTMenuItem subMenu51 = new MTMenuItem("Browser", null);
	        subMenu51.addInputEventListener(browserListener);
	        subMenu5.addSubMenuItem(subMenu51);
	        final MTMenuItem subMenu52 = new MTMenuItem("PDF", null);
	        subMenu52.addInputEventListener(showPDFlistener);
	        subMenu5.addSubMenuItem(subMenu52);
	        
	        subMenu5.addSubMenuItem(new MTMenuItem("Special", null));

	        menu1.addSubMenuItem(subMenu5);

	        final MTMenuItem menu2 = new MTMenuItem("Action", null);
	        final MTMenuItem subMenu2 = new MTMenuItem("File", null);
	        final MTMenuItem subMenu221 = new MTMenuItem("Load", null);
	        final MTMenuItem subMenu223 = new MTMenuItem("LoadThomas", null);
	        final MTMenuItem subMenu222 = new MTMenuItem("Save", null);
	        subMenu2.addSubMenuItem(subMenu221);
	        subMenu2.addSubMenuItem(subMenu222);
	        subMenu2.addSubMenuItem(subMenu223);
	        
	        subMenu223.addInputEventListener(loadButtonInput2);
	        subMenu221.addInputEventListener(loadButtonInput);
	        subMenu222.addInputEventListener(saveButtonInput);
	        
	        subMenu2.addSubMenuItem(new MTMenuItem("Copy", null));
	        subMenu2.addSubMenuItem(new MTMenuItem("Send", null));
	        final MTMenuItem submenu21 = new MTMenuItem("Reporting", null);
	        submenu21.addInputEventListener(startReporting); 
	        
	        menu2.addSubMenuItem(submenu21);
	        menu2.addSubMenuItem(subMenu2);
	        menu2.addSubMenuItem(new MTMenuItem("Copy Objecet", null));
	        menu2.addSubMenuItem(new MTMenuItem("Paste", null));
	        menu2.addSubMenuItem(new MTMenuItem("Clear", null));
	        final MTMenuItem menu3 = new MTMenuItem("Maximize", null);
	        final MTMenuItem menu4 = new MTMenuItem("Minimize", null);
	        
	        menu4.addInputEventListener(hide2InputListener);
	        menu3.addInputEventListener(showInputListener);
	                
	        final MTMenuItem menu5 = new MTMenuItem("Exit", null);
	        menu5.addInputEventListener(exitButtonInput); 
	     
	        menuItems.add(menu1);
	        menuItems.add(menu2);
	        menuItems.add(menu3);
	        menuItems.add(menu4);
	        menuItems.add(menu5);

	        // Create menu
	        final Vector3D vector = new Vector3D(ic.getCurrentEvtPosX(), ic.getCurrentEvtPosY());
	        
	        final IFont font = FontManager.getInstance().createFont(mtApplication, "arial.ttf",
	            16, // Font size
	            new MTColor(255, 255, 255, 255), // Font fill color
	            true); // Anti-alias
	        this.mtRadialMenu1 = new MTRadialMenu(mtApplication, vector, font, 1f, menuItems);
	        
	        
	        if (startxGestureVector < endxGestureVector) {
	        	this.mtRadialMenu1.rotateZ(mtRadialMenu1.getCenterPointLocal(), 0);
	        } else if (startyGestureVector < endyGestureVector) {
	        	this.mtRadialMenu1.rotateZ(mtRadialMenu1.getCenterPointLocal(), 90);
	        } else if (startxGestureVector > endxGestureVector) {
	        	this.mtRadialMenu1.rotateZ(mtRadialMenu1.getCenterPointLocal(), 180);
	        } else if (startyGestureVector > endyGestureVector) {
	        	this.mtRadialMenu1.rotateZ(mtRadialMenu1.getCenterPointLocal(), 270);
	        }
	        System.out.println("start x " + startxGestureVector);
	        System.out.println("start y " + startyGestureVector);
	        System.out.println("end x " + endxGestureVector);
	        System.out.println("end y " + endyGestureVector);
	        
	        
	        //AnimationUtil.rotate2D(mtRectangle, 720);
	        Mitoco.getCanvas().addChild(mtRadialMenu1);
	      }
	}


