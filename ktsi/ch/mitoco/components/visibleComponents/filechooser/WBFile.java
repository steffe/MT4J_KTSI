package ch.mitoco.components.visibleComponents.filechooser;

import java.io.File;

import javax.swing.filechooser.FileSystemView;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.math.Vector3D;

import ch.mitoco.main.MitocoScene;

/** 
 * WBFile - Class to represent a file in a WBFileChooser
 * @author Chris Allen
 * @Precondition A whiteboard application has been created and the WBScene
 *		has added a WBFileChooser which requires a new file
 * @Postcondition A cell is created that represents the passed file which
 * 		can be selected and opened
 */

public class WBFile extends MTListCell
{
	private MitocoScene scene;
	static WBFile computer;
	private AbstractMTApplication app;
	
	private MTTextArea textBox;
	private File file;
	private MTRectangle icon;
	private boolean belowComp;
	private boolean desktopLookIn;
	
	public WBFile(MitocoScene sc, File inFile) {
		this(sc, inFile, 3);
	}
	
	public WBFile(MitocoScene sc, File inFile, float indent) {
		super(sc.getMTApplication(), 400, 28);
		this.app = sc.getMTApplication();
		this.scene = sc;
		this.file = inFile;
		//this.setIndent(3+indent);
		this.setName(file.getName());
		this.setLineStipple((short)0xDDDD);
		this.setNoStroke(true);
		
		
		// Create text field for the cell
		textBox = new MTTextArea(app,
				FontManager.getInstance().createFont(app, "arial", 
						22, 	//Font size
						new MTColor(0, 0, 0, 255),   //Font fill color
						new MTColor(0, 0, 0, 75)));
		textBox.setPickable(false);
		if(inFile.getParent() == null) {
			if(file.canRead()) 
				textBox.setText("     "+FileSystemView.getFileSystemView().getSystemDisplayName(file));
			else
				textBox.setText("     "+file.getAbsolutePath());
		}
		else
			textBox.setText("     "+inFile.getName());
		this.setFillColor(new MTColor(51,153,255, 100));
		this.setNoFill(true);
		textBox.setNoFill(true);
		textBox.setNoStroke(true);
		
		this.setWidthLocal(textBox.getWidthXY(TransformSpace.GLOBAL));
		this.addChild(textBox);
		
		final WBFile thisFile = this;
		this.registerInputProcessor(new TapProcessor(app));
		this.registerInputProcessor(new TapAndHoldProcessor(app, 500));
		this.addGestureListener(TapAndHoldProcessor.class, new TapAndHoldVisualizer(app, scene.getGuiOverlay()));
		this.addGestureListener(TapAndHoldProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapAndHoldEvent th = (TapAndHoldEvent)ge;
				switch (th.getId()) {
				case MTGestureEvent.GESTURE_DETECTED:
					scene.getFc().setSelectedFile(thisFile);
					setNoFill(false);
					setStrokeColor(new MTColor(0,0,0, 150));
					setStrokeWeight(2);
					setNoStroke(false);
					break;
				case MTGestureEvent.GESTURE_UPDATED:
					break;
				case MTGestureEvent.GESTURE_ENDED:
					if (th.isHoldComplete())
						scene.getFc().openSelected();
					break;
				}
				return false;
			}
		});
	}
	
	public void setComputer() {
		computer = this;
		textBox.setText("     Computer");
		this.setWidthLocal(textBox.getWidthXY(TransformSpace.GLOBAL));
		icon = new MTRectangle(app.loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
				+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
				+ "computer.png"), app);
		icon.setNoStroke(true);
		icon.setPickable(false);
		this.addChild(icon);
		icon.translate(new Vector3D(3,4));
	}
	
	public boolean isComputer() {
		if(this == computer)
			return true;
		else
			return false;
	}
	
	public void setIcon(String type) {
		if(type == "directory") {
			icon = new MTRectangle(app.loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
					+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
					+"folder.png"), app);
		}
		else if(type == "image") {
			icon = new MTRectangle(app.loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
					+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
					+ "image.png"), app);
		}
		else if(type == "video") {
			icon = new MTRectangle(app.loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
					+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
					+"video.png"), app);
		}
		else if(type == "xml") {
			icon = new MTRectangle(app.loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
					+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
					+"xml.png"), app);
		}
		else if(type == "pdf") {
			icon = new MTRectangle(app.loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
					+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
					+"video.png"), app);
		}
		else if(type == "desktop") {
			icon = new MTRectangle(app.loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
					+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
					+"desktop.png"), app);
		}
		else if(type == "drive") {
			icon = new MTRectangle(app.loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
					+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
					+"drive.png"), app);
		}
		else {
			icon = new MTRectangle(app.loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
					+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
					+"file.png"), app);
		}
		icon.setNoStroke(true);
		icon.setPickable(false);
		this.addChild(icon);
		icon.translate(new Vector3D(3,4));
	}

	public MTTextArea getTextBox() {
		return textBox;
	}

	public void setTextBox(MTTextField textBox) {
		this.textBox = textBox;
	}

	public File getFile() {
		return file;
	}

	public void setBelowComp (boolean in) {
		this.belowComp = in;
	}

	public boolean isBelowComp() {
		return belowComp;
	}

	public void setDesktopLookIn(boolean in) {
		this.desktopLookIn = in;
	}

	public boolean isDesktopLookIn() {
		return desktopLookIn;
	}
}