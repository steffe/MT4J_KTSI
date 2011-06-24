package ch.mitoco.components.visibleComponents.filechooser;

import java.io.File;

import javax.swing.filechooser.FileSystemView;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.clipping.Clip;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.components.visibleComponents.widgets.buttons.MTSvgButton;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTKeyboard;
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
import org.mt4j.util.math.Vector3D;


public class FileChooserView 
{
	FileChooser fc_model;
	
	FileChooserView(FileChooser model)
	{
		this(FileSystemView.getFileSystemView().getHomeDirectory().toString(), model);
	}
	
	FileChooserView(String homeDir, final FileChooser model) 
	{
		///super(model.getScene().getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "whiteboard"
		//		+File.separator + "data"+  File.separator+"images"+ File.separator
		//		+"browseButton(WB).png"), model.getScene().getMTApplication());
		fc_model = model;
		//this.setName("FileChooser");
		//this.setNoFill(false);
		//this.setNoStroke(true);
		//this.translate(new Vector3D(5,model.getScene().getMTApplication().height-this.getHeightXY(TransformSpace.GLOBAL)-5,0));	
		
		
	    //Main window
		fc_model.window = new MTRoundRectangle(fc_model.getScene().getMTApplication(), 0, 0, 0, 440, 520, 5, 5);
		fc_model.window.setGestureAllowance(RotateProcessor.class, false);
		fc_model.window.setGestureAllowance(ScaleProcessor.class, false);
		fc_model.window.addGestureListener(DragProcessor.class, new InertiaDragAction());
		fc_model.window.setFillColor(new MTColor(225,225,225,200));
		fc_model.window.setStrokeColor(new MTColor (0,0,0));

		//Scrollable list of files in current directory
		fc_model.fileSelector = new MTList(model.getScene().getMTApplication(), 0,0, 400, 300, 2);
		fc_model.window.addChild(fc_model.fileSelector);
		fc_model.window.sendToFront();
		fc_model.fileSelector.setAnchor(PositionAnchor.UPPER_LEFT);
		fc_model.fileSelector.setPositionRelativeToParent(new Vector3D(0,0,0));
		fc_model.fileSelector.translateGlobal(new Vector3D(20,60, 0));
		fc_model.fileSelector.setStrokeColor(new MTColor(0,0,0));
		
		//Setup the GUI
		setupGUI();
		

		
		
		model.getScene().getGuiOverlay().addChild(fc_model.window);
		fc_model.window.translate(new Vector3D(100,50));
		fc_model.window.setVisible(false);	
		
		//Draw this component and its children above 
		//everything previously drawn and avoid z-fighting
		fc_model.window.setDepthBufferDisabled(true);
	}
	
	//Method that initializes all of the GUI and it's functionality
	protected void setupGUI() 
	{
		//Back button
		fc_model.backButton = new MTImageButton(fc_model.getScene().getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
				+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
				+"noBack.png"), fc_model.getScene().getMTApplication());
		fc_model.window.addChild(fc_model.backButton);
		fc_model.backButton.setEnabled(false);
		fc_model.backButton.setNoStroke(true);
		fc_model.backButton.setPositionRelativeToParent(new Vector3D(0,0,0));
		fc_model.backButton.translateGlobal(new Vector3D(fc_model.window.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)-100,32,0));
		//backButton.addActionListener();
		fc_model.backButton.setName("backButton");
		
		//Up button
		fc_model.upButton = new MTImageButton(fc_model.getScene().getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
				+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
				+"noUpFolder.png"), fc_model.getScene().getMTApplication());
		fc_model.window.addChild(fc_model.upButton);
		fc_model.upButton.setNoStroke(true);
		fc_model.upButton.setPositionRelativeToParent(new Vector3D(0,0,0));
		fc_model.upButton.translateGlobal(new Vector3D(fc_model.window.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)-55,32,0));
		//upButton.addActionListener();
		fc_model.upButton.setName("upButton");
		fc_model.upButton.setEnabled(false);
		
		//Open button
		fc_model.openButton = new MTImageButton(fc_model.getScene().getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
				+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
				+"okButton.png"), fc_model.getScene().getMTApplication());
		fc_model.window.addChild(fc_model.openButton);
		fc_model.openButton.setNoStroke(true);
		fc_model.openButton.setPositionRelativeToParent(new Vector3D(0,0,0));
		fc_model.openButton.translateGlobal(new Vector3D(fc_model.window.getWidthXY(TransformSpace.RELATIVE_TO_PARENT) -145,
				fc_model.window.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)-30,0));
		//openButton.addActionListener();
		fc_model.openButton.setName("openButton");
		
		MTTextArea ok = new MTTextArea(fc_model.getScene().getMTApplication(), 
				FontManager.getInstance().createFont(fc_model.getScene().getMTApplication(), "arial.ttf", 
						15, 	//Font size
						MTColor.BLACK));
		ok.setFillColor(new MTColor(150,150,150));
		ok.setNoFill(true);
		ok.setNoStroke(true);
		ok.setText("Open");
		ok.setPickable(false);
		fc_model.openButton.addChild(ok);
		ok.setPositionRelativeToParent(new Vector3D(33,12,0));
		
		//Cancel button
		fc_model.cancelButton = new MTImageButton(fc_model.getScene().getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
				+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
				+"okButton.png"), fc_model.getScene().getMTApplication());
		fc_model.window.addChild(fc_model.cancelButton);
		fc_model.cancelButton.setNoStroke(true);
		fc_model.cancelButton.setPositionRelativeToParent(new Vector3D(0,0,0));
		fc_model.cancelButton.translateGlobal(new Vector3D(fc_model.window.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)-60,
				fc_model.window.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)-30,0));
		//cancelButton.addActionListener();
		fc_model.cancelButton.setName("cancelButton");
		
		MTTextArea cancel = new MTTextArea(fc_model.getScene().getMTApplication(), 
				FontManager.getInstance().createFont(fc_model.getScene().getMTApplication(), "arial.ttf", 
						15, 	//Font size
						MTColor.BLACK));
		cancel.setFillColor(new MTColor(150,150,150));
		cancel.setNoFill(true);
		cancel.setNoStroke(true);
		cancel.setText("Cancel");
		cancel.setPickable(false);
		fc_model.cancelButton.addChild(cancel);
		cancel.setPositionRelativeToParent(new Vector3D(33,12,0));
		
		//Close button
		fc_model.closeButton = new MTSvgButton(fc_model.getRenderer(), System.getProperty("user.dir")+File.separator + "ktsi"
				+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
				+"keybClose.svg");
		fc_model.window.addChild(fc_model.closeButton);
		//Transform
		fc_model.closeButton.scale(0.35f, 0.35f, 1, new Vector3D(0,0,0));
		fc_model.closeButton.setPositionRelativeToParent(new Vector3D(0,0,0));
		fc_model.closeButton.translateGlobal(new Vector3D(fc_model.window.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)-14,
				15,0));
		fc_model.closeButton.setBoundsPickingBehaviour(AbstractShape.BOUNDS_ONLY_CHECK);
		//closeButton.addActionListener();
		fc_model.closeButton.setName("closeButton");
		
		//Look in
		fc_model.lookInDropDown = new MTList(0,0, 280, 350, 0, fc_model.getScene().getMTApplication());
		fc_model.window.addChild(fc_model.lookInDropDown);
		fc_model.lookInDropDown.translateGlobal(new Vector3D(20,45, 0));
		fc_model.lookInDropDown.setStrokeColor(new MTColor(0,0,0));
		
		//Fill window with directory contents
		File desktop = new File(FileSystemView.getFileSystemView().getHomeDirectory().toString());
		fc_model.top = new WBFile(fc_model.getScene(), desktop);
		fc_model.lookInDropDown.addListElement(fc_model.top);
		fc_model.top.setIcon("desktop");
		
		//		My Computer
		File dummy = new File(FileSystemView.getFileSystemView().getHomeDirectory().toString());
		fc_model.myComputer = new WBFile(fc_model.getScene(), dummy, 20);
		fc_model.myComputer.setComputer();
		fc_model.lookInDropDown.addListElement(fc_model.myComputer);
		
		File[] f = File.listRoots();
        for (int i = 0; i < f.length; i++)
        { 
            WBFile tmpFile = new WBFile(fc_model.getScene(), f[i], 40);
            fc_model.lookInDropDown.addListElement(tmpFile);
            tmpFile.setIcon("drive");
            tmpFile.setBelowComp(true);
        }
		
		File[] list = desktop.listFiles();
		for(int i=0; i<list.length; i++) {
			WBFile tmpFile = new WBFile(fc_model.getScene(), list[i], 20);
			if(tmpFile.getFile().isDirectory()) {
				tmpFile.setIcon("directory");
				fc_model.lookInDropDown.addListElement(tmpFile);
				tmpFile.setDesktopLookIn(true);
			}
		}
		fc_model.lookInDropDown.setVisible(false);
										
		fc_model.lookIn = new MTRoundRectangle (fc_model.getScene().getMTApplication(), 0, 0, 0, 280, 30, 5, 5);
		fc_model.lookIn.translate(new Vector3D(20,15));
		fc_model.lookIn.setStrokeColor(new MTColor(0,0,0, 150));
		fc_model.lookIn.setStrokeWeight(2);
		fc_model.lookIn.removeAllGestureEventListeners();
		fc_model.lookIn.registerInputProcessor(new TapProcessor(fc_model.getScene().getMTApplication()));
		fc_model.lookIn.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				switch (te.getId()) {
				case MTGestureEvent.GESTURE_DETECTED:
					//Same as drop down button
					if(fc_model.lookInShown) {
						fc_model.lookInDropDown.setVisible(false);
						
					}
					else {
						fc_model.lookInDropDown.setVisible(true);
						fc_model.lookInDropDown.sendToFront();
					}
					fc_model.lookInShown = !fc_model.lookInShown;
					break;
				case MTGestureEvent.GESTURE_UPDATED:
					break;
				case MTGestureEvent.GESTURE_ENDED:
					break;
				}
				return false;
			}
		});
		
		//Drop down button
		fc_model.dropDown = new MTImageButton(fc_model.getScene().getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
				+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
				+ "dropDown.png"), fc_model.getScene().getMTApplication());
		fc_model.dropDown.setNoStroke(true);
		fc_model.dropDown.translateGlobal(new Vector3D(fc_model.lookIn.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)-fc_model.dropDown.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)-2,
				5,0));
		//dropDown.addActionListener();
		fc_model.lookIn.addChild(fc_model.dropDown);
		fc_model.window.addChild(fc_model.lookIn);
		fc_model.currLookInIcon = new MTRectangle(fc_model.getScene().getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
				+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
				+"desktop.png"), fc_model.getScene().getMTApplication());
		//Disable drag, rotate and scale
		fc_model.currLookInIcon.setGestureAllowance(DragProcessor.class, false);
		fc_model.currLookInIcon.setGestureAllowance(RotateProcessor.class, false);
		fc_model.currLookInIcon.setGestureAllowance(ScaleProcessor.class, false);
		fc_model.currLookInIcon.setNoStroke(true);
		fc_model.lookIn.addChild(fc_model.currLookInIcon);
		fc_model.currLookInIcon.translate(new Vector3D(4,5));
		
		// Create text field for the cell
		fc_model.lookInText = new MTTextArea(fc_model.getScene().getMTApplication(),
				FontManager.getInstance().createFont(fc_model.getScene().getMTApplication(), "arial.ttf", 
						15, 	//Font size
						MTColor.BLACK));
		fc_model.lookInText.setPickable(false);
		fc_model.lookInText.unregisterAllInputProcessors();
		fc_model.lookInText.setText("     "+fc_model.currDir.getName());
		fc_model.lookInText.setNoFill(true);
		fc_model.lookInText.setNoStroke(true);
		//Add clip so text doesn't cover up drop down button
		fc_model.lookInText.setClip(new Clip(fc_model.getScene().getMTApplication(),
				fc_model.lookInText.getVerticesLocal()[0].x, 
				fc_model.lookInText.getVerticesLocal()[0].y, 
				253, 30));
		fc_model.lookIn.addChild(fc_model.lookInText);
		fc_model.lookInText.translate(new Vector3D(1, 1));
		
		//File name
		// Create text area for the file name
		MTTextArea filenameText = new MTTextArea(fc_model.getScene().getMTApplication(),
				FontManager.getInstance().createFont(fc_model.getScene().getMTApplication(), "arial", 
				22, 	//Font size
				new MTColor(0, 0, 0, 255),   //Font fill color
				new MTColor(0, 0, 0, 75)));
		filenameText.unregisterAllInputProcessors();
		filenameText.setText("File name:");
		filenameText.setPickable(false);
		filenameText.setNoFill(true);
		filenameText.setNoStroke(true);
		fc_model.window.addChild(filenameText);
		filenameText.translate(new Vector3D(20, 382));
        
		fc_model.fileText = new MTTextArea(fc_model.getScene().getMTApplication(), FontManager.getInstance().createFont(fc_model.getScene().getMTApplication(), "arial.ttf", 
				15, 	//Font size
				MTColor.BLACK)); //Stroke color
        filenameText.setNoFill(true);
		filenameText.setNoStroke(true);
		fc_model.fileText.setPickable(false);
		fc_model.fileText.unregisterAllInputProcessors();
		fc_model.fileText.setEnableCaret(true);
		
        //File name box to spawn keyboard and recieve input text
		MTRoundRectangle filename = new MTRoundRectangle(fc_model.getScene().getMTApplication(), 0, 0, 0, 290 , 30, 5, 5);
		filename.translate(new Vector3D(130,380));
		filename.setStrokeColor(new MTColor(0,0,0, 150));
		filename.setStrokeWeight(2);
		filename.removeAllGestureEventListeners();
		filename.registerInputProcessor(new TapProcessor(fc_model.getScene().getMTApplication()));
		filename.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				switch (te.getId()) {
				case MTGestureEvent.GESTURE_DETECTED:
					//Open Button for the keyboard
			        final MTImageButton openButton = new MTImageButton(fc_model.getScene().getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
							+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
							+"okButton.png"), fc_model.getScene().getMTApplication());
			        openButton.setNoStroke(true);;
			        //flickrButton.scale(0.4f, 0.4f, 1, new Vector3D(0,0,0), TransformSpace.LOCAL);
			        openButton.translate(new Vector3D(10, 20,0));
			       // flickrButton.setBoundsPickingBehaviour(AbstractShape.BOUNDS_ONLY_CHECK);
			        
			        MTTextArea ok = new MTTextArea(fc_model.getScene().getMTApplication(), 
			        		FontManager.getInstance().createFont(fc_model.getScene().getMTApplication(), "arial.ttf", 
									15, 	//Font size
									MTColor.BLACK));
					ok.setFillColor(new MTColor(150,150,150));
					ok.setNoFill(true);
					ok.setNoStroke(true);
					ok.setText("Open");
					ok.setPickable(false);
					openButton.addChild(ok);
					ok.setPositionRelativeToParent(new Vector3D(33,12,0));
					
					//Flickr Keyboard
					fc_model.keyb = new MTKeyboard(fc_model.getScene().getMTApplication());
					fc_model.keyb.setFillColor(new MTColor(30, 30, 30, 210));
					fc_model.keyb.setStrokeColor(new MTColor(0,0,0,255));
			        //Add actionlistener to flickr button
					//openButton.addActionListener(new ActionListener() {
			        openButton.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			    			@Override
			    			public boolean processGestureEvent(final MTGestureEvent ge) {
			    				TapEvent te = (TapEvent) ge;
			    				switch(te.getTapID()) {
			    				case TapEvent.TAPPED:
			    					/*
									if (arg0.getSource() instanceof MTComponent){
										//MTBaseComponent clickedComp = (MTBaseComponent)arg0.getSource();
										switch (arg0.getID()) {
										case TapEvent.BUTTON_CLICKED:
											if(fileText.getText().matches("^[^ \t\n].*")) {
												setSelectedFile(new WBFile(scene, new File(currDir.getFile().getPath()+"\\"+fileText.getText())));
												openSelected();
												fileText.clear();
												keyb.destroy();
											}
											setSelectedFile(null);
											break;
										default:
											break;
										}
									}*/
			    					break;
			    				
			    				default:
			    					break;
			    				}
			    				
			    				
			    				
			    				
			    			return false;
			    			}
			    		});

			        fc_model.keyb.addTextInputListener(fc_model.fileText);
			        fc_model.keyb.addChild(openButton);
			        fc_model.keyb.setPositionGlobal(new Vector3D(fc_model.getScene().getMTApplication().width/2f, fc_model.getScene().getMTApplication().height/2f-55,0));
					fc_model.getScene().getGuiOverlay().addChild(fc_model.keyb);
					break;
				case MTGestureEvent.GESTURE_UPDATED:
					break;
				case MTGestureEvent.GESTURE_ENDED:
					break;
				}
				return false;
			}
		});
		filename.addChild(fc_model.fileText);
		fc_model.window.addChild(filename);
		fc_model.fileText.setPickable(false);
		fc_model.fileText.translate(new Vector3D(1, 3));
		fc_model.fileText.setClip(new Clip(fc_model.getScene().getMTApplication(),
				fc_model.fileText.getVerticesLocal()[0].x+2, 
				fc_model.fileText.getVerticesLocal()[0].y+4, 
				285, 26));
		
		//File type
		// Create text area for the file name
		MTTextArea filetypeText = new MTTextArea(fc_model.getScene().getMTApplication(),
				FontManager.getInstance().createFont(fc_model.getScene().getMTApplication(), "arial.ttf", 
						15, 	//Font size
						MTColor.BLACK));
		filetypeText.setGestureAllowance(DragProcessor.class, false);
		filetypeText.setGestureAllowance(RotateProcessor.class, false);
		filetypeText.setGestureAllowance(ScaleProcessor.class, false);
		filetypeText.setPickable(false);
		filetypeText.setText("File type:");
		filetypeText.setNoFill(true);
		filetypeText.setNoStroke(true);
		fc_model.window.addChild(filetypeText);
		filetypeText.translate(new Vector3D(20, 432));
		
		//Look in
		//Scrollable list of files in current directory
		fc_model.types = new MTList(0,0, 380, 100, 2, fc_model.getScene().getMTApplication());
		fc_model.window.addChild(fc_model.types);
		fc_model.types.translateGlobal(new Vector3D(130,460));
		fc_model.types.setStrokeColor(new MTColor(0,0,0));
		
		//Fill window with file filters		
		//Images
		fc_model.imagesFilter = new WBFileFilter(fc_model.getScene(), (ExtensionFileFilter)fc_model.images);
		fc_model.imagesFilter.setName("images...");
		fc_model.types.addListElement(fc_model.imagesFilter);
		//Videos
		fc_model.videosFilter = new WBFileFilter(fc_model.getScene(), (ExtensionFileFilter)fc_model.videos);
		fc_model.videosFilter.setName("videos...");
		fc_model.types.addListElement(fc_model.videosFilter);
		//XML
		fc_model.XMLFilter = new WBFileFilter(fc_model.getScene(), (ExtensionFileFilter)fc_model.xml);
		fc_model.XMLFilter.setName("XML...");
		fc_model.types.addListElement(fc_model.XMLFilter);
		//All Formats
		fc_model.allFilter = new WBFileFilter(fc_model.getScene(), null);
		fc_model.allFilter.setName("all types...");
		fc_model.types.addListElement(fc_model.allFilter);
		fc_model.types.setVisible(false);
		fc_model.currFilter = fc_model.allFilter;
		fc_model.currFilter.setNoFill(false);
		fc_model.currFilter.setStrokeColor(new MTColor(0,0,0, 150));
		fc_model.currFilter.setStrokeWeight(2);
		fc_model.currFilter.setNoStroke(false);
		
		//File type drop down
		MTRoundRectangle filetype = new MTRoundRectangle (fc_model.getScene().getMTApplication(), 0, 0, 0, 290, 30, 5, 5 );
		filetype.translate(new Vector3D(130,430));
		filetype.setStrokeColor(new MTColor(0,0,0, 150));
		filetype.setStrokeWeight(2);
		filetype.removeAllGestureEventListeners();
		filetype.registerInputProcessor(new TapProcessor(fc_model.getScene().getMTApplication()));
		filetype.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				switch (te.getId()) {
				case MTGestureEvent.GESTURE_DETECTED:
					if(fc_model.typesShown) {
						if(fc_model.currSelectedFilter != fc_model.currFilter && fc_model.currSelectedFilter != null) {
							fc_model.currSelectedFilter.setStrokeColor(new MTColor(0,0,0,0));
							fc_model.currSelectedFilter.setNoFill(true);
						}
						fc_model.currFilter.setNoFill(false);
						fc_model.currFilter.setStrokeColor(new MTColor(0,0,0, 150));
						fc_model.currFilter.setStrokeWeight(2);
						fc_model.currFilter.setNoStroke(false);
						fc_model.types.setVisible(false);
					}
					else {
						fc_model.types.setVisible(true);
						fc_model.types.sendToFront();
					}
					fc_model.typesShown = !fc_model.typesShown;				
					break;
				case MTGestureEvent.GESTURE_UPDATED:
					break;
				case MTGestureEvent.GESTURE_ENDED:
					break;
				}
				return false;
			}
		});
		fc_model.window.addChild(filetype);
		
		// Create text field for the cell
		fc_model.typeText = new MTTextArea(fc_model.getScene().getMTApplication(),
				FontManager.getInstance().createFont(fc_model.getScene().getMTApplication(), "arial.ttf", 
						15, 	//Font size
						MTColor.BLACK));
		fc_model.typeText.setGestureAllowance(DragProcessor.class, false);
		fc_model.typeText.setGestureAllowance(RotateProcessor.class, false);
		fc_model.typeText.setGestureAllowance(ScaleProcessor.class, false);
		fc_model.typeText.setText(fc_model.currFilter.getTextBox().getText());
		fc_model.typeText.setNoFill(true);
		fc_model.typeText.setNoStroke(true);
		//Add clip so text doesn't cover up drop down button
		fc_model.typeText.setClip(new Clip(fc_model.getScene().getMTApplication(),
				fc_model.typeText.getVerticesLocal()[0].x, 
				fc_model.typeText.getVerticesLocal()[0].y, 
				263, 30));
		filetype.addChild(fc_model.typeText);
		fc_model.typeText.setPickable(false);
		fc_model.typeText.translate(new Vector3D(1, 1));
		
		//Drop down button
		fc_model.dropDown = new MTImageButton(fc_model.getScene().getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
				+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
				+"dropDown.png"), fc_model.getScene().getMTApplication());
		fc_model.dropDown.setNoStroke(true);
		fc_model.dropDown.translateGlobal(new Vector3D(filetype.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)-fc_model.dropDown.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)-2,
				5,0));
		
		fc_model.dropDown.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				switch(te.getTapID()) {
				case TapEvent.TAPPED:
					if(fc_model.typesShown) {
						if(fc_model.currSelectedFilter != fc_model.currFilter && fc_model.currSelectedFilter != null) {
							fc_model.currSelectedFilter.setStrokeColor(new MTColor(0,0,0,0));
							fc_model.currSelectedFilter.setNoFill(true);
						}
						fc_model.currFilter.setNoFill(false);
						fc_model.currFilter.setStrokeColor(new MTColor(0,0,0, 150));
						fc_model.currFilter.setStrokeWeight(2);
						fc_model.currFilter.setNoStroke(false);
						fc_model.types.setVisible(false);
					}
					else {
						fc_model.types.setVisible(true);
						fc_model.types.sendToFront();
					}
					fc_model.typesShown = !fc_model.typesShown;				
					break;
				
				default:
					break;
				}
			return false;
			}
		});
		filetype.addChild(fc_model.dropDown);
	}
	// Listeners
	//TODO: action Listern implementieren
	void addBackButtonListener(IGestureEventListener mal) {
		//fc_model.backButton.addActionListener(mal);
		fc_model.backButton.addGestureListener(TapProcessor.class, mal);
    }
	
	void addUpButtonListener(IGestureEventListener mal) {
		//fc_model.upButton.addActionListener(mal);
		fc_model.upButton.addGestureListener(TapProcessor.class, mal);
    }
	
	void addOpenButtonListener(IGestureEventListener mal) {
		//fc_model.openButton.addActionListener(mal);
		fc_model.openButton.addGestureListener(TapProcessor.class, mal);
    }
	
	void addCancelButtonListener(IGestureEventListener mal) {
		//fc_model.cancelButton.addActionListener(mal);
		fc_model.cancelButton.addGestureListener(TapProcessor.class, mal);
    }
	
	void addCloseButtonListener(IGestureEventListener mal) {
		//fc_model.closeButton.addActionListener(mal);
		fc_model.closeButton.addGestureListener(TapProcessor.class, mal);
    }
	
	void addDropDownListener(IGestureEventListener mal) {
		//fc_model.dropDown.addActionListener(mal);
		fc_model.dropDown.addGestureListener(TapProcessor.class, mal);
    }
	


}
