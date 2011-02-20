/***********************************************************************
 * mt4j Copyright (c) 2008 - 2009 C.Ruff, Fraunhofer-Gesellschaft All rights reserved.
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
package org.mt4j.input.inputProcessors.componentProcessors.tapProcessor;

import java.util.ArrayList;

import org.mt4j.components.MTCanvas;
import org.mt4j.components.interfaces.IMTComponent3D;
import org.mt4j.input.inputData.InputCursor;
import org.mt4j.input.inputData.MTFingerInputEvt;
import org.mt4j.input.inputProcessors.IInputProcessor;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.AbstractComponentProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.AbstractCursorProcessor;
import org.mt4j.util.math.Tools3D;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

/**
 * The Class TapProcessor. Tap multitouch gesture. Triggered on a component
 * that is tapped with a finger.
 * Fires TapEvent gesture events.
 * @author Christopher Ruff
 */
public class TapProcessor extends AbstractCursorProcessor {
	
	/** The applet. */
	private PApplet applet;
	
	/** The max finger up dist. */
	private float maxFingerUpDist;
	
	/** The un used cursors. */
	private ArrayList<InputCursor> unUsedCursors;
	
	/** The locked cursors. */
	private ArrayList<InputCursor> lockedCursors;
	
	/** The button down screen pos. */
	private Vector3D buttonDownScreenPos;
	
	/** The enable double tap. */
	private boolean enableDoubleTap;
	
	/** The time last tap. */
	private long timeLastTap;
	
	/** The double tap time. */
	private int doubleTapTime = 300;
	
	/**
	 * Instantiates a new tap processor.
	 * 
	 * @param pa the pa
	 */
	public TapProcessor(PApplet pa) {
		this(pa, 18.0f);
	}
	
	/**
	 * Instantiates a new tap processor.
	 * 
	 * @param pa the pa
	 * @param maxFingerUpDistance the max finger up distance
	 */
	public TapProcessor(PApplet pa, float maxFingerUpDistance) {
		this(pa, maxFingerUpDistance, false, 300); 
	}
	
	/**
	 * Instantiates a new tap processor.
	 * 
	 * @param pa the pa
	 * @param maxFingerUpDistance the max finger up distance
	 * @param enableDoubleTap the enable double tap
	 */
	public TapProcessor(PApplet pa, float maxFingerUpDistance, boolean enableDoubleTap){
		this(pa, maxFingerUpDistance, enableDoubleTap, 300);
	}
	
	/**
	 * Instantiates a new tap processor.
	 * 
	 * @param pa the pa
	 * @param maxFingerUpDistance the max finger up distance
	 * @param enableDoubleTap the enable double tap
	 * @param doubleTapTime the double tap time
	 */
	public TapProcessor(PApplet pa, float maxFingerUpDistance, boolean enableDoubleTap, int doubleTapTime){
		super();
		this.applet = pa;
		this.maxFingerUpDist = maxFingerUpDistance;
		this.unUsedCursors 	= new ArrayList<InputCursor>();
		this.lockedCursors 	= new ArrayList<InputCursor>();
		this.setLockPriority(1);
		this.setDebug(false);
		
		
		this.enableDoubleTap = enableDoubleTap;
		this.doubleTapTime = doubleTapTime;
		this.timeLastTap = -1;
		//System.out.println("Double click default time:" + Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval"));
	}



	/* (non-Javadoc)
	 * @see org.mt4j.input.inputProcessors.componentProcessors.AbstractCursorProcessor#cursorStarted(org.mt4j.input.inputData.InputCursor, org.mt4j.input.inputData.MTFingerInputEvt)
	 */
	@Override
	public void cursorStarted(InputCursor m, MTFingerInputEvt positionEvent) {
		IMTComponent3D comp = positionEvent.getTargetComponent();
		if (lockedCursors.size() >= 1){ //We assume that the gesture is already in progress and add this new cursor to the unUsedList 
			unUsedCursors.add(m); 
		}else{
			if (unUsedCursors.size() == 0){ //Only start gesture if no other finger on the component yet
				if (this.canLock(m)){//See if we can obtain a lock on this cursor (depends on the priority)
					this.getLock(m);
					logger.debug(this.getName() + " successfully locked cursor (id:" + m.getId() + ")");
					lockedCursors.add(m);
					buttonDownScreenPos = new Vector3D(m.getCurrentEvent().getPosX(), m.getCurrentEvent().getPosY(), 0);
					this.fireGestureEvent(new TapEvent(this, MTGestureEvent.GESTURE_DETECTED, comp, m, buttonDownScreenPos, TapEvent.BUTTON_DOWN));
				}else{
					unUsedCursors.add(m);
				}
			}else{
				unUsedCursors.add(m);
			}
		}
	}


	/* (non-Javadoc)
	 * @see org.mt4j.input.inputProcessors.componentProcessors.AbstractCursorProcessor#cursorUpdated(org.mt4j.input.inputData.InputCursor, org.mt4j.input.inputData.MTFingerInputEvt)
	 */
	@Override
	public void cursorUpdated(InputCursor m, MTFingerInputEvt positionEvent) {
		
	}
	
	
	/* (non-Javadoc)
	 * @see org.mt4j.input.inputProcessors.componentProcessors.AbstractCursorProcessor#cursorEnded(org.mt4j.input.inputData.InputCursor, org.mt4j.input.inputData.MTFingerInputEvt)
	 */
	@Override
	public void cursorEnded(InputCursor m, MTFingerInputEvt positionEvent) {
		IMTComponent3D comp = positionEvent.getTargetComponent();
		logger.debug(this.getName() + " INPUT_ENDED RECIEVED - CURSOR: " + m.getId());

		if (lockedCursors.contains(m)){ //cursor was a actual gesture cursor
			lockedCursors.remove(m);
			if (unUsedCursors.size() > 0){ //check if there are other cursors on the component, we could use 
				InputCursor otherCursor = unUsedCursors.get(0); //TODO cycle through all available unUsedCursors and try to lock one, maybe the first one is lock but another isnt!
				if (this.canLock(otherCursor)){ //Check if we have the priority to use this cursor
					this.getLock(otherCursor);
					unUsedCursors.remove(otherCursor);
					lockedCursors.add(otherCursor);
					//TODO fire started? maybe not.. do we have to?
				}else{
					this.endGesture(m, comp);
				}
			}else{
				this.endGesture(m, comp);
			}
			this.unLock(m); //FIXME TEST
		}else{ //cursor was not used here
			if (unUsedCursors.contains(m)){
				unUsedCursors.remove(m);
			}
		}
	}

	

	
	/**
	 * End gesture.
	 * 
	 * @param m the m
	 * @param comp the comp
	 */
	private void endGesture(InputCursor m, IMTComponent3D comp){
		//Default where for the event if no intersections are found
		Vector3D buttonUpScreenPos = new Vector3D(m.getCurrentEvent().getPosX(), m.getCurrentEvent().getPosY(), 0);
		
		//If component is detached from tree, destroyed etc
		if (comp.getViewingCamera() == null){
			this.fireGestureEvent(new TapEvent(this, MTGestureEvent.GESTURE_ENDED, comp, m, buttonUpScreenPos, TapEvent.BUTTON_CLICKED));			
			return;
		}
		
		Vector3D intersection = comp.getIntersectionGlobal(Tools3D.getCameraPickRay(applet, comp, m.getCurrentEvent().getPosX(), m.getCurrentEvent().getPosY()));
		//logger.debug("Distance between buttondownScreenPos: " + buttonDownScreenPos + " and upScrPos: " + buttonUpScreenPos +  " is: " + Vector3D.distance(buttonDownScreenPos, buttonUpScreenPos));
		//Check if at finger_Up the cursor is still on that object or if the cursor has moved too much 
		if ((intersection != null || comp instanceof MTCanvas)
				&& 
			Vector3D.distance(buttonDownScreenPos, buttonUpScreenPos) <= this.maxFingerUpDist
		){
			//We have a valid TAP!
			if (this.isEnableDoubleTap()){
				//Check if it was a double tap by comparing the now time to the time of the last valid tap
				long now = m.getCurrentEvent().getWhen();
				if (this.timeLastTap != -1 && (now - this.timeLastTap) <= this.getDoubleTapTime()){
					//Its a Double tap
					this.timeLastTap = -1;
					this.fireGestureEvent(new TapEvent(this, MTGestureEvent.GESTURE_ENDED, comp, m, buttonUpScreenPos, TapEvent.BUTTON_DOUBLE_CLICKED));
				}else{
					this.timeLastTap = now;
					this.fireGestureEvent(new TapEvent(this, MTGestureEvent.GESTURE_ENDED, comp, m, buttonUpScreenPos, TapEvent.BUTTON_CLICKED));
				}
			}else{
				this.fireGestureEvent(new TapEvent(this, MTGestureEvent.GESTURE_ENDED, comp, m, buttonUpScreenPos, TapEvent.BUTTON_CLICKED));
			}
		}else{
			//logger.debug("FINGER UP NOT ON SAME OBJ!");
			this.fireGestureEvent(new TapEvent(this, MTGestureEvent.GESTURE_ENDED, comp, m, buttonUpScreenPos, TapEvent.BUTTON_UP));
		}
	}


	/* (non-Javadoc)
	 * @see org.mt4j.input.inputProcessors.componentProcessors.AbstractCursorProcessor#cursorLocked(org.mt4j.input.inputData.InputCursor, org.mt4j.input.inputProcessors.IInputProcessor)
	 */
	@Override
	public void cursorLocked(InputCursor m, IInputProcessor lockingProcessor) {
		if (lockingProcessor instanceof AbstractComponentProcessor){
			logger.debug(this.getName() + " Recieved CURSOR LOCKED by (" + ((AbstractComponentProcessor)lockingProcessor).getName()  + ") - cursor ID: " + m.getId());
		}else{
			logger.debug(this.getName() + " Recieved CURSOR LOCKED by higher priority signal - cursor ID: " + m.getId());
		}

		if (lockedCursors.contains(m)){ //cursor was in use here
			lockedCursors.remove(m);
			unUsedCursors.add(m);
			logger.debug(this.getName() + " cursor:" + m.getId() + " CURSOR LOCKED. Was an active cursor in this gesture!");
			
			//FIXME TEST -> dont allow resuming of gesture if the cursor was locked by a higher priority gesture
			//-> fire gesture ended
			this.fireGestureEvent(new TapEvent(this, MTGestureEvent.GESTURE_ENDED, m.getCurrentEvent().getTargetComponent(), m, new Vector3D(m.getCurrentEvent().getPosX(), m.getCurrentEvent().getPosY()), TapEvent.BUTTON_UP));	
			
		}else{ //Else is just for debug
			if (unUsedCursors.contains(m)){
				logger.debug(this.getName() + " CURSOR LOCKED. But it was NOT an active cursor in this gesture!");
			}
		}
	}



	/* (non-Javadoc)
	 * @see org.mt4j.input.inputProcessors.componentProcessors.AbstractCursorProcessor#cursorUnlocked(org.mt4j.input.inputData.InputCursor)
	 */
	@Override
	public void cursorUnlocked(InputCursor m) {
		logger.debug(this.getName() + " Recieved UNLOCKED signal for cursor ID: " + m.getId());

		if (lockedCursors.size() >= 1){ //we dont need the unlocked cursor, gesture still in progress
			logger.debug(this.getName() + " still in progress - we dont need the unlocked cursor" );
			return;
		}

		/*
		//FIXME TEST -> dont allow resuming of tap if the tap cursor was locked by a higher priority gesture
		//-> dont resume gesture here
		if (unUsedCursors.contains(m)){
			if (this.canLock(m)){
				this.getLock(m);
				unUsedCursors.remove(m);
				lockedCursors.add(m);
				//TODO fire started? maybe not.. do we have to?
				logger.debug(this.getName() + " can resume its gesture with cursor: " + m.getId());
			}
		}
		*/
	}

	
	
	/**
	 * Gets the max finger up dist.
	 * 
	 * @return the max finger up dist
	 */
	public float getMaxFingerUpDist() {
		return maxFingerUpDist;
	}


	/**
	 * Sets the maximum allowed distance of the position
	 * of the finger_down event and the finger_up event
	 * that fires a click event (in screen pixels).
	 * <br>This ensures that a click event is only raised
	 * if the finger didnt move too far during the click.
	 * 
	 * @param maxFingerUpDist the max finger up dist
	 */
	public void setMaxFingerUpDist(float maxFingerUpDist) {
		this.maxFingerUpDist = maxFingerUpDist;
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.mt4j.input.inputProcessors.componentProcessors.AbstractComponentProcessor#getName()
	 */
	@Override
	public String getName() {
		return "Tap Processor";
	}

	/**
	 * Checks if is enable double tap.
	 * 
	 * @return true, if is enable double tap
	 */
	public boolean isEnableDoubleTap() {
		return this.enableDoubleTap;
	}

	/**
	 * Sets the enable double tap.
	 * 
	 * @param enableDoubleTap the new enable double tap
	 */
	public void setEnableDoubleTap(boolean enableDoubleTap) {
		this.enableDoubleTap = enableDoubleTap;
	}

	/**
	 * Gets the double tap time.
	 * 
	 * @return the double tap time
	 */
	public int getDoubleTapTime() {
		return this.doubleTapTime;
	}

	/**
	 * Sets the double tap time.
	 * 
	 * @param doubleTapTime the new double tap time
	 */
	public void setDoubleTapTime(int doubleTapTime) {
		this.doubleTapTime = doubleTapTime;
	}

	
	
	

}
