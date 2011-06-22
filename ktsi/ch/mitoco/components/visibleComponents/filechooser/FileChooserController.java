package ch.mitoco.components.visibleComponents.filechooser;

import java.io.File;

import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.util.MTColor;

/**
 * Generates the Action Listeners for the File Chooser.
 */
public class FileChooserController 
{
	FileChooser fc_model;
	FileChooserView  fc_view;
	
	/**
	 * Constructor.
	 * @param model
	 * @param view
	 */
	FileChooserController(FileChooser model, FileChooserView view) 
	{
        fc_model = model;
        fc_view  = view;
        
        // Add listeners to the view
        view.addBackButtonListener(new BackBtnListener());
        view.addUpButtonListener(new UpBtnListener());
        view.addOpenButtonListener(new OpenBtnListener());
        view.addCancelButtonListener(new CancelBtnListener());
        view.addCloseButtonListener(new CloseBtnListener());
        view.addDropDownListener(new DropDownListener());
	}

	
//	class BackBtnListener implements IMTInputEventListener {
//		
//	}
	
	/**
	 * The back button listener.
	*/
	class BackBtnListener implements IGestureEventListener
	{	@Override
		public boolean processGestureEvent(MTGestureEvent ge) {
			TapEvent te = (TapEvent) ge;
			switch (te.getTapID()) 
			{
			case TapEvent.TAPPED:
				WBFile tmp = fc_model.prevDirs.pop();
				if(fc_model.prevDirs.size() == 0) {
					fc_model.backButton.setEnabled(false);
					fc_model.backButton.setTexture(fc_model.getScene().getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
							+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
							+"noBack.png"));
					fc_model.upButton.setEnabled(false);
					fc_model.upButton.setTexture(fc_model.getScene().getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
							+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
							+"noUpFolder.png"));
					fc_model.stopAtDesktop = true;
				}
				fc_model.changeDirectory(tmp);
				if(fc_model.prevDirs.size() != 0) {
					fc_model.upButton.setEnabled(true);
					fc_model.upButton.setTexture(fc_model.getScene().getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
							+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
							+"UpFolder.png"));
				}
				if(fc_model.stopAtDesktop && fc_model.currDir.getFile().getPath().toString().compareTo(fc_model.top.getFile().getPath().toString()) == 0) {
					fc_model.upButton.setEnabled(false);
					fc_model.upButton.setTexture(fc_model.getScene().getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
							+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
							+"noUpFolder.png"));
    			}
			default:
				break;
			}//switch aeID			
			
			return false;
		}
	}

	
	/**
	 * The up button listener.
	 */
	class UpBtnListener implements IGestureEventListener 
	{
		
		@Override
		public boolean processGestureEvent(MTGestureEvent ge) {
			TapEvent te = (TapEvent) ge;
			switch (te.getTapID()) {
			case TapEvent.TAPPED:
			fc_model.prevDirs.push(fc_model.currDir);
			if (fc_model.currDir.isComputer()) {
				fc_model.stopAtDesktop = true;
				fc_model.changeDirectory(fc_model.top);
				fc_model.upButton.setEnabled(false);
				fc_model.upButton.setTexture(fc_model.getScene().getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
						+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
						+"noUpFolder.png"));
			}
			else if(fc_model.currDir.getFile().getParent() == null) { //Root directory
				fc_model.changeDirectory(WBFile.computer);
			}
			else if (fc_model.stopAtDesktop && fc_model.currDir.getFile().getParent().toString().compareTo(fc_model.top.getFile().getPath().toString()) == 0) {
				fc_model.changeDirectory(new WBFile(fc_model.getScene(), 
						new File(fc_model.currDir.getFile().getParent())));
				fc_model.upButton.setEnabled(false);
				fc_model.upButton.setTexture(fc_model.getScene().getMTApplication().loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
						+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
						+"noUpFolder.png"));
			}
			else {
				fc_model.changeDirectory(new WBFile(fc_model.getScene(), 
						new File(fc_model.currDir.getFile().getParent())));
			}
			default:
				break;
			}//switch aeID			
			
			return false;
		}
	}	
	
	/**
	 * The open button listener.
	 */
	class OpenBtnListener implements IGestureEventListener { 
		@Override
		public boolean processGestureEvent(MTGestureEvent ge) {
			TapEvent te = (TapEvent) ge;
			switch (te.getTapID()) {
			case TapEvent.TAPPED:
				if(fc_model.currFile != null)
					fc_model.openSelected();
			default:
				break;
			}//switch aeID			
			
			return false;
		}
	}
	
	/**
	 * The cancel button listener.
	 */
	class CancelBtnListener implements IGestureEventListener {
		
		@Override
		public boolean processGestureEvent(MTGestureEvent ge) {
			TapEvent te = (TapEvent) ge;
			switch (te.getTapID()) {
			case TapEvent.TAPPED:
				if(fc_model.currFile != null) {
					fc_model.currFile.setStrokeColor(new MTColor(0,0,0,0));
					fc_model.currFile.setNoFill(true);
					fc_model.currFile = null;
				}
				fc_model.toggleFileChooser();
			default:
				break;
			}//switch aeID			
			
			return false;
		}
	}
	
	/**
	 * The close button listener.
	 */
	class CloseBtnListener implements IGestureEventListener {
		@Override
		public boolean processGestureEvent(MTGestureEvent ge) {
			TapEvent te = (TapEvent) ge;
			switch (te.getTapID()) {
			case TapEvent.TAPPED:
				if( fc_model.currFile != null ) {
					fc_model.currFile.setStrokeColor(new MTColor(0,0,0,0));
					fc_model.currFile.setNoFill(true);
					fc_model.currFile = null;
				}
				fc_model.toggleFileChooser();
			default:
				break;
			}//switch aeID			
			
			return false;
		}
	}
	
	/**
	 * The drop down listener.
	 */
	class DropDownListener implements IGestureEventListener {
		@Override
		public boolean processGestureEvent(MTGestureEvent ge) {
			TapEvent te = (TapEvent) ge;
			switch (te.getTapID()) {
			case TapEvent.TAPPED:
				if(fc_model.lookInShown)
					fc_model.lookInDropDown.setVisible(false);
				else {
					fc_model.lookInDropDown.setVisible(true);
					fc_model.lookInDropDown.sendToFront();
				}
				fc_model.lookInShown = !fc_model.lookInShown;
			default:
				break;
			}//switch aeID			
			
			return false;
		}
	}


}
