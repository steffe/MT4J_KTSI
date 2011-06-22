package ch.mitoco.components.visibleComponents.filechooser;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.TransformSpace;
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

import ch.mitoco.main.MitocoScene;

/** 
 * WBFileFilter - Class to extend java's FileFilter class and fit in a MTList
 * @author Chris Allen
 * @Precondition A whiteboard application has been created and the WBScene
 *		has called WBFileChooser which added a WBFileFilter to an MTList.
 * @Postcondition A list cell that represents a file filter for filtering
 *		file types that are acceptable
 */

public class WBFileFilter extends MTListCell
{
	private MitocoScene scene;
	private AbstractMTApplication app;
	private MTTextArea textBox;
	private ExtensionFileFilter filter;
	
	public WBFileFilter(MitocoScene sc, ExtensionFileFilter ff) {
		super(400, 28, sc.getMTApplication());
		this.app = sc.getMTApplication();
		this.scene = sc;
		this.filter = ff;
		this.setNoStroke(true);
		
		// Create text field for the cell
		textBox = new MTTextArea(app,
				FontManager.getInstance().createFont(app, "arial", 
				22, 	//Font size
				new MTColor(0, 0, 0, 255),   //Font fill color
				new MTColor(0, 0, 0, 75)));
		textBox.setPickable(false);
		if(filter == null)
			textBox.setText("All Formats");
		else
			textBox.setText(filter.getDescription());
		
		this.setFillColor(new MTColor(51,153,255));
		this.setNoFill(true);
		textBox.setNoFill(true);
		textBox.setNoStroke(true);
		
		this.setWidthLocal(textBox.getWidthXY(TransformSpace.GLOBAL));
		this.addChild(textBox);
		
		final WBFileFilter thisFilter = this;
		this.registerInputProcessor(new TapProcessor(app));
		this.registerInputProcessor(new TapAndHoldProcessor(app, 500));
		this.addGestureListener(TapAndHoldProcessor.class, new TapAndHoldVisualizer(app, scene.getGuiOverlay()));
		this.addGestureListener(TapAndHoldProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapAndHoldEvent th = (TapAndHoldEvent)ge;
				switch (th.getId()) {
				case MTGestureEvent.GESTURE_DETECTED:
					scene.getFc().setSelectedFileFilter(thisFilter);
					setNoFill(false);
					setStrokeColor(new MTColor(0,0,0, 150));
					setStrokeWeight(2);
					setNoStroke(false);
					break;
				case MTGestureEvent.GESTURE_UPDATED:
					break;
				case MTGestureEvent.GESTURE_ENDED:
					if (th.isHoldComplete()) {
						scene.getFc().setFileFilter(thisFilter);
						scene.getFc().refreshDirectory();
					}
					break;
				}
				return false;
			}
		});
	}

	public MTTextArea getTextBox() {
		return textBox;
	}

	public void setTextBox(MTTextField textBox) {
		this.textBox = textBox;
	}

	public ExtensionFileFilter getFilter() {
		return filter;
	}
}