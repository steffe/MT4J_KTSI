package org.mt4jx.components.visibleComponents.widgets.pdf.example;
import java.io.File;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.components.visibleComponents.widgets.pdf.MTPDF;

public class PDFExampleScene extends AbstractScene {
	private MTApplication app;
	
	public PDFExampleScene(final MTApplication mtApplication, String name) {
		super(mtApplication, name);
		this.app = mtApplication;
		
		final MTPDF pdf = new MTPDF(mtApplication, new File("d:/Test.pdf"));
		pdf.scaleGlobal(.5f, .5f, .5f, pdf.getCenterPointGlobal());
//		pdf.addGestureListener(DragProcessor.class, new IGestureEventListener() {
//			@Override
//			public boolean processGestureEvent(MTGestureEvent ge) {
//				switch (ge.getId()) {
//				case DragEvent.GESTURE_ENDED:
//					pdf.setPageNumber(pdf.getPageNumber()+1);
//					break;
//				default:
//					break;
//				}
//				return false;
//			}
//
//		});
		MTTextArea previous = new MTTextArea(app, FontManager.getInstance().getDefaultFont(app));
		previous.setText("Previous");
		previous.registerInputProcessor(new TapProcessor(app));
		previous.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if(te.isTapped()){
					pdf.setPageNumber(pdf.getPageNumber()-1);
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
					pdf.setPageNumber(pdf.getPageNumber()+1);
				}
				return false;
			}
		});
		next.translate(new Vector3D(200,0));
		pdf.addChild(next);
		this.getCanvas().addChild(pdf);
		
//		this.registerPreDrawAction(new IPreDrawAction() {
//			int cnt=0;
//			@Override
//			public void processAction() {
//				if(app.frameCount%60==0){
//					if(pdf.getNumberOfPages()>cnt){
//						pdf.setPageNumber(cnt++);
//					}else{
//						cnt = 0;
//						pdf.setPageNumber(cnt++);
//					}
//				}
//			}
//			@Override
//			public boolean isLoop() {
//				return true;
//			}
//		});
	}
}
