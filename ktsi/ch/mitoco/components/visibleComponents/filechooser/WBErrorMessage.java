package ch.mitoco.components.visibleComponents.filechooser;

import java.awt.event.ActionEvent;
import java.io.File;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.components.visibleComponents.widgets.buttons.MTSvgButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.Animation;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.MultiPurposeInterpolator;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.math.Vector3D;

/** 
 * WBErrorMessage - Class that forwards error messages to the gui of a MitocoScene
 * @author Chris Allen
 * @Precondition An error or exception detected.
 * @Postcondition The 'msg' string passed to the class is displayed in a
 *		MTTextArea box.
 */
public class WBErrorMessage extends MTTextArea {
	//private MitocoScene scene;

	public WBErrorMessage(AbstractMTApplication sc, String msg) {
		super(sc, FontManager.getInstance().createFont(sc, "arial", 15, new MTColor(0,0,0), new MTColor(0,0,0)));
		this.setFillColor(new MTColor(225,225,225));
		this.setStrokeColor(new MTColor (0,0,0));
		//this.scene = sc;
		
		//Set rotate and scale to false
		this.setGestureAllowance(RotateProcessor.class, false);
		this.setGestureAllowance(ScaleProcessor.class, false);
		
		setText("\n\n"+msg);
		this.setHeightLocal(100);
		this.translate(new Vector3D((sc.getWidth()/2)-(getWidthXY(TransformSpace.RELATIVE_TO_PARENT)/2),
				(sc.getHeight()/2)-(getHeightXY(TransformSpace.RELATIVE_TO_PARENT)/2),0));
		
		//Draw this component and its children above 
		//everything previously drawn and avoid z-fighting
		this.setDepthBufferDisabled(true);
		
		//Close button
		MTSvgButton closeButton = new MTSvgButton( this.getRenderer(), System.getProperty("user.dir")+File.separator + "ktsi"
				+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
				+"keybClose.svg");
		this.addChild(closeButton);
		//Transform
		closeButton.scale(0.35f, 0.35f, 1, new Vector3D(0,0,0));
		closeButton.setPositionRelativeToParent(new Vector3D(0,0,0));
		closeButton.translateGlobal(new Vector3D(getWidthXY(TransformSpace.RELATIVE_TO_PARENT)-14,
				-5,0));
		closeButton.setBoundsPickingBehaviour(AbstractShape.BOUNDS_ONLY_CHECK);
		//closeButton.addActionListener(new CloseActionListener(new MTComponent[]{this, closeButton}) );
		closeButton.addGestureListener(TapProcessor.class, new CloseActionListener(new MTComponent[]{this, closeButton}));
		
		closeButton.setName("closeButton");
		
		//Ok button
		MTImageButton okButton = new MTImageButton( sc, sc.loadImage(System.getProperty("user.dir")+File.separator + "ktsi"
				+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
				+"okButton.png"));
		this.addChild(okButton);
		okButton.setNoStroke(true);
		okButton.setPositionRelativeToParent(new Vector3D(0,0,0));
		okButton.translateGlobal(new Vector3D(getWidthXY(TransformSpace.RELATIVE_TO_PARENT)/2,
				getHeightXY(TransformSpace.RELATIVE_TO_PARENT)-45,0));
		//TODO:okButton.addActionListener(new CloseActionListener(new MTComponent[]{this, okButton}) );
		
		
		
		okButton.setName("okButton");
		
		MTTextArea ok = new MTTextArea(sc, 
				FontManager.getInstance().createFont(sc, "arial", 15, new MTColor(0,0,0), new MTColor(0,0,0)));
		ok.setFillColor(new MTColor(150,150,150));
		ok.setNoFill(true);
		ok.setNoStroke(true);
		ok.setText("Ok");
		ok.setPickable(false);
		okButton.addChild(ok);
		ok.setPositionRelativeToParent(new Vector3D(33,12,0));
	}
	
	private class CloseActionListener implements IGestureEventListener{
		/** The comps. */
		public MTComponent[] comps;
		
		/** The reference poly for resizing the button. */
		private MTPolygon referencePoly;
		
		/**
		 * Instantiates a new close action listener.
		 * 
		 * @param comps the comps
		 */
		public CloseActionListener(MTComponent[] comps) {
			super();
			this.comps = comps;
		}
		
		@Override
		public boolean processGestureEvent(MTGestureEvent ge) {
			TapEvent te = (TapEvent) ge;
			switch (te.getTapID()) {
			case TapEvent.TAPPED:
				//Get the first polygon type out of the array
				for (int i = 0; i < comps.length; i++) { //TODO this is stupid.. redo this whole thing
					MTComponent comp = comps[i];
					if (comp instanceof MTPolygon) {
						MTPolygon poly = (MTPolygon) comp;
						if (referencePoly == null){//nur 1. occur zuweisen
							referencePoly = poly;
						}
					}
				}
				float width = referencePoly.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);

				Animation closeAnim = new Animation("comp Fade", new MultiPurposeInterpolator(width, 1, 300, 0.5f, 0.8f, 1), referencePoly);
				closeAnim.addAnimationListener(new IAnimationListener(){
					public void processAnimationEvent(AnimationEvent ae) {
						switch (ae.getId()) {
						case AnimationEvent.ANIMATION_STARTED:
						case AnimationEvent.ANIMATION_UPDATED:
							float currentVal = ae.getAnimation().getValue();
							resize(referencePoly, comps[0], currentVal, currentVal);
							break;
						case AnimationEvent.ANIMATION_ENDED:
							comps[0].setVisible(false);
							for (int i = comps.length-1; i >0 ; i--) {
								MTComponent currentComp =  comps[i];
								//Call destroy which fires a destroy state change event
								currentComp.destroy();
								//System.out.println("destroyed: " + currentComp.getName());
							}
							destroy();
							//System.out.println("destroyed: " + getName());
							break;	
						default:
							destroy();
							break;
						}//switch
					}//processanimation
				});//new IAnimationListener
				closeAnim.start(); 
				break;
			default:
				break;
			}//switch aeID			
			
			return false;
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent arg0) {
			switch (arg0.getID()) {
			case TapEvent.BUTTON_CLICKED:
				//Get the first polygon type out of the array
				for (int i = 0; i < comps.length; i++) { //TODO this is stupid.. redo this whole thing
					MTComponent comp = comps[i];
					if (comp instanceof MTPolygon) {
						MTPolygon poly = (MTPolygon) comp;
						if (referencePoly == null){//nur 1. occur zuweisen
							referencePoly = poly;
						}
					}
				}
				float width = referencePoly.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);

				Animation closeAnim = new Animation("comp Fade", new MultiPurposeInterpolator(width, 1, 300, 0.5f, 0.8f, 1), referencePoly);
				closeAnim.addAnimationListener(new IAnimationListener(){
					public void processAnimationEvent(AnimationEvent ae) {
						switch (ae.getId()) {
						case AnimationEvent.ANIMATION_STARTED:
						case AnimationEvent.ANIMATION_UPDATED:
							float currentVal = ae.getAnimation().getValue();
							resize(referencePoly, comps[0], currentVal, currentVal);
							break;
						case AnimationEvent.ANIMATION_ENDED:
							comps[0].setVisible(false);
							for (int i = comps.length-1; i >0 ; i--) {
								MTComponent currentComp =  comps[i];
								//Call destroy which fires a destroy state change event
								currentComp.destroy();
								//System.out.println("destroyed: " + currentComp.getName());
							}
							destroy();
							//System.out.println("destroyed: " + getName());
							break;	
						default:
							destroy();
							break;
						}//switch
					}//processanimation
				});//new IAnimationListener
				closeAnim.start(); 
				break;
			default:
				break;
			}//switch aeID
		}
	}
	
	/**
	 * Resize.
	 * 
	 * @param referenceComp the reference comp
	 * @param compToResize the comp to resize
	 * @param width the width
	 * @param height the height
	 */
	protected void resize(MTPolygon referenceComp, MTComponent compToResize, float width, float height){ 
		Vector3D centerPoint = getRefCompCenterRelParent(referenceComp);
		compToResize.scale(1/referenceComp.getWidthXY(TransformSpace.RELATIVE_TO_PARENT), (float)1/referenceComp.getWidthXY(TransformSpace.RELATIVE_TO_PARENT), 1, centerPoint, TransformSpace.RELATIVE_TO_PARENT);
		compToResize.scale(width, width, 1, centerPoint, TransformSpace.RELATIVE_TO_PARENT);
	}
	
	/**
	 * Gets the ref comp center local.
	 * 
	 * @param shape the shape
	 * 
	 * @return the ref comp center local
	 */
	@SuppressWarnings("deprecation")
	protected Vector3D getRefCompCenterRelParent(AbstractShape shape){
		Vector3D centerPoint;
		if (shape.isBoundingShapeSet()){
			centerPoint = shape.getBoundingShape().getCenterPointLocal();
			centerPoint.transform(shape.getLocalMatrix()); //macht den punkt in self space
		}else{
			Vector3D localObjCenter = shape.getCenterPointGlobal();
			localObjCenter.transform(shape.getGlobalInverseMatrix()); //to localobj space
			localObjCenter.transform(shape.getLocalMatrix()); //to parent relative space
			centerPoint = localObjCenter;
		}
		return centerPoint;
	}
}
