package org.mt4jx.input.gestureAction.dnd;

import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.interfaces.IMTComponent3D;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimation;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.ani.AniAnimation;

import processing.core.PApplet;

/**
 * @author laufs
 * The DragVisualisationAction visualized dragging "undraggable" elements e.g. from a MTList
 */
public class DragVisualisationAction extends DefaultDragAction {
	private MTComponent dndVisualisationLayer;
	private MTRoundRectangle dragVis;
	private PApplet pApplet;
	
	public DragVisualisationAction(MTComponent dndVisualisationLayer, PApplet pApplet){
		this.dndVisualisationLayer = dndVisualisationLayer;
		this.pApplet = pApplet;
	}
	@Override
	public boolean processGestureEvent(final MTGestureEvent g) {
		IMTComponent3D target;
		if(g instanceof DragEvent){
			DragEvent de = (DragEvent)g;
			target = de.getCurrentTarget();
			switch (g.getId()) {
			case DragEvent.GESTURE_DETECTED:
				if(target instanceof AbstractShape){
					AbstractShape as = (AbstractShape)target;
					float w = as.getWidthXY(TransformSpace.LOCAL);
					float h = as.getHeightXY(TransformSpace.LOCAL);
					this.dragVis = new MTRoundRectangle(0,0,0,w,h,10,10,pApplet);
//					this.addChangeColorAnimation(dragVis);
					dragVis.setFillColor(new MTColor(0,0,0,32));
					dragVis.setStrokeColor(new MTColor(0,0,0,32));
					dragVis.setPositionGlobal(as.getCenterPointGlobal());
					dragVis.setPickable(false);
					dragVis.removeAllGestureEventListeners();
					this.dndVisualisationLayer.addChild(dragVis);
				}
				
				break;
			case DragEvent.GESTURE_UPDATED:
				if(target instanceof AbstractShape){
					AbstractShape as = (AbstractShape)target;
					dragVis.setPositionGlobal(de.getDragCursor().getCurrentEvent().getPosition());
				}
				break;
			case DragEvent.GESTURE_ENDED:
				if(target instanceof AbstractShape){
					AbstractShape as = (AbstractShape)target;
					dragVis.setVisible(false);
					this.dndVisualisationLayer.removeChild(dragVis);
					dragVis.destroy();
				}
				break;

			default:
				break;
			}
		}
		return false;
	}
	
//	public void addChangeColorAnimation(final AbstractShape as){
//			IAnimation colorAnim = new AniAnimation(64, 168, 1000,-1, AniAnimation.LINEAR, as);
//			colorAnim.addAnimationListener(new IAnimationListener(){
//				public void processAnimationEvent(AnimationEvent ae) {
//					switch (ae.getId()) {
//					case AnimationEvent.ANIMATION_STARTED:
//					case AnimationEvent.ANIMATION_UPDATED:
//						float currentVal = ae.getCurrentValue();
//						MTColor color = as.getFillColor();
//						color.setAlpha(ae.getCurrentValue());
//						as.setFillColor(color);
//						as.setStrokeColor(color);
//						break;
//					case AnimationEvent.ANIMATION_ENDED:
//						break;	
//					default:
//						break;
//					}//switch
//				}//processanimation
//			});
//			colorAnim.start();
//		}
}
