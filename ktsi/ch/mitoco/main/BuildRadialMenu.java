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
import org.mt4j.components.StateChange;
import org.mt4j.components.StateChangeEvent;
import org.mt4j.components.StateChangeListener;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTTextKeyboard;
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
import ch.mitoco.startmenu.SceneMitoco;

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
	
	private MTTextArea textarea;
	
	/** Object Index */
	private int objectindex;
	
	/** Test Objektausrichtung */
	private float endxGestureVector;
	private float startxGestureVector;
	private float endyGestureVector;
	private float startyGestureVector;
	private String filename;
	
	/** Spezifische ScenenDaten	 */
	private ModelSceneList sceneData;
	
	/** Transparenc Color for keyboard (fix). */
	private MTColor trans = new MTColor(0, 0, 0, 10);
	

	
	/**Builds the whole Contextmenu based on RadialMenu
	 * It reads the Objekttyps an Builds the only the selectable Objects
	 * 
	 * @param mtAppl MTApplication
	 * @param dataController DataController object to acces the XMLs
	 * @param Mitoco the Scene for drawing on it
	 * @param sceneData the Datamodel
	 * @param ic the Gesture Coordinates to calculate the direction
	 */
	public BuildRadialMenu(final MTApplication mtAppl, final DataController dataController, final MitocoScene Mitoco, final ModelSceneList sceneData, final InputCursor ic) {
		super(mtAppl);
			this.dataController = dataController;
			this.mtApplication = mtAppl;
			this.Mitoco = Mitoco;
			this.sceneData = sceneData;
			this.ic = ic;
			
			startyGestureVector = ic.getStartPosY();
			startxGestureVector = ic.getStartPosX();
			endyGestureVector = ic.getCurrentEvtPosY();
			endxGestureVector = ic.getCurrentEvtPosX();
			
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
				    	  filename = new String();
				        // Most input events in MT4j are an instance of AbstractCursorInputEvt (mouse, multi-touch..)
				        if (inEvt instanceof MTFingerInputEvt) {
				          final MTFingerInputEvt cursorInputEvt = (MTFingerInputEvt) inEvt;
				          switch (cursorInputEvt.getId()) {
				            case TapEvent.GESTURE_STARTED:
				            	final IFont font = FontManager.getInstance().createFont(mtApplication, "arial.ttf",
				        	            16, // Font size
				        	            new MTColor(255, 255, 255, 255), // Font fill color
				        	            true);
				          
				            	
				            	MTTextKeyboard textKeyboard = new MTTextKeyboard(mtApplication);
								textKeyboard.setFillColor(trans);
								textKeyboard.setNoStroke(true);
								textKeyboard.setPositionRelativeToParent(new Vector3D(textKeyboard.getWidthXY(TransformSpace.LOCAL) / 2, (textKeyboard.getHeightXY(TransformSpace.LOCAL) / 2) + 50));
								
								
							  	textarea = new MTTextArea(mtApplication, 35, -20, 100, 16, font);
				        		textarea.setInnerPadding(0);
				        		textarea.setInnerPaddingLeft(3);
				        		textarea.setInnerPaddingTop(0);
				        		textarea.setText("Filename");
				        		textarea.setFillColor(new MTColor(0, 0, 0, 0));
				        		textarea.setNoStroke(false);
								
								textKeyboard.addTextInputListener(textarea);
								
								Mitoco.getCanvas().addChild(textKeyboard);
								textKeyboard.addChild(textarea);	
								textKeyboard.addStateChangeListener(StateChange.COMPONENT_DESTROYED, new StateChangeListener() {
									
									@Override
									public void stateChanged(final StateChangeEvent evt) {
										filename = textarea.getText();
										dataController.saveSceneXML(filename);
						            	Mitoco.getMTApplication().saveFrame(SceneMitoco.getExportPath() + filename + "_Output-###.png");
									}
								}
								);
				            	
				            	
				            	
				            	
				              break;
				            case TapEvent.GESTURE_ENDED:
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
					            	
					            	//fileChooser.toggleFileChooser("xml");
					        		//fileChooser.getUI().sendToFront();
					            	//fileChooser = new FileChooser("C:\\", BuildRadialMenu.this.Mitoco);
					        		//Mitoco.getCanvas().addChild(fileChooser.getUI());
					            	MitocoScene.setImageload(true);
					            	Mitoco.drawFilechooser("xml");
					        		//fileChooser.toggleFileChooser("xml");
					          		//fileChooser.getUI().sendToFront(); 
					        		
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
					      
					      /**
					       * Listener Object to Hide an Component on the Scene. It will hide the Object of the Scene with an Animation
					       * and will hide the link over the DataController without an Animation 
					       */
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
						            			  dataController.hideLink(it.getID());
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
						      
						      /**
						       * 
						       * 
						       */
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
					        
					        final IMTInputEventListener deleteListener = new IMTInputEventListener() {
						          @Override
						          public boolean processInputEvent(final MTInputEvent inEvt) {
						            // Most input events in MT4j are an instance of AbstractCursorInputEvt (mouse, multi-touch..)
						            if (inEvt instanceof MTFingerInputEvt) {
						              final MTFingerInputEvt cursorInputEvt = (MTFingerInputEvt) inEvt;
						              switch (cursorInputEvt.getId()) {
						                case TapEvent.GESTURE_STARTED:
						                	//hideObjects(dataController.getMyobjectList().get(0));
						                   dataController.deleteObject();
						                   for (MyMTObject it : dataController.getMyobjectList()) {
						               		  if (it.getTagFlag()) {
						               			Mitoco.getCanvas().removeChild(it);
						               			
						               		  }
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
					    
					    
					    
			
			
			// Build list of menu items
	        final List<MTMenuItem> menuItems = new ArrayList<MTMenuItem>();
	        final MTMenuItem objectsmenu = new MTMenuItem("New", null);
	        
	        if (sceneData.getShowAll()) {
	        	for (Iterator<ModelTypDescription> it =  dataController.getObjectetyps().getObjectdescription().iterator(); it.hasNext();) {
	        		final MTMenuItem subMenu11 = new MTMenuItem(it.next().getObjectdescription(), null);
	        		//menu1.addSubMenuItem(new MTMenuItem(it.next().getObjectdescription(), null));
	        		//final IMTInputEventListener createObjectInput = new IMTInputEventListener() {
	        		//menu1.addSubMenuItem(new MTMenuItem(it.next().getObjectdescription(), new ConcurrentHashMap<Class<? extends IInputProcessor>, IGestureEventListener>(){
	        		objectsmenu.addSubMenuItem(subMenu11);
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
	            		
	        			objectsmenu.addSubMenuItem(subMenu11);
	            		subMenu11.addInputEventListener(createObjectInput);
	        		//}
	        		
	        	}
	        	
	        }
	        final MTMenuItem viewmenu = new MTMenuItem("View", null);
	        final MTMenuItem browser = new MTMenuItem("Browser", null);
	        browser.addInputEventListener(browserListener);
	       
	        if(System.getProperty("sun.arch.data.model").equals("32")){
	        	viewmenu.addSubMenuItem(browser);
	        }
	        
	        
	        final MTMenuItem pdf = new MTMenuItem("PDF", null);
	        pdf.addInputEventListener(showPDFlistener);
	        viewmenu.addSubMenuItem(pdf);
	    
	        final MTMenuItem actionmenu = new MTMenuItem("Action", null);
	        
	        final MTMenuItem loadfile = new MTMenuItem("Load File", null);
	        final MTMenuItem loadfileTest = new MTMenuItem("LoadThomas", null);
	        final MTMenuItem saveFile = new MTMenuItem("Save File", null);
	        final MTMenuItem submenu21 = new MTMenuItem("Reporting", null);
	        submenu21.addInputEventListener(startReporting); 
	        actionmenu.addSubMenuItem(submenu21);
	        actionmenu.addSubMenuItem(loadfile);
	        actionmenu.addSubMenuItem(loadfileTest);
	        actionmenu.addSubMenuItem(saveFile);
	        actionmenu.addSubMenuItem(new MTMenuItem("Save Picture", null));
	        
	        loadfileTest.addInputEventListener(loadButtonInput2);
	        loadfile.addInputEventListener(loadButtonInput);
	        saveFile.addInputEventListener(saveButtonInput);
	        //actionmenu.addSubMenuItem(new MTMenuItem("Copy Objecet", null));
	        //actionmenu.addSubMenuItem(new MTMenuItem("Paste", null));
	        actionmenu.addSubMenuItem(new MTMenuItem("Clear", null));
	        final MTMenuItem maximizemenu = new MTMenuItem("Maximize", null);
	        final MTMenuItem minimizemenu = new MTMenuItem("Minimize", null);
	        final MTMenuItem deletemenu = new MTMenuItem("Delete selected", null);
	        
	        maximizemenu.addInputEventListener(showInputListener);
	        minimizemenu.addInputEventListener(hide2InputListener);
	        deletemenu.addInputEventListener(deleteListener);
	                
	        final MTMenuItem menu5 = new MTMenuItem("Exit", null);
	        menu5.addInputEventListener(exitButtonInput); 
	     
	        menuItems.add(objectsmenu);
	        menuItems.add(viewmenu);
	        menuItems.add(actionmenu);
	        menuItems.add(maximizemenu);
	        menuItems.add(minimizemenu);
	        menuItems.add(deletemenu);
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


