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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.bounds.BoundsZPlaneRectangle;
import org.mt4j.components.bounds.IBoundingShape;
import org.mt4j.components.interfaces.IMTComponent3D;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTSlider;
import org.mt4j.components.visibleComponents.widgets.buttons.MTSvgButton;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.lassoProcessor.ILassoable;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.Animation;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.MultiPurposeInterpolator;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;
import org.mt4j.util.opengl.GLTexture;

import codeanticode.gsvideo.GSMovie;

/** 
 * WBMovieClip - Class to create objects that play movies
 * @author Chris Allen -- ported and altered from MTMovieClip
 * @date April 28, 2010
 * @Precondition: A whiteboard application has been created and the user
 *		has chosen a movie file from the file chooser.
 * @Postcondition: A movie object has been added to the scene and is able
 *		to be played when tapped and held by the user.
 */

public class MTMovieClip extends MTRectangle 
//MTRoundRectangle
implements ILassoable {
	
	/** The app */
	private AbstractScene scene;
	private MTMovieClip thisMovie = this;
	
	/** The movie. */
	private GSMovie movie;
	
	/** The first time read. */
	private boolean firstTimeRead;
	
	/** Clip selected. */
	private boolean selected;
	
	/** Clip is playing. */
	private boolean playing;
	
	/** The play button. */
	//private MTSvgButton playButton;
	
	/** The close button */
	private MTSvgButton keybCloseSvg;

	/**
	 * Instantiates a new MT movie clip.
	 * 
	 * @param movieFile the movie file
	 * @param upperLeft the upper left
	 * @param pApplet the applet
	 */
	public MTMovieClip(String movieFile, Vertex upperLeft, AbstractScene scene) {
		this(movieFile, upperLeft, 30, scene);
		this.scene = scene;
	}
	
	/**
	 * Instantiates a new MT movie clip.
	 * 
	 * @param movieFile the movie file - located in the ./data directory
	 * @param upperLeft the upper left movie position
	 * @param ifps the ifps the frames per second
	 * @param pApplet the applet
	 */
	public MTMovieClip(String movieFile, Vertex upperLeft, int ifps,  AbstractScene scene) {
		super(upperLeft, 400, 300, scene.getMTApplication());
		this.scene = scene;
		
		//super(upperLeft.x,upperLeft.y,upperLeft.z, 105,127, 15,15, pApplet);
		try {
			final MTMovieClip thisObj = this;
			this.registerInputProcessor(new TapProcessor(scene.getMTApplication()));
			this.registerInputProcessor(new TapAndHoldProcessor(scene.getMTApplication(), 500));
			this.addGestureListener(TapAndHoldProcessor.class, new TapAndHoldVisualizer(scene.getMTApplication(),scene.getCanvas()));
			this.addGestureListener(TapAndHoldProcessor.class, new IGestureEventListener() {
				public boolean processGestureEvent(MTGestureEvent ge) {
					TapAndHoldEvent th = (TapAndHoldEvent)ge;
					IMTComponent3D target = th.getTargetComponent();
					switch (th.getId()) {
					case MTGestureEvent.GESTURE_DETECTED:
						//Make the object update settings
						//getScene().updateSettings(thisObj);
						break;
					case MTGestureEvent.GESTURE_UPDATED:
						break;
					case MTGestureEvent.GESTURE_ENDED:
						if (th.isHoldComplete()) {
							MTMovieClip movie = (MTMovieClip) target;
							if(!isPlaying()) { 
								jump(slider.getValue());
								movie.play(); 
								slider.setVisible(true);
								}
							else { 
								movie.pause(); 
								//slider.setVisible(false);
							}
						}
						break;
					}
					return false;
				}
			});
			movie = new GSMovie(scene.getMTApplication(), movieFile, ifps);
			this.setName(movieFile);
			
			/*
			playButton = new MTSvgButton(MT4jSettings.getInstance().getDefaultSVGPath() 
					+ "play.svg" , pApplet);
			playButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					//movie.play();
					switch (arg0.getID()) {
					case TapEvent.BUTTON_CLICKED:
						movie.play();
						movie.volume(100);
						slider.setVisible(true);
						break;
					default:
						break;
					}
				}
			});
			playButton.scale(0.5f, 0.5f, 1, new Vector3D(0,0,0));
			playButton.translate(upperLeft);
			this.addChild(playButton);
			*/
			/*
			MTSvgButton stopButton = new MTSvgButton(MT4jSettings.getInstance().getDefaultSVGPath() 
					+ "stop.svg" , pApplet);
			stopButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					switch (arg0.getID()) {
					case TapEvent.BUTTON_CLICKED:
						movie.stop();
						movie.goToBeginning();
						slider.setVisible(false);
						break;
					default:
						break;
					}
				}
			});
			//TODO müsste eigentlich grösste comp aus svg holen, dann center an die stelle positionieren
			this.addChild(stopButton);
			stopButton.scale(0.5f, 0.5f, 1, new Vector3D(0,0,0));
			stopButton.translate(new Vector3D(upperLeft.x + 30 , upperLeft.y, upperLeft.z));
			*/
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		firstTimeRead = true;
		
		
		if (MT4jSettings.getInstance().isOpenGlMode())
			this.setUseDirectGL(true);
		
		try{
			/**(Chris) -- We'll probably want to make our own versions of some
			 * of the MT* shapes to be able to set thumbnails on WBMovieClip's **/
			//PImage movieImg = (PImage)movie.get();
			this.setStrokeColor(new MTColor(0,0,0));
			//this.setTexture(null);
			//this.setTexture(movieImg);
			//this.setTextureEnabled(true);
			//this.setSizeXYRelativeToParent(movie.width, movie.height);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//Slider
		this.duration = 0.0f;
		sliderXOffset = 10;
		sliderHeight = 30;
		slider = new MTSlider(upperLeft.x + sliderXOffset, upperLeft.y + this.getHeightXY(TransformSpace.LOCAL) - 30, this.getWidthXY(TransformSpace.LOCAL) - sliderXOffset, sliderHeight, 0, 10, scene.getMTApplication());
		slider.getOuterShape().setFillColor(new MTColor(0, 0, 0, 50));
		slider.getOuterShape().setStrokeColor(new MTColor(0, 0, 0, 50));
		slider.getKnob().setFillColor(new MTColor(100, 100, 100, 50));
		slider.getOuterShape().setStrokeColor(new MTColor(100, 100, 100, 50));
		slider.getKnob().addGestureListener(DragProcessor.class, new IGestureEventListener() {
			//@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				DragEvent de = (DragEvent)ge;
				float currValue = slider.getValue();
				switch (de.getId()) {
				case MTGestureEvent.GESTURE_DETECTED:
					dragging = true;
					break;
				case MTGestureEvent.GESTURE_UPDATED:
					break;
				case MTGestureEvent.GESTURE_ENDED:
					if (m.isPlaying()){
						//float currValue = slider.getValue();
						jump(currValue);
					}
					dragging = false;
					break;
				default:
					break;
				}
			return true;
			}
		});
		this.addChild(slider);
		slider.setVisible(false);
		dragging = false;
	}
	
	
	/* (non-Javadoc)
	 * @see org.mt4j.components.visibleComponents.shapes.MTRoundRectangle#computeDefaultBounds()
	 */
	@Override
	protected IBoundingShape computeDefaultBounds() {
		return new BoundsZPlaneRectangle(this);
	}
	
	/** The slider. */
	private MTSlider slider;
	
	/** The dragging. */
	private boolean dragging;
	
	/** Show thumbnail. */
	private boolean thumbnail;
	
	/** The slider x offset. */
	private int sliderXOffset;
	
	/** The slider height. */
	private int sliderHeight;
	
	/** The m. */
	private GSMovie m;
	
	/**
	 * Movie event.
	 * 
	 * @param myMovie the my movie
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	public void movieEvent(GSMovie myMovie) throws InterruptedException {
		m = myMovie;
		
		if (!dragging){
			slider.setValue(myMovie.time()); //ONLY DO THIS WHEN NOT DRAGGING THE SLIDER
		}

		if (firstTimeRead && myMovie.available() && !thumbnail) {
			myMovie.read();
			System.out.println("Movie img format: " + m.format);
			
			//FIXME TEST - dont do every frame! Duration is only valid if playing..
			slider.setValueRange(0, myMovie.duration());
			
			//this.setSizeLocal(400, 300);
//			/*
			slider.setSizeXYRelativeToParent(400 - 2*sliderXOffset, sliderHeight);
			Vector3D movieClipCenterLocal = this.getCenterPointLocal();
			slider.setPositionRelativeToParent(new Vector3D(movieClipCenterLocal.x, movieClipCenterLocal.y + this.getHeightXY(TransformSpace.LOCAL)*0.5f - slider.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)*0.5f - 5,0 ));
//			*/
			
			//this.setUseDirectGL(false);
			this.setTexture(null); //TO force to rescale of new texture coordianates to RECTANGLE (0..width)
			this.setTexture(m);
			this.setTextureEnabled(true);
			//this.setUseDirectGL(true);
			
			if(firstTimeRead) {
				//keybCloseSvg.setPositionRelativeToParent(new Vector3D(this.getWidthXY(TransformSpace.RELATIVE_TO_PARENT), 36,0)); 
				firstTimeRead = false;
			}
		}
	}
	
	
	/* (non-Javadoc)
	 * @see com.jMT.components.MTBaseComponent#updateComponent(long)
	 */
	@Override
	public void updateComponent(long timeDelta){
		super.updateComponent(timeDelta);
		if (   m != null 
			&& m.isPlaying()
			&& m.available() //if unread frame available
			){
			if (this.getTexture() instanceof GLTexture){
				if (this.isUseDirectGL() && MT4jSettings.getInstance().isOpenGlMode()){
					//Directly put the new frame buffer into the texture only if in openGL mode 
					//without filling the PImage array of this objects texture and also not of the GSMovie PImage =>performance
					//((GLTexture)this.getTexture()).putBuffer(m.getMoviePixelsBuffer(),  GLConstants.TEX4, GLConstants.TEX_UBYTE);
					((GLTexture)this.getTexture()).updateGLTexture(m.getMoviePixelsBuffer());
				}else{
					//Fill the PImage with the new movieframe
					//dont fill the openGL texture
					m.read();
					//((GLTexture)this.getTexture()).putImageOnly(m);
					((GLTexture)this.getTexture()).loadPImageTexture(m);

				}
			}else{
				//Usually all textures should be GLTextures instances, but just to be sure..
				m.read();
				this.setTexture(m); //SLOW!
			}
		}
//		*/
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
					+ "keybClose.svg");
			//Transform
			keybCloseSvg.scale(0.25f, 0.25f, 1, new Vector3D(0,0,0));
			keybCloseSvg.translate(new Vector3D(this.getWidthXY(TransformSpace.RELATIVE_TO_PARENT), 23,0));
			keybCloseSvg.setBoundsPickingBehaviour(AbstractShape.BOUNDS_ONLY_CHECK);
			//TODO: keybCloseSvg.addActionListener(new CloseActionListener(new MTComponent[]{this, keybCloseSvg}) );
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
	
	//FIXME TEST
	//Duration
	float duration;
	
	//Gets the duration
	public float getDuration(){//duration only valid if video is playing
		if (movie.duration() == 0.0){
			return duration;
		}else{
			duration = movie.duration();
			return duration;
		}
	}
	

	//Jump to where
	public void jump(float where) {
		movie.jump(where);
	}


	//Loop the movie
	public void loop() {
		movie.loop();
	}


	//No looping
	public void noLoop() {
		movie.noLoop();
	}


	//Pause
	public void pause() {
		movie.pause();
		playing = false;
	}

	//Play
	public void play() {
		movie.play();
		playing = true;
	}

	//Stop
	public void stop() {
		movie.stop();
		playing = false;
	}

	//Time -- the time the movie plays in float
	public float getTime() {
		return movie.time();
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
	
	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setVolume(double volume) {
		movie.volume(volume);
	}
	
	public AbstractScene getScene() {
		return this.scene;
	}

	/**
	 * The Class CloseActionListener. 
	 * 
	 * @author Cruff
	 */
	private class CloseActionListener implements ActionListener{
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
								}
								scene.getCanvas().removeChild(thisMovie);
								//System.out.println("destroyed: " + getName());
								break;	
							default:
								scene.getCanvas().removeChild(thisMovie);
								break;
							}//switch
						}//processanimation
					});//new IAnimationListener
					movie.stop();
					closeAnim.start(); 
					break;
				default:
					break;
				}//switch aeID
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
}
