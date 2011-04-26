package test.thomas;

import java.awt.dnd.DragGestureEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4jx.components.visibleComponents.widgets.MTSuggestionTextArea;
/**
 * MTDropDownList provide a DropDownList with five import levels
 * 
 * @author tandrich
 *
 */

public class MTDropDownList extends MTRoundRectangle {
	
	/** The MTApplication. */
	private AbstractMTApplication app;
	
	/** The Font  */
	private IFont ifont;
	
	/** Width */
	private int width;
	
	/** Height */
	private int height;
	
	/** Fieldname*/
	private String fname;
	
	/** Standard Color */
	final MTColor blue1 = new MTColor(51,102,204,180);
	
	/** Transparenz Color*/
	final MTColor trans = new MTColor(0,0,0,110);
	
	/** Textare Label*/
	private MTTextArea label;
	
	/** Label Font*/
	private IFont labelfont;
	
	private MTSuggestionTextArea sa;
	
	public MTDropDownList(AbstractMTApplication app,IFont font,int width, int height,int arc, String labelname, IFont labelfont){
		super(app,0,0,0,width,height,5,5);
		this.ifont = font;
		this.width = width;
		this.height= height;
		this.fname=labelname;
		this.labelfont=labelfont;
		this.setFillColor(blue1);
		this.setStrokeColor(MTColor.BLACK);
		
		this.init(app);
		
	}

	private void init(AbstractMTApplication app2) {		
		final String[] animals = { "sehr wichtig", "wichtig", "nötig","vorhanden", "unnötig", "nicht benötigt"};
		final List<String> list = Arrays.asList( animals);
		sa = new MTSuggestionTextArea(app2, width, list);
		sa.setPickable(true);
		sa.setFontColor(MTColor.BLACK);
		sa.setFillColor(trans);
		sa.setFont(ifont);
		sa.setNoStroke(true);
		sa.setWidthLocal(width);
		sa.setGestureAllowance(TapProcessor.class, true);
		sa.setGestureAllowance(DragProcessor.class, false);
		
		this.addChild(sa);
		
	}
	

	
	
	
	
}
