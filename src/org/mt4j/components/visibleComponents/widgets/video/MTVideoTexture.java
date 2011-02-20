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
package org.mt4j.components.visibleComponents.widgets.video;

import org.mt4j.components.bounds.BoundsZPlaneRectangle;
import org.mt4j.components.bounds.IBoundingShape;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.math.Vertex;
import org.mt4j.util.opengl.GLTexture;

import processing.core.PApplet;
import codeanticode.gsvideo.GSMovie;

/**
 * This class allows to run a video texture on a plain MTRectangle without any
 * UI-Controls.
 * <br>NOTE: Needs to have the GStreamer framework to be installed on the system.
 * 
 * @author Christopher Ruff
 */
public class MTVideoTexture extends 
MTRectangle 
//MTRoundRectangle
{

	/** The movie. */
	protected GSMovie movie;
	
	/** The m. */
	private GSMovie m;
	
	/** The first time read. */
	private boolean firstTimeRead;

	/** The duration. */
	float duration;
	
	
	/**
	 * Instantiates a MTVideoTexture
	 * 
	 * @param movieFile the movie file
	 * @param upperLeft the upper left
	 * @param pApplet the applet
	 */
	public MTVideoTexture(String movieFile, Vertex upperLeft, PApplet pApplet) {
		this(movieFile, upperLeft, 30, pApplet);
	}
	
	/**
	 * Instantiates a new MTVideoTexture
	 * 
	 * @param movieFile the movie file - located in the ./data directory
	 * @param upperLeft the upper left movie position
	 * @param ifps the ifps the frames per second
	 * @param pApplet the applet
	 */
	public MTVideoTexture(String movieFile, Vertex upperLeft, int ifps,  PApplet pApplet) {
		//		super(upperLeft, 150, 100, pApplet);
		super(upperLeft.x,upperLeft.y,upperLeft.z, 105,127, pApplet);
		
		this.duration = 0.0f;
		this.setName("movieclip: " + movieFile);
		firstTimeRead = true;
		
		try {
			movie = new GSMovie(pApplet, movieFile, ifps);
			movie.setEventHandlerObject(this);

			//			if (pApplet instanceof MTApplication) {
			//				MTApplication app = (MTApplication) pApplet;
			//				movie.play();
			//				app.invokeLater(new Runnable() {
			//					public void run() {
			//						movie.pause();
			//					}
			//				});
			//			}

			if (MT4jSettings.getInstance().isOpenGlMode()){
				this.setUseDirectGL(true);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	protected IBoundingShape computeDefaultBounds() {
		return new BoundsZPlaneRectangle(this);
	}
	
	/**
	 * Movie event.
	 * @param myMovie the my movie
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	public void movieEvent(GSMovie myMovie) throws InterruptedException {
		m = myMovie;
//		if (!dragging){
//			slider.setValue(myMovie.time()); //ONLY DO THIS WHEN NOT DRAGGING THE SLIDER
//		}
	}
	
	public GSMovie getMovie(){
		return this.movie;
	}
	
	protected void onFirstFrame(){
		if (m.available()){
			m.read();
			System.out.println("Movie img format: " + m.format);
			
			this.setSizeLocal(m.width, m.height);

//			this.setTexture(null); //TO force to rescale of new texture coordianates to RECTANGLE (0..width)
			this.setTexture(m); //FIXME this will call glGenTextures from NOT OPENGL THREAD! FIX THIS!
			this.setTextureEnabled(true);
			
			//FIXME try to use PBO for pixel->texture transfer -> even slower??
			if (this.getTexture() instanceof GLTexture){
//				GLTexture glTex = (GLTexture)this.getTexture();
//				glTex.enableFastTextureLoad();
			}
		}
	}
	
	protected void onNewFrame(){
		
	}

	
	@Override
	public void updateComponent(long timeDelta){
		super.updateComponent(timeDelta);

		if (m != null){
			if (firstTimeRead && m.available()){
				this.onFirstFrame();
				firstTimeRead = false;
			}
			else{
				if (m != null 
					&& m.isPlaying()
					&& m.available() //if unread frame available
				){
					if (this.getTexture() instanceof GLTexture){
						if (this.isUseDirectGL() && MT4jSettings.getInstance().isOpenGlMode()){
							//Directly put the new frame buffer into the texture only if in openGL mode 
							//without filling the PImage array of this objects texture and also not of the GSMovie PImage =>performance
//							((MTTexture)this.getTexture()).putBuffer(m.getMoviePixelsBuffer(),  GLConstants.TEX4, GLConstants.TEX_UBYTE);
							((GLTexture)this.getTexture()).updateGLTexture(m.getMoviePixelsBuffer());
						}else{
							//Fill the PImage with the new movieframe
							//dont fill the openGL texture
							m.read();
//							((MTTexture)this.getTexture()).putImageOnly(m);	
							((GLTexture)this.getTexture()).loadPImageTexture(m);
						}
					}else{
						//Usually all textures should be GLTextures instances, but just to be sure..
						m.read();
						this.setTexture(m); //SLOW!!!
					}
					
					this.onNewFrame();
				}
			}
		}
	}
	
	
	@Override
	protected void destroyComponent() {
		super.destroyComponent();
		
		if (m != null){
			m.dispose();
		}
	}
	
	
	/**
	 * Gets the duration.
	 * 
	 * @return the duration
	 */
	public float getDuration(){//duration only valid if video is playing
		GSMovie movie = getMovie();
		if (movie.duration() == 0.0){
			return duration;
		}else{
			duration = movie.duration();
			return duration;
		}
	}
	

	/**
	 * Jump.
	 * 
	 * @param where the where
	 */
	public void jump(float where) {
		GSMovie movie = getMovie();
		movie.jump(where);
	}
	
	
	/**
	 * Play.
	 */
	public void play() {
		GSMovie movie = getMovie();
		movie.play();
	}


	/**
	 * Stop.
	 */
	public void stop() {
		GSMovie movie = getMovie();
		movie.stop();
	}

	/**
	 * Loop the movie.
	 */
	public void loop() {
		GSMovie movie = getMovie();
		movie.loop();
	}


	/**
	 * No looping.
	 */
	public void noLoop() {
		GSMovie movie = getMovie();
		movie.noLoop();
	}


	/**
	 * Pause.
	 */
	public void pause() {
		GSMovie movie = getMovie();
		movie.pause();
	}


	/**
	 * Time.
	 * 
	 * @return the time the movie plays in float
	 */
	public float getTime() {
		GSMovie movie = getMovie();
		return movie.time();
	}
	
	/**
	 * Go to beginning.
	 */
	public void goToBeginning() {
		GSMovie movie = getMovie();
		movie.goToBeginning();
	}
	
	/**
	 * Change the volume. Values are from 0 to 1.
	 * @param v the new volume
	 */
	public void setVolume(double v){
		getMovie().volume(v);
	}

}
