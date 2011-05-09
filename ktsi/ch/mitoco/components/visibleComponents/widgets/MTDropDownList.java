package ch.mitoco.components.visibleComponents.widgets;


import java.util.Arrays;
import java.util.List;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.IFont;
import org.mt4jx.components.visibleComponents.widgets.MTSuggestionTextArea;
/**
 * MTDropDownList provide a DropDownList with five import levels.
 * 
 * @author tandrich
 *
 */

public class MTDropDownList extends MTRoundRectangle {
	
	/** The MTApplication. */
	private AbstractMTApplication app;
	
	/** The Font.  */
	private IFont ifont;
	
	/** Width. */
	private int width;
	
	/** Height. */
	private int height;
	
	/** Fieldname. */
	private String fname;
	
	/** Standard Color. */
	 private MTColor blue1 = new MTColor(51, 102, 204, 180);
	
	/** Transparenz Color. */
	 private MTColor trans = new MTColor(0, 0, 0, 10);
	
	/** Textare Label.*/
	private MTTextArea label;
	
	/** Label Font. */
	private IFont labelfont;
	
	/** MTSugestionTextArea. */
	private MTSuggestionTextArea sa;
	
	/** Dropdown List String*/
	private String[] values;

	/** */
	private List<String> list;
	
	/** 
	 * Construtor MTDropDownList.
	 * 
	 * @param app AbstractMTApplication
	 * @param font Ifont
	 * @param width int
	 * @param height int
	 * @param arc int 
	 * @param labelname String
	 * @param labelfont IFont
	 * 
	 * */
	public MTDropDownList(final AbstractMTApplication app, final IFont font, final int width, final int height, final int arc, final String labelname, final IFont labelfont) {
		super(app, 0, 0, 0, width, height, 5, 5);
		this.ifont = font;
		this.width = width;
		this.height = height;
		this.fname = labelname;
		this.labelfont = labelfont;
		this.setFillColor(blue1);
		this.setStrokeColor(MTColor.BLACK);
		this.init(app);
		
	}
	/** 
	 * Init MTDropDown List.
	 * @param app2 AbststractMTApplication
	 * */

	private void init(final AbstractMTApplication app2) {		
		
		final String[] defaultText = { "sehr wichtig", "wichtig", "nötig", "vorhanden", "unnötig", "nicht benötigt"};
		list = Arrays.asList(defaultText);
		sa = new MTSuggestionTextArea(app2, width, list);
		sa.setPickable(true);
		sa.setFontColor(MTColor.BLACK);
		sa.setFillColor(trans);
		sa.setFont(ifont);
		sa.setNoStroke(true);
		sa.setWidthLocal(width);
		
		
		sa.setGestureAllowance(DragProcessor.class, false);
		sa.setGestureAllowance(ScaleProcessor.class, false);
		sa.setGestureAllowance(RotateProcessor.class, false);
		sa.setGestureAllowance(TapProcessor.class, true);
		this.addChild(sa);
		
		label = new MTTextArea(app, 0, -labelfont.getOriginalFontSize(), width, height, labelfont);
		label.setInnerPadding(0);
		label.setNoFill(true);
		label.setStrokeColor(MTColor.LIME);
		label.setNoStroke(true);
		label.setText(fname);
		label.setPickable(false);
		label.setVisible(false);
		
		this.addChild(label);
		
	}
	
	/**
	 * Set Attribut to min Modus.
	 */
	public final void setMin() {
		label.setVisible(false);
	}
	
	/**
	 * Set Attribut to max Modus.
	 */
	public final void setMax() {
		label.setVisible(true);
	}
	
	/** 
	 * Set Label Text.
	 * @param labeltext String
	 */
	public final void setLabel(final String labeltext) {
		fname = labeltext;
	}
	
	/** 
	 * Get Label Text.
	 * @return fname String
	 */
	public final String getLabel() {
		return fname;
	}

	/** 
	 * Set Value Text.
	 * @param value String[]
	 */
	public final void setValue(final String[] value) {
		list.clear();
		list = Arrays.asList(value);
	}
	
	/** 
	 *Get Value Text.
	 *@return dString String 
	 */
	public final String[] getValue() {
		return values;
	}

	
	
}
