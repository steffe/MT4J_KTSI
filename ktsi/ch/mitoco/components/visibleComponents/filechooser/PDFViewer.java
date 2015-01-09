package ch.mitoco.components.visibleComponents.filechooser;

import java.io.File;
import java.io.IOException;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.components.visibleComponents.widgets.pdf.MTPDF;

public class PDFViewer extends MTRoundRectangle{
public MTPDF pdf;
	
private MTApplication app;
	
	public PDFViewer(final MTApplication mtApplication, String name, String filename) {
		super(mtApplication,  30, 30, 0, 40, 40, 5, 5);
		this.app = mtApplication;
	
	pdf = new MTPDF(mtApplication, new File(filename));
	pdf.scaleGlobal(.5f, .5f, .5f, pdf.getCenterPointGlobal());
	this.setNoStroke(false);
	MTTextArea previous = new MTTextArea(app, FontManager.getInstance().getDefaultFont(app));
	previous.setText("Previous");
	previous.registerInputProcessor(new TapProcessor(app));
	previous.addGestureListener(TapProcessor.class, new IGestureEventListener() {
		@Override
		public boolean processGestureEvent(MTGestureEvent ge) {
			TapEvent te = (TapEvent)ge;
			if(te.isTapped()){
				try {
					pdf.setPageNumber(pdf.getPageNumber()-1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return false;
		}
	
	});
	pdf.addChild(previous);

	MTTextArea next = new MTTextArea(app, FontManager.getInstance().getDefaultFont(app));
	next.setText("Next");
	next.registerInputProcessor(new TapProcessor(app));
	next.addGestureListener(TapProcessor.class, new IGestureEventListener() {
		@Override
		public boolean processGestureEvent(MTGestureEvent ge) {
			TapEvent te = (TapEvent)ge;
			if(te.isTapped()){
				try {
					pdf.setPageNumber(pdf.getPageNumber()+1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return false;
		}
	});
	next.translate(new Vector3D(200,0));
	pdf.addChild(next);
	
	this.addChild(pdf);
	

	}
	
	public MTComponent getPDFComponent(){
		
		return pdf;
	}
}
