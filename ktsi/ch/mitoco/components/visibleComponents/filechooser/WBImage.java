/***********************************************************************
 * mt4j Copyright (c) 2008 - 2009, C.Ruff, Fraunhofer-Gesellschaft All rights reserved.
 *  
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 ***********************************************************************/
package ch.mitoco.components.visibleComponents.filechooser;

import java.io.File;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.components.visibleComponents.widgets.buttons.MTSvgButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.lassoProcessor.ILassoable;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.Animation;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.MultiPurposeInterpolator;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;
import ch.mitoco.main.MitocoScene;

/** 
 * WBImage - Class to create objects that display images
 * @author Chris Allen - ported and altered from MTImage(Christopher Ruff)
 * @Precondition A whiteboard application has been created and the user
 *		has chosen an image file of a supported type from the file 
 *		chooser.
 * @Postcondition An image object has been added to the scene and is able
 *		to be moved and resized by the user.
 */

public class WBImage extends MTRectangle implements ILassoable{
	/** The scene */
	private MitocoScene scene;
	/** Boolean for IdragClusterable */
	private boolean selected;
	/** The image */
	private MTRectangle image;
	private WBImage thisImage = this;
	/** Close button */
	private MTSvgButton keybCloseSvg;
	
	//TODO Make close button stay fixed size relative to the scene - independent
	//of the image size
	public WBImage(MTImage image, MitocoScene scene) {
		this(image.getTexture(), scene);
	}
	
	public WBImage(PImage texture, MitocoScene scene) {
		super(0, 0, texture.width, texture.height, scene.getMTApplication());
		this.scene = scene;
		image = new MTRectangle(texture, scene.getMTApplication());
		//image.setStrokeColor(new MTColor(255,255,255,255));
		image.setNoStroke(true);
		image.setPickable(false);
		//Set the image to use direct gl - thread safe
		if (MT4jSettings.getInstance().isOpenGlMode()){
			if (scene.getMTApplication() instanceof MTApplication) {
				MTApplication app = (MTApplication) scene.getMTApplication();
				app.invokeLater(new Runnable() {
					public void run() {
						image.setUseDirectGL(true);
					}
				});
			}
		}
		this.addChild(image);
		
		//Make the object update settings
		final WBImage thisObj = this;
		this.registerInputProcessor(new TapProcessor(scene.getMTApplication()));
		this.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				switch (te.getId()) {
				case MTGestureEvent.GESTURE_DETECTED:
					//TODO: getScene().updateSettings(thisObj);
					break;
				case MTGestureEvent.GESTURE_UPDATED:
					break;
				case MTGestureEvent.GESTURE_ENDED:
					break;
				}
				return false;
			}
		});
		
		//Draw this component and its children above 
		//everything previously drawn and avoid z-fighting
		//this.setDepthBufferDisabled(true);
	}
	
	public MTRectangle getImage(){
		return this.image;
	}
	
	public MitocoScene getScene() {
		return this.scene;
	}
	
	public void setImage(PImage texture){
		this.removeChild(image);
		this.setSizeLocal(texture.width, texture.height);
		this.setHeightXYGlobal(300);
		//Matrix remember = this.getGlobalMatrix();
		image = new MTRectangle(texture, scene.getMTApplication());
		image.setStrokeColor(new MTColor(0,0,0));
		image.setNoStroke(true);
		image.setPickable(false);
		//Set the image to use direct gl - thread safe
		if (MT4jSettings.getInstance().isOpenGlMode()){
			if (scene.getMTApplication() instanceof MTApplication) {
				MTApplication App = (MTApplication) (scene.getMTApplication());
				App.invokeLater(new Runnable() {
					public void run() {
						image.setUseDirectGL(true);
					}
				});
			}
		}
		this.addChild(image);
		if(keybCloseSvg != null) {
			keybCloseSvg.setPositionRelativeToParent(new Vector3D(0,0));
			keybCloseSvg.translateGlobal(new Vector3D(this.getWidthXY(TransformSpace.GLOBAL)-keybCloseSvg.getWidthXYGlobal(), 
					keybCloseSvg.getHeightXYGlobal(),0));
			keybCloseSvg.sendToFront();
			//keybCloseSvg.translate(new Vector3D(this.getWidthXY(TransformSpace.GLOBAL) - 45, 2,0));

		}

	}

	/* (non-Javadoc)
	 * @see com.jMT.input.inputAnalyzers.clusterInputAnalyzer.IdragClusterable#isSelected()
	 */
	public boolean isSelected() {
		return selected;
	}

	/* (non-Javadoc)
	 * @see com.jMT.input.inputAnalyzers.clusterInputAnalyzer.IdragClusterable#setSelected(boolean)
	 */
	public void setSelected(boolean selected) {
		
		this.selected = selected;
	}

	
	/**
	 * Sets the display close button.
	 * 
	 * @param dispClose the new display close button
	 */
	public void setDisplayCloseButton(boolean dispClose){
		if (dispClose){
			keybCloseSvg = new MTSvgButton(this.getRenderer(), System.getProperty("user.dir")+File.separator + "ktsi"
					+File.separator + "ch"+  File.separator+"mitoco"+ File.separator+"data"+ File.separator+"filechooser"+ File.separator
					+"keybClose.svg");
			//Transform
			keybCloseSvg.scale(0.5f, 0.5f, 1, new Vector3D(0,0,0));
			//keybCloseSvg.scale(0.5f, 0.5f, 1, new Vector3D(0,0,0));
			keybCloseSvg.translate(new Vector3D(this.getWidthXY(TransformSpace.RELATIVE_TO_PARENT) - 45, 2,0));
			keybCloseSvg.setBoundsPickingBehaviour(AbstractShape.BOUNDS_ONLY_CHECK);
			//keybCloseSvg.addActionListener(new CloseActionListener(new MTComponent[]{this, keybCloseSvg}) );
			//keybCloseSvg.addGestureListener(TapProcessor.class, new CloseActionListener(new MTComponent[]{this, keybCloseSvg}));
			//			pic.addChild(keybCloseSvg);
			keybCloseSvg.addGestureListener(TapProcessor.class, new CloseActionListener(new MTComponent[]{this, keybCloseSvg}));
			keybCloseSvg.setName("closeButton");
			this.addChild(keybCloseSvg);
		}else{
			//Remove svg button and destroy child display lists
			MTComponent[] childs = this.getChildren();
			for (int i = 0; i < childs.length; i++) {
				MTComponent component = childs[i];
				if (component.getName().equals("closeButton")) {
					MTSvgButton svgButton = (MTSvgButton) component;
					svgButton.destroy();
				}
			}
		}
	}

	/**
	 * The Class CloseActionListener. 
	 * 
	 * @author Cruff
	 */
	public class CloseActionListener implements IGestureEventListener{
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

			/* (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
	
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
									currentComp.destroy();
								}
								//TODO: scene.removeFromMediaLayer(thisImage);
								break;	
							default:
								//TODO: scene.removeFromMediaLayer(thisImage);
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
	}	
}
