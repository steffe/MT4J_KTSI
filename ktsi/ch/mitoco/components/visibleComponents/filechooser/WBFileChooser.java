package ch.mitoco.components.visibleComponents.filechooser;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.clipping.Clip;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
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

import processing.core.PImage;
import ch.mitoco.main.MitocoScene;

/** 
 * WBFileChooser - Class to spawn a file chooser
 * @author Chris Allen
 * @Precondition A whiteboard application has been created and the WBScene
 *		has added a WBFileChooser.
 * @Postcondition An icon object has been added to the scene and, when
 *		clicked by the user, will bring up a browser that allows the user
 *		to choose files to be displayed.   
 */

public class WBFileChooser  extends MTImageButton  {
	/** Parent scene */
	private MitocoScene scene;
	
	/** File browser globals **/
	private WBFile currFile=null;
	private List<WBFile> currDispFiles;
	private WBFile currDir;
	private Stack<WBFile> prevDirs;
	private WBFile top;
	private WBFile myComputer;
	private boolean stopAtDesktop = true;
	
	/** GUI globals **/
	private MTRectangle window;
	private boolean windowShown;
	private MTList fileSelector;
	private MTImageButton backButton;
	private MTImageButton upButton;
	private MTImageButton openButton;
	private MTRectangle lookIn;
	private MTRectangle currLookInIcon;
	private MTTextArea lookInText;
	private boolean lookInShown;
	private MTList lookInDropDown;
	private MTList types;
	private MTTextArea typeText;
	private boolean typesShown;
	private MTTextArea fileText;
	private MTKeyboard keyb;
	
	/** File filters to specify acceptable file types **/
	private WBFileFilter currFilter = null;
	private WBFileFilter imagesFilter;
	private WBFileFilter videosFilter;
	private WBFileFilter xmlFilter;
	private WBFileFilter allFilter;
	private WBFileFilter currSelectedFilter = null;
	private FileFilter images;
	private FileFilter videos;
	private FileFilter xml;
	private FileFilter powerpoints;
	private String selectionPath;
	
	//TODO Add button to hold for selecting multiple files
	//TODO Add functionality for opening entire folders as slideshows
	
	public WBFileChooser(final MitocoScene sc) 
	{
		this(FileSystemView.getFileSystemView().getHomeDirectory().toString(), sc);
	}
	
	public WBFileChooser(String homeDir, final MitocoScene sc) 
	{
		super(sc.getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
				+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
				+"browseButton(WB).png"), sc.getMTApplication());
		scene = sc;
		this.setName("FileChooser");
		this.setNoFill(false);
		this.setNoStroke(true);
		this.translate(new Vector3D(5,scene.getMTApplication().height-this.getHeightXY(TransformSpace.GLOBAL)-5,0));	
		
		currDir = new WBFile(scene, new File(homeDir));
		currDispFiles = new ArrayList<WBFile>();
		prevDirs = new Stack<WBFile>();
		
		images = new ExtensionFileFilter("Images(*.jpg, *.jpeg, *.png, *.bmp)", 
	    		new String[] { "JPG", "JPEG", "PNG", "BMP" });
	    videos = new ExtensionFileFilter("Videos(*.wmv, *.mov, *.avi, *.flv, *mp4)", 
	    		new String[] { "WMV", "MOV", "AVI", "FLV", "MP4" });
	    xml = new ExtensionFileFilter("XML(*.xml)", new String[] { "XML" });
	    powerpoints = new ExtensionFileFilter("XML(*.ppt)", new String[] { "PPT" });

	    //Main window
		window = new MTRectangle(0, 0, 0, 440, 520, scene.getMTApplication());
		window.setGestureAllowance(RotateProcessor.class, false);
		window.setGestureAllowance(ScaleProcessor.class, false);
		window.addGestureListener(DragProcessor.class, new InertiaDragAction());
		window.setFillColor(new MTColor(225,225,225,200));
		window.setStrokeColor(new MTColor (0,0,0));

		//Scrollable list of files in current directory
		fileSelector = new MTList(0,0, 400, 300, 2, scene.getMTApplication());
		window.addChild(fileSelector);
		window.sendToFront();
		fileSelector.setAnchor(PositionAnchor.UPPER_LEFT);
		fileSelector.setPositionRelativeToParent(new Vector3D(0,0,0));
		fileSelector.translateGlobal(new Vector3D(20,60, 0));
		fileSelector.setStrokeColor(new MTColor(0,0,0));
		
		//Setup the GUI
		setupGUI();	
		
		//Fill window with directory contents
		changeDirectory(currDir);
		
		scene.getGuiOverlay().addChild(window);
		window.translate(new Vector3D(100,50));
		window.setVisible(false);	
		
		//Disable drag, rotate and scale
		this.setGestureAllowance(DragProcessor.class, false);
		this.setGestureAllowance(RotateProcessor.class, false);
		this.setGestureAllowance(ScaleProcessor.class, false);
		
		//Make button display the file chooser
		 this.addGestureListener(TapProcessor.class, new IGestureEventListener() {
 			@Override
 			public boolean processGestureEvent(final MTGestureEvent ge) {
 				TapEvent te = (TapEvent) ge;
 				switch(te.getTapID()) {
 				case TapEvent.TAPPED:
 						toggleFileChooser();
 					break;
 				
 				default:
 					break;
 				}

 			return false;
 			}
 		});
		
		//Draw this component and its children above 
		//everything previously drawn and avoid z-fighting
		window.setDepthBufferDisabled(true);
	}
	
	
	//Method to show file chooser with the specified file filter applied
	public void toggleFileChooser(String filter) {
		if(filter == "image") {
			setSelectedFileFilter(imagesFilter);
			setFileFilter(imagesFilter);
			refreshDirectory();
		}
		if(filter == "movie") {
			setSelectedFileFilter(videosFilter);
			setFileFilter(videosFilter);
			refreshDirectory();
		}
		if(filter == "xml") {
			setSelectedFileFilter(xmlFilter);
			setFileFilter(xmlFilter);
			refreshDirectory();
		}
		if(filter == "slideshow") {
			setSelectedFileFilter(allFilter);
			setFileFilter(allFilter);
			refreshDirectory();
		}
		
		if(!windowShown) {
			window.setVisible(true);
			window.sendToFront();
		}
		
		windowShown = true;
	}
	
	//Method to toggle the file chooser's visibilty
	public void toggleFileChooser() {
		if(!windowShown) {
			window.setVisible(true);
			window.sendToFront();
		}
		else 
			window.setVisible(false);
		
		windowShown = !windowShown;
	}
	
	//Method to set the selected file
	public void setSelectedFile(WBFile file) {
		if(currFile == null)
			currFile = file;
		if( currFile != file) {
			currFile.setStrokeColor(new MTColor(0,0,0,0));
			currFile.setNoFill(true);
			currFile = file;
		}
	}

	//Method to set the visually selected filter
	public void setSelectedFileFilter(WBFileFilter filter) {
		if(currSelectedFilter == null)
			currSelectedFilter = filter;
		if(currSelectedFilter != filter) {
			currSelectedFilter.setStrokeColor(new MTColor(0,0,0,0));
			currSelectedFilter.setNoFill(true);
			currSelectedFilter = filter;
		}
		if(currSelectedFilter != currFilter && !currFilter.isNoFill()) {
			currFilter.setStrokeColor(new MTColor(0,0,0,0));
			currFilter.setNoFill(true);
		}
	}
	//Method to set filter to be used and takes in to account the
	//possibility of not finishing the tap+hold geusture
	public void setFileFilter(WBFileFilter filter) {
		if(currFilter == null)
			currFilter = filter;
		if(currSelectedFilter != currFilter && currSelectedFilter != null) {
			currSelectedFilter.setStrokeColor(new MTColor(0,0,0,0));
			currSelectedFilter.setNoFill(true);
		}
		if(currFilter != filter) {
			currFilter = filter;
		}
	}
	
	//Method to find and add the roots of the machine to the list
	//Called for WBFile.computer
	public void dispDrives() {
		//Fill window with root directories
        File[] f = File.listRoots();
        for (int i = 0; i < f.length; i++)
        { 
            WBFile tmpFile = new WBFile(scene, f[i]);
            fileSelector.addListElement(tmpFile);
            currDispFiles.add(tmpFile);
            tmpFile.setIcon("directory");
        }
        lookInText.setText("     Computer");
	}
	
	//Method to refresh the file list when a new filter is selected
	public void refreshDirectory() {
		changeDirectory(currDir);
		typeText.setText(currFilter.getTextBox().getText());
		if(currFilter.isNoFill()) {
			currFilter.setNoFill(false);
			currFilter.setStrokeColor(new MTColor(0,0,0, 150));
			currFilter.setStrokeWeight(2);
			currFilter.setNoStroke(false);
		}
		types.setVisible(false);
		typesShown = false;	
	}
	
	//Method to remove all currently displayed files and show the 
	//specified directory's contents
	public void changeDirectory(WBFile dir) {
		for(WBFile target: currDispFiles) {
			fileSelector.removeListElement(target);
		}
		if(dir.isComputer()) {
			dispDrives();
			currDir = dir;
			currLookInIcon.setTexture(scene.getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
					+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
					+ "computer.png"));
		}
		else {
			//Fill window with directory contents
			if(dir.getFile().getPath() != null) {
				if(dir.getFile().isDirectory()) {
					boolean allFiles = false;
					if(currFilter.getFilter() == null)
						allFiles = true;
					ArrayList<File> list = new ArrayList<File>(Arrays.asList(dir.getFile().listFiles()));
					int target=0;
					int size = list.size();
					//First add directories
					for(int i=0; i<size; i++) {
						if(list.get(target).isDirectory()) {
							WBFile tmpDir = new WBFile(scene, list.get(target));
							fileSelector.addListElement(tmpDir);
							tmpDir.setIcon("directory");
							currDispFiles.add(tmpDir);
							list.remove(list.get(target));
						}
						else
							target++;
					}
					
					target=0;
					size = list.size();
					//Then add images
					if(allFiles || currFilter.getFilter() == images) {
						for(int i=0; i<size; i++) {
							if(images.accept(list.get(target))) {
								WBFile tmpImage = new WBFile(scene, list.get(target));
								fileSelector.addListElement(tmpImage);
								tmpImage.setIcon("image");
								currDispFiles.add(tmpImage);
								list.remove(list.get(target));
							}
							else
								target++;
						}
					}
					
					target=0;
					size = list.size();
					//Then add videos
					if(allFiles || currFilter.getFilter() == videos) {
						for(int i=0; i<size; i++) {
							if(videos.accept(list.get(target))) {
								WBFile tmpMovie = new WBFile(scene, list.get(target));
								fileSelector.addListElement(tmpMovie);
								tmpMovie.setIcon("video");
								currDispFiles.add(tmpMovie);
								list.remove(list.get(target));
							}
							else
								target++;
						}
					}
					//XMLs
					if(allFiles || currFilter.getFilter() == xml) {
						for(int i=0; i<size; i++) {
							if(xml.accept(list.get(target))) {
								WBFile tmpXML = new WBFile(scene, list.get(target));
								fileSelector.addListElement(tmpXML);
								tmpXML.setIcon("xml");
								currDispFiles.add(tmpXML);
								list.remove(list.get(target));
							}
							else
								target++;
						}
					}
					
					if(allFiles) {
						for(File file: list) {
							WBFile tmpFile = new WBFile(scene, file);
							fileSelector.addListElement(tmpFile);
							tmpFile.setIcon("");
							currDispFiles.add(tmpFile);
						}
					}
				}				
				currDir = dir;
				//File system root
				if(currDir.getFile().getParent() == null) {
					lookInText.setText("     "+currDir.getFile().getAbsolutePath());
					currLookInIcon.setTexture(scene.getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
							+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
							+"drive.png"));
				}
				//Top desktop
				else if(stopAtDesktop && currDir.getFile().getPath().toString().compareTo(top.getFile().getPath().toString()) == 0) {
					lookInText.setText("     "+currDir.getName());
					currLookInIcon.setTexture(scene.getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
							+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
							+"desktop.png"));
				}
				//Normal desktop
				else {
					lookInText.setText("     "+currDir.getName());
					currLookInIcon.setTexture(scene.getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
							+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
							+ "folder.png"));
				}
			}
		}
		//Enable back button
		if(prevDirs.size() == 1) { //Minimize the times the image is loaded
			backButton.setEnabled(true);
			backButton.setTexture(scene.getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
					+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
					+"back.png"));
		}
	}
	
	//Method that opens the currently selected file -- triggered by tapping
	//the "Open" button or finishing the tap+hold gesture
	public void openSelected() {
		File file = currFile.getFile();
		if(!file.exists() && !FileSystemView.getFileSystemView().isDrive(currFile.getFile())) {
			scene.getGuiOverlay().addChild(new WBErrorMessage(scene, "File does not exist!"));
		}
		else if(file.isDirectory()) {
			//Set whether the up button will disable when it reaches desktop
			if(currFile.isBelowComp())
				stopAtDesktop = false;
			if(currFile == top || currFile.isDesktopLookIn())
				stopAtDesktop = true;
			//Up should always be disabled when the back button stack is empty
			//and obviously if we are at the top
			if(prevDirs.size() == 0 && currFile == top) {
				currFile.setStrokeColor(new MTColor(0,0,0,0));
    			currFile.setNoFill(true);
    			if(lookInDropDown.isVisible())
					lookInDropDown.setVisible(false);
			}
			else {
				prevDirs.push(currDir);
				changeDirectory(currFile);
				if(lookInDropDown.isVisible())
					lookInDropDown.setVisible(false);
				currFile.setStrokeColor(new MTColor(0,0,0,0));
    			currFile.setNoFill(true);
    			//Disable up button if at top
    			if(currFile == top) {
    				upButton.setEnabled(false);
        			upButton.setTexture(scene.getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
							+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
							+"noUpFolder.png"));
    			}
    			else {
        			upButton.setEnabled(true);
        			upButton.setTexture(scene.getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
							+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
							+ "upFolder.png"));
    			}
			}
		}
		else {
        	//Add video
        	
			if(videos.accept(file) && !file.isDirectory()){
            	/*
				MTMovieClip clip = new MTMovieClip(file.getPath(), new Vertex(20,20,0), scene.getMTApplication());
            	clip.setName(file.getName());
            	clip.setUseDirectGL(true);
    			clip.setHeightXYGlobal(300);
    			clip.addGestureListener(DragProcessor.class, new InertiaDragAction());
    			scene.getLassoProcessor().addClusterable(clip); //make clip lasso-able
    			scene.addToMediaLayer(clip);
            	System.out.println("Opening: " + file.getName() + ".");
            	//Update settings
            	scene.updateSettings(clip);
            	*/
				toggleFileChooser();
        	}
        	//Add image
        	else if(images.accept(file) && !file.isDirectory()){
            	PImage texture = 
            		scene.getMTApplication().loadImage(file.getPath());
        		WBImage photo = new WBImage(texture, scene);
            	photo.setName(file.getName());
            	photo.setNoFill(true);
            	photo.setNoStroke(true);
            	photo.setDisplayCloseButton(true);
            	photo.setHeightXYGlobal(300);
            	photo.addGestureListener(DragProcessor.class, new InertiaDragAction());
    			//scene.getLassoProcessor().addClusterable(photo); //make photo lasso-able
    			scene.getCanvas().addChild(photo);
            	System.out.println("Opening: " + file.getName() + ".");
            	//Update settings
            	//scene.updateSettings(photo); 
            	toggleFileChooser();
        	}
        	else if(xml.accept(file) && !file.isDirectory()){
        		setSelectionPath(file.getPath());
            	toggleFileChooser();
        	}
        	// Add powerpoint
        	else if(powerpoints.accept(file) && !file.isDirectory()) {
				// Run powerpoint to image conversion thread.
				/*
        		int numberOfSlides = 0;
				try{
				numberOfSlides = PowerpointToImageConverter.powerPointToImageConversion(file.getCanonicalPath());
				}catch(Exception e){}
				
				// Add powerpoint images to scene.
				ArrayList<File> slides = new ArrayList<File>();
				for(int i = 0; i < numberOfSlides; i++) {
					slides.add(new File("slide-" + (i+1) + ".png"));
				}
        		WBSlideShow photo = new WBSlideShow(scene, slides);
            	photo.setName(file.getName());
            	photo.setNoFill(true);
            	photo.setNoStroke(true);
            	photo.setDisplayCloseButton(true);
            	photo.setHeightXYGlobal(300);
            	photo.addGestureListener(DragProcessor.class, new InertiaDragAction());
    			scene.getLassoProcessor().addClusterable(photo); //make photo lasso-able
    			scene.addToMediaLayer(photo);
    			*/
    			setSelectionPath(file.getPath());
    			
            	toggleFileChooser();
        	}
        	
        	//FIXME see WBSlideShow and WBImage for needed changes
        	//Add slideshow
        	/*else if(file.length == 1 && file[0].isDirectory()){
        		//Remove all non-image files
        		File[] holder = file[0].listFiles();
        		ArrayList<File> dir = createArrayList(holder);
        		for(int index=0; index<dir.size(); index++) {
        			System.out.println("List contains: "+dir.get(index).getName());
        			if(!dir.get(index).canExecute() 
                			|| !images.accept(dir.get(index))
                			|| dir.get(index).isDirectory()){
        				System.out.println("removing "+dir.get(index).getName());
        				dir.remove(index);
        			}
        		}
        		if(dir.size() > 0) {
            		WBSlideShow show = new WBSlideShow(scene.getMTApplication(),dir);
            		scene.getMediaLayer().addChild(show);
            		show.setName(file[i].getName());
            		show.setNoFill(true);
            		show.setDisplayCloseButton(true);
            		show.setHeightXYGlobal(300);
            		show.addGestureListener(DragProcessor.class, new InertiaDragAction());
        			scene.getLassoProcessor().addClusterable(show); //make show lasso-able
                	System.out.println("Opening: " + file[i].getName() + ".");
                	//Update settings
                	scene.updateSettings(show);
        		}
        		else {
        			scene.getCanvas().addChild(new WBErrorMessage(scene, 
    					"There weren't any images in that folder!"));
        		}
        	}*/
        	//else {
        		//Unreadable drive
        		if(FileSystemView.getFileSystemView().isDrive(currFile.getFile())) {
            		scene.getGuiOverlay().addChild(new WBErrorMessage(scene, 
					"Drive cannot be read from!"));
            		currFile.setStrokeColor(new MTColor(0,0,0,0));
        			currFile.setNoFill(true);
        			if(lookInDropDown.isVisible())
						lookInDropDown.setVisible(false);
            	}
        		//Unsupported file type
            	else {
            		scene.getGuiOverlay().addChild(new WBErrorMessage(scene, 
    				"Can't open file of that type!"));
            	}
        	//}
		}
	}
	
	//Method that initializes all of the GUI and it's functionality
	protected void setupGUI() {
		//Back button
		backButton = new MTImageButton(scene.getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
				+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
				+"noBack.png"), scene.getMTApplication());
		window.addChild(backButton);
		backButton.setEnabled(false);
		backButton.setNoStroke(true);
		backButton.setPositionRelativeToParent(new Vector3D(0,0,0));
		backButton.translateGlobal(new Vector3D(window.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)-100,
				32,0));
		
		
		backButton.addGestureListener(TapProcessor.class, new IGestureEventListener() {
 			@Override
 			public boolean processGestureEvent(final MTGestureEvent ge) {
 				TapEvent te = (TapEvent) ge;
 				switch(te.getTapID()) {
 				case TapEvent.TAPPED:
 					WBFile tmp = prevDirs.pop();
					if(prevDirs.size() == 0) {
						backButton.setEnabled(false);
						backButton.setTexture(scene.getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
								+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
								+ "noBack.png"));
						upButton.setEnabled(false);
						upButton.setTexture(scene.getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
								+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
								+ "noUpFolder.png"));
						stopAtDesktop = true;
					}
					changeDirectory(tmp);
					if(prevDirs.size() != 0) {
						upButton.setEnabled(true);
	        			upButton.setTexture(scene.getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
								+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
								+ "upFolder.png"));
					}
					if(stopAtDesktop && currDir.getFile().getPath().toString().compareTo(top.getFile().getPath().toString()) == 0) {
        				upButton.setEnabled(false);
	        			upButton.setTexture(scene.getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
								+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
								+ "noUpFolder.png"));
        			}
 					break;
 				
 				default:
 					break;
 				}

 			return false;
 			}
 		});

		backButton.setName("backButton");
		
		//Up button
		upButton = new MTImageButton(scene.getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
				+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
				+ "noUpFolder.png"), scene.getMTApplication());
		window.addChild(upButton);
		upButton.setNoStroke(true);
		upButton.setPositionRelativeToParent(new Vector3D(0,0,0));
		upButton.translateGlobal(new Vector3D(window.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)-55,
				32,0));
		
		upButton.addGestureListener(TapProcessor.class, new IGestureEventListener() {
 			@Override
 			public boolean processGestureEvent(final MTGestureEvent ge) {
 				TapEvent te = (TapEvent) ge;
 				switch(te.getTapID()) {
 				case TapEvent.TAPPED:
 					prevDirs.push(currDir);
					if (currDir.isComputer()) {
						stopAtDesktop = true;
						changeDirectory(top);
						upButton.setEnabled(false);
						upButton.setTexture(scene.getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
								+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
								+ "noUpFolder.png"));
					}
					else if(currDir.getFile().getParent() == null) { //Root directory
						changeDirectory(WBFile.computer);
					}
					else if (stopAtDesktop && currDir.getFile().getParent().toString().compareTo(top.getFile().getPath().toString()) == 0) {
						changeDirectory(new WBFile(scene, 
								new File(currDir.getFile().getParent())));
						upButton.setEnabled(false);
						upButton.setTexture(scene.getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
								+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
								+ "noUpFolder.png"));
					}
					else {
						changeDirectory(new WBFile(scene, 
								new File(currDir.getFile().getParent())));
					}
 					break;
 				
 				default:
 					break;
 				}

 			return false;
 			}
 		});

		upButton.setName("upButton");
		upButton.setEnabled(false);
		
		//Open button
		openButton = new MTImageButton(scene.getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
				+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
				+ "okButton.png"), scene.getMTApplication());
		window.addChild(openButton);
		openButton.setNoStroke(true);
		openButton.setPositionRelativeToParent(new Vector3D(0,0,0));
		openButton.translateGlobal(new Vector3D(window.getWidthXY(TransformSpace.RELATIVE_TO_PARENT) -145,
				window.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)-30,0));
		
		openButton.addGestureListener(TapProcessor.class, new IGestureEventListener() {
 			@Override
 			public boolean processGestureEvent(final MTGestureEvent ge) {
 				TapEvent te = (TapEvent) ge;
 				switch(te.getTapID()) {
 				case TapEvent.TAPPED:
 					if(currFile != null)
						openSelected();
 					break;
 				
 				default:
 					break;
 				}

 			return false;
 			}
 		});
		
		openButton.setName("openButton");
		
		MTTextArea ok = new MTTextArea(scene.getMTApplication(), 
				FontManager.getInstance().createFont(scene.getMTApplication(), "arial", 15, new MTColor(0,0,0), new MTColor(0,0,0)));
		ok.setFillColor(new MTColor(150,150,150));
		ok.setNoFill(true);
		ok.setNoStroke(true);
		ok.setText("Open");
		ok.setPickable(false);
		openButton.addChild(ok);
		ok.setPositionRelativeToParent(new Vector3D(33,12,0));
		
		//Cancel button
		MTImageButton cancelButton = new MTImageButton(scene.getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
				+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
				+ "okButton.png"), scene.getMTApplication());
		window.addChild(cancelButton);
		cancelButton.setNoStroke(true);
		cancelButton.setPositionRelativeToParent(new Vector3D(0,0,0));
		cancelButton.translateGlobal(new Vector3D(window.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)-60,
				window.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)-30,0));
		cancelButton.addGestureListener(TapProcessor.class, new IGestureEventListener() {
 			@Override
 			public boolean processGestureEvent(final MTGestureEvent ge) {
 				TapEvent te = (TapEvent) ge;
 				switch(te.getTapID()) {
 				case TapEvent.TAPPED:
 					if(currFile != null) {
						currFile.setStrokeColor(new MTColor(0,0,0,0));
						currFile.setNoFill(true);
						currFile = null;
					}
					toggleFileChooser();
 					break;
 				
 				default:
 					break;
 				}

 			return false;
 			}
 		});
		cancelButton.setName("cancelButton");
		
		MTTextArea cancel = new MTTextArea(scene.getMTApplication(), 
				FontManager.getInstance().createFont(scene.getMTApplication(), "arial", 15, new MTColor(0,0,0), new MTColor(0,0,0)));
		cancel.setFillColor(new MTColor(150,150,150));
		cancel.setNoFill(true);
		cancel.setNoStroke(true);
		cancel.setText("Cancel");
		cancel.setPickable(false);
		cancelButton.addChild(cancel);
		cancel.setPositionRelativeToParent(new Vector3D(33,12,0));
		
		//Close button
		MTSvgButton closeButton = new MTSvgButton( this.getRenderer(), System.getProperty("user.dir")+File.separator + "ktsi"
				+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
				+"keybClose.svg");
		window.addChild(closeButton);
		//Transform
		closeButton.scale(0.35f, 0.35f, 1, new Vector3D(0,0,0));
		closeButton.setPositionRelativeToParent(new Vector3D(0,0,0));
		closeButton.translateGlobal(new Vector3D(window.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)-14,
				15,0));
		closeButton.setBoundsPickingBehaviour(AbstractShape.BOUNDS_ONLY_CHECK);
		
		closeButton.addGestureListener(TapProcessor.class, new IGestureEventListener() {
 			@Override
 			public boolean processGestureEvent(final MTGestureEvent ge) {
 				TapEvent te = (TapEvent) ge;
 				switch(te.getTapID()) {
 				case TapEvent.TAPPED:
 					if( currFile != null ) {
						currFile.setStrokeColor(new MTColor(0,0,0,0));
						currFile.setNoFill(true);
						currFile = null;
					}
					toggleFileChooser();
 					break;
 				
 				default:
 					break;
 				}

 			return false;
 			}
 		});
		closeButton.setName("closeButton");
		
		//Look in
		lookInDropDown = new MTList(0,0, 280, 350, 0, scene.getMTApplication());
		window.addChild(lookInDropDown);
		lookInDropDown.translateGlobal(new Vector3D(20,45, 0));
		lookInDropDown.setStrokeColor(new MTColor(0,0,0));
		
		//Fill window with directory contents
		File desktop = new File(FileSystemView.getFileSystemView().getHomeDirectory().toString());
		top = new WBFile(scene, desktop);
		lookInDropDown.addListElement(top);
		top.setIcon("desktop");
		
		//My Computer
		File dummy = new File(FileSystemView.getFileSystemView().getHomeDirectory().toString());
		myComputer = new WBFile(scene, dummy, 20);
		myComputer.setComputer();
		lookInDropDown.addListElement(myComputer);
		
		File[] f = File.listRoots();
        for (int i = 0; i < f.length; i++)
        { 
            WBFile tmpFile = new WBFile(scene, f[i], 40);
            lookInDropDown.addListElement(tmpFile);
            tmpFile.setIcon("drive");
            tmpFile.setBelowComp(true);
        }
		
		File[] list = desktop.listFiles();
		for(int i=0; i<list.length; i++) {
			WBFile tmpFile = new WBFile(scene, list[i], 20);
			if(tmpFile.getFile().isDirectory()) {
				tmpFile.setIcon("directory");
				lookInDropDown.addListElement(tmpFile);
				tmpFile.setDesktopLookIn(true);
			}
		}
		lookInDropDown.setVisible(false);
		
		lookIn = new MTRectangle (0,0,280, 30, scene.getMTApplication());
		lookIn.translate(new Vector3D(20,15));
		lookIn.setStrokeColor(new MTColor(0,0,0, 150));
		lookIn.setStrokeWeight(2);
		lookIn.removeAllGestureEventListeners();
		lookIn.registerInputProcessor(new TapProcessor(scene.getMTApplication()));
		lookIn.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				switch (te.getId()) {
				case MTGestureEvent.GESTURE_DETECTED:
					//Same as drop down button
					if(lookInShown) {
						lookInDropDown.setVisible(false);
						
					}
					else {
						lookInDropDown.setVisible(true);
						lookInDropDown.sendToFront();
					}
					lookInShown = !lookInShown;
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
		MTImageButton dropDown = new MTImageButton(scene.getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
				+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
				+ "dropDown.png"), scene.getMTApplication());
		dropDown.setNoStroke(true);
		dropDown.translateGlobal(new Vector3D(lookIn.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)-dropDown.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)-2,
				5,0));
		
		dropDown.addGestureListener(TapProcessor.class, new IGestureEventListener() {
 			@Override
 			public boolean processGestureEvent(final MTGestureEvent ge) {
 				TapEvent te = (TapEvent) ge;
 				switch(te.getTapID()) {
 				case TapEvent.TAPPED:
 					//Same as lookin rectangle
					if(lookInShown)
						lookInDropDown.setVisible(false);
					else {
						lookInDropDown.setVisible(true);
						lookInDropDown.sendToFront();
					}
					lookInShown = !lookInShown;
 					break;
 				
 				default:
 					break;
 				}

 			return false;
 			}
 		});
		
		lookIn.addChild(dropDown);
		window.addChild(lookIn);
		currLookInIcon = new MTRectangle(scene.getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
				+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
				+ "desktop.png"), scene.getMTApplication());
		//Disable drag, rotate and scale
		currLookInIcon.setGestureAllowance(DragProcessor.class, false);
		currLookInIcon.setGestureAllowance(RotateProcessor.class, false);
		currLookInIcon.setGestureAllowance(ScaleProcessor.class, false);
		currLookInIcon.setNoStroke(true);
		lookIn.addChild(currLookInIcon);
		currLookInIcon.translate(new Vector3D(4,5));
		
		// Create text field for the cell
		lookInText = new MTTextArea(scene.getMTApplication(),
				FontManager.getInstance().createFont(scene.getMTApplication(), "arial", 
				22, 	//Font size
				new MTColor(0, 0, 0, 255),   //Font fill color
				new MTColor(0, 0, 0, 75)));
		lookInText.setPickable(false);
		lookInText.unregisterAllInputProcessors();
		lookInText.setText("     "+currDir.getName());
		lookInText.setNoFill(true);
		lookInText.setNoStroke(true);
		//Add clip so text doesn't cover up drop down button
		lookInText.setClip(new Clip(scene.getMTApplication(),
				lookInText.getVerticesLocal()[0].x, 
				lookInText.getVerticesLocal()[0].y, 
				253, 30));
		lookIn.addChild(lookInText);
		lookInText.translate(new Vector3D(1, 1));
		
		//File name
		// Create text area for the file name
		MTTextArea filenameText = new MTTextArea(scene.getMTApplication(),
				FontManager.getInstance().createFont(scene.getMTApplication(), "arial", 
				22, 	//Font size
				new MTColor(0, 0, 0, 255),   //Font fill color
				new MTColor(0, 0, 0, 75)));
		filenameText.unregisterAllInputProcessors();
		filenameText.setText("File name:");
		filenameText.setPickable(false);
		filenameText.setNoFill(true);
		filenameText.setNoStroke(true);
		window.addChild(filenameText);
		filenameText.translate(new Vector3D(20, 382));
        
        fileText = new MTTextArea(scene.getMTApplication(), FontManager.getInstance().createFont(scene.getMTApplication(), "arial", 22, 
        		new MTColor(0,0,0,255), //Fill color 
				new MTColor(0,0,0,75))); //Stroke color
        filenameText.setNoFill(true);
		filenameText.setNoStroke(true);
        fileText.setPickable(false);
        fileText.unregisterAllInputProcessors();
        fileText.setEnableCaret(true);
		
        //File name box to spawn keyboard and recieve input text
		MTRectangle filename = new MTRectangle (0,0,290, 30, scene.getMTApplication());
		filename.translate(new Vector3D(130,380));
		filename.setStrokeColor(new MTColor(0,0,0, 150));
		filename.setStrokeWeight(2);
		filename.removeAllGestureEventListeners();
		filename.registerInputProcessor(new TapProcessor(scene.getMTApplication()));
		filename.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				switch (te.getId()) {
				case MTGestureEvent.GESTURE_DETECTED:
					//Open Button for the keyboard
			        final MTImageButton openButton = new MTImageButton(scene.getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
							+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
							+ "okButton.png"), scene.getMTApplication());
			        openButton.setNoStroke(true);;
			        //flickrButton.scale(0.4f, 0.4f, 1, new Vector3D(0,0,0), TransformSpace.LOCAL);
			        openButton.translate(new Vector3D(10, 20,0));
			       // flickrButton.setBoundsPickingBehaviour(AbstractShape.BOUNDS_ONLY_CHECK);
			        
			        MTTextArea ok = new MTTextArea(scene.getMTApplication(), 
							FontManager.getInstance().createFont(scene.getMTApplication(), "arial", 15, new MTColor(0,0,0), new MTColor(0,0,0)));
					ok.setFillColor(new MTColor(150,150,150));
					ok.setNoFill(true);
					ok.setNoStroke(true);
					ok.setText("Open");
					ok.setPickable(false);
					openButton.addChild(ok);
					ok.setPositionRelativeToParent(new Vector3D(33,12,0));
					
					//Flickr Keyboard
			        keyb = new MTKeyboard(scene.getMTApplication());
			        keyb.setFillColor(new MTColor(30, 30, 30, 210));
			        keyb.setStrokeColor(new MTColor(0,0,0,255));
			        //Add actionlistener to flickr button
			        
			        openButton.addGestureListener(TapProcessor.class, new IGestureEventListener() {
		    			@Override
		    			public boolean processGestureEvent(final MTGestureEvent ge) {
		    				TapEvent te = (TapEvent) ge;
		    				switch(te.getTapID()) {
		    				case TapEvent.TAPPED:
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

		    			return false;
		    			}
		    		});
			        
			        
			        keyb.addTextInputListener(fileText);
			        keyb.addChild(openButton);
			        keyb.setPositionGlobal(new Vector3D(scene.getMTApplication().width/2f, scene.getMTApplication().height/2f-55,0));
					scene.getGuiOverlay().addChild(keyb);
					break;
				case MTGestureEvent.GESTURE_UPDATED:
					break;
				case MTGestureEvent.GESTURE_ENDED:
					break;
				}
				return false;
			}
		});
		filename.addChild(fileText);
		window.addChild(filename);
		fileText.setPickable(false);
		fileText.translate(new Vector3D(1, 3));
		fileText.setClip(new Clip(scene.getMTApplication(),
				fileText.getVerticesLocal()[0].x+2, 
				fileText.getVerticesLocal()[0].y+4, 
				285, 26));
		
		//File type
		// Create text area for the file name
		MTTextArea filetypeText = new MTTextArea(scene.getMTApplication(),
				FontManager.getInstance().createFont(scene.getMTApplication(), "arial", 
				22, 	//Font size
				new MTColor(0, 0, 0, 255),   //Font fill color
				new MTColor(0, 0, 0, 75)));
		filetypeText.setGestureAllowance(DragProcessor.class, false);
		filetypeText.setGestureAllowance(RotateProcessor.class, false);
		filetypeText.setGestureAllowance(ScaleProcessor.class, false);
		filetypeText.setPickable(false);
		filetypeText.setText("File type:");
		filetypeText.setNoFill(true);
		filetypeText.setNoStroke(true);
		window.addChild(filetypeText);
		filetypeText.translate(new Vector3D(20, 432));
		
		//Look in
		//Scrollable list of files in current directory
		types = new MTList(0,0, 380, 100, 2, scene.getMTApplication());
		window.addChild(types);
		types.translateGlobal(new Vector3D(130,460));
		types.setStrokeColor(new MTColor(0,0,0));
		
		//Fill window with file filters		
		//Images
		imagesFilter = new WBFileFilter(scene, (ExtensionFileFilter)images);
		imagesFilter.setName("images...");
		types.addListElement(imagesFilter);
		//Videos
		videosFilter = new WBFileFilter(scene, (ExtensionFileFilter)videos);
		videosFilter.setName("videos...");
		types.addListElement(videosFilter);
		//All Formats
		allFilter = new WBFileFilter(scene, null);
		allFilter.setName("all types...");
		types.addListElement(allFilter);
		types.setVisible(false);
		currFilter = allFilter;
		currFilter.setNoFill(false);
		currFilter.setStrokeColor(new MTColor(0,0,0, 150));
		currFilter.setStrokeWeight(2);
		currFilter.setNoStroke(false);
		
		//File type drop down
		MTRectangle filetype = new MTRectangle (0,0,290, 30, scene.getMTApplication());
		filetype.translate(new Vector3D(130,430));
		filetype.setStrokeColor(new MTColor(0,0,0, 150));
		filetype.setStrokeWeight(2);
		filetype.removeAllGestureEventListeners();
		filetype.registerInputProcessor(new TapProcessor(scene.getMTApplication()));
		filetype.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				switch (te.getId()) {
				case MTGestureEvent.GESTURE_DETECTED:
					if(typesShown) {
						if(currSelectedFilter != currFilter && currSelectedFilter != null) {
							currSelectedFilter.setStrokeColor(new MTColor(0,0,0,0));
							currSelectedFilter.setNoFill(true);
						}
						currFilter.setNoFill(false);
						currFilter.setStrokeColor(new MTColor(0,0,0, 150));
						currFilter.setStrokeWeight(2);
						currFilter.setNoStroke(false);
						types.setVisible(false);
					}
					else {
						types.setVisible(true);
						types.sendToFront();
					}
					typesShown = !typesShown;				
					break;
				case MTGestureEvent.GESTURE_UPDATED:
					break;
				case MTGestureEvent.GESTURE_ENDED:
					break;
				}
				return false;
			}
		});
		window.addChild(filetype);
		
		// Create text field for the cell
		typeText = new MTTextArea(scene.getMTApplication(),
				FontManager.getInstance().createFont(scene.getMTApplication(), "arial", 
				22, 	//Font size
				new MTColor(0, 0, 0, 255),   //Font fill color
				new MTColor(0, 0, 0, 75)));
		typeText.setGestureAllowance(DragProcessor.class, false);
		typeText.setGestureAllowance(RotateProcessor.class, false);
		typeText.setGestureAllowance(ScaleProcessor.class, false);
		typeText.setText(currFilter.getTextBox().getText());
		typeText.setNoFill(true);
		typeText.setNoStroke(true);
		//Add clip so text doesn't cover up drop down button
		typeText.setClip(new Clip(scene.getMTApplication(),
				typeText.getVerticesLocal()[0].x, 
				typeText.getVerticesLocal()[0].y, 
				263, 30));
		filetype.addChild(typeText);
		typeText.setPickable(false);
		typeText.translate(new Vector3D(1, 1));
		
		//Drop down button
		dropDown = new MTImageButton(scene.getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
				+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
				+ "dropDown.png"), scene.getMTApplication());
		dropDown.setNoStroke(true);
		dropDown.translateGlobal(new Vector3D(filetype.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)-dropDown.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)-2,
				5,0));
		
		dropDown.addGestureListener(TapProcessor.class, new IGestureEventListener() {
 			@Override
 			public boolean processGestureEvent(final MTGestureEvent ge) {
 				TapEvent te = (TapEvent) ge;
 				switch(te.getTapID()) {
 				case TapEvent.TAPPED:
 					if(typesShown) {
						if(currSelectedFilter != currFilter && currSelectedFilter != null) {
							currSelectedFilter.setStrokeColor(new MTColor(0,0,0,0));
							currSelectedFilter.setNoFill(true);
						}
						currFilter.setNoFill(false);
						currFilter.setStrokeColor(new MTColor(0,0,0, 150));
						currFilter.setStrokeWeight(2);
						currFilter.setNoStroke(false);
						types.setVisible(false);
					}
					else {
						types.setVisible(true);
						types.sendToFront();
					}
					typesShown = !typesShown;
 					break;
 				
 				default:
 					break;
 				}

 			return false;
 			}
 		});
		

		filetype.addChild(dropDown);
	}

	/**
	 * @param selectionPath the selectionPath to set
	 */
	private void setSelectionPath(String selectionPath) {
		this.selectionPath = selectionPath;
	}

	/**
	 * @return the selectionPath
	 */
	public String getSelectionPath() {
		return selectionPath;
	}
}
