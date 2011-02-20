/***********************************************************************
 * mt4j Copyright (c) 2008 - 2009 Christopher Ruff, Fraunhofer-Gesellschaft All rights reserved.
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
package org.mt4j.input.inputProcessors.componentProcessors;

import java.util.ArrayList;
import java.util.List;

import org.mt4j.components.interfaces.IMTComponent3D;
import org.mt4j.input.inputData.ActiveCursorPool;
import org.mt4j.input.inputData.InputCursor;
import org.mt4j.input.inputData.MTFingerInputEvt;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.input.inputProcessors.IInputProcessor;
import org.mt4j.util.math.Tools3D;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

public abstract class AbstractCursorProcessor extends AbstractComponentProcessor{
	private List<InputCursor> activeCursors;
	
	
	/** The lock priority. */
	private int lockPriority;


	private ArrayList<InputCursor> activeCursorsWithEndedOnes;
	
	
	public AbstractCursorProcessor(){
		activeCursors = new ArrayList<InputCursor>();
		activeCursorsWithEndedOnes = new ArrayList<InputCursor>();
		this.lockPriority = 1;
	}

	@Override
	public boolean isInterestedIn(MTInputEvent inputEvt) {
//		return inputEvt instanceof AbstractCursorInputEvt;
		return inputEvt instanceof MTFingerInputEvt 
			&& inputEvt.hasTarget();
	}

	
	@Override
	public void preProcess(MTInputEvent inputEvent) {
		super.preProcess(inputEvent);
		
		MTFingerInputEvt posEvt = (MTFingerInputEvt)inputEvent;
		InputCursor c = posEvt.getCursor();
		switch (posEvt.getId()) {
		case MTFingerInputEvt.INPUT_DETECTED:
			activeCursors.add(c);
			activeCursorsWithEndedOnes.add(c);
			c.registerForLocking(this);
			break;
		case MTFingerInputEvt.INPUT_UPDATED:
			break;
		case MTFingerInputEvt.INPUT_ENDED:
			activeCursors.remove(c);
			c.unregisterForLocking(this);
			break;
		default:
			break;
		}
	}

	@Override
	protected void processInputEvtImpl(MTInputEvent inputEvent) {
//		AbstractCursorInputEvt posEvt = (AbstractCursorInputEvt)inputEvent;
		MTFingerInputEvt posEvt = (MTFingerInputEvt)inputEvent;
		InputCursor c = posEvt.getCursor();
		switch (posEvt.getId()) {
		case MTFingerInputEvt.INPUT_DETECTED:
//			activeCursors.add(c);
//			c.registerGeneralInterest(this);
			cursorStarted(c, posEvt);
			break;
		case MTFingerInputEvt.INPUT_UPDATED:
			cursorUpdated(c, posEvt);
			break;
		case MTFingerInputEvt.INPUT_ENDED:
//			activeCursors.remove(c);
			cursorEnded(c, posEvt);
//			if (c.isLockedBy(this)){
				unLock(c);
//			}
//			c.unregisterGeneralInterest(this);
			activeCursorsWithEndedOnes.remove(c);
			break;
		default:
			break;
		}
	}
	
	
	/**
	 * Gets all active cursors which started on this component.
	 * It is not check whether this input processor could lock any of them.
	 * User <code>getAvailableComponentCursors()</code> instead for that.
	 * 
	 * @return the active component cursors
	 */
	public List<InputCursor> getCurrentComponentCursors(){
		return this.activeCursors;
	}
	
	public InputCursor[] getCurrentComponentCursorsArray(){
		return this.activeCursors.toArray(new InputCursor[getCurrentComponentCursors().size()]);
	}
	
	//////////////////////////////////////////////////////////
	//TODO check at getAvailable/fartherst etc cursor if cursor on component!? -> not usable if not! -> intersect cursor target? -> but what about canvas which has no intersection -> hardcode?
	//TODO allow to subscribe to other application cursors??
	//TODO method to get the 2 cursors which are farthest away from each other
	//TODO getNearestAvailableCursorTo()
	//TODO caution: if cursorLocked() is invoked we cant check if gesture used the cursor by checking 
	//if the cursor was in getLockedCurosor list because its already removed
	
	
	//exclude INPUT_ENDED?
	//FIXME this will also be cursors from scenes the component isnt even in!
	//TODO we should only listen to InputRetargeter from the component's scene, and only get the onces which arent already
	//targeted at this component..
	public InputCursor[] getAllActiveApplicationCursors(){
		return ActiveCursorPool.getInstance().getActiveCursors();
	}
	
	//TODO dont include the input_ended cursors!?
	/**
	 * Returns all component cursors that are not yet locked but could be locked 
	 * by this input processor.
	 * @return the free component cursors array
	 */
	public InputCursor[] getFreeComponentCursorsArray(){
		List<InputCursor> freeCursors = getFreeComponentCursors();
		return freeCursors.toArray(new InputCursor[freeCursors.size()]); 
	}
		
	
	//TODO should we also check if the cursors are on the component? / input_ended?
	//TODO dont include the input_ended cursors!?
	/**
	 * Returns all component cursors that are not yet locked but could be locked 
	 * by this input processor.
	 * @return the free component cursors
	 */
	public List<InputCursor> getFreeComponentCursors(){
		List<InputCursor> activeCursorsOnComp = this.getCurrentComponentCursors();
		List<InputCursor> freeCursors = new ArrayList<InputCursor>();
		for (InputCursor inputCursor : activeCursorsOnComp) {
			if (!inputCursor.isLockedBy(this) && inputCursor.canLock(this)){
				freeCursors.add(inputCursor);
			}
		}
//		return freeCursors.toArray(new InputCursor[freeCursors.size()]);
		return freeCursors;
	}
	
	/**
	 * Return all the component cursors that this component processor has successfully locked.
	 * @return the locked cursors array
	 */
	public InputCursor[] getLockedCursorsArray(){
		List<InputCursor> locked = getLockedCursors();
		return locked.toArray(new InputCursor[locked.size()]);
	}
	
	/**
	 * Return all the component cursors that this component processor has successfully locked.
	 * @return the locked cursors
	 */
	public List<InputCursor> getLockedCursors(){
		List<InputCursor> activeCursorsOnCompWithENDED = this.activeCursorsWithEndedOnes;
		List<InputCursor> lockedCursors = new ArrayList<InputCursor>();
		for (InputCursor inputCursor : activeCursorsOnCompWithENDED) {
			if (inputCursor.isLockedBy(this)){
				lockedCursors.add(inputCursor);
			}
		}
		return lockedCursors;
	}
	
	/**
	 * Releases all cursors that this component input processor currently holds a lock on.
	 */
	public void unLockAllCursors(){
		//FIXME we should also unlock the cursors that have input_ended, so that processors with lower priority can start the gesture and end it correctly aferwards
//		List<InputCursor> activeCursorsOnComp = acp.getActiveComponentCursors(); 
		List<InputCursor> activeCursorsOnCompWithENDED = this.activeCursorsWithEndedOnes;
		for (InputCursor inputCursor : activeCursorsOnCompWithENDED) {
			if (inputCursor.isLockedBy(this)){
				unLock(inputCursor);
			}
		}
	}
	
	/**
	 * Returns the most far away component cursor to the specified cursor.
	 * Only returns a cursor if it is free to use and could be locked by this processor.
	 *
	 * @param cursor the cursor
	 * @return the farthest free component cursor to
	 */
	public InputCursor getFarthestFreeComponentCursorTo(InputCursor cursor){
		return getFarthestFreeCursorTo(cursor,  new InputCursor[]{});
	}
	
	/**
	 * Returns the most far away component cursor to the specified cursor.
	 * Only returns a cursor if it is free to use and could be locked by this processor.
	 * 
	 * @param cursor the cursor
	 * @param excludedFromSearch the excluded from search
	 * @return the farthest free cursor to
	 */
	public InputCursor getFarthestFreeCursorTo(InputCursor cursor, InputCursor... excludedFromSearch){
		float currDist = Float.MIN_VALUE;
		InputCursor fartherstCursor = null;
		
		Vector3D cursorPos = cursor.getPosition();
		for (InputCursor currentCursor : this.getCurrentComponentCursors()) {
			if (currentCursor.equals(cursor) || !currentCursor.canLock(this) || currentCursor.isLockedBy(this))
				continue;
			
			for (InputCursor excludedCursor : excludedFromSearch) {
				if (currentCursor.equals(excludedCursor)){
					continue;
				}
			}
			
			float distanceToCurrentCursor = currentCursor.getPosition().distance2D(cursorPos);
			if (distanceToCurrentCursor >= currDist || distanceToCurrentCursor == 0.0f){
				currDist = distanceToCurrentCursor;
				fartherstCursor = currentCursor;
			}
		}
		return fartherstCursor;
	}
	

	
	/**
	 * Checks if the distance between a reference cursor and a cursor is greater than the distance to another cursor.
	 *
	 * @param reference the reference
	 * @param oldCursor the old cursor
	 * @param newCursor the new cursor
	 * @return true, if is cursor distance greater
	 */
	public boolean isCursorDistanceGreater(InputCursor reference, InputCursor oldCursor, InputCursor newCursor){
//		float distanceToOldCursor = reference.getPosition().distance2D(oldCursor.getPosition());
//		float distanceToNewCursor = reference.getPosition().distance2D(newCursor.getPosition());
//		return distanceToNewCursor > distanceToOldCursor;
		return getDistance(reference, newCursor) > getDistance(reference, oldCursor);
	}
	
	/**
	 * Gets the distance between two cursors.
	 *
	 * @param a the a
	 * @param b the b
	 * @return the distance
	 */
	public float getDistance(InputCursor a, InputCursor b){
		return a.getPosition().distance2D(b.getPosition());
	}
	
	/**
	 * Gets the intersection point of a cursor and a specified component.
	 * Can return null if the cursor doesent intersect the component.
	 *
	 * @param app the app
	 * @param component the component
	 * @param c the c
	 * @return the intersection
	 */
	public Vector3D getIntersection(PApplet app, IMTComponent3D component, InputCursor c){
		return component.getIntersectionGlobal(Tools3D.getCameraPickRay(app, component, c));
	}
	

	///////////////////////////////////////////////////////
	
	
	
	/**
	 * Gets the input cursor locking priority.
	 * 
	 * @return the input cursor locking priority
	 */
	public int getLockPriority() {
		return lockPriority;
	}


	/**
	 * Sets the  input cursor locking priority.
	 * 
	 * @param gesturePriority the new input cursor locking priority
	 */
	public void setLockPriority(int gesturePriority) {
		this.lockPriority = gesturePriority;
	}
	
	
	/**
	 * Checks if this input processor would have the 
	 * sufficient priority to lock the specified input cursors.
	 * 
	 * @param cursors the cursors
	 * 
	 * @return true, if successful
	 */
	protected boolean canLock(InputCursor... cursors){
		int locked = 0;
		for (int i = 0; i < cursors.length; i++) {
			InputCursor m = cursors[i];
			if (m.canLock(this)){
				locked++;
			}
		}
		return locked == cursors.length;
	}
	
	
	
	/**
	 * Locks the cursor with this processor if the processors lock priority
	 * is higher or equal than the current lock priority of this cursor.
	 * 
	 * @param cursors the cursors
	 * 
	 * @return true, if all specified cursors could get locked
	 */
	protected boolean getLock(InputCursor... cursors){
		int locked = 0;
		for (int i = 0; i < cursors.length; i++) {
			InputCursor m = cursors[i];
			if (m.getLock(this)){
				locked++;
			}
		}
		return locked == cursors.length;
	}
	
	
	
	/**
	 * Unlocks the specified cursors if they are not longer used by this processor.
	 * If the priority by which the cursors are locked changes by that, 
	 * the <code>cursorUnlocked</code> method is invoked on processors 
	 * with a lower priority who by that get a chance to lock this cursor again.
	 * 
	 * @param cursors the cursors
	 */
	protected void unLock(InputCursor... cursors){
		for (int i = 0; i < cursors.length; i++) {
			InputCursor inputCursor = cursors[i];
			inputCursor.unlock(this);
		}
	}
	
	
	@Override
	public int compareTo(AbstractComponentProcessor o) {
		if (o instanceof AbstractCursorProcessor) {
			AbstractCursorProcessor o2 = (AbstractCursorProcessor) o;
			
			if (this.getLockPriority() < o2.getLockPriority()){
				return -1;
			}else if (this.getLockPriority() > o2.getLockPriority()){
				return 1;
			}else{
				if (!this.equals(o2)
					&& this.getLockPriority() == o2.getLockPriority()
				){
					return -1;
				}
				return 0;
			}
		}else{
			return 1;
		}
	}	
	
	/**
	 * This method is called if a input processor with a higher locking-priority than this one sucessfully
	 * locked the specified cursor. If this cursor was used in this input processor, we have to stop using it until it
	 * is unlocked by the other processor!
	 * 
	 * @param cursor the cursor
	 * @param lockingprocessor the locking processor
	 */
	abstract public void cursorLocked(InputCursor cursor, IInputProcessor lockingprocessor);
	
	/**
	 * This method is called if a input processor with a higher locking-priority than this one removes his lock on the specified
	 * cursor (i.e. because the conditions for continuing the gesture aren't met anymore). This gives this input processor the chance to
	 * see if it can use the cursor and try to lock it again.
	 * 
	 * @param cursor the cursor
	 */
	abstract public void cursorUnlocked(InputCursor cursor);
	
	/**
	 * Called when a new cursor has been detected.
	 * @param inputCursor
	 * @param currentEvent
	 */
	abstract public void cursorStarted(InputCursor inputCursor, MTFingerInputEvt currentEvent);
	
	/**
	 * Called when a cursor has been updated with a new input event.
	 * @param inputCursor
	 * @param currentEvent
	 */
	abstract public void cursorUpdated(InputCursor inputCursor, MTFingerInputEvt currentEvent);
	
	/**
	 * Called when a cursor has been removed.
	 * @param inputCursor
	 * @param currentEvent
	 */
	abstract public void cursorEnded(InputCursor inputCursor, MTFingerInputEvt currentEvent);
	

}
