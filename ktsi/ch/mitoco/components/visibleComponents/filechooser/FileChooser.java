package ch.mitoco.components.visibleComponents.filechooser;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
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
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;

import processing.core.PImage;
import ch.mitoco.main.MitocoScene;

/**
 * The object used to establish filters for the types of files that can be opened by the whiteboard application.
 * Also, opens the files and imports into the application.
 */
public class FileChooser extends MTComponent
//extends MTRoundRectangle
{
	/** Parent scene */
	private MitocoScene scene;
	
	/** File browser globals **/
	protected WBFile currFile=null;
	protected List<WBFile> currDispFiles;
	protected WBFile currDir;
	protected Stack<WBFile> prevDirs;
	protected WBFile top;
	protected WBFile myComputer;
	protected boolean stopAtDesktop = true;
	
	/** File filters to specify acceptable file types **/
	protected WBFileFilter currFilter = null;
	protected WBFileFilter imagesFilter;
	protected WBFileFilter videosFilter;
	protected WBFileFilter XMLFilter;
	protected WBFileFilter PDFFilter;
	protected WBFileFilter allFilter;
	protected WBFileFilter currSelectedFilter = null;
	protected FileFilter images;
	protected FileFilter videos;
	protected FileFilter xml;
	protected FileFilter pdf;
	protected FileFilter powerpoints;
	
	/** GUI globals **/
	protected MTRoundRectangle window;
	protected boolean windowShown;
	protected MTList fileSelector;
	protected MTImageButton backButton;
	protected MTImageButton upButton;
	protected MTImageButton openButton;
	protected MTSvgButton closeButton;
	protected MTImageButton cancelButton;
	protected MTImageButton dropDown;
	protected MTRoundRectangle lookIn;
	protected MTRectangle currLookInIcon;
	protected MTTextArea lookInText;
	protected boolean lookInShown;
	protected MTList lookInDropDown;
	protected MTList types;
	protected MTTextArea typeText;
	protected boolean typesShown;
	protected MTTextArea fileText;
	protected MTKeyboard keyb;
	private String selectionPath;
	
	/**
	 * Constructor.
	 * @param sc
	 */
	public FileChooser(final MitocoScene sc)
	{
		this(FileSystemView.getFileSystemView().getHomeDirectory().toString(), sc);
	}
	
	/**
	 * Constructor.
	 * @param homeDir
	 * @param sc
	 */
	public FileChooser(String homeDir, final MitocoScene sc) 
	{
		//super(sc.getMTApplication(), 30, 30, 0, 40, 40, 5, 5);
		super(sc.getMTApplication());
		scene = sc;
		this.setName("FileChooser");
		//this.setNoFill(true);
		//this.setNoStroke(true);
		this.translate(new Vector3D(100, 100, -10));
		//this.setSizeLocal(400,400);
		
		selectionPath = "";
		
		currDir = new WBFile(scene, new File(homeDir));
		//currDir = new WBFile(scene, new File("C:\\webserver\\"));
		currDispFiles = new ArrayList<WBFile>();
		prevDirs = new Stack<WBFile>();
		
		images = new ExtensionFileFilter("Images(*.jpg, *.jpeg, *.png, *.bmp)", 
	    		new String[] { "JPG", "JPEG", "PNG", "BMP" });
	    videos = new ExtensionFileFilter("Videos(*.wmv, *.mov, *.avi, *.flv, *mp4)", 
	    		new String[] { "WMV", "MOV", "AVI", "FLV", "MP4" });
	    xml = new ExtensionFileFilter("XML(*.xml)", new String[] { "XML" });
	    pdf = new ExtensionFileFilter("PDF(*.pdf)", new String[] { "PDF" });
	    powerpoints = new ExtensionFileFilter("PowerPoint(*.ppt)", new String[] { "PPT" });
	    
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
		
		// Create the GUI components
	    FileChooserView fc_view = new FileChooserView(this);
	    
	    //Fill window with directory contents
		changeDirectory(currDir);		
				
		FileChooserController fc_controller = new FileChooserController(this, fc_view);
		this.addChild(window);
		
		this.setVisible(true);
		this.sendToFront();
	}
	
	public MTComponent getUI(){
		return window;
	}
	
	
	/**
	 * Gets the scene.
	 * @return The WhiteboardView scene.
	 */
	public MitocoScene getScene() 
	{
        return scene;
    }
	
	/**
	 * Method to show file chooser with the specified file filter applied.
	 */
	public void toggleFileChooser(String filter) 
	{
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
			setSelectedFileFilter(XMLFilter);
			setFileFilter(XMLFilter);
			refreshDirectory();
		}
		if(filter == "pdf") {
			setSelectedFileFilter(PDFFilter);
			setFileFilter(PDFFilter);
			refreshDirectory();
		}
		if(filter == "slideshow") {
			setSelectedFileFilter(allFilter);
			setFileFilter(allFilter);
			refreshDirectory();
		}
		
		if(!windowShown) {
			this.setVisible(true);
			this.sendToFront();
			window.setVisible(true);
			window.sendToFront();
		}
		
		windowShown = true;
	}
	
	/**
	 * Method to toggle the file chooser's visibilty.
	 */
	public void toggleFileChooser() {
		if(!windowShown) {
			//this.setVisible(true);
			//this.sendToFront();
			window.setVisible(true);
			window.sendToFront();
		}
		else 
			//this.setVisible(false);
			window.setVisible(false);
		
		windowShown = !windowShown;
	}
	

	/**
	 * Method to set the selected file.
	 * @param file
	 */
	public void setSelectedFile(WBFile file) {
		if(currFile == null)
			currFile = file;
		if( currFile != file) {
			currFile.setStrokeColor(new MTColor(0,0,0,0));
			currFile.setNoFill(true);
			currFile = file;
		}
	}

	/**
	 * Method to set the visually selected filter
	 * @param filter
	 */
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
	/**
	 * Method to set filter to be used and takes in to account the possibility of not finishing the tap+hold gesture.
	 * @param filter
	 */
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
	
	/**
	 * Method to find and add the roots of the machine to the list.  Called for WBFile.computer
	 */
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
	
	/**
	 * Method to refresh the file list when a new filter is selected.
	 */
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
	
	/**
	 * Method to remove all currently displayed files and show the specified directory's contents
	 * @param dir
	 */
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
					target=0;
					size = list.size();
					if(allFiles || currFilter.getFilter() == xml) {
						for(int i=0; i<size; i++) {
							if(xml.accept(list.get(target))) {
								WBFile tmpXml = new WBFile(scene, list.get(target));
								fileSelector.addListElement(tmpXml);
								tmpXml.setIcon("xml");
								currDispFiles.add(tmpXml);
								list.remove(list.get(target));
							}
							else
								target++;
						}
					}
					
					target=0;
					size = list.size();
					if(allFiles || currFilter.getFilter() == pdf) {
						for(int i=0; i<size; i++) {
							if(pdf.accept(list.get(target))) {
								WBFile tmpPdf = new WBFile(scene, list.get(target));
								fileSelector.addListElement(tmpPdf);
								tmpPdf.setIcon("pdf");
								currDispFiles.add(tmpPdf);
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
							+ "drive.png"));
				}
				//Top desktop
				else if(stopAtDesktop && currDir.getFile().getPath().toString().compareTo(top.getFile().getPath().toString()) == 0) {
					lookInText.setText("     "+currDir.getName());
					currLookInIcon.setTexture(scene.getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
							+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
							+ "desktop.png"));
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
	

	/**
	 * Method that opens the currently selected file -- triggered by tapping
	 * the "Open" button or finishing the tap+hold gesture
	 */
	public void openSelected() {
		File file = currFile.getFile();
		if(!file.exists() && !FileSystemView.getFileSystemView().isDrive(currFile.getFile())) {
			scene.getGuiOverlay().addChild(new WBErrorMessage(scene.getMTApplication(), "File does not exist!"));
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
							+ "noUpFolder.png"));
    			}
    			else {
        			upButton.setEnabled(true);
        			upButton.setTexture(scene.getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
							+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
							+"upFolder.png"));
    			}
			}
		}
		else {
        	//Add video
			
        	if(videos.accept(file) && !file.isDirectory()){
            	
        		MTMovieClip clip = new MTMovieClip(file.getPath(), new Vertex(20, 20, 0), scene);
            	clip.setName(file.getName());
            	clip.setUseDirectGL(true);
    			clip.setHeightXYGlobal(300);
    			clip.addGestureListener(DragProcessor.class, new InertiaDragAction());
    			//scene.getLassoProcessor().addClusterable(clip); //make clip lasso-able
    			scene.getCanvas().addChild(clip);
            	System.out.println("Opening: " + file.getName() + ".");
            	//Update settings
            	//scene.updateSettings(clip);
            	
            	toggleFileChooser();
        	}
        	//Add image
        	else if(images.accept(file) && !file.isDirectory()){
            	System.out.println("FileChooser: ImageLoad: "+ MitocoScene.getImageload() );
        		if(MitocoScene.getImageload()){
        		PImage texture = scene.getMTApplication().loadImage(file.getPath());
        		WBImage photo = new WBImage(texture, scene);
            	photo.setName(file.getName());
            	photo.setNoFill(true);
            	photo.setNoStroke(true);
            	photo.setDisplayCloseButton(true);
            	photo.setHeightXYGlobal(300);
            	photo.addGestureListener(DragProcessor.class, new InertiaDragAction());
    			//scene.getLassoProcessor().addClusterable(photo); //make photo lasso-able
    			scene.getCanvas().addChild(photo);
    			MitocoScene.setImageload(false);
            	}
            	else {
            		selectionPath = file.getPath();
            	}
        		toggleFileChooser();
            	System.out.println("Opening: " + file.getName() + ".");
            	//Update settings
            	//scene.updateSettings(photo); 
            	
        	}
        	// Add xml
        	else if(xml.accept(file) && !file.isDirectory()){
        		selectionPath = file.getPath();
            	scene.drawXMLload(getSelectionPath());
            	MitocoScene.setImageload(false);
            	toggleFileChooser();
        	}
        	
        	else if(pdf.accept(file) && !file.isDirectory()){
        		selectionPath = file.getPath();
        		PDFViewer testpdf = new PDFViewer((MTApplication) (scene.getMTApplication()), "Test", selectionPath);
            	scene.getCanvas().addChild(testpdf);
            	testpdf.setVisible(true);
            	testpdf.sendToFront();
            	MitocoScene.setImageload(false);
            	//scene.drawXMLload(getSelectionPath());
            	toggleFileChooser();
        	}
        	
        	else if(powerpoints.accept(file) && !file.isDirectory()) {
				/*
        		// Run powerpoint to image conversion thread.
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
            	selectionPath = file.getPath();
            	//scene.drawXMLload(getSelectionPath());
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
            		WBSlideShow show = new WBSlideShow(scene.getApp(),dir);
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
        	} */
        	else {
        		//Unreadable drive
        		if(FileSystemView.getFileSystemView().isDrive(currFile.getFile())) {
            		scene.getGuiOverlay().addChild(new WBErrorMessage(scene.getMTApplication(), 
					"Drive cannot be read from!"));
            		currFile.setStrokeColor(new MTColor(0,0,0,0));
        			currFile.setNoFill(true);
        			if(lookInDropDown.isVisible())
						lookInDropDown.setVisible(false);
            	}
        		//Unsupported file type
            	else {
            		scene.getGuiOverlay().addChild(new WBErrorMessage(scene.getMTApplication(), 
    				"Can't open file of that type!"));
            	}
        	}
        }
        	
	}

	/**
	 * @param selectionPath the selectionPath to set
	 */
	final void setSelectionPath(String selectionPath) {
		this.selectionPath = selectionPath;
	}

	/**
	 * @return the selectionPath
	 */
	public String getSelectionPath() {
		return selectionPath;
	}



}
